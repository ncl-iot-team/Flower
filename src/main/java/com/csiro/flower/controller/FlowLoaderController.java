/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

//import java.util.Map;
import com.csiro.flower.dao.CloudSettingDao;
import com.csiro.flower.dao.FlowDao;
import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.Flow;
import com.csiro.flower.model.FlowDetailSetting;
import com.csiro.flower.service.DynamoMgmtService;
import com.csiro.flower.service.FlowCtrlsService;
import com.csiro.flower.service.KinesisMgmtService;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

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

    @Autowired
    KinesisMgmtService kinesisMgmtService;
    
    @Autowired
    DynamoMgmtService dynamoMgmtService;

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
        ModelAndView modelAndView = new ModelAndView("flowCtrlStepForm");
        return modelAndView;
    }

    @RequestMapping(value = "/submitFlowCtrlSettingForm", method = {RequestMethod.POST})
    public String submitFlowCtrlSetting(@ModelAttribute("flowSetting") FlowDetailSetting flowSetting, @ModelAttribute("flow") Flow flow) {

        int flowId = flow.getFlowId();
        flowCtrlsService.saveFlowControllerSettings(flow.getPlatforms().split(","), flowId, flowSetting);
        return "redirect:/";// + flowId;
    }

    @RequestMapping(value = "/loadDynamoTables", method = RequestMethod.POST)
    public @ResponseBody
    List<String> getTableList(@RequestBody CloudSetting cloudSetting) {
        dynamoMgmtService.initService(
                cloudSetting.getCloudProvider(),
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());
        return dynamoMgmtService.getTableList();

    }
    
    @RequestMapping(value="/loadKinesisStreams", method = RequestMethod.POST)
    public @ResponseBody
    List<String> getStreamList(@RequestBody CloudSetting cloudSetting){
        kinesisMgmtService.initService(
                cloudSetting.getCloudProvider(), 
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());
        return kinesisMgmtService.getStreamList();
    }
}
