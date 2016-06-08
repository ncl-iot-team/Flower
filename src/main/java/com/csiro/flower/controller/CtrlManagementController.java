/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

import com.csiro.flower.dao.CtrlDao;
import com.csiro.flower.model.CloudSetting;
import com.csiro.flower.model.Ctrl;
import com.csiro.flower.model.CtrlMonitoringResultSet;
import com.csiro.flower.model.Flow;
import com.csiro.flower.model.FlowDetailSetting;
import com.csiro.flower.service.CtrlsRunnerService;
import com.csiro.flower.service.DynamoMgmtService;
import com.csiro.flower.service.FlowCtrlsManagerService;
import com.csiro.flower.service.KinesisMgmtService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    private FlowCtrlsManagerService flowCtrlsManagerService;

    @Autowired
    private KinesisMgmtService kinesisMgmtService;

    @Autowired
    private DynamoMgmtService dynamoMgmtService;

    @Autowired
    private CtrlDao ctrlDao;

    @Autowired
    private CtrlsRunnerService ctrlsRunnerService;

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

    // this method submit and launch the ctrl service
    @RequestMapping(value = "/submitFlowCtrlSettingForm", method = {RequestMethod.POST})
    public String submitFlowCtrlSetting(
            @ModelAttribute("flowSetting") FlowDetailSetting flowSetting,
            @ModelAttribute("flow") Flow flow) {

        int flowId = flow.getFlowId();
        flowCtrlsManagerService.saveFlowCtrlsSettings(flowId, flowSetting);
        startFlowCtrlService(flowSetting);
        return "redirect:/ctrls/flowCtrlServicePage";
    }

    private void startFlowCtrlService(FlowDetailSetting flowSetting) {
        ctrlsRunnerService.startCtrls(flowSetting);
    }

    @RequestMapping(value = "/flowCtrlServicePage", method = RequestMethod.GET)
    public String launchCtrlServicePage() {
        return "flowCtrlServicePage";
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
    @ResponseBody
    public List<String> getTableList(@RequestBody CloudSetting cloudSetting) {
        dynamoMgmtService.initService(
                cloudSetting.getCloudProvider(),
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());
        return dynamoMgmtService.getTableList();
    }

    @RequestMapping(value = "/loadKinesisStreams")
    @ResponseBody
    public List<String> getStreamList(@RequestBody CloudSetting cloudSetting) {
        kinesisMgmtService.initService(
                cloudSetting.getCloudProvider(),
                cloudSetting.getAccessKey(),
                cloudSetting.getSecretKey(),
                cloudSetting.getRegion());
        return kinesisMgmtService.getStreamList();
    }

    @RequestMapping(value = "/getCtrls/{flowId}")
    @ResponseBody
    public List<Ctrl> getCtrls(@PathVariable int flowId) {
        return ctrlDao.get(flowId);
    }

    @RequestMapping(value = "/getCtrl")
    @ResponseBody
    public Ctrl getCtrl(
            @RequestParam("ctrlName") String ctrlName,
            @RequestParam("flowId") int flowId,
            @RequestParam("resource") String resource,
            @RequestParam("measurementTarget") String measurementTarget) {

        return ctrlDao.get(flowId, ctrlName, resource, measurementTarget);
    }

    @RequestMapping(value = "/updateCtrlSettings", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateCtrl(@ModelAttribute("ctrl") Ctrl ctrl) {
        ctrlDao.update(ctrl);
    }

//    @RequestMapping(value = "/kinesisCtrl/{flowId}")
//    public @ResponseBody
//    List<KinesisCtrl> getKinesisCtrl(@PathVariable int flowId) {
//        return kinesisCtrlDao.get(flowId);
//    }
//
//    @RequestMapping(value = "/stormCtrl/{flowId}")
//    public @ResponseBody
//    StormCtrl getStormCtrl(@PathVariable int flowId) {
//        return stormCtrlDao.get(flowId);
//    }
    @RequestMapping(value = "/restartCtrl")
    @ResponseBody
    public void restartCtrlService(
            @RequestParam("ctrlName") String ctrlName,
            @RequestParam("flowId") int flowId,
            @RequestParam("resource") String resource,
            @RequestParam("measurementTarget") String measurementTarget) {

        ctrlsRunnerService.restartCtrl(ctrlName, flowId, resource, measurementTarget);
    }

    @RequestMapping(value = "/stopCtrl", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void stopCtrlService(
            @RequestParam("ctrlName") String ctrlName,
            @RequestParam("flowId") int flowId,
            @RequestParam("resource") String resource,
            @RequestParam("measurementTarget") String measurementTarget) {

        ctrlsRunnerService.stopCtrl(flowId, ctrlName, resource, measurementTarget);
    }

    @RequestMapping(value = "/getCtrlStatus")
    public @ResponseBody
    String getCtrlStatus(
            @RequestParam("ctrlName") String ctrlName,
            @RequestParam("flowId") int flowId,
            @RequestParam("resource") String resource,
            @RequestParam("measurementTarget") String measurementTarget) {

        return ctrlsRunnerService.getCtrlStatus(flowId, ctrlName, resource, measurementTarget);
    }

    @RequestMapping(value = "/getCtrlStats")
    public @ResponseBody
    List<CtrlMonitoringResultSet> getCtrlMonitoringStats(
            @RequestParam("ctrlName") String ctrlName,
            @RequestParam("flowId") int flowId,
            @RequestParam("resource") String resource,
            @RequestParam("measurementTarget") String measurementTarget,
            @RequestParam("timeStamp") long timeStamp) {

        return ctrlsRunnerService.getCtrlMonitoringStats(flowId, ctrlName, resource, measurementTarget, timeStamp);
    }

//Using flashattributes for sending objects after redirect
//    @RequestMapping(value = "/submitFlowCtrlSettingForm", method = {RequestMethod.POST})
//    public RedirectView submitFlowCtrlSetting(
//            @ModelAttribute("flowSetting") FlowDetailSetting flowSetting,
//            @ModelAttribute("flow") Flow flow,
//            RedirectAttributes redirectAttributes) {
//
//        int flowId = flow.getFlowId();
//        flowCtrlsManagerService.saveFlowCtrlsSettings(flow.getPlatforms().split(","), flowId, flowSetting);
//        startFlowCtrlService(flowSetting);
//        redirectAttributes.addFlashAttribute("flowSetting", flowSetting);
//        return new RedirectView("/Flower/ctrls/flowCtrlServicePage");
//    }
//    @RequestMapping(value = "/launchFlowCtrlServicePage", method = RequestMethod.GET)
//    public String launchCtrlServicePage(Model model) {
//        FlowDetailSetting flowSetting = (FlowDetailSetting) model.asMap().get("flowSetting");
//        if (flowSetting.getStormCtrl() != null) {
//            stormCtrlServiceImpl.startController(
//                    flowSetting.getCloudSetting(),
//                    flowSetting.getStormCluster(),
//                    flowSetting.getStormCtrl());
//        }
//
//        if (flowSetting.getDynamoCtrls() != null) {
//            for (DynamoCtrl dynamoCtrl : flowSetting.getDynamoCtrls()) {
//                dynamoCtrlServiceImpl.startConroller(flowSetting.getCloudSetting(), dynamoCtrl);
//            }
//        }
//
//        if (flowSetting.getKinesisCtrls() != null) {
//            for (KinesisCtrl kinesisCtrl : flowSetting.getKinesisCtrls()) {
//                kinesisCtrlServiceImpl.startController(flowSetting.getCloudSetting(), kinesisCtrl);
//            }
//        }
//        return "flowCtrlServicePage";
//    }
}
