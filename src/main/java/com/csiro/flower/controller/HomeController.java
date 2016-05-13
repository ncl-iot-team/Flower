/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author kho01f
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String viewHome() {
        return "home";
    }
    
    @RequestMapping("/ElasticityService")
    public String viewElasticityService(){
        // If user has signed in successfully go to 1, else go to sign in page.
        // 1) If user has already created and selected a particular Flow go to 3, else go to 2
        // 2) load flowCreationForm.
        // 3) Show flowCtrlServicePage
        return "redirect:/flowCreationForm";
    }
}
