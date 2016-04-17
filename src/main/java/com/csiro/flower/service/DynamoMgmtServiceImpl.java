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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author kho01f
 */
@Service
public class DynamoMgmtServiceImpl implements DynamoMgmtService {

    DynamoDB dynamoDB;

    @Override
    public void DynamoMgmtService(String accessKey, String secretKey, String dynamoEndpoint) {
        AmazonDynamoDBClient client = (new AmazonDynamoDBClient(
                new BasicAWSCredentials(accessKey, secretKey)));
        client.setEndpoint(dynamoEndpoint);
        dynamoDB = new DynamoDB(client);
    }

    @Override
    public List<String> getTableList() {
        TableCollection<ListTablesResult> tables = dynamoDB.listTables();
        Iterator<Table> iterator = tables.iterator();
        List<String> tableNames = new ArrayList<String>();
        while (iterator.hasNext()) {
            Table table = iterator.next();
            tableNames.add(table.getTableName());
        }
        return tableNames;
    }

}
