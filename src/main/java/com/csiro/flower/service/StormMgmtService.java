/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import java.util.List;

/**
 *
 * @author kho01f
 */
public interface StormMgmtService {

    public void initService(String provider, String accessKey, String secretKey, String region);

    public List<String> getRunningWorkerIds();

    public String getInstanceState(String instanceId);

    public List<String> getStoppedWorkerIds();

    public void startWorkers(int no);

    public void stopWorkers(int no);

    public void simpleRebalanceTopology(String topologyName);

    public void fairRebalanceTopology();

    public void buildStormClient(String nimbusIp);

    public String getClusterSummary(String uiHost, String uiPort);

    public String getSupervisorList(String uiHost, String uiPort);

    public String getTopologyList(String uiHost, String uiPort);

    public String getTopologyStats(String uiHost, String uiPort, String topologyId);

    public String getComponentStats(String uiHost, String uiPort, String topologyId, String componentId);
}
