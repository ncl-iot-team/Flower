/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.CloudSetting;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kho01f
 */
@Repository
public class CloudSettingDaoImpl implements CloudSettingDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    public void setDatasource(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
    @Override
    public void save(CloudSetting cloudSetting) {
        String sqlInsert = "INSERT INTO cloud_setting_tbl (flow_id_fk, cloud_provider, "
                + "region, accesskey, secretkey) VALUES (?,?,?,?,?)";
        Object[] params = new Object[]{cloudSetting.getFlowIdFk(),
            cloudSetting.getCloudProvider(),
            cloudSetting.getRegion(),
            cloudSetting.getAccessKey(),
            cloudSetting.getSecretKey()};

        jdbcTemplate.update(sqlInsert, params);
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloudSetting get(int flowId) {
        String sqlSelect = "SELECT * FROM cloud_setting_tbl WHERE flow_id_fk=" + flowId;
        try {
            return jdbcTemplate.queryForObject(sqlSelect, new RowMapper<CloudSetting>() {
                @Override
                public CloudSetting mapRow(ResultSet result, int rowNum) throws SQLException {
                    CloudSetting cloudSetting = new CloudSetting();
                    cloudSetting.setAccessKey(result.getString("accesskey"));
                    cloudSetting.setSecretKey(result.getString("secretkey"));
                    cloudSetting.setCloudProvider(result.getString("cloud_provider"));
                    cloudSetting.setRegion(result.getString("region"));
                    cloudSetting.setFlowIdFk(result.getInt("flow_id_fk"));
                    return cloudSetting;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(CloudSetting cloudSetting) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
