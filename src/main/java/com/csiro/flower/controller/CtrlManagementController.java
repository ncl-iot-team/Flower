/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

import com.csiro.flower.dao.CloudSettingDao;
import com.csiro.flower.dao.CtrlStatsDao;
import com.csiro.flower.dao.DynamoCtrlDao;
import com.csiro.flower.dao.KinesisCtrlDao;
import com.csiro.flower.dao.StormClusterDao;
import com.csiro.flower.dao.StormCtrlDao;
import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.DynamoCtrl;
import com.csiro.flower.model.Flow;
import com.csiro.flower.model.FlowDetailSetting;
import com.csiro.flower.model.KinesisCtrl;
import com.csiro.flower.service.DynamoCtrlServiceImpl;
import com.csiro.flower.service.DynamoMgmtService;
import com.csiro.flower.service.FlowCtrlsManagerService;
import com.csiro.flower.service.KinesisCtrlServiceImpl;
import com.csiro.flower.service.KinesisMgmtService;
import com.csiro.flower.service.StormCtrlServiceImpl;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    StormCtrlServiceImpl stormCtrlServiceImpl;

    @Autowired
    KinesisCtrlServiceImpl kinesisCtrlServiceImpl;

    @Autowired
    DynamoCtrlServiceImpl dynamoCtrlServiceImpl;

    @Autowired
    CtrlStatsDao ctrlStatsDao;

    String STOPPED_STATUS = "Stopped";

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
    @RequestMapping(value = "/loadDynamoTables")
    public @ResponseBody
    List<String> getTableList(@RequestBody CloudSetting cloudSetting) {
        dynamoMgmtService.initService(
                cloudSetting.getCloudProvider(),
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());
        return dynamoMgmtService.getTableList();
    }

    @RequestMapping(value = "/loadKinesisStreams")
    public @ResponseBody
    List<String> getStreamList(@RequestBody CloudSetting cloudSetting) {
        kinesisMgmtService.initService(
                cloudSetting.getCloudProvider(),
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());
        return kinesisMgmtService.getStreamList();
    }

    @RequestMapping(value = "/dynamoCtrl/{flowId}")
    public @ResponseBody
    List<DynamoCtrl> getDynamoCtrl(@PathVariable int flowId) {
        return dynamoCtrlDao.get(flowId);
    }

    @RequestMapping(value = "/kinesisCtrl/{flowId}")
    public @ResponseBody
    List<KinesisCtrl> getKinesisCtrl(@PathVariable int flowId) {
        return kinesisCtrlDao.get(flowId);
    }

    @RequestMapping(value = "/stopCtrl")
    public @ResponseBody
    void stopCtrlService(
            @RequestParam("ctrlName") String ctrlName,
            @RequestParam("flowId") int flowId,
            @RequestParam("resource") String resource) {
        int id = 0;
        switch (ctrlName) {
            case "AmazonKinesis":
                id = kinesisCtrlDao.getPkId(flowId, resource);
                break;
            case "ApacheStorm":
                id = stormCtrlDao.getPkId(flowId, resource);
                break;
            case "DynamoDB":
                id = dynamoCtrlDao.getPkId(flowId, resource);
                break;
        }
        ctrlStatsDao.updateCtrlStatus(id, ctrlName, STOPPED_STATUS, new Timestamp(new Date().getTime()));
    }

    @RequestMapping(value = "/launchFlowCtrlServicePage", method = RequestMethod.GET)
    public String launchCtrlServicePage(Model model) {
        FlowDetailSetting flowSetting = (FlowDetailSetting) model.asMap().get("flowSetting");
        if (flowSetting.getStormCtrl() != null) {
            stormCtrlServiceImpl.startController(
                    flowSetting.getCloudSetting(),
                    flowSetting.getStormCluster(),
                    flowSetting.getStormCtrl());
        }

        if (flowSetting.getDynamoCtrls() != null) {
            for (DynamoCtrl dynamoCtrl : flowSetting.getDynamoCtrls()) {
                dynamoCtrlServiceImpl.startConroller(flowSetting.getCloudSetting(), dynamoCtrl);
            }
        }

        if (flowSetting.getKinesisCtrls() != null) {
            for (KinesisCtrl kinesisCtrl : flowSetting.getKinesisCtrls()) {
                kinesisCtrlServiceImpl.startController(flowSetting.getCloudSetting(), kinesisCtrl);
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
