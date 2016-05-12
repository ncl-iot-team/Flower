/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.model.DynamoCtrl;
import com.csiro.flower.model.FlowDetailSetting;
import com.csiro.flower.model.KinesisCtrl;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 *
 * @author kho01f
 */
@Service
@Scope("prototype")
public class CtrlsRunnerService {

    private final int taskNo = 6;

//    public int getTaskNo() {
//        return taskNo;
//    }
//
//    public void setTaskNo(int taskNo) {
//        this.taskNo = taskNo;
//    }
//    
    @Autowired
    private StormCtrlServiceImpl stormCtrlServiceImpl;

    @Autowired
    private KinesisCtrlServiceImpl kinesisCtrlServiceImpl;

    @Autowired
    private DynamoCtrlServiceImpl dynamoCtrlServiceImpl;
    
    public void startCtrls(FlowDetailSetting flowSetting) {

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(taskNo);
        ApplicationContext context = new FileSystemXmlApplicationContext("file:C:/Users/kho01f/Documents/Flower/src/main/webapp/WEB-INF/config.xml");

        if (flowSetting.getStormCtrl() != null) {
//            StormCtrlServiceImpl stormCtrlServiceImpl = (StormCtrlServiceImpl) context.getBean(StormCtrlServiceImpl.class);
            stormCtrlServiceImpl.setScheduler(scheduledThreadPool);
            stormCtrlServiceImpl.startController(flowSetting.getCloudSetting(), flowSetting.getStormCluster(), flowSetting.getStormCtrl());
        }
        if (flowSetting.getDynamoCtrls() != null) {
            for (DynamoCtrl dynamoCtrl : flowSetting.getDynamoCtrls()) {
//                DynamoCtrlServiceImpl dynamoCtrlServiceImpl = (DynamoCtrlServiceImpl) context.getBean(DynamoCtrlServiceImpl.class);
//                DynamoCtrlServiceImpl dynamoCtrlServiceImpl = new DynamoCtrlServiceImpl();
                dynamoCtrlServiceImpl.setScheduler(scheduledThreadPool);
                dynamoCtrlServiceImpl.startConroller(flowSetting.getCloudSetting(), dynamoCtrl);
            }
        }
        if (flowSetting.getKinesisCtrls() != null) {
            for (KinesisCtrl kinesisCtrl : flowSetting.getKinesisCtrls()) {
//                KinesisCtrlServiceImpl kinesisCtrlServiceImpl = (KinesisCtrlServiceImpl) context.getBean(KinesisCtrlServiceImpl.class);
                kinesisCtrlServiceImpl.setScheduler(scheduledThreadPool);
                kinesisCtrlServiceImpl.startController(flowSetting.getCloudSetting(), kinesisCtrl);
            }
        }
    }

}
