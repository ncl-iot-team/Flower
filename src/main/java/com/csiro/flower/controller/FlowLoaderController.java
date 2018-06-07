/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

//import java.util.Map;
import com.csiro.flower.dao.FlowDao;
import com.csiro.flower.model.Flow;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author kho01f
 */
@Controller
//@RequestMapping("flow")
public class FlowLoaderController {

    @Autowired
    private FlowDao flowDao;

    @RequestMapping("/flowCreationForm")
    public String viewFlowLoadPage() {
        return "flowCreationForm";
    }

    @RequestMapping(value = "/submitFlowSettingForm", method = {RequestMethod.POST})
    public ModelAndView submitFlowSetting(@ModelAttribute("flow") Flow flow) {
//            ,RedirectAttributes redirectAttributes) {

        flow.setCreationDate(new Timestamp(new Date().getTime()));
        int flowId = flowDao.save(flow);
        flow.setFlowId(flowId);

        ModelAndView model = new ModelAndView();
//        redirectAttributes.addFlashAttribute(flow);
//        model.setViewName("redirect:/redirectToStepForm/" + flowId);
        model.setViewName("redirect:/flowList");
        return model;
    }

    @RequestMapping(value = "/redirectToCtrlStepForm/{flowId}", method = {RequestMethod.GET})
    public ModelAndView viewCtrlStepFormPage(@PathVariable int flowId) {
        Flow flow = flowDao.get(flowId);
        flow.setFlowId(flowId);
        ModelAndView model = new ModelAndView();
        model.addObject(flow);
        model.setViewName("/flowCtrlStepForm");
        return model;
    }

    @RequestMapping(value = "/redirectToMntrStepForm/{flowId}", method = {RequestMethod.GET})
    public ModelAndView viewMntrStepFormPage(@PathVariable int flowId) {
        Flow flow = flowDao.get(flowId);
        flow.setFlowId(flowId);
        ModelAndView model = new ModelAndView();
        model.addObject(flow);
        model.setViewName("/flowMntrStepForm");
        return model;
    }

    @RequestMapping("/flowList")
    public String viewFlowListPage() {
        return "flowListPage";
    }

    @RequestMapping("/getFlowList/{user:.+}")
    @ResponseBody
    public List<Flow> getFlowList(@PathVariable String user) {
        List<Flow> list = flowDao.getAll(user);
        return list;
    }
}
