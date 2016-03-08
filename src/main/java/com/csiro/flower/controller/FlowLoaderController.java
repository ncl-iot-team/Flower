/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

//import java.util.Map;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author kho01f
 */
@Controller
public class FlowLoaderController {

    @RequestMapping("/flowload")
    public String viewPage() {
        return "flowload";
    }


//    public ModelAndView t(@PathVariable Map<String, String> varMap) {
//        ModelAndView m = new ModelAndView("configform");
//        String fname = varMap.get("ali");
//        String lname = varMap.get("reza");
//
//        m.addObject("msg", "hello" + fname + lname);
//        return m;
//    }
}
