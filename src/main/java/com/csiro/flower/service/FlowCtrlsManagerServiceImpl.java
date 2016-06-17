/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.dao.CloudSettingDao;
import com.csiro.flower.dao.CtrlDao;
import com.csiro.flower.dao.StormClusterDao;
import com.csiro.flower.model.Ctrl;
import com.csiro.flower.model.FlowDetailSetting;
import java.sql.Timestamp;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kho01f
 */
@Service
public class FlowCtrlsManagerServiceImpl implements FlowCtrlsManagerService {

    @Autowired
    CloudSettingDao cloudSettingDao;

    @Autowired
    CtrlDao ctrlDao;

    @Autowired
    StormClusterDao stormClusterDao;

    @Override
    @Transactional
    public void saveFlowCtrlsSettings(int flowId, FlowDetailSetting flowSetting) {

        flowSetting.getCloudSetting().setFlowIdFk(flowId);
        cloudSettingDao.save(flowSetting.getCloudSetting());

        if (flowSetting.getStormCluster() != null) {
            flowSetting.getStormCluster().setFlowIdFk(flowId);
            stormClusterDao.save(flowSetting.getStormCluster());
        }

        for (Ctrl ctrl : flowSetting.getCtrls()) {
            ctrl.setFlowIdFk(flowId);
            ctrl.setCreationDate(new Timestamp(new Date().getTime()));
            ctrl.setLastUpdateDate(new Timestamp(new Date().getTime()));
            if (ctrl.getCtrlName() != null) {
                ctrlDao.save(ctrl);
            }
        }

    }

}
