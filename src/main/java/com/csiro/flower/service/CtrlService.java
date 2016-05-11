/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author kho01f
 */
public abstract class CtrlService {

    static String RUNNING_STATUS = "Running";
    static String STOPPED_STATUS = "Stopped";
    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);

}
