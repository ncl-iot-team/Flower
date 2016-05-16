/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.DynamoCtrl;
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
public class DynamoCtrlDaoImpl implements DynamoCtrlDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(DynamoCtrl dynamoCtrl) {
        String sqlInsert = "INSERT INTO dynamodb_ctrl_tbl (flow_id_fk, table_name, "
                + "measurement_target, ref_value, monitoring_period, backoff_no) VALUES (?,?,?,?,?,?)";
        Object[] params = new Object[]{
            dynamoCtrl.getFlowIdFk(),
            dynamoCtrl.getTableName(),
            dynamoCtrl.getMeasurementTarget(),
            dynamoCtrl.getRefValue(),
            dynamoCtrl.getMonitoringPeriod(),
            dynamoCtrl.getBackoffNo()
        };

        jdbcTemplate.update(sqlInsert, params);
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DynamoCtrl> get(int flowId) {
        String sqlSelect = "SELECT * FROM dynamodb_ctrl_tbl WHERE flow_id_fk=" + flowId;
        List<DynamoCtrl> dynamoCtrls = jdbcTemplate.query(sqlSelect, new BeanPropertyRowMapper(DynamoCtrl.class));
        return dynamoCtrls;
    }

    @Override
    public void update(DynamoCtrl dynamoCtrl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getPkId(int flowId, String tbl) {
        String sqlSelect = "SELECT id FROM dynamodb_ctrl_tbl WHERE "
                + "flow_id_fk = ? AND table_name = ?";
        int id = (int) jdbcTemplate.queryForObject(sqlSelect,
                new Object[]{flowId, tbl}, Integer.class);
        return id;
    }

    @Override
    public DynamoCtrl get(int flowId, String tbl, String measurementTarget) {
        String sqlSelect = "SELECT id FROM dynamodb_ctrl_tbl WHERE "
                + "flow_id_fk = ? AND table_name = ? AND measurement_target=?";
        DynamoCtrl dynamoCtrl = (DynamoCtrl) jdbcTemplate.queryForObject(sqlSelect,
                new Object[]{flowId, tbl, measurementTarget}, DynamoCtrl.class);
        return dynamoCtrl;
    }

}
