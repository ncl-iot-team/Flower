/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csiro.flower.service;

import com.csiro.flower.model.UserAccount;

/**
 *
 * @author kho01f
 */
public interface UserService {
    public boolean createAccount(UserAccount userAccount);
    public boolean login(String email, String pass);
    public boolean logout(int userId);
}
