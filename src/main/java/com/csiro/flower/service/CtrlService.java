/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.dao.CtrlStatsDao;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kho01f
 */
public abstract class CtrlService {

    @Autowired
    CtrlStatsDao ctrlStatsDao;

    private int flowId;
    private String resourceName;

    public int getFlowId() {
        return flowId;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    private long ctrlThreadId;

   public abstract String getCtrlStatus();

    public long getCtrlThreadId() {
        return ctrlThreadId;
    }

    public void setCtrlThreadId(long ctrlThreadId) {
        this.ctrlThreadId = ctrlThreadId;
    }

    public abstract void updateCtrlStatus(String ctrlStatus, long threadId,
            Timestamp date);
}
