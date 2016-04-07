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
public class StormCluster {

    private String nimbusIp;
    private String zookeeperEndpoint;
    private int flowIdFk;
    private String supervisorPrefix;

    public int getFlowIdFk() {
        return flowIdFk;
    }

    public void setFlowIdFk(int flowIdFk) {
        this.flowIdFk = flowIdFk;
    }

//    private String supervisorPrefix;
    public String getNimbusIp() {
        return nimbusIp;
    }

    public void setNimbusIp(String nimbusIp) {
        this.nimbusIp = nimbusIp;
    }

    public String getZookeeperEndpoint() {
        return zookeeperEndpoint;
    }

    public void setZookeeperEndpoint(String zookeeperEndpoint) {
        this.zookeeperEndpoint = zookeeperEndpoint;
    }

    public String getSupervisorPrefix() {
        return supervisorPrefix;
    }

    public void setSupervisorPrefix(String supervisorPrefix) {
        this.supervisorPrefix = supervisorPrefix;
    }

}
