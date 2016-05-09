/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.dao.CtrlStatsDao;
import com.csiro.flower.dao.DynamoCtrlDao;
import com.csiro.flower.dao.KinesisCtrlDao;
import com.csiro.flower.dao.StormCtrlDao;
import java.sql.Timestamp;
import java.util.Queue;
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

    @Autowired
    DynamoCtrlDao dynamoCtrlDao;

    @Autowired
    KinesisCtrlDao kinesisCtrlDao;

    @Autowired
    StormCtrlDao stormCtrlDao;

    Queue ctrlGainQ;
    
    String RUNNING_STATUS = "Running";
    String STOPPED_STATUS = "Stopped";

    private int ctrlId;
    private String ctrlName;
    private int flowId;
    private String resource;

    int getFlowId() {
        return flowId;
    }

    void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    String getResource() {
        return resource;
    }

    void setResource(String resource) {
        this.resource = resource;
    }

    String getCtrlName() {
        return ctrlName;
    }

    void setCtrlName(String ctrlName) {
        this.ctrlName = ctrlName;
    }

    int getCtrlId() {
        return ctrlId;
    }

    void setCtrlId(int ctrlId) {
        this.ctrlId = ctrlId;
    }

    void saveCtrlStatus(int id, String ctrl, String ctrlStatus, Timestamp date) {
        ctrlStatsDao.saveCtrlStatus(id, ctrl, ctrlStatus, date);
    }

    public void updateCtrlStatus(String ctrl, int flowId, String resource, String ctrlStatus, Timestamp date) {
        int id = retrieveCtrlId(ctrl, flowId, resource);
        ctrlStatsDao.updateCtrlStatus(id, ctrl, ctrlStatus, date);
    }

   public String getCtrlStatus(int id, String ctrl) {
        return ctrlStatsDao.getCtrlStatus(id, ctrl);
    }

    void saveMonitoringStats(int id, String ctrl, double error, Timestamp timestamp,
            double k0, double writeRate, double uk0, double uk1, int roundedUk1) {

        ctrlStatsDao.saveCtrlMonitoringStats(id, ctrl, error, timestamp,
                k0, writeRate, uk0, uk1, roundedUk1);
    }

    public boolean isCtrlStopped(int id, String ctrl) {
        boolean ctrlStopped = false;
        if (getCtrlStatus(id, ctrl).equals(STOPPED_STATUS)) {
            ctrlStopped = true;
        }
        return ctrlStopped;
    }

    int retrieveCtrlId(String ctrlName, int flowId, String resource) {
        int id = 0;
        switch (ctrlName) {
            case "AmazonKinesis":
                id = kinesisCtrlDao.getPkId(flowId, resource);
                break;
            case "DynamoDB":
                id = dynamoCtrlDao.getPkId(flowId, resource);
                break;
            case "ApacheStorm":
                id = stormCtrlDao.getPkId(flowId, resource);
                break;
        }
        return id;
    }
}
