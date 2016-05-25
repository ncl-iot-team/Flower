/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.CtrlInternalSetting;
import com.csiro.flower.model.StormCtrl;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kho01f
 */
@Repository
public class StormCtrlDaoImpl implements StormCtrlDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(StormCtrl stormCtrl) {
        String sqlInsert = "INSERT INTO storm_ctrl_tbl (flow_id_fk, target_topology, "
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
                stormCtrl.setTargetTopology(result.getString("target_topology"));
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
                + "flow_id_fk = ? AND target_topology = ?";
        int id = (int) jdbcTemplate.queryForObject(sqlSelect,
                new Object[]{flowId, topology}, Integer.class);
        return id;
    }

    @Override
    public StormCtrl get(int flowId, String measurementTarget) {
//        String sqlSelect = "SELECT * FROM storm_ctrl_tbl "
//                + "WHERE flow_id_fk=? AND measurement_target=?";
        String sqlSelect = "SELECT * FROM storm_ctrl_tbl "
                + "WHERE flow_id_fk=? AND measurement_target=?";
        StormCtrl stormCtrl = (StormCtrl) jdbcTemplate.queryForObject(sqlSelect,
                new Object[]{flowId, measurementTarget},
                new BeanPropertyRowMapper(StormCtrl.class));
        return stormCtrl;
    }

    @Override
    public CtrlInternalSetting getInternalSetting(int id) {
        String sqlSelect = "SELECT epsilon, upper_k0, up_init_k0, low_init_k0, lower_k0, k_init, gamma "
                + "FROM storm_ctrl_tbl WHERE id=?";
        CtrlInternalSetting ctrlInternalSetting = (CtrlInternalSetting) jdbcTemplate.queryForObject(sqlSelect,
                new Object[]{id}, new BeanPropertyRowMapper(CtrlInternalSetting.class));
        return ctrlInternalSetting;
    }

    @Override
    public void updateInternalSetting(int flowId, String measurementTarget) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
