/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csiro.flower.service;

import backtype.storm.generated.Nimbus;
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
    public void simpleRebalanceTopology();
    public void fairRebalanceTopology();
    public Nimbus.Client getStormClient();
}
