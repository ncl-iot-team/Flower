/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

import com.csiro.flower.dao.UserDao;
import com.csiro.flower.model.UserAccount;
import com.csiro.flower.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author kho01f
 */
@Controller
@RequestMapping("user")
public class UserManagementController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "signup")
    public String viewSignupPage() {
        return "signup";
    }

    @RequestMapping(value = {"login","login/success"})
    public String viewSigninPage() {
        return "login";
    }

    @RequestMapping(value = "signupForm", method = RequestMethod.POST)
    @ResponseBody
    public boolean createAccount(@ModelAttribute("userAccount") UserAccount userAccount) {
        return userService.createAccount(userAccount);
    }

}
