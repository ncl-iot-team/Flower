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

    static String RUNNING_STATUS = "Running";
    static String STOPPED_STATUS = "Stopped";

    static final double epsilon = 0.0001;
    static final double upperK0 = 0.1;
    static final double upInitK0 = 0.08;
    static final double lowInitK0 = 0.02;
    static final double lowerK0 = 0;
    static final double k_init = 0.03;
    static final double gamma = 0.0003;

    static final int twoMinMil = 1000 * 60 * 2;
    static final int twoMinSec = 120;

    @Autowired
    CtrlStatsDao ctrlStatsDao;
}
