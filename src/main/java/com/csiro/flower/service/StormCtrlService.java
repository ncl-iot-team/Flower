/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.StormCluster;
import com.csiro.flower.model.StormCtrl;

/**
 *
 * @author kho01f
 */
public interface StormCtrlService {

    public void startStormController(CloudSetting cloudSetting,
            StormCluster stormCluster, StormCtrl stormCtrl);
    
}
