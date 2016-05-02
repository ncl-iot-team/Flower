/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.KinesisCtrl;

/**
 *
 * @author kho01f
 */
public interface KinesisCtrlService{

     public void startKinesisController(CloudSetting cloudSetting, KinesisCtrl kinesisCtrl);

}
