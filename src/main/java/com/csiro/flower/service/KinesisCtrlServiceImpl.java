/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.csiro.flower.dao.KinesisCtrlDao;
import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.KinesisCtrl;
import java.sql.Timestamp;
import java.util.Date;
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
public class KinesisCtrlServiceImpl extends CtrlService {

    final int twoMinMil = 1000 * 60 * 2;
    final int twoMinSec = 120;

    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
    double epsilon = 0.0001;
    double upperK0 = 0.1;
    double upInitK0 = 0.08;
    double lowInitK0 = 0.02;
    double lowerK0 = 0;
    double k_init = 0.03;
    double gamma = 0.0003;

    Queue kinesisCtrlGainQ;
    private String ctrlName = "Kinesis";

    @Autowired
    CloudWatchService cloudWatchService;

    @Autowired
    KinesisMgmtService kinesisMgmtService;

    @Autowired
    KinesisCtrlDao kinesisCtrlDao;

    public long startKinesisController(CloudSetting cloudSetting, KinesisCtrl kinesisCtrl) {

        initService(
                cloudSetting.getCloudProvider(),
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());
        startKinesisCtrl(
                kinesisCtrl.getStreamName(),
                kinesisCtrl.getMeasurementTarget(),
                kinesisCtrl.getRefValue(),
                kinesisCtrl.getMonitoringPeriod(),
                kinesisCtrl.getBackoffNo());

        setFlowId(kinesisCtrl.getFlowIdFk());
        setResourceName(kinesisCtrl.getStreamName());

        return getCtrlThreadId();
    }

    private void initService(String provider, String accessKey, String secretKey, String region) {
        cloudWatchService.initService(provider, accessKey, secretKey, region);
        kinesisMgmtService.initService(provider, accessKey, secretKey, region);
    }

    private void startKinesisCtrl(final String streamName, final String measurementTarget,
            final double putRecordUtilizationRef, int schedulingPeriod, final int backoffNo) {

        kinesisCtrlGainQ = new LinkedList<>();
        final Runnable runMonitorAndControl = new Runnable() {
            @Override
            public void run() {
                runController(streamName, measurementTarget, putRecordUtilizationRef, backoffNo);
                setCtrlThreadId(Thread.currentThread().getId());
            }
        };
        scheduledThreadPool.scheduleAtFixedRate(runMonitorAndControl, 0, schedulingPeriod, TimeUnit.MINUTES);
    }

    private void runController(String streamName, String measurementTarget, double putRecordUtilizationRef, int backoffNo) {
        double error;
        double k0;
        double uk0;
        double uk1;
        double putRecordUtilizationPercent;
        int roundedUk1;
        int roundedUk0;
        boolean isGainUpdated = false;

        double incomingRecords = getKinesisIncomingRecStat(streamName, measurementTarget);
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

        saveMonitoringStats(error, new Timestamp(new Date().getTime()), k0,
                incomingRecords, uk0, uk1, roundedUk1);
        // If clouadwatch datapoint is null for current period, do not update gains and shard size!
        if (incomingRecords != 0) {
            if (uk1 > uk0) {
                roundedUk1 = roundedUk1 - roundedUk0;
                if (roundedUk1 != 0) {
                    kinesisMgmtService.increaseShards(streamName, roundedUk1);
                }
            } else if ((uk1 < uk0) && (uk0 != 1) /*&& (Math.abs(error) >= threshold)*/) {
                if (roundedUk1 <= 0) {
                    roundedUk1 = roundedUk0 - 1;
                } else {
                    roundedUk1 = roundedUk0 - roundedUk1;
                }
                kinesisMgmtService.decreaseShards(streamName, roundedUk1);
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

    public double getKinesisIncomingRecStat(String streamName, String measurementTarget) {

        GetMetricStatisticsResult statsResult = cloudWatchService.
                getCriticalResourceStats(ctrlName, streamName, measurementTarget, twoMinMil);
        return getIncomingRecords(statsResult);
    }

    public double getIncomingRecords(GetMetricStatisticsResult result) {
        double val = 0;
        if (!result.getDatapoints().isEmpty()) {
            for (Datapoint dataPoint : result.getDatapoints()) {
                val += dataPoint.getSum();
            }
            return (val / twoMinSec);
        } else {
            return 0;
        }
    }

    @Override
    public void updateCtrlStatus(String ctrlStatus, long threadId, Timestamp date) {
        int ctrlId = kinesisCtrlDao.getPkId(getFlowId(), getResourceName());
        ctrlStatsDao.saveCtrlStatus(ctrlId, ctrlName, ctrlStatus, threadId, date);
    }

    private void saveMonitoringStats(double error, Timestamp timestamp, double k0,
            double incomingRecords, double uk0, double uk1, int roundedUk1) {

        int ctrlId = kinesisCtrlDao.getPkId(getFlowId(), getResourceName());
        ctrlStatsDao.saveCtrlMonitoringStats(ctrlId, ctrlName, error, timestamp,
                k0, incomingRecords, uk0, uk1, roundedUk1);
    }

}
