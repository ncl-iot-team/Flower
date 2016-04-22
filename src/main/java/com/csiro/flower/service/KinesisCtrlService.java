/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

/**
 *
 * @author kho01f
 */
public interface KinesisCtrlService {

    public void monitorKinesisStat();

    public void runKinesisController(KinesisManagement kinesisManagmement, double incomingRecords);
}
