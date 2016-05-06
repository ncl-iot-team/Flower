/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.StormCtrl;

/**
 *
 * @author kho01f
 */
public interface StormCtrlDao {

    public void save(StormCtrl stormCtrl);

    public void delete(int id);

    public StormCtrl get(int flowId);

    public void update(StormCtrl stormCtrl);

    public int getPkId(int flowId, String topology);
}
