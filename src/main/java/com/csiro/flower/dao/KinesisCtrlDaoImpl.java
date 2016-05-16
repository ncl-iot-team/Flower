/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.KinesisCtrl;
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
public class KinesisCtrlDaoImpl implements KinesisCtrlDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(KinesisCtrl kinesisCtrl) {
        String sqlInsert = "INSERT INTO kinesis_ctrl_tbl (flow_id_fk, stream_name, "
                + "measurement_target, ref_value, monitoring_period, backoff_no) VALUES (?,?,?,?,?,?)";
        Object[] params = new Object[]{kinesisCtrl.getFlowIdFk(),
            kinesisCtrl.getStreamName(),
            kinesisCtrl.getMeasurementTarget(),
            kinesisCtrl.getRefValue(),
            kinesisCtrl.getMonitoringPeriod(),
            kinesisCtrl.getBackoffNo()
        };

        jdbcTemplate.update(sqlInsert, params);
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<KinesisCtrl> get(int flowId) {
        String sqlSelect = "SELECT * FROM kinesis_ctrl_tbl WHERE flow_id_fk=" + flowId;
        List<KinesisCtrl> kinesisCtrls = jdbcTemplate.query(sqlSelect,
                new BeanPropertyRowMapper(KinesisCtrl.class));
        return kinesisCtrls;
    }

    @Override
    public void update(KinesisCtrl kinesisCtrl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getPkId(int flowId, String stream) {
        String sqlSelect = "SELECT id FROM kinesis_ctrl_tbl WHERE "
                + "flow_id_fk = ? AND stream_name = ?";
        int id = (int) jdbcTemplate.queryForObject(sqlSelect,
                new Object[]{flowId, stream}, Integer.class);
        return id;
    }

    @Override
    public KinesisCtrl get(int flowId, String stream, String measurementTarget) {
        String sqlSelect = "SELECT * FROM kinesis_ctrl_tbl WHERE "
                + " flow_id_fk = ? AND stream_name = ? AND measurement_target = ?";
        KinesisCtrl kinesisCtrl = (KinesisCtrl) jdbcTemplate.queryForObject(sqlSelect,
                new Object[]{flowId, stream, measurementTarget},
                new BeanPropertyRowMapper(KinesisCtrl.class));
        return kinesisCtrl;
    }
}
