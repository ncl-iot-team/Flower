/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.UserAccount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
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
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int saveAccount(UserAccount userAccount) {
        int enabled = 1;
        String sqlInsert = "INSERT INTO user_account (user_name, user_email, registration_date, password, enabled) VALUES (?,?,?,?,?)";
        Object[] params = new Object[]{
            userAccount.getUserName(),
            userAccount.getUserEmail(),
            userAccount.getRegistrationDate(),
            userAccount.getPassword(),
            enabled
        };
        return jdbcTemplate.update(sqlInsert, params);
    }

    @Override
    public boolean checkDuplicateEmail(String email) {
        String sqlSelect = "SELECT COUNT(*) FROM user_account WHERE user_email = '" + email + "'";
        Integer count = jdbcTemplate.queryForObject(sqlSelect, Integer.class);
        return count > 0;
    }

    @Override
    public int saveLogin(int userId) {
        String sqlInsert = "INSERT INTO user_login (user_fk_id, login_date) VALUES (?,?)";
        return jdbcTemplate.update(sqlInsert, new Object[]{userId, new Timestamp(new Date().getTime())});
    }

    @Override
    public UserAccount getAccountCredentials(String email, String password) {
        String sqlSelect = "SELECT * FROM user_account WHERE user_email = ? AND password = ?";
        Object[] params = new Object[]{
            email, password
        };
        try {
            return jdbcTemplate.queryForObject(sqlSelect, params, new RowMapper<UserAccount>() {
                @Override
                public UserAccount mapRow(ResultSet result, int rowNum) throws SQLException {
                    UserAccount userAccount = new UserAccount();
                    userAccount.setUserId(result.getInt("user_id"));
                    userAccount.setUserEmail(result.getString("user_email"));
                    return userAccount;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int saveLogout(int userId) {
        String sqlUpdate = "UPDATE user_login SET logout_date = ? WHERE user_fk_id = ?";
        return jdbcTemplate.update(sqlUpdate, new Object[]{new Timestamp(new Date().getTime()), userId});
    }

    @Override
    public int saveUserRole(String user, String role) {
        String sqlInsert = "INSERT INTO user_role (user_email, role) VALUES (?,?)";
        return jdbcTemplate.update(sqlInsert, new Object[]{user, role});
    }

}
