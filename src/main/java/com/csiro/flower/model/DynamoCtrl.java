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
public class DynamoCtrl {

    private String measurementTarget;
    private String tableName;
    private int refValue;
    private int monitoringPeriod;
    private int backoffNo;
    private int flowIdFk;

    public String getMeasurementTarget() {
        return measurementTarget;
    }

    public void setMeasurementTarget(String measurementTarget) {
        this.measurementTarget = measurementTarget;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getRefValue() {
        return refValue;
    }

    public void setRefValue(int refValue) {
        this.refValue = refValue;
    }

    public int getMonitoringPeriod() {
        return monitoringPeriod;
    }

    public void setMonitoringPeriod(int monitoringPeriod) {
        this.monitoringPeriod = monitoringPeriod;
    }

    public int getBackoffNo() {
        return backoffNo;
    }

    public void setBackoffNo(int backoffNo) {
        this.backoffNo = backoffNo;
    }

    public int getFlowIdFk() {
        return flowIdFk;
    }

    public void setFlowIdFk(int flowIdFk) {
        this.flowIdFk = flowIdFk;
    }

}
