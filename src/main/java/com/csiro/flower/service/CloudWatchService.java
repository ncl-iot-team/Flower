/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;

/**
 *
 * @author kho01f
 */
public interface CloudWatchService {

    public void initService(String provider, String accessKey, String secretKey, String region);

    public GetMetricStatisticsResult getCriticalResourceStats(String resource, String resourceId, String metric, long startTime);

}
