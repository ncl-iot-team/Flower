/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.dao.CtrlStatsDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author kho01f
 */
public abstract class CtrlService {

    @Autowired
    CloudWatchService cloudWatchService;

    @Autowired
    CtrlStatsDao ctrlStatsDao;

    
    String RUNNING_STATUS = "Running";
    String STOPPED_STATUS = "Stopped";

}