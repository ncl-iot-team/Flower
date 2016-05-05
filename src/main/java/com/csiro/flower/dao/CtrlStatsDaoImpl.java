/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.CtrlMonitoringResultSet;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kho01f
 */
@Repository
public class CtrlStatsDaoImpl implements CtrlStatsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveCtrlStatus(int ctrlFkId, String ctrlName, String ctrlStatus, long threadId, Timestamp date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCtrlStatus(int ctrlFkId, String ctrlName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveCtrlMonitoringStats(int ctrlFkId, String ctrlName, double error, Timestamp date, double k0, double measurementTargetValue, double uk0, double uk1, int roundedUk1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CtrlMonitoringResultSet> getCtrlMonitoringStats(Timestamp startDate, int ctrlFkId, String ctrlName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
