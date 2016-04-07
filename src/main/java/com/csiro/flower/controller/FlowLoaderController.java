/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

//import java.util.Map;
import com.csiro.flower.dao.FlowDao;
import com.csiro.flower.model.Flow;
import com.csiro.flower.model.FlowDetailSetting;
import com.csiro.flower.service.FlowCtrlsService;
import java.util.Date;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    FlowDao flowDao;

    @Autowired
    FlowCtrlsService flowCtrlsService;

    @RequestMapping("/flowCreationForm")
    public String viewFlowLoadPage() {
        return "flowCreationForm";
    }

    @RequestMapping(value = "/submitFlowSettingForm", method = {RequestMethod.POST})
    public String submitFlowSetting(@ModelAttribute Flow flow) {

        Date date = new Date();
        long milis = date.getTime();
        flow.setCreationDate(new Timestamp(milis));
        int flowId = flowDao.save(flow);

        return "redirect:flowCtrlStepForm/" + flowId;
    }

    @RequestMapping(value = "/flowCtrlStepForm/{id}", method = {RequestMethod.GET})
    public ModelAndView viewStepFormPage(@PathVariable("id") int flowId) {
        Flow flow = flowDao.get(flowId);
        ModelAndView modelAndView = new ModelAndView("flowCtrlStepForm");
        modelAndView.addObject("platforms", flow.getPlatforms());
        modelAndView.addObject("flowId", flowId);
        return modelAndView;
    }

    @RequestMapping("/configTestForm")
    public String viewConfigForm() {
        return "configTestForm";
    }

    @RequestMapping(value = "/flowCtrlStepForm/submitFlowCtrlSettingForm", method = {RequestMethod.POST})
    public String submitFlowCtrlSetting(@ModelAttribute FlowDetailSetting flowSetting, @RequestParam("platforms") String platforms) {
        flowCtrlsService.saveFlowControllerSettings(platforms.split(","), flowSetting);

        return "redirect:/";// + flowId;
    }

//    @RequestMapping(value = "/flowCtrlStepForm/flowCtrlService/{id}", method = {RequestMethod.GET})
//    public String viewFlowCtrlServicePage() {
//        return "flowCtrlServicePage";
//    }

}
