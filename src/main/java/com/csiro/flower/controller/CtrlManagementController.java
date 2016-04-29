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
import com.csiro.flower.model.Flow;
import com.csiro.flower.model.FlowDetailSetting;
import com.csiro.flower.model.KinesisCtrl;
import com.csiro.flower.model.StormCluster;
import com.csiro.flower.model.StormCtrl;
import com.csiro.flower.service.DynamoCtrlService;
import com.csiro.flower.service.DynamoMgmtService;
import com.csiro.flower.service.FlowCtrlsService;
import com.csiro.flower.service.KinesisCtrlService;
import com.csiro.flower.service.KinesisMgmtService;
import com.csiro.flower.service.StormCtrlService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author kho01f
 */
@Controller
@RequestMapping("ctrls")
@SessionAttributes("flow")
public class CtrlManagementController {

    @Autowired
    FlowCtrlsService flowCtrlsService;

    @Autowired
    KinesisMgmtService kinesisMgmtService;

    @Autowired
    DynamoMgmtService dynamoMgmtService;

    @Autowired
    StormCtrlService stormCtrlService;

    @Autowired
    KinesisCtrlService kinesisCtrlService;

    @Autowired
    DynamoCtrlService dynamoCtrlService;

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
    public String submitFlowCtrlSetting(@ModelAttribute("flowSetting") FlowDetailSetting flowSetting, @ModelAttribute("flow") Flow flow) {
        int flowId = flow.getFlowId();
        flowCtrlsService.saveFlowControllerSettings(flow.getPlatforms().split(","), flowId, flowSetting);
        return "redirect:/ctrls/flowCtrlServicePage";
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

    @RequestMapping(value = "/flowCtrlServicePage", method = RequestMethod.GET)
    public String viewFlowLoadPage(@ModelAttribute("flow") Flow flow) {
        return "flowCtrlServicePage";
    }

    @RequestMapping(value = "/runControllerService", method = RequestMethod.POST)
    public @ResponseBody
    FlowDetailSetting startCtrlService(@ModelAttribute("flow") Flow flow,
            @RequestParam("ctrlName") String ctrlName) {
        int id = flow.getFlowId();
        CloudSetting cloudSetting = cloudSettingDao.get(id);
        FlowDetailSetting flowDetailSetting = new FlowDetailSetting();
        switch (ctrlName) {
            case "ApacheStormCtrl":
                stormCtrlService.initService(
                        cloudSetting.getCloudProvider(),
                        cloudSetting.getAccessKey(),
                        cloudSetting.getSecretKey(),
                        cloudSetting.getRegion());
                StormCluster stormCluster = stormClusterDao.get(id);
                StormCtrl stormCtrl = stormCtrlDao.get(id);
                stormCtrlService.startStormCtrl(
                        stormCluster.getNimbusIp(),
                        stormCtrl.getTargetTopology(),
                        stormCtrl.getMeasurementTarget(),
                        stormCtrl.getRefValue(),
                        stormCtrl.getMonitoringPeriod(),
                        stormCtrl.getBackoffNo());
                flowDetailSetting.setStormCtrl(stormCtrl);
                break;
            case "AmazonKinesisCtrl":
                kinesisCtrlService.initService(
                        cloudSetting.getCloudProvider(),
                        cloudSetting.getAccessKey(),
                        cloudSetting.getSecretKey(),
                        cloudSetting.getRegion());
                List<KinesisCtrl> kinesisCtrls = kinesisCtrlDao.get(id);
                for (KinesisCtrl kinesisCtrl : kinesisCtrls) {
                    kinesisCtrlService.startKinesisCtrl(
                            kinesisCtrl.getStreamName(),
                            kinesisCtrl.getMeasurementTarget(),
                            kinesisCtrl.getRefValue(),
                            kinesisCtrl.getMonitoringPeriod(),
                            kinesisCtrl.getBackoffNo());
                }
                flowDetailSetting.setKinesisCtrls(kinesisCtrls);
                break;
        }
        return flowDetailSetting;

    }
}
