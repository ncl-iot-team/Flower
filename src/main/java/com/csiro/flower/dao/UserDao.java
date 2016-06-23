/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csiro.flower.dao;

import com.csiro.flower.model.UserAccount;
import com.csiro.flower.model.UserLogin;

/**
 *
 * @author kho01f
 */
public interface UserDao {
    public void createAccount(UserAccount userAccount);
    public int saveLogin(int userId);
    public void saveLogout(int userId);
}
