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
import com.csiro.flower.model.DynamoCtrl;
import com.csiro.flower.model.FlowDetailSetting;
import com.csiro.flower.model.KinesisCtrl;
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

        flowSetting.getCloudSetting().setFlowIdFk(flowId);
        cloudSettingDao.save(flowSetting.getCloudSetting());

        for (String platform : platforms) {
            if (platform.equals("Apache Storm")) {

                flowSetting.getStormCluster().setFlowIdFk(flowId);
                stormClusterDao.save(flowSetting.getStormCluster());

                flowSetting.getStormCtrl().setFlowIdFk(flowId);
                stormCtrlDao.save(flowSetting.getStormCtrl());
            }
            if (platform.equals("DynamoDB")) {

                //further improvement: Batch insertion
                for (DynamoCtrl dynamoCtrl : flowSetting.getDynamoCtrls()) {
                    dynamoCtrl.setFlowIdFk(flowId);
                    dynamoCtrlDao.save(dynamoCtrl);
                }
            }
            if (platform.equals("Amazon Kinesis")) {
                
                //further improvement: Batch insertion
                for (KinesisCtrl kinesisCtrl : flowSetting.getKinesisCtrls()) {
                    kinesisCtrl.setFlowIdFk(flowId);
                    kinesisCtrlDao.save(kinesisCtrl);
                }
            }
        }
    }

}
