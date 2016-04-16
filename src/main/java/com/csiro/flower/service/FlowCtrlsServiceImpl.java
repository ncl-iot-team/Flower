/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.dao.CloudSettingDao;
import com.csiro.flower.dao.DynamoCtrlDao;
import com.csiro.flower.dao.KinesisCtrlDao;
import com.csiro.flower.dao.StormClusterDao;
import com.csiro.flower.dao.StormCtrlDao;
import com.csiro.flower.model.FlowDetailSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kho01f
 */
@Service
public class FlowCtrlsServiceImpl implements FlowCtrlsService {

    @Autowired
    CloudSettingDao cloudSettingDao;

    @Autowired
    DynamoCtrlDao dynamoCtrlDao;

    @Autowired
    KinesisCtrlDao kinesisCtrlDao;

    @Autowired
    StormClusterDao stormClusterDao;

    @Autowired
    StormCtrlDao stormCtrlDao;

    @Override
    public void runFlowController(int flowId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public void saveFlowControllerSettings(String[] platforms, int flowId, FlowDetailSetting flowSetting) {
        cloudSettingDao.save(flowSetting.getCloudSetting());
        for (String platform : platforms) {
//            if (platform.equals("Cloud Setting")) {
//                CloudSetting cloudSetting = new CloudSetting();
//                cloudSetting.setFlowIdFk(flowId);
//                cloudSetting.setCloudProvider(paramSettings.get("cloudProviderCat"));
//                cloudSetting.setAccessKey(paramSettings.get("accessKey"));
//                cloudSetting.setSecretKey(paramSettings.get("secretKey"));
//                cloudSetting.setRegion(paramSettings.get("region"));
//            }
            if (platform.equals("Apache Storm")) {
//
//                StormCluster stormCluster = new StormCluster();
//                stormCluster.setFlowIdFk(flowId);
//                stormCluster.setNimbusIp(paramSettings.get("nimbusIP"));
//                stormCluster.setZookeeperEndpoint(paramSettings.get("zookeeper"));
//                stormClusterDao.save(stormCluster);
//
//                StormCtrl stormCtrl = new StormCtrl();
//                stormCtrl.setFlowIdFk(flowId);
//                stormCtrl.setMeasurementTarget(paramSettings.get("stormMeasurment"));
//                stormCtrl.setTargetTopology(paramSettings.get("topologyList"));
//                stormCtrl.setRefValue(Integer.parseInt(paramSettings.get("stormRef")));
//                stormCtrl.setMonitoringPeriod(Integer.parseInt(paramSettings.get("stormMonitoring")));
//                stormCtrl.setBackoffNo(Integer.parseInt(paramSettings.get("stormBackoff")));
//                stormCtrlDao.save(stormCtrl);
                flowSetting.getStormCluster().setFlowIdFk(flowId);
                stormClusterDao.save(flowSetting.getStormCluster());
                
                flowSetting.getStormCtrl().setFlowIdFk(flowId);
                stormCtrlDao.save(flowSetting.getStormCtrl());
//
            }
            if (platform.equals("DynamoDB")) {
//                DynamoCtrl dynamoCtrl = new DynamoCtrl();
//                dynamoCtrl.setFlowIdFk(flowId);
//                dynamoCtrl.setMeasurementTarget("");
                flowSetting.getDynamoCtrl().setFlowIdFk(flowId);
                dynamoCtrlDao.save(flowSetting.getDynamoCtrl());
            }
            if (platform.equals("Amazon Kinesis")) {
                flowSetting.getKinesisCtrl().setFlowIdFk(flowId);
                kinesisCtrlDao.save(flowSetting.getKinesisCtrl());
//                KinesisCtrl kinesisCtrl = new KinesisCtrl();
//                kinesisCtrl.setFlowIdFk(flowId);
//                kinesisCtrl.setStreamName(platform);
//                kinesisCtrl.setMeasurementTarget("strMeasure");
//                kinesisCtrl.setRefValue(Integer.parseInt("strRef"));
//                kinesisCtrl.setMonitoringPeriod(Integer.parseInt("strMonitoring"));
            }
        }
    }

}
