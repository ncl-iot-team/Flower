/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import java.util.List;

/**
 *
 * @author kho01f
 */
public interface DynamoMgmtService {

    public void initService(String provider, String accessKey, String secretKey, String dynamoEndpoint);

    public List<String> getTableList();

}
