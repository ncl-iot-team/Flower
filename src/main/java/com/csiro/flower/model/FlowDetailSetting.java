/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csiro.flower.model;

import java.util.List;

/**
 *
 * @author kho01f
 */
public class FlowDetailSetting {
    
    private CloudSetting cloudSetting;
    private List<Ctrl> ctrls;
    private StormCluster stormCluster;

    public List<Ctrl> getCtrls() {
        return ctrls;
    }

    public void setCtrls(List<Ctrl> ctrls) {
        this.ctrls = ctrls;
    }

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
//
//    public StormCtrl getStormCtrl() {
//        return stormCtrl;
//    }
//
//    public void setStormCtrl(StormCtrl stormCtrl) {
//        this.stormCtrl = stormCtrl;
//    }
//
//    public List<DynamoCtrl> getDynamoCtrls() {
//        return dynamoCtrls;
//    }
//
//    public void setDynamoCtrls(List<DynamoCtrl> dynamoCtrls) {
//        this.dynamoCtrls = dynamoCtrls;
//    }
//
//    public List<KinesisCtrl> getKinesisCtrls() {
//        return kinesisCtrls;
//    }
//
//    public void setKinesisCtrls(List<KinesisCtrl> kinesisCtrls) {
//        this.kinesisCtrls = kinesisCtrls;
//    }

}
