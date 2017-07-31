/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.amazonaws.services.kinesis.model.Shard;
import com.csiro.flower.model.KinesisStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kho01f
 */
public interface KinesisMgmtService {

    public void initService(String provider, String accessKey, String secretKey, String region);

    public List<String> getStreamList();

    public void decreaseShards(String streamName, int mergeSize);

    public void increaseShards(String streamName, int splitSize);

    public List<Shard> getOpenShards(String streamName);

    public Map<String, Integer> getStreamShardMap();
    
    public List<KinesisStream> getStreamDetails();

}
