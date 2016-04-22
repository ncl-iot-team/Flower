/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.constant;

/**
 *
 * @author kho01f
 */
public interface CloudWatchConstant {

    interface NameSpaces {

        String KINESIS_NAMESPACE = "AWS/Kinesis";
        String ELASTICACHE_NAMESPACE = "AWS/ElastiCache";
        String EC2_NAMESPACE = "AWS/EC2";
        String DYNAMODB_NAMESPACE = "AWS/DynamoDB";
    }

    interface Dimensions {

        String INSTANCE_ID = "InstanceId";
        String CACHE_CLUSTER_ID = "CacheClusterId";
        String STREAM_NAME_KEY = "StreamName";
        String TABLE_NAME = "TableName";
    }

    enum KinesisMetric {

        PUT_RECORD_BYTES("PutRecord.Bytes"),
        PUT_RECORD_LATENCY("PutRecord.Latency"),
        PUT_RECORD_SUCCESS("PutRecord.Success"),
        INCOMING_BYTES("IncomingBytes"),
        INCOMING_RECORDS("IncomingRecords"),
        GET_RECORD_BYTES("GetRecords.Bytes"),
        GET_RECORD_LATENCY("GetRecords.Latency"),
        GET_RECORDS_SUCCESS("GetRecords.Success");

        private String metric;

        KinesisMetric(String m) {
            metric = m;
        }

        public String getMetric() {
            return metric;
        }

    }

    enum EC2Metric {

        CPU_UTILIZATION("CPUUtilization"),
        NETWORK_IN("NetworkIn"),
        NETWORK_OUT("NetworkOut");

        private String metric;

        EC2Metric(String m) {
            metric = m;
        }

        public String getMetric() {
            return metric;
        }

    }

    enum ElastiCacheMetric {

        CPU_UTILIZATION("CPUUtilization"),
        FREEABLE_MEMORY("FreeableMemory"),
        NETWORK_BYTES_IN("NetworkBytesIn"),
        NETWORK_BYTES_OUT("NetworkBytesOut");

        private String metric;

        ElastiCacheMetric(String m) {
            metric = m;
        }

        public String getMetric() {
            return metric;
        }

    }

    interface Stats {

        String TIME_STAMP = "TimeStamp";
        String AVERAGE = "Average";
        String MAX = "Maximum";
        String MIN = "Minimum";
        String SUM = "Sum";
    }

    enum DynamoDBMetric {

        CONSUMED_READ_CAPACITY_UNITS("ConsumedReadCapacityUnits"),
        CONSUMED_WRITE_CAPACITY_UNITS("ConsumedWriteCapacityUnits"),
        PROVISIOINED_READ_CAPACITY_UNITS("ProvisionedReadCapacityUnits"),
        PROVISIOINED_WRITE_CAPACITY_UNITS("ProvisionedWriteCapacityUnits"),
        READ_THROTTLE_EVENTS("ReadThrottleEvents"),
        THROTTLED_REQUESTS("ThrottledRequests"),
        WRITE_THROTTLE_EVENTS("WriteThrottleEvents");

        private String metric;

        DynamoDBMetric(String m) {
            metric = m;
        }

        public String getMetric() {
            return metric;
        }
    }
}
