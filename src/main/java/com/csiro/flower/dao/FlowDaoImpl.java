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
public class FlowDaoImpl implements FlowDao {

    I datasource;

    public void setDatasource(I datasource) {
        this.datasource = datasource;
    }

    @Override
    public List<Flow> getAll() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(Flow flow) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(int flowId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Flow get(int flowId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
