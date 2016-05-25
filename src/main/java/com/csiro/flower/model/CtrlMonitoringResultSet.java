/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csiro.flower.model;

/**
 *
 * @author kho01f
 */
public class CtrlMonitoringResultSet {
    
    private double measurementTargetValue;
    //allocatedResource is in fact uk0 in our Ctrl Terms.
    private double allocatedResource;
    private double error;
    private int nextCtrlDecisionValue;
    private long timeStamp;
    private String ctrlName;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCtrlName() {
        return ctrlName;
    }

    public void setCtrlName(String ctrlName) {
        this.ctrlName = ctrlName;
    }

    public double getMeasurementTargetValue() {
        return measurementTargetValue;
    }

    public void setMeasurementTargetValue(double measurementTargetValue) {
        this.measurementTargetValue = measurementTargetValue;
    }

    public double getAllocatedResource() {
        return allocatedResource;
    }

    public void setAllocatedResource(double allocatedResource) {
        this.allocatedResource = allocatedResource;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public int getNextCtrlDecisionValue() {
        return nextCtrlDecisionValue;
    }

    public void setNextCtrlDecisionValue(int nextCtrlDecisionValue) {
        this.nextCtrlDecisionValue = nextCtrlDecisionValue;
    }
    
}
