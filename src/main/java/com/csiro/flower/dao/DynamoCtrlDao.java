/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.CtrlInternalSetting;
import com.csiro.flower.model.DynamoCtrl;
import java.util.List;

/**
 *
 * @author kho01f
 */
public interface DynamoCtrlDao {

    public void save(DynamoCtrl dynamoCtrl);

    public void delete(int id);

    public List<DynamoCtrl> get(int flowId);

    public void update(DynamoCtrl dynamoCtrl);

    public int getPkId(int flowId, String tbl);

    public DynamoCtrl get(int flowId, String resource, String measurementTarget);

    public CtrlInternalSetting getInternalSetting(int id);

    public void updateInternalSetting(int flowId, String tbl);
}
