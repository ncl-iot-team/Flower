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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
    public void saveCtrlStatus(int ctrlFkId, String ctrlName, String ctrlStatus, Timestamp date) {
        String sqlInsert = "INSERT INTO ctrl_status_track (ctrl_fk_id, ctrl_name,"
                + "ctrl_status, start_date) VALUES (?,?,?,?)";
        Object[] params = new Object[]{
            ctrlFkId, ctrlName, ctrlStatus, date
        };
        jdbcTemplate.update(sqlInsert, params);
    }

    @Override
    public void updateCtrlStatus(int ctrlFkId, String ctrlName, String ctrlStatus, Timestamp date) {
        String sqlUpdate = "UPDATE ctrl_status_track SET ctrl_status = ?, stop_date=? "
                + "WHERE start_date = (SELECT MAX(start_date) FROM "
                + "ctrl_status_track WHERE ctrl_fk_id=? AND ctrl_name=?)";
        Object[] params = new Object[]{
            ctrlStatus, date, ctrlFkId, ctrlName
        };
        jdbcTemplate.update(sqlUpdate, params);
    }

    @Override
    public String getCtrlStatus(int ctrlFkId, String ctrlName) {
        String sqlSelect = "SELECT ctrl_status FROM ctrl_status_track "
                + "WHERE start_date = (SELECT MAX(start_date) FROM "
                + "ctrl_status_track WHERE ctrl_fk_id=? AND ctrl_name=?) ";
        String ctrlStatus = (String) jdbcTemplate.queryForObject(
                sqlSelect, new Object[]{ctrlFkId, ctrlName}, String.class);
        return ctrlStatus;
    }

//    @Override
//    public void updateCtrlStatus(int ctrlFkId, String ctrlName, String ctrlStatus, Timestamp date) {
//        String sqlUpdate = "UPDATE ctrl_status_track SET ctrl_status = ?, stop_date=? "
//                + "WHERE ctrl_fk_id=? AND ctrl_name=?";
//        Object[] params = new Object[]{
//            ctrlStatus, date, ctrlFkId, ctrlName
//        };
//        jdbcTemplate.update(sqlUpdate, params);
//    }
//
//    @Override
//    public String getCtrlStatus(int ctrlFkId, String ctrlName) {
//        String sqlSelect = "SELECT ctrl_status FROM ctrl_status_track "
//                + "WHERE ctrl_fk_id = ? AND ctrl_name= ? ";
//        String ctrlStatus = (String) jdbcTemplate.queryForObject(
//                sqlSelect, new Object[]{ctrlFkId, ctrlName}, String.class);
//        return ctrlStatus;
//    }
    @Override
    public void saveCtrlMonitoringStats(int ctrlFkId, String ctrlName,
            double error, long date, double k0,
            double measurementTargetValue, double uk0, double uk1, int roundedUk1) {
        String sqlInsert = "INSERT INTO ctrl_monitor_tbl (ctrl_fk_id, ctrl_name,"
                + " error, date_created, k0, measurement_target_value, uk0, uk1, round_uk1)"
                + " VALUES (?,?,?,?,?,?,?,?,?)";
        Object[] params = new Object[]{
            ctrlFkId, ctrlName, error, date, k0,
            measurementTargetValue, uk0, uk1, roundedUk1
        };
        jdbcTemplate.update(sqlInsert, params);
    }

    @Override
    public List<CtrlMonitoringResultSet> getCtrlMonitoringStats(int ctrlFkId,
            String ctrlName, long startDate) {
        String sqlSelect = "SELECT measurement_target_value AS measurementTargetValue,"
                + " uk0 AS allocatedResource, error, round_uk1 AS nextCtrlDecisionValue,"
                + " date_created AS timeStamp, ctrl_name FROM ctrl_monitor_tbl WHERE ctrl_fk_id = ? "
                + "AND ctrl_name = ? AND date_created >= ?";
        List<CtrlMonitoringResultSet> result = jdbcTemplate.query(sqlSelect,
                new Object[]{ctrlFkId, ctrlName, startDate},
                new BeanPropertyRowMapper(CtrlMonitoringResultSet.class));
        return result;
    }

}
