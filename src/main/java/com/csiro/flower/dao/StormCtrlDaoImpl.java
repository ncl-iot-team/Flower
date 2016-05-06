/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.StormCtrl;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kho01f
 */
@Repository
public class StormCtrlDaoImpl implements StormCtrlDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDatasource(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(StormCtrl stormCtrl) {
        String sqlInsert = "INSERT INTO storm_ctrl_tbl (flow_id_fk, topology_name, "
                + "measurement_target, ref_value, monitoring_period, backoff_no) VALUES (?,?,?,?,?,?)";
        Object[] params = new Object[]{stormCtrl.getFlowIdFk(),
            stormCtrl.getTargetTopology(),
            stormCtrl.getMeasurementTarget(),
            stormCtrl.getRefValue(),
            stormCtrl.getMonitoringPeriod(),
            stormCtrl.getBackoffNo()
        };
        jdbcTemplate.update(sqlInsert, params);
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StormCtrl get(int flowId) {
        String sqlSelect = "SELECT * FROM storm_ctrl_tbl WHERE flow_id_fk=" + flowId;
        return jdbcTemplate.queryForObject(sqlSelect, new RowMapper<StormCtrl>() {
            @Override
            public StormCtrl mapRow(ResultSet result, int rowNum) throws SQLException {
                StormCtrl stormCtrl = new StormCtrl();
                stormCtrl.setMeasurementTarget(result.getString("measurement_target"));
                stormCtrl.setMonitoringPeriod(Integer.parseInt(result.getString("monitoring_period")));
                stormCtrl.setBackoffNo(Integer.parseInt(result.getString("backoff_no")));
                stormCtrl.setRefValue(Integer.parseInt(result.getString("ref_value")));
                stormCtrl.setTargetTopology(result.getString("topology_name"));
                stormCtrl.setFlowIdFk(result.getInt("flow_id_fk"));
                return stormCtrl;
            }
        });

    }

    @Override
    public void update(StormCtrl stormCtrl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getPkId(int flowId, String topology) {
        String sqlSelect = "SELECT id FROM storm_ctrl_tbl WHERE "
                + "flow_id_fk = ? AND topology_name = ?";
        int id = (int) jdbcTemplate.queryForObject(sqlSelect,
                new Object[]{flowId, topology}, int.class);
        return id;
    }

}
