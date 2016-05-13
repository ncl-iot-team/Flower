/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.model.DynamoCtrl;
import com.csiro.flower.model.FlowDetailSetting;
import com.csiro.flower.model.KinesisCtrl;
import java.sql.Timestamp;
import java.util.Date;
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

    @Autowired
    CtrlFactoryService ctrlFactoryService;

    public void startCtrls(FlowDetailSetting flowSetting) {

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(taskNo);
        ScheduledFuture<?> futureTask;

        if (flowSetting.getStormCtrl() != null) {
            StormCtrlServiceImpl stormCtrlServiceImpl = ctrlFactoryService.getStormCtrlServiceImpl();
            stormCtrlServiceImpl.setupController(flowSetting.getCloudSetting(), flowSetting.getStormCluster(), flowSetting.getStormCtrl());
            futureTask = scheduledThreadPool.scheduleAtFixedRate(stormCtrlServiceImpl, 0, flowSetting.getStormCtrl().getMonitoringPeriod(), TimeUnit.MINUTES);
            stormCtrlServiceImpl.setFutureTask(futureTask);
        }
        if (flowSetting.getDynamoCtrls() != null) {
            for (DynamoCtrl dynamoCtrl : flowSetting.getDynamoCtrls()) {
                DynamoCtrlServiceImpl dynamoCtrlServiceImpl = ctrlFactoryService.getDynamoCtrlServiceImpl();
                dynamoCtrlServiceImpl.setupConroller(flowSetting.getCloudSetting(), dynamoCtrl);
                futureTask = scheduledThreadPool.scheduleAtFixedRate(dynamoCtrlServiceImpl, 0, dynamoCtrl.getMonitoringPeriod(), TimeUnit.MINUTES);
                dynamoCtrlServiceImpl.setFutureTask(futureTask);
            }
        }
        if (flowSetting.getKinesisCtrls() != null) {
            for (KinesisCtrl kinesisCtrl : flowSetting.getKinesisCtrls()) {

                KinesisCtrlServiceImpl kinesisCtrlServiceImpl = ctrlFactoryService.getKinesisCtrlServiceImpl();
                kinesisCtrlServiceImpl.setupController(flowSetting.getCloudSetting(), kinesisCtrl);
                futureTask = scheduledThreadPool.scheduleAtFixedRate(kinesisCtrlServiceImpl, 0, kinesisCtrl.getMonitoringPeriod(), TimeUnit.MINUTES);
                kinesisCtrlServiceImpl.setFutureTask(futureTask);
            }
        }
    }

    public void stopCtrl(String ctrlName, int flowId, String resource) {
        int id = 0;
        switch (ctrlName) {
            case "AmazonKinesis":
                id = kinesisCtrlDao.getPkId(flowId, resource);
                break;
            case "ApacheStorm":
                id = stormCtrlDao.getPkId(flowId, resource);
                break;
            case "DynamoDB":
                id = dynamoCtrlDao.getPkId(flowId, resource);
                break;
        }
        ctrlStatsDao.updateCtrlStatus(id, ctrlName, STOPPED_STATUS, new Timestamp(new Date().getTime()));
    }

}
