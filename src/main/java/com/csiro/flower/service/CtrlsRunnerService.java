/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.dao.CloudSettingDao;
import com.csiro.flower.dao.CtrlDao;
import com.csiro.flower.dao.CtrlStatsDao;
import com.csiro.flower.dao.StormClusterDao;
import com.csiro.flower.model.Ctrl;
import com.csiro.flower.model.CtrlMonitoringResultSet;
import com.csiro.flower.model.FlowDetailSetting;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@Service
@Scope("prototype")
public class CtrlsRunnerService {

    private final int taskNo = 10;
    private final String STOPPED_STATUS = "Stopped";

    @Autowired
    CtrlFactoryService ctrlFactoryService;

    @Autowired
    private StormClusterDao stormClusterDao;

    @Autowired
    private CtrlDao ctrlDao;

    @Autowired
    private CtrlStatsDao ctrlStatsDao;

    @Autowired
    private CloudSettingDao cloudSettingDao;

    public void startCtrls(FlowDetailSetting flowSetting) {

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(taskNo);
        ScheduledFuture<?> futureTask;

        for (Ctrl ctrl : flowSetting.getCtrls()) {
            if (ctrl.getCtrlName() != null) {
                switch (ctrl.getCtrlName()) {
                    case "AmazonKinesis":
                        KinesisCtrlServiceImpl kinesisCtrlServiceImpl = ctrlFactoryService.getKinesisCtrlServiceImpl();
                        kinesisCtrlServiceImpl.setupController(flowSetting.getCloudSetting(), ctrl);
                        futureTask = scheduledThreadPool.scheduleAtFixedRate(kinesisCtrlServiceImpl, 0, ctrl.getMonitoringPeriod(), TimeUnit.MINUTES);
                        kinesisCtrlServiceImpl.setFutureTask(futureTask);
                        break;
                    case "ApacheStorm":
                        StormCtrlServiceImpl stormCtrlServiceImpl = ctrlFactoryService.getStormCtrlServiceImpl();
                        stormCtrlServiceImpl.setupController(flowSetting.getCloudSetting(), flowSetting.getStormCluster(), ctrl);
                        futureTask = scheduledThreadPool.scheduleAtFixedRate(stormCtrlServiceImpl, 0, ctrl.getMonitoringPeriod(), TimeUnit.MINUTES);
                        stormCtrlServiceImpl.setFutureTask(futureTask);
                        break;
                    case "DynamoDB":
                        DynamoCtrlServiceImpl dynamoCtrlServiceImpl = ctrlFactoryService.getDynamoCtrlServiceImpl();
                        dynamoCtrlServiceImpl.setupConroller(flowSetting.getCloudSetting(), ctrl);
                        futureTask = scheduledThreadPool.scheduleAtFixedRate(dynamoCtrlServiceImpl, 0, ctrl.getMonitoringPeriod(), TimeUnit.MINUTES);
                        dynamoCtrlServiceImpl.setFutureTask(futureTask);
                        break;
                }
            }
        }
    }

    public void restartCtrl(String ctrlName, int flowId, String resource, String measurementTarget) {
        FlowDetailSetting flowSetting = new FlowDetailSetting();
        flowSetting.setCloudSetting(cloudSettingDao.get(flowId));
        List<Ctrl> ctrls = new ArrayList<>();
        ctrls.add(ctrlDao.get(flowId, ctrlName, resource, measurementTarget));
        flowSetting.setCtrls(ctrls);

        if (ctrlName.equals("ApacheStorm")) {
            flowSetting.setStormCluster(stormClusterDao.get(flowId));
        }

        startCtrls(flowSetting);
    }

    public void stopCtrl(int flowId, String ctrlName, String resource, String measurementTarget) {
        int id = getCtrlPkId(flowId, ctrlName, resource, measurementTarget);
        ctrlStatsDao.updateCtrlStatus(id, ctrlName, STOPPED_STATUS, new Timestamp(new Date().getTime()));
    }

    public List<CtrlMonitoringResultSet> getCtrlMonitoringStats(int flowId, String ctrlName, String resource, String measurementTarget, long timeStamp) {
        int id = getCtrlPkId(flowId, ctrlName, resource, measurementTarget);
        return ctrlStatsDao.getCtrlMonitoringStats(id, ctrlName, timeStamp);
    }

    public String getCtrlStatus(int flowId, String ctrlName, String resource, String measurementTarget) {
        int id = getCtrlPkId(flowId, ctrlName, resource, measurementTarget);
        return ctrlStatsDao.getCtrlStatus(id, ctrlName);
    }

    private int getCtrlPkId(int flowId, String ctrlName, String resource, String measurementTarget) {
        return ctrlDao.getPkId(flowId, ctrlName, resource, measurementTarget);
    }

}
