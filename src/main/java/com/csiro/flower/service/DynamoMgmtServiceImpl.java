/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputDescription;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.csiro.flower.model.DynamoDBTable;
import com.csiro.flower.util.CloudServiceRegionUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author kho01f
 */
@Service
@Scope("prototype")
public class DynamoMgmtServiceImpl implements DynamoMgmtService {

    private DynamoDB dynamoDB;
    private final String serviceName = "dynamodb";

    @Override
    public void initService(String provider, String accessKey, String secretKey, String region) {

        String dynamoEndpoint = CloudServiceRegionUtil.resolveEndpoint(provider, serviceName, region);
        AmazonDynamoDBClient client = (new AmazonDynamoDBClient(
                new BasicAWSCredentials(accessKey, secretKey)));
        client.setEndpoint(dynamoEndpoint);
        dynamoDB = new DynamoDB(client);
    }

    @Override
    public List<String> getTableList() {
        TableCollection<ListTablesResult> tables = dynamoDB.listTables();
        Iterator<Table> iterator = tables.iterator();
        List<String> tableNames = new ArrayList<>();
        while (iterator.hasNext()) {
            Table table = iterator.next();
            tableNames.add(table.getTableName());
        }
        return tableNames;
    }

    @Override
    public ProvisionedThroughputDescription getProvisionedThroughput(String tableName) {
        TableDescription tableDescription = dynamoDB.getTable(tableName).describe();
        return tableDescription.getProvisionedThroughput();
    }
    
    public List<DynamoDBTable> getDynamoDBTableDetail(){
        List<DynamoDBTable> tableList = new ArrayList<DynamoDBTable>();
        for(String tbl: getTableList()){
            tableList.add(new DynamoDBTable(tbl, 
                    dynamoDB.getTable(tbl).describe().getProvisionedThroughput().getReadCapacityUnits(), 
                    dynamoDB.getTable(tbl).describe().getProvisionedThroughput().getWriteCapacityUnits()));
        }
        return tableList;
    }

    @Override
    public void updateProvisionedThroughput(String tableName, long readCapacityUnit, long writeCapacityUnit) {
        Table table = dynamoDB.getTable(tableName);
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput().
                withReadCapacityUnits(readCapacityUnit).
                withWriteCapacityUnits(writeCapacityUnit);
        table.updateTable(provisionedThroughput);
//        LOGGER.info("Updaing the Provisioned Throughputs of DynamoDB Tbl...");
        try {
            table.waitForActive();
        } catch (InterruptedException ex) {
            Logger.getLogger(DynamoMgmtServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
