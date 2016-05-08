/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.StormCluster;
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
public class StormClusterDaoImpl implements StormClusterDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(StormCluster stormCluster) {
        String sqlInsert = "INSERT INTO storm_cluster_tbl (flow_id_fk, nimbus_ip, "
                + "supervisor_prefix) VALUES (?,?,?)";
        Object[] params = new Object[]{stormCluster.getFlowIdFk(),
            stormCluster.getNimbusIp(),
            stormCluster.getSupervisorPrefix()
        };
        jdbcTemplate.update(sqlInsert, params);
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StormCluster get(int flowId) {
        String sqlSelect = "SELECT * FROM storm_cluster_tbl WHERE flow_id_fk=" + flowId;
        return jdbcTemplate.queryForObject(sqlSelect, new RowMapper<StormCluster>() {
            @Override
            public StormCluster mapRow(ResultSet result, int rowNum) throws SQLException {
                StormCluster stormCluster = new StormCluster();
                stormCluster.setNimbusIp(result.getString("nimbus_ip"));
                stormCluster.setSupervisorPrefix(result.getString("supervisor_prefix"));
                stormCluster.setFlowIdFk(result.getInt("flow_id_fk"));
                return stormCluster;
            }
        });
    }

    @Override
    public void update(StormCluster stormCluster) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
