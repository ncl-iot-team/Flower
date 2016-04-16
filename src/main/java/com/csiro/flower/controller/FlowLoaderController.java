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
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author kho01f
 */
@Controller
@SessionAttributes("flow")
public class FlowLoaderController {
    

    @Autowired
    FlowDao flowDao;

    @Autowired
    FlowCtrlsService flowCtrlsService;

    @ModelAttribute("flow")
    public Flow flow() {
        return new Flow();
    }

    @InitBinder
    public void nullValueHandler(WebDataBinder binder) {
        binder.registerCustomEditor(int.class, new CustomNumberEditor(Integer.class, true) {
            @Override
            public void setValue(Object o) {
                super.setValue((o == null) ? 0 : o);
            }
        });
    }

    @RequestMapping("/flowCreationForm")
    public String viewFlowLoadPage() {
        return "flowCreationForm";
    }

    @RequestMapping(value = "/submitFlowSettingForm", method = {RequestMethod.POST})
    public ModelAndView submitFlowSetting(@ModelAttribute("flow") Flow flow) {

        Date date = new Date();
        long milis = date.getTime();
        flow.setCreationDate(new Timestamp(milis));
        int flowId = flowDao.save(flow);
        flow.setFlowId(flowId);

        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:flowCtrlStepForm");

        return model;
    }

    @RequestMapping(value = "/flowCtrlStepForm", method = {RequestMethod.GET})
    public ModelAndView viewStepFormPage() {
//        Flow flow = flowDao.get(flow.getFlowId());
        ModelAndView modelAndView = new ModelAndView("flowCtrlStepForm");
//        modelAndView.addObject("platforms", flow.getPlatforms());
//        modelAndView.addObject("flowId", flow.getFlowId());
        return modelAndView;
    }


    @RequestMapping(value = "/submitFlowCtrlSettingForm", method = {RequestMethod.POST})
    public String submitFlowCtrlSetting(@ModelAttribute("flowSetting") FlowDetailSetting flowSetting, @ModelAttribute("flow") Flow flow) {

        int flowId = flow.getFlowId();
        flowCtrlsService.saveFlowControllerSettings(flow.getPlatforms().split(","), flowId, flowSetting);
        return "redirect:/";// + flowId;
    }


}
