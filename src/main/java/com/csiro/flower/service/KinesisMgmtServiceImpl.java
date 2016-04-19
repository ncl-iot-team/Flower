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
import com.csiro.flower.util.CloudServiceRegionMgmt;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kho01f
 */
@Service
public class KinesisMgmtServiceImpl implements KinesisMgmtService {

    @Autowired
    CloudServiceRegionMgmt cloudServiceRegionMgmt;

    AmazonKinesis kinesis;

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

}
