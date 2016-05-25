/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.csiro.flower.dao.CtrlStatsDao;
import com.csiro.flower.dao.StormCtrlDao;
import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.CtrlInternalSetting;
import com.csiro.flower.model.StormCluster;
import com.csiro.flower.model.StormCtrl;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author kho01f
 */
public class StormCtrlServiceImpl extends CtrlService implements Runnable {

    long TRANSITION_WAIT_TIME = 45000;

    @Autowired
    private StormMgmtService stormMgmtService;

    @Autowired
    private StormCtrlDao stormCtrlDao;

    @Autowired
    private CloudWatchService cloudWatchService;

    private ScheduledFuture<?> futureTask;

    Queue stormCtrlGainQ;

    private final String ctrlName = "ApacheStorm";
    private int ctrlId;
    private String nimbusIp;
    private String topologyName;
    private String measurementTarget;
    private double cpuRef;
    private int backoffNo;
    private int flowId;

    private double upperK0;
    private double upInitK0;
    private double lowInitK0;
    private double lowerK0;
    private double k_init;
    private double gamma;

    public ScheduledFuture<?> getFutureTask() {
        return futureTask;
    }

    public void setFutureTask(ScheduledFuture<?> futureTask) {
        this.futureTask = futureTask;
    }

    public void setupController(CloudSetting cloudSetting, StormCluster stormCluster, StormCtrl stormCtrl) {

        initValues(stormCluster, stormCtrl);

        initService(cloudSetting.getCloudProvider(),
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());

        ctrlStatsDao.saveCtrlStatus(ctrlId, ctrlName, RUNNING_STATUS, new Timestamp(new Date().getTime()));
    }

    private void initValues(StormCluster stormCluster, StormCtrl stormCtrl) {
        stormCtrlGainQ = new LinkedList<>();
        nimbusIp = stormCluster.getNimbusIp();
        flowId = stormCtrl.getFlowIdFk();
        topologyName = stormCtrl.getTargetTopology();
        measurementTarget = stormCtrl.getMeasurementTarget();
        cpuRef = stormCtrl.getRefValue();
        backoffNo = stormCtrl.getBackoffNo();
        ctrlId = stormCtrlDao.getPkId(flowId, topologyName);

        CtrlInternalSetting ctrlInternalSetting = stormCtrlDao.getInternalSetting(ctrlId);
        upperK0 = ctrlInternalSetting.getUpperK0();
        upInitK0 = ctrlInternalSetting.getUpInitK0();
        lowInitK0 = ctrlInternalSetting.getLowInitK0();
        lowerK0 = ctrlInternalSetting.getLowerK0();
        k_init = ctrlInternalSetting.getK_init();
        gamma = ctrlInternalSetting.getGamma();
    }

    private void initService(String provider, String accessKey, String secretKey, String region) {
        cloudWatchService.initService(provider, accessKey, secretKey, region);
        stormMgmtService.initService(provider, accessKey, secretKey, region);
    }

    @Override
    public void run() {
        if (!isCtrlStopped(ctrlId, ctrlName)) {
            runController();
        } else {
            futureTask.cancel(false);
        }
    }

    private boolean isCtrlStopped(int id, String ctrl) {
        return ctrlStatsDao.getCtrlStatus(id, ctrl).equals(STOPPED_STATUS);
    }

    private void runController() {
        try {
            int clusterSizeLimit;
            double error;
            int uk0;
            double uk1;
            double k0;
            int roundedUk1;

            if (stormCtrlGainQ.isEmpty()) {
                k0 = k_init;
            } else {
                k0 = (double) stormCtrlGainQ.poll();
                if (k0 >= upperK0) {
                    k0 = upInitK0;
                }
                if (k0 <= lowerK0) {
                    k0 = lowInitK0;
                }
            }

            List<String> list = stormMgmtService.getRunningWorkerIds();
            double cpu = getClusterStats(list, measurementTarget);

            clusterSizeLimit = stormMgmtService.getStoppedWorkerIds().size();
            uk0 = stormMgmtService.getRunningWorkerIds().size();

            error = cpu - cpuRef;
            uk1 = uk0 + k0 * error;
            roundedUk1 = (int) Math.round(uk1);

            ctrlStatsDao.saveCtrlMonitoringStats(ctrlId, ctrlName, error,
                    new Date().getTime(), k0, (cpu + 10), (uk0 + 100), uk1, roundedUk1 + 10);

            if (roundedUk1 > uk0) {
                roundedUk1 = roundedUk1 - uk0;
                if (roundedUk1 > clusterSizeLimit) {
                    roundedUk1 = clusterSizeLimit;
                }

                stormMgmtService.startWorkers(roundedUk1);
                Thread.sleep(TRANSITION_WAIT_TIME);
                stormMgmtService.buildStormClient(nimbusIp);
                stormMgmtService.simpleRebalanceTopology(topologyName);
            } else if ((roundedUk1 < uk0) && (uk0 != 1) /* && (Math.abs(error) >= threshold)*/) {
                if (roundedUk1 <= 0) {
                    roundedUk1 = uk0 - 1;
                } else {
                    roundedUk1 = uk0 - roundedUk1;
                }
                stormMgmtService.stopWorkers(roundedUk1);
            }

            // if Ctrl descision revoked, do not update the gain
//            if ((uk1 < uk0) && ((uk0 == 1) /*|| (Math.abs(error) < threshold)*/)) {
//                stormCtrlGainQ.add(Math.abs(k0));
//            } else {
            stormCtrlGainQ.add(k0 + gamma * error);
//            }
//            stormCtrlGainQ.add(Math.abs(k0 + (lambda * ((cpu / CPUref) * 100))));

        } catch (Exception e) {
            Logger.getLogger(StormCtrlServiceImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public double getClusterStats(List<String> runningIds, String measurementTarget) {

        HashMap<String, Double> cpuStats = new HashMap<>();
        double avgCpu = 0.0;
        double finalAvg;
        GetMetricStatisticsResult statsResult;
        for (String instance : runningIds) {
            statsResult = cloudWatchService.
                    getCriticalResourceStats(ctrlName, instance, measurementTarget, twoMinMil);
            cpuStats.put(instance, getStormAvgCPU(statsResult));
        }
        for (double cpu : cpuStats.values()) {
            if (Double.isNaN(cpu)) {
                cpu = 0;
            }
            avgCpu += cpu;
        }
        finalAvg = avgCpu / cpuStats.size();

        return Double.isNaN(finalAvg) ? 0.0 : finalAvg;
    }

    public double getStormAvgCPU(GetMetricStatisticsResult result) {
        double cpu = 0.0;
        for (Datapoint dataPoint : result.getDatapoints()) {
            cpu += dataPoint.getAverage();
        }
        return cpu / result.getDatapoints().size();
    }

}
