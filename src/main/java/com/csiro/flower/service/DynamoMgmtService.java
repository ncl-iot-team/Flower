/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputDescription;
import com.csiro.flower.model.DynamoDBTable;
import java.util.List;

/**
 *
 * @author kho01f
 */
public interface DynamoMgmtService {

    public void initService(String provider, String accessKey, String secretKey, String region);

    public List<String> getTableList();

    public void updateProvisionedThroughput(String tableName, long readCapacityUnit, long writeCapacityUnit);

    public ProvisionedThroughputDescription getProvisionedThroughput(String tableName);
    
    public List<DynamoDBTable> getDynamoDBTableDetail(); 
}
