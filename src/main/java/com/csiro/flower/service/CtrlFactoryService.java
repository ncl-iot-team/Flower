/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csiro.flower.service;

import org.springframework.stereotype.Service;

/**
 *
 * @author kho01f
 */
@Service
public abstract class CtrlFactoryService {
    abstract DynamoCtrlServiceImpl getDynamoCtrlServiceImpl();
    abstract KinesisCtrlServiceImpl getKinesisCtrlServiceImpl();
    abstract StormCtrlServiceImpl getStormCtrlServiceImpl();
}
