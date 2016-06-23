/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csiro.flower.model;

import java.sql.Timestamp;

/**
 *
 * @author kho01f
 */
public class UserLogin {
    private Timestamp loginDate;
    private Timestamp logoutDate;
    private int userFkId;

    public Timestamp getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Timestamp loginDate) {
        this.loginDate = loginDate;
    }

    public Timestamp getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Timestamp logoutDate) {
        this.logoutDate = logoutDate;
    }

    public int getUserFkId() {
        return userFkId;
    }

    public void setUserFkId(int userFkId) {
        this.userFkId = userFkId;
    }
    
}
