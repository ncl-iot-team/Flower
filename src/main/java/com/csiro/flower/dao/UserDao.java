/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.UserAccount;

/**
 *
 * @author kho01f
 */
public interface UserDao {

    public int saveAccount(UserAccount userAccount);

    public int saveLogin(int userId);

    public int saveLogout(int userId);

    public boolean checkDuplicateEmail(String email);

    public UserAccount getAccountCredentials(String email, String password);
}
