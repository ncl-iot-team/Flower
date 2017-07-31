/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.DescribeStreamRequest;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.MergeShardsRequest;
import com.amazonaws.services.kinesis.model.ResourceNotFoundException;
import com.amazonaws.services.kinesis.model.Shard;
import com.amazonaws.services.kinesis.model.SplitShardRequest;
import com.csiro.flower.model.KinesisStream;
import com.csiro.flower.util.HashMapUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author kho01f
 */
@Service
@Scope("prototype")
public class KinesisMgmtServiceImpl implements KinesisMgmtService {

    private final String activeStatus = "ACTIVE";
    private AmazonKinesis kinesis;

    @Override
    public void initService(String provider, String accessKey, String secretKey, String regionName) {
//        String serviceName = "kinesis";
//        String kinesisEndpoint = cloudServiceRegionMgmt.resolveEndpoint(provider, serviceName, regionName);
        kinesis = new AmazonKinesisClient(new BasicAWSCredentials(accessKey, secretKey));
        Region region = RegionUtils.getRegion(regionName);
        kinesis.setRegion(region);
    }

    @Override
    public List<String> getStreamList() {
        return kinesis.listStreams().getStreamNames();
    }
    
    @Override
    public List<KinesisStream> getStreamDetails(){
        List<KinesisStream> streamList = new ArrayList<KinesisStream>();
        for(String stream: getStreamList()){
            streamList.add(new KinesisStream(stream, getOpenShards(stream).size(), getClosedShards(stream).size()));
        } 
        return streamList;
    }

    // decreaseShards method merges shards together to reduce the number of shards
    @Override
    public void decreaseShards(String streamName, int mergeSize) {

        for (int i = 0; i < mergeSize; i++) {

            List<Shard> shards = getTwoAdjacentShards(streamName);

            MergeShardsRequest mergeShardsRequest = new MergeShardsRequest();
            mergeShardsRequest.setStreamName(streamName);
            mergeShardsRequest.setShardToMerge(shards.get(0).getShardId());
            mergeShardsRequest.setAdjacentShardToMerge(shards.get(1).getShardId());

            kinesis.mergeShards(mergeShardsRequest);
//            LOGGER.info("Decreasing the number of Shards...");
            waitForReshardingTransition(streamName);
        }
    }

    // increaseShards method splits shards to boost the number of shards
    @Override
    public void increaseShards(String streamName, int splitSize) {

        for (int i = 0; i < splitSize; i++) {

            List<Shard> shards = getOpenShards(streamName);
            SplitShardRequest splitShardRequest = new SplitShardRequest();
            splitShardRequest.setStreamName(streamName);
            splitShardRequest.setShardToSplit(shards.get(i).getShardId());

            BigInteger startingHashKey = new BigInteger(shards.get(i).getHashKeyRange().getStartingHashKey());
            BigInteger endingHashKey = new BigInteger(shards.get(i).getHashKeyRange().getEndingHashKey());
            String newStartingHashKey = startingHashKey.add(endingHashKey).divide(new BigInteger("2")).toString();

            splitShardRequest.setNewStartingHashKey(newStartingHashKey);
            kinesis.splitShard(splitShardRequest);
//            LOGGER.info("Increasing the number of Shards...");
            waitForReshardingTransition(streamName);
        }
    }

    public List<Shard> getTwoAdjacentShards(String streamName) {
        List<Shard> adjacentShards = new ArrayList<Shard>();
        BigInteger shard1End, shard2Start;
        Map<Shard, BigInteger> sortedShardsByHashKeyRange = HashMapUtil.sortShardBigIntHashMap(getOpenShardsWithEndingHashKey(streamName), true);

        OuterLoop:
        for (Map.Entry<Shard, BigInteger> shard1 : sortedShardsByHashKeyRange.entrySet()) {
            for (Map.Entry<Shard, BigInteger> shard2 : sortedShardsByHashKeyRange.entrySet()) {
                shard1End = new BigInteger(shard1.getKey().getHashKeyRange().getEndingHashKey());
                shard2Start = new BigInteger(shard2.getKey().getHashKeyRange().getStartingHashKey());
                if (shard1End.subtract(shard2Start).abs().equals(BigInteger.ONE)) {
                    adjacentShards.add(shard1.getKey());
                    adjacentShards.add(shard2.getKey());
                    break OuterLoop;
                }
            }
        }
        return adjacentShards;
    }

    public HashMap<Shard, BigInteger> getOpenShardsWithEndingHashKey(String streamName) {
        List<Shard> shards = getListOfShards(streamName);
        HashMap<Shard, BigInteger> shardsWithEndingHashKeyMap = new HashMap<Shard, BigInteger>();

        for (Shard shard : shards) {
            // Shard is OPEN if getEndingSequenceNumber is NULL
            if (null == shard.getSequenceNumberRange().getEndingSequenceNumber()) {
                shardsWithEndingHashKeyMap.put(shard, new BigInteger(shard.getHashKeyRange().getEndingHashKey()));
            }
        }
        return shardsWithEndingHashKeyMap;
    }

    public void waitForReshardingTransition(String streamName) {
        DescribeStreamRequest describeStreamRequest = new DescribeStreamRequest();
        describeStreamRequest.setStreamName(streamName);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (10 * 60 * 1000);
        while (System.currentTimeMillis() < endTime) {
            try {
                Thread.sleep(20 * 1000);
            } catch (Exception e) {
            }

            try {
                DescribeStreamResult describeStreamResponse = kinesis.describeStream(describeStreamRequest);
                String streamStatus = describeStreamResponse.getStreamDescription().getStreamStatus();
                if (streamStatus.equals(activeStatus)) {
                    break;
                }

                // sleep for one second
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            } catch (ResourceNotFoundException e) {
            }
        }
        if (System.currentTimeMillis() >= endTime) {
            throw new RuntimeException("Stream " + streamName + " never went active");
        }
    }

    @Override
    public List<Shard> getOpenShards(String streamName) {
        List<Shard> shards = getListOfShards(streamName);
        List<Shard> openShards = new ArrayList<Shard>();

        for (Shard shard : shards) {
            // Shard is OPEN if getEndingSequenceNumber is NULL
            if (null == shard.getSequenceNumberRange().getEndingSequenceNumber()) {
                openShards.add(shard);
            }
        }
        return openShards;
    }
    
        public List<Shard> getClosedShards(String streamName) {
        List<Shard> shards = getListOfShards(streamName);
        List<Shard> closedShards = new ArrayList<Shard>();

        for (Shard shard : shards) {
            // Shard is OPEN if getEndingSequenceNumber is NULL
            if (null != shard.getSequenceNumberRange().getEndingSequenceNumber()) {
                closedShards.add(shard);
            }
        }
        return closedShards;
    }

    public List<Shard> getListOfShards(String streamName) {
        DescribeStreamRequest describeStreamRequest = new DescribeStreamRequest();
        describeStreamRequest.setStreamName(streamName);
        List<Shard> shards = new ArrayList<Shard>();
        String exclusiveStartShardId = null;
        do {
            describeStreamRequest.setExclusiveStartShardId(exclusiveStartShardId);
            DescribeStreamResult describeStreamResult = kinesis.describeStream(describeStreamRequest);
            shards.addAll(describeStreamResult.getStreamDescription().getShards());
            if (describeStreamResult.getStreamDescription().getHasMoreShards() && shards.size() > 0) {
                exclusiveStartShardId = shards.get(shards.size() - 1).getShardId();
            } else {
                exclusiveStartShardId = null;
            }
        } while (exclusiveStartShardId != null);

        return shards;
    }

    @Override
    public Map<String, Integer> getStreamShardMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
