/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.model;

/**
 *
 * @author alireza
 */
public class DynamoDBTable {
    private String tableName;
    private Long provisionedReadThroughput;
    private Long provisionedWriteThroughput;
    
    public DynamoDBTable(String tableName, Long provisionedReadThroughput, Long provisionedWriteThroughput){
        this.tableName = tableName;
        this.provisionedReadThroughput = provisionedReadThroughput;
        this.provisionedWriteThroughput = provisionedWriteThroughput;
    }

    public String getTableName() {
        return tableName;
    }

    public Long getProvisionedReadThroughput() {
        return provisionedReadThroughput;
    }
    
    public Long getProvisionedWriteThroughput() {
        return provisionedWriteThroughput;
    }
}
