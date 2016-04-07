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
public class FlowDetailSetting {
    
    private CloudSetting cloudSetting;
    private StormCluster stormCluster;
    private StormCtrl stormCtrl;
    private DynamoCtrl dynamoCtrl;
    private KinesisCtrl kinesisCtrl;
    

    public CloudSetting getCloudSetting() {
        return cloudSetting;
    }

    public void setCloudSetting(CloudSetting cloudSetting) {
        this.cloudSetting = cloudSetting;
    }

    public StormCluster getStormCluster() {
        return stormCluster;
    }

    public void setStormCluster(StormCluster stormCluster) {
        this.stormCluster = stormCluster;
    }

    public StormCtrl getStormCtrl() {
        return stormCtrl;
    }

    public void setStormCtrl(StormCtrl stormCtrl) {
        this.stormCtrl = stormCtrl;
    }

    public DynamoCtrl getDynamoCtrl() {
        return dynamoCtrl;
    }

    public void setDynamoCtrl(DynamoCtrl dynamoCtrl) {
        this.dynamoCtrl = dynamoCtrl;
    }

    public KinesisCtrl getKinesisCtrl() {
        return kinesisCtrl;
    }

    public void setKinesisCtrl(KinesisCtrl kinesisCtrl) {
        this.kinesisCtrl = kinesisCtrl;
    }

}
