/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

import com.csiro.flower.dao.CloudSettingDao;
import com.csiro.flower.dao.FlowDao;
import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.CtrlMonitoringResultSet;
import com.csiro.flower.model.Flow;
import com.csiro.flower.model.FlowDetailSetting;
import com.csiro.flower.service.CloudWatchService;
import com.csiro.flower.service.DynamoMgmtService;
import com.csiro.flower.service.FlowManagerService;
import com.csiro.flower.service.KinesisMgmtService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    private CloudSettingDao cloudSettingDao;

    @Autowired
    private FlowManagerService flowManagerService;

    @Autowired
    private KinesisMgmtService kinesisMgmtService;

    @Autowired
    private DynamoMgmtService dynamoMgmtService;

    @Autowired
    private CloudWatchService cloudWatchService;

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

    @RequestMapping(value = "/loadKinesisStreams/{flowId}")
    @ResponseBody
    public List<String> getStreamList(@PathVariable int flowId) {
        CloudSetting cloudSetting = cloudSettingDao.get(flowId);
        kinesisMgmtService.initService(
                cloudSetting.getCloudProvider(),
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());
        return kinesisMgmtService.getStreamList();
    }

    @RequestMapping(value = "/getCloudWatchStats")
    public @ResponseBody
    double getCtrlMonitoringStats(
            @RequestParam("platform") String platform,
            @RequestParam("flowId") int flowId,
            @RequestParam("resource") String resource,
            @RequestParam("metric") String metric,
            @RequestParam("timeStamp") long timeStamp) {

        CloudSetting cloudSetting = cloudSettingDao.get(flowId);
        cloudWatchService.initService(
                cloudSetting.getCloudProvider(),
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());

        return cloudWatchService.getSingleStat(platform, resource, metric, timeStamp) + Math.random() * 100;
    }
}
