/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.dao.UserDao;
import com.csiro.flower.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kho01f
 */
@Repository
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public boolean createAccount(UserAccount userAccount) {
        String role = "USER_ROLE";
        boolean successRegistration = false;
        if (!userDao.checkDuplicateEmail(userAccount.getUserEmail())) {
            successRegistration = (userDao.saveAccount(userAccount) > 0 && userDao.saveUserRole(userAccount.getUserEmail(), role) > 0);
        }
        return successRegistration;
    }

    @Override
    public boolean login(String email, String pass) {
        boolean successLogin = false;
        UserAccount userAccount = userDao.getAccountCredentials(email, pass);
        if (userAccount != null) {
            successLogin = userDao.saveLogin(userAccount.getUserId()) > 0;
        }
        return successLogin;
    }

    @Override
    public boolean logout(int userId) {
        return (userDao.saveLogout(userId) > 0);
    }

}
