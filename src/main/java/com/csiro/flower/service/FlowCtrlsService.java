/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.service;

import com.csiro.flower.model.FlowDetailSetting;

/**
 *
 * @author kho01f
 */
public interface FlowCtrlsService {

    public void runFlowController(int flowId);

    public void saveFlowControllerSettings(String[] platforms, int flowId, FlowDetailSetting flowSetting);
}
