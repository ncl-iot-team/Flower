/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.Ctrl;
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
public class CtrlDaoImpl implements CtrlDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(Ctrl ctrl) {
        String sqlInsert = "INSERT INTO ctrl_service_tbl (flow_id_fk, resource_name, "
                + "ctrl_name, measurement_target, ref_value, monitoring_period, backoff_no,"
                + "upper_k0, up_init_k0, low_init_k0, lower_k0, k_init, gamma, creation_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params = new Object[]{
            ctrl.getFlowIdFk(),
            ctrl.getResourceName(),
            ctrl.getCtrlName(),
            ctrl.getMeasurementTarget(),
            ctrl.getRefValue(),
            ctrl.getMonitoringPeriod(),
            ctrl.getBackoffNo(),
            ctrl.getUpperK0(),
            ctrl.getUpInitK0(),
            ctrl.getLowInitK0(),
            ctrl.getLowerK0(),
            ctrl.getK_init(),
            ctrl.getGamma(),
            ctrl.getCreationDate()
        };
        jdbcTemplate.update(sqlInsert, params);
    }

    @Override
    public List<Ctrl> get(int flowId) {
        String sqlSelect = "SELECT * FROM ctrl_service_tbl WHERE flow_id_fk=" + flowId;
        List<Ctrl> ctrls = jdbcTemplate.query(sqlSelect, new BeanPropertyRowMapper(Ctrl.class));
        return ctrls;
    }

    @Override
    public void update(Ctrl ctrl) {
        // Update date feild needs also to be added....
        String sqlUpdate = "UPDATE ctrl_service_tbl SET ref_value = ?, monitoring_period = ?, backoff_no = ?,"
                + "upper_k0 = ?, up_init_k0 = ?, low_init_k0 = ?, lower_k0 = ?, k_init = ?, gamma = ? "
                + "WHERE flow_id_fk = ? AND resource_name = ? AND ctrl_name = ? AND measurement_target = ?";
        Object[] params = new Object[]{
            ctrl.getRefValue(),
            ctrl.getMonitoringPeriod(),
            ctrl.getBackoffNo(),
            ctrl.getUpperK0(),
            ctrl.getUpInitK0(),
            ctrl.getLowInitK0(),
            ctrl.getLowerK0(),
            ctrl.getK_init(),
            ctrl.getGamma(),
            ctrl.getFlowIdFk(),
            ctrl.getResourceName(),
            ctrl.getCtrlName(),
            ctrl.getMeasurementTarget()
        };
        jdbcTemplate.update(sqlUpdate, params);
    }

    @Override
    public int getPkId(int flowId, String ctrlName, String resource, String measurementTarget) {
         String sqlSelect = "SELECT id FROM ctrl_service_tbl WHERE "
                + "flow_id_fk = ? AND ctrl_name = ? AND resource_name = ? AND measurement_target = ?";
        int id = (int) jdbcTemplate.queryForObject(sqlSelect,
                new Object[]{flowId, ctrlName, resource, measurementTarget}, Integer.class);
        return id;
    }

    @Override
    public Ctrl get(int flowId, String ctrlName, String resource, String measurementTarget) {
        String sqlSelect = "SELECT * FROM ctrl_service_tbl WHERE "
                + "flow_id_fk = ? AND ctrl_name = ? AND resource_name = ? AND measurement_target = ?";
        Ctrl ctrl = (Ctrl) jdbcTemplate.queryForObject(sqlSelect,
                new Object[]{flowId, ctrlName, resource, measurementTarget}, new BeanPropertyRowMapper(Ctrl.class));
        return ctrl;
    }

    @Override
    public List<Ctrl> get(String user) {
//        String sqlSelect = "SELECT ";
        return null;
    }

}
