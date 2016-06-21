/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

import com.csiro.flower.dao.FlowDao;
import com.csiro.flower.model.Flow;
import com.csiro.flower.model.FlowDetailSetting;
import com.csiro.flower.service.FlowManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author kho01f
 */
@Controller
@RequestMapping("mntr")
public class MntrManagementController {

    @Autowired
    private FlowDao flowDao;
    
    @Autowired
    private FlowManagerService flowManagerService;

    // this method submit and launch the ctrl service
    @RequestMapping(value = "/submitFlowMntrSettingForm/{flowId}", method = {RequestMethod.POST})
    public String submitFlowMntrSetting(@PathVariable int flowId,
            @ModelAttribute("flowSetting") FlowDetailSetting flowSetting) {

        if (!flowManagerService.isFlowConfiged(flowId)) {
            flowManagerService.saveFlowMntrSettings(flowId, flowSetting);
        }
        return "redirect:/mntr/flowMntrServicePage/" + flowId;
    }

    @RequestMapping(value = "/flowMntrServicePage/{flowId}", method = RequestMethod.GET)
    public ModelAndView launchMntrServicePage(@PathVariable int flowId) {
        ModelAndView model = new ModelAndView();
        Flow flow = flowDao.get(flowId);
        flow.setFlowId(flowId);
        model.addObject(flow);
        model.setViewName("/flowMntrServicePage");
        return model;
    }
}
