/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.csiro.flower.dao.CtrlStatsDao;
import com.csiro.flower.dao.DynamoCtrlDao;
import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.DynamoCtrl;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author kho01f
 */
//@Service
//@Scope("prototype")
public class DynamoCtrlServiceImpl extends CtrlService implements Runnable {

    @Autowired
    private DynamoMgmtService dynamoMgmtService;

    @Autowired
    private DynamoCtrlDao dynamoCtrlDao;

    @Autowired
    private CloudWatchService cloudWatchService;

    String ctrlName = "DynamoDB";

    public ScheduledFuture<?> getFutureTask() {
        return futureTask;
    }

    public void setFutureTask(ScheduledFuture<?> futureTask) {
        this.futureTask = futureTask;
    }

//    private ScheduledExecutorService scheduledThreadPool;
    Queue dynamoCtrlGainQ;
    private ScheduledFuture<?> futureTask;

    private int ctrlId;

    int flowId;
    String tblName;
    String measurementTarget;
    double writeUtilizationRef;
    int initBackoff;

    public void setupConroller(CloudSetting cloudSetting, DynamoCtrl dynamoCtrl) {

        initValues(dynamoCtrl);
        initService(
                cloudSetting.getCloudProvider(),
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());
        ctrlStatsDao.saveCtrlStatus(ctrlId, ctrlName, RUNNING_STATUS, new Timestamp(new Date().getTime()));
    }

    private void initValues(DynamoCtrl dynamoCtrl) {
        dynamoCtrlGainQ = new LinkedList<>();
        flowId = dynamoCtrl.getFlowIdFk();
        tblName = dynamoCtrl.getTableName();
        measurementTarget = dynamoCtrl.getMeasurementTarget();
        writeUtilizationRef = dynamoCtrl.getRefValue();
        initBackoff = dynamoCtrl.getBackoffNo();
        ctrlId = dynamoCtrlDao.getPkId(flowId, tblName);
    }

    private void initService(String provider, String accessKey, String secretKey, String region) {
        cloudWatchService.initService(provider, accessKey, secretKey, region);
        dynamoMgmtService.initService(provider, accessKey, secretKey, region);
    }

    @Override
    public void run() {
        if (!isCtrlStopped(ctrlId, ctrlName)) {
            runController();
        } else {
            futureTask.cancel(false);
        }
    }

//    private void startDynamoCtrl(final String tblName, final String measurementTarget,
//            final double refValue, int schedulingPeriod, final int backoffNo) {
//
//        final Runnable runMonitorAndControl = new Runnable() {
//            @Override
//            public void run() {
//                if (!isCtrlStopped(ctrlId, ctrlName)) {
//                    runController(tblName, measurementTarget, refValue, backoffNo);
//                } else {
//                    futureTask.cancel(false);
//                }
//            }
//        };
//    }
    private boolean isCtrlStopped(int id, String ctrl) {
        return ctrlStatsDao.getCtrlStatus(id, ctrl).equals(STOPPED_STATUS);
    }

    private void runController() {
        double error;
        double k0;
        double uk0;
        double uk1;
        double writeUtilizationPercent;
        int roundedUk1;
        boolean decisionRevoked = true;
        int backoffNo = initBackoff;

        /*
         e.g. 10 units of Write Capacity is enough to do up to 36,000 writes per hour
         Units of Capacity required for writes = Number of item writes per second x item size in 1KB blocks
         Units of Capacity required for reads* = Number of item reads per second x item size in 4KB blocks 
         */
        double writeRate = getDynamoStats(tblName, measurementTarget);
        uk0 = dynamoMgmtService.getProvisionedThroughput(tblName).getWriteCapacityUnits();
        writeUtilizationPercent = (writeRate / uk0) * 100;

        if (dynamoCtrlGainQ.isEmpty()) {
            k0 = k_init;
        } else {
            k0 = (double) dynamoCtrlGainQ.poll();
            if (k0 >= upperK0) {
                k0 = upInitK0;
            }
            if (k0 <= lowerK0) {
                k0 = lowInitK0;
            }
        }

        error = (writeUtilizationPercent - writeUtilizationRef);
        uk1 = uk0 + k0 * error;
        roundedUk1 = (int) Math.round(Math.abs(uk1));

        ctrlStatsDao.saveCtrlMonitoringStats(ctrlId, ctrlName, error,
                new Timestamp(new Date().getTime()), k0, writeRate, uk0, uk1, roundedUk1);

        // If clouadwatch datapoint is null for current period, do not update gains and ProvisionedThroughput!
        if (writeRate != 0) {
            if (((uk1 > uk0)) || ((uk1 < uk0) && /*(Math.abs(error) >= threshold) && */ (backoffNo == 0))) {
                if ((uk1 <= 0) || (roundedUk1 == 0)) {
                    roundedUk1 = 1;
                }
                dynamoCtrlGainQ.add(k0 + gamma * error);
//                dynamoCtrlGainQ.add(Math.abs(k0 + (lambda * ((writeRate / writeUtilizationRef) * 100))));
                decisionRevoked = false;
                //if something really change, go ahead
                if (roundedUk1 != uk0) {
                    dynamoMgmtService.updateProvisionedThroughput(tblName,
                            dynamoMgmtService.getProvisionedThroughput(tblName).getReadCapacityUnits(),
                            roundedUk1);
                }
            }

            if ((uk1 < uk0) /*&& (Math.abs(error) >= threshold)*/ && (backoffNo != 0)) {
                backoffNo = backoffNo - 1;
            }

            // Reset backoffNo if the workload reduction is not consecutive 
            // OR we observe increasing workload.
            if (/*((uk1 < uk0) && (Math.abs(error) < threshold)) || */(uk1 > uk0)) {
                backoffNo = initBackoff;
            }

        }
        // If Ctrl descision revoked, do not update the gain
        if (decisionRevoked) {
            dynamoCtrlGainQ.add(Math.abs(k0));
        }
    }

    public double getDynamoStats(String tblName, String measurementTarget) {

        GetMetricStatisticsResult statsResult = cloudWatchService.
                getCriticalResourceStats(ctrlName, tblName, measurementTarget, twoMinMil);
        return getAvgConsumedWriteCapacity(statsResult);
    }

    public double getAvgConsumedWriteCapacity(GetMetricStatisticsResult result) {
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

}
