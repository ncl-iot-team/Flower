/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.Flow;
import java.util.List;

/**
 *
 * @author kho01f
 */
public interface FlowDao {

    public List<Flow> getAll() throws Exception;

    public int save(Flow flow);

    public void delete(int flowId);

    public Flow get(int flowId);
    
    public void update(Flow flow);
}
