/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

import com.csiro.flower.dao.CloudSettingDao;
import com.csiro.flower.dao.DynamoCtrlDao;
import com.csiro.flower.dao.KinesisCtrlDao;
import com.csiro.flower.dao.StormClusterDao;
import com.csiro.flower.dao.StormCtrlDao;
import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.DynamoCtrl;
import com.csiro.flower.model.Flow;
import com.csiro.flower.model.FlowDetailSetting;
import com.csiro.flower.model.KinesisCtrl;
import com.csiro.flower.service.DynamoCtrlService;
import com.csiro.flower.service.DynamoCtrlServiceImpl;
import com.csiro.flower.service.DynamoMgmtService;
import com.csiro.flower.service.FlowCtrlsManagerService;
import com.csiro.flower.service.KinesisCtrlService;
import com.csiro.flower.service.KinesisMgmtService;
import com.csiro.flower.service.StormCtrlService;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author kho01f
 */
@Controller
@RequestMapping("ctrls")
@SessionAttributes("flow")
public class CtrlManagementController {

    @Autowired
    FlowCtrlsManagerService flowCtrlsManagerService;

    @Autowired
    KinesisMgmtService kinesisMgmtService;

    @Autowired
    DynamoMgmtService dynamoMgmtService;

    @Autowired
    CloudSettingDao cloudSettingDao;

    @Autowired
    StormCtrlDao stormCtrlDao;

    @Autowired
    StormClusterDao stormClusterDao;

    @Autowired
    KinesisCtrlDao kinesisCtrlDao;

    @Autowired
    DynamoCtrlDao dynamoCtrlDao;

    @Autowired
    StormCtrlService stormCtrlService;

    @Autowired
    KinesisCtrlService kinesisCtrlService;

    @Autowired
    DynamoCtrlServiceImpl dynamoCtrlServiceImpl;

    String activeStatus = "Active";

    @InitBinder
    public void nullValueHandler(WebDataBinder binder) {
        binder.registerCustomEditor(int.class, new CustomNumberEditor(Integer.class, true) {
            @Override
            public void setValue(Object o) {
                super.setValue((o == null) ? 0 : o);
            }
        });

        binder.registerCustomEditor(double.class, new CustomNumberEditor(Double.class, true) {
            @Override
            public void setValue(Object o) {
                super.setValue((o == null) ? 0.0 : o);
            }
        });
    }

    @ModelAttribute("flow")
    public Flow flow() {
        return new Flow();
    }

    @RequestMapping(value = "/flowCtrlStepForm", method = {RequestMethod.GET})
    public ModelAndView viewStepFormPage() {
        ModelAndView modelAndView = new ModelAndView("flowCtrlStepForm");
        return modelAndView;
    }

    @RequestMapping(value = "/submitFlowCtrlSettingForm", method = {RequestMethod.POST})
    public RedirectView submitFlowCtrlSetting(
            @ModelAttribute("flowSetting") FlowDetailSetting flowSetting,
            @ModelAttribute("flow") Flow flow,
            RedirectAttributes redirectAttributes) {
        int flowId = flow.getFlowId();
        flowCtrlsManagerService.saveFlowCtrlsSettings(flow.getPlatforms().split(","), flowId, flowSetting);
        redirectAttributes.addFlashAttribute("flowSetting", flowSetting);
        return new RedirectView("/Flower/ctrls/launchFlowCtrlServicePage");
    }

//    @RequestMapping(value = "/submitFlowCtrlSettingForm", method = {RequestMethod.POST})
//    public String submitFlowCtrlSetting(
//            @ModelAttribute("flowSetting") FlowDetailSetting flowSetting,
//            @ModelAttribute("flow") Flow flow) {
//        int flowId = flow.getFlowId();
//        flowCtrlsManagerService.saveFlowCtrlsSettings(flow.getPlatforms().split(","), flowId, flowSetting);
//        return "redirect:/ctrls/launchFlowCtrlServicePage";
//    }
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

    @RequestMapping(value = "/loadKinesisStreams", method = RequestMethod.POST)
    public @ResponseBody
    List<String> getStreamList(@RequestBody CloudSetting cloudSetting) {
        kinesisMgmtService.initService(
                cloudSetting.getCloudProvider(),
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());
        return kinesisMgmtService.getStreamList();
    }

    @RequestMapping(value = "/dynamoCtrl/{flowId}", method = RequestMethod.GET)
    public @ResponseBody
    List<DynamoCtrl> getDynamoCtrl(@PathVariable int flowId) {
        return dynamoCtrlDao.get(flowId);
    }

    @RequestMapping(value = "/launchFlowCtrlServicePage", method = RequestMethod.GET)
    public String launchCtrlServicePage(Model model) {
        FlowDetailSetting flowSetting = (FlowDetailSetting) model.asMap().get("flowSetting");
        if (flowSetting.getStormCluster() != null) {
            stormCtrlService.startStormController(
                    flowSetting.getCloudSetting(),
                    flowSetting.getStormCluster(),
                    flowSetting.getStormCtrl());
            // The below condition will be replaced with a 
            // return success status from start ctrl command

        }
        if (flowSetting.getDynamoCtrls() != null) {
            for (DynamoCtrl dynamoCtrl : flowSetting.getDynamoCtrls()) {

                long threadId = dynamoCtrlServiceImpl.startDynamoConroller(
                        flowSetting.getCloudSetting(),
                        dynamoCtrl);

                dynamoCtrlServiceImpl.updateCtrlStatus(activeStatus, threadId,
                        new Timestamp(new Date().getTime()));
            }
        }
        if (flowSetting.getKinesisCtrls() != null) {
            for (KinesisCtrl kinesisCtrl : flowSetting.getKinesisCtrls()) {
                kinesisCtrlService.startKinesisController(flowSetting.getCloudSetting(),
                        kinesisCtrl);
            }
        }
        return "flowCtrlServicePage";
    }

//    @RequestMapping(value = "/runControllerService", method = RequestMethod.POST)
//    public @ResponseBody
//    FlowDetailSetting startCtrlService(@ModelAttribute("flow") Flow flow,
//            @RequestParam("ctrlName") String ctrlName) {
//        int id = flow.getFlowId();
//        CloudSetting cloudSetting = cloudSettingDao.get(id);
//        FlowDetailSetting flowDetailSetting = new FlowDetailSetting();
//        switch (ctrlName) {
//            case "ApacheStormCtrl":
//                StormCluster stormCluster = stormClusterDao.get(id);
//                StormCtrl stormCtrl = stormCtrlDao.get(id);
//                controllerServiceRunner.startStormController(cloudSetting, stormCluster, stormCtrl);
//                flowDetailSetting.setStormCtrl(stormCtrl);
//                break;
//            case "AmazonKinesisCtrl":
//                List<KinesisCtrl> kinesisCtrls = kinesisCtrlDao.get(id);
//                controllerServiceRunner.startKinesisController(cloudSetting, kinesisCtrls);
//                flowDetailSetting.setKinesisCtrls(kinesisCtrls);
//                break;
//        }
//        return flowDetailSetting;
//    }
}
