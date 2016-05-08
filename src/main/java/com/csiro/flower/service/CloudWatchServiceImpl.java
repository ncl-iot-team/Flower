/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.csiro.flower.util.CloudServiceRegionUtil;
import org.springframework.stereotype.Service;
import com.csiro.flower.constant.CloudWatchConstant.*;
import java.util.Date;

/**
 *
 * @author kho01f
 */
@Service
public class CloudWatchServiceImpl implements CloudWatchService {

    final int oneMin = 60;
    String serviceName = "monitoring";
    AmazonCloudWatchClient client;

    @Override
    public void initService(String provider, String accessKey, String secretKey, String region) {

        String cloudwatchEndpoint = CloudServiceRegionUtil.resolveEndpoint(provider, serviceName, region);
        client = (new AmazonCloudWatchClient(
                new BasicAWSCredentials(accessKey, secretKey)));
        client.setEndpoint(cloudwatchEndpoint);
    }

    @Override
    public GetMetricStatisticsResult getCriticalResourceStats(String resource, String resourceId, String metric, long startTime) {

        GetMetricStatisticsResult result = null;
        switch (resource) {
            case "Kinesis":
                result = getResult(NameSpaces.KINESIS_NAMESPACE, Dimensions.STREAM_NAME_KEY, resourceId, metric, startTime);
                break;
//            case "EC2":
            case "Storm":
                result = getResult(NameSpaces.EC2_NAMESPACE, Dimensions.INSTANCE_ID, resourceId, metric, startTime);
                break;
            case "DynamoDB":
                result = getResult(NameSpaces.DYNAMODB_NAMESPACE, Dimensions.TABLE_NAME, resourceId, metric, startTime);
                break;
            case "ElastiCache":
                result = getResult(NameSpaces.ELASTICACHE_NAMESPACE, Dimensions.CACHE_CLUSTER_ID, resourceId, metric, startTime);
                break;
        }
        return result;
    }

    private GetMetricStatisticsResult getResult(String NameSpace, String DimensionName, String DimensionValue, String MetricName, long startTime) {
        Date endTime = new Date();
        GetMetricStatisticsRequest req = new GetMetricStatisticsRequest()
                .withStartTime(new Date(endTime.getTime() - startTime))
                .withNamespace(NameSpace)
                .withPeriod(oneMin)
                .withDimensions(new Dimension().withName(DimensionName).withValue(DimensionValue))
                .withMetricName(MetricName)
                .withStatistics(Stats.AVERAGE, Stats.MAX, Stats.MIN, Stats.SUM)
                .withEndTime(endTime);
        return client.getMetricStatistics(req);
    }
}
