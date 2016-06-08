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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author kho01f
 */

@Controller
public class FlowLoaderController {

    @Autowired
    private FlowDao flowDao;

    @RequestMapping("/flowCreationForm")
    public String viewFlowLoadPage() {
        return "flowCreationForm";
    }

    @RequestMapping(value = "/submitFlowSettingForm", method = {RequestMethod.POST})
    public ModelAndView submitFlowSetting(@ModelAttribute("flow") Flow flow,
            RedirectAttributes redirectAttributes) {

        flow.setCreationDate(new Timestamp(new Date().getTime()));
        int flowId = this.flowDao.save(flow);
        flow.setFlowId(flowId);

        ModelAndView model = new ModelAndView();
        redirectAttributes.addFlashAttribute(flow);
        model.setViewName("redirect:/ctrls/flowCtrlStepForm");
        return model;
    }
    
    @RequestMapping("/flowList")
    public String viewFlowListPage(){
        return "flowListPage";
    }
}
