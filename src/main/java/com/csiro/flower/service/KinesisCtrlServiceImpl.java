/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kho01f
 */
@Service
public class KinesisCtrlServiceImpl implements KinesisCtrlService {

    final int fiveMinMil = 1000 * 60 * 5;
    final int oneMinMil = 1000 * 60 * 1;
    final int twoMinMil = 1000 * 60 * 2;
    final int threeMinMil = 1000 * 60 * 3;

    final int twoMinSec = 120;
    final int fiveMinSec = 300;
    final int oneMinSec = 60;
    final int threeMinSec = 180;

    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
    static ControllerMonitor ctrlMonitor;
    double epsilon = 0.0001;
    double upperK0 = 0.1;
    double upInitK0 = 0.08;
    double lowInitK0 = 0.02;
    double lowerK0 = 0;
    double k_init = 0.03;
    double gamma = 0.0003;
    long longSchedulingPeriod = 4;
    long shortSchedulingPeriod = 2;
    int initBackoff = 2;
    int backoffNo = initBackoff;

    Queue kinesisCtrlGainQ;

    @Autowired
    CloudWatchService cloudWatchService;

    @Autowired
    KinesisMgmtService kinesisMgmtService;

    public void initService(String provider, String accessKey, String secretKey, String region) {
        cloudWatchService.initService(provider, accessKey, secretKey, region);
        kinesisMgmtService.initService(provider, accessKey, secretKey, region);
    }

    public void startKinesisCtrl(final String streamName) {

        kinesisCtrlGainQ = new LinkedList<>();

        final Runnable runMonitorAndControl = new Runnable() {
            @Override
            public void run() {
//                double incomingRecords = getKinesisStats(streamName);
                runKinesisController(streamName);
//                System.out.println("Kinesis Incoming Records : " + incomingRecords);
                //System.out.println(Thread.currentThread().getName());
            }
        };
        scheduledThreadPool.scheduleAtFixedRate(runMonitorAndControl, 0, shortSchedulingPeriod, TimeUnit.MINUTES);
    }

    public void runKinesisController(String streamName) {
        double error;
        //double threshold = 30;
        double k0;
        double uk0;
        double uk1;
        double putRecordUtilizationRef = 60;
        double putRecordUtilizationPercent;
        int roundedUk1;
        int roundedUk0;
        boolean isGainUpdated = false;

        double incomingRecords = getKinesisStats(streamName);
        uk0 = kinesisMgmtService.getOpenShards(streamName).size();
        roundedUk0 = (int) Math.round(uk0);

        // One shard can support up to 1000 PUT records per second.
        putRecordUtilizationPercent = (incomingRecords / (uk0 * 1000)) * 100;

        if (kinesisCtrlGainQ.isEmpty()) {
            k0 = k_init;
        } else {
            k0 = (double) kinesisCtrlGainQ.poll();
            if (k0 >= upperK0) {
                k0 = upInitK0;
            }
            if (k0 <= lowerK0) {
                k0 = lowInitK0;
            }
        }

        error = (putRecordUtilizationPercent - putRecordUtilizationRef);
        uk1 = uk0 + k0 * error;
        roundedUk1 = (int) Math.round(Math.abs(uk1));

        ctrlMonitor.pushCtrlParams("Kinesis_Ctrl_Stats", uk1, roundedUk1, k0, error, gamma, uk0, incomingRecords);

        // If clouadwatch datapoint is null for current period, do not update gains and shard size!
        if (incomingRecords != 0) {
            if (uk1 > uk0) {
                roundedUk1 = roundedUk1 - roundedUk0;
                if (roundedUk1 != 0) {
                    kinesisManagmement.increaseShards(streamName, roundedUk1);
                }
            } else if ((uk1 < uk0) && (uk0 != 1) /*&& (Math.abs(error) >= threshold)*/) {
                if (roundedUk1 <= 0) {
                    roundedUk1 = roundedUk0 - 1;
                } else {
                    roundedUk1 = roundedUk0 - roundedUk1;
                }
                kinesisManagmement.decreaseShards(streamName, roundedUk1);
            }
//             If Ctrl descision revoked, do not update the gain
//            if ((uk1 < uk0) && ((uk0 == 1) /*|| (Math.abs(error) < threshold)*/)) {
//                kinesisCtrlGainQ.add(Math.abs(k0));
//            } else {
            kinesisCtrlGainQ.add(k0 + gamma * error);
            isGainUpdated = true;
//            }
//            kinesisCtrlGainQ.add(Math.abs(k0 + (lambda * ((incomingRecords / putRecordUtilizationRef) * 100))));
        }
        //Re-pass old gain if it has not updated!
        if (!isGainUpdated) {
            kinesisCtrlGainQ.add(Math.abs(k0));
        }
    }

    public double getKinesisStats(String streamName) {

        GetMetricStatisticsResult statsResult = cloudWatchService.
                getCriticalResourceStats("Kinesis", streamName, "IncomingRecords", threeMinMil);
        return getIncomingRecords(statsResult);
    }

    public double getIncomingRecords(GetMetricStatisticsResult result) {
        double val = 0;
        if (!result.getDatapoints().isEmpty()) {
            for (Datapoint dataPoint : result.getDatapoints()) {
                val += dataPoint.getSum();
            }
            return (val / threeMinSec);
        } else {
            return 0;
        }
    }

}
