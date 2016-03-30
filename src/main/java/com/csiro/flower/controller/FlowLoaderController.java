/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

//import java.util.Map;
import com.csiro.flower.dao.FlowDao;
import com.csiro.flower.model.Flow;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author kho01f
 */
@Controller
public class FlowLoaderController {

    @Autowired
    FlowDao dao;

    @RequestMapping("/flowload")
    public String viewFlowLoadPage() {
        return "flowload";
    }

    @RequestMapping(value = "/submitFlowFormSetting", method = {RequestMethod.POST})
    public String submitFlowSetting(@RequestParam Map<String, String> reqPar) {

        Flow flow = new Flow();
        flow.setFlowName(reqPar.get("flowName"));
        flow.setFlowOwner(reqPar.get("owner"));
        flow.setPlatforms(reqPar.get("systems"));
        Date date = new Date();
        long milis = date.getTime();
        flow.setCreationDate(new Timestamp(milis));
        int flowId = dao.save(flow);

        return "redirect:stepform/" + flowId;
    }

    @RequestMapping(value = "/stepform/{id}", method = {RequestMethod.GET})
    public ModelAndView viewStepFormPage(@PathVariable("id") int flowId) {
        Flow flow = dao.get(flowId);
        ModelAndView modelAndView = new ModelAndView("stepform");
        modelAndView.addObject("platforms",flow.getPlatforms());
        return modelAndView;
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
