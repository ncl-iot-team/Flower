/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.StormCluster;

/**
 *
 * @author kho01f
 */
public interface StormClusterDao {

    public void save(StormCluster stormCluster);

    public void delete(int id);

    public StormCluster get(int flowId);

    public void update(StormCluster stormCluster);

}
