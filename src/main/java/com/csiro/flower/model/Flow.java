/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csiro.flower.model;

import java.sql.Timestamp;

/**
 *
 * @author kho01f
 */
public class Flow {
    private String flowName;
    private int flowId;
    private String flowOwner;
    private String platforms;
    private Timestamp creationDate;

    /**
     * @return the FlowName
     */
    public String getFlowName() {
        return flowName;
    }

    /**
     * @param flowName the FlowName to set
     */
    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    /**
     * @return the FlowId
     */
    public int getFlowId() {
        return flowId;
    }

    /**
     * @param flowId the FlowId to set
     */
    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    /**
     * @return the FlowOwner
     */
    public String getFlowOwner() {
        return flowOwner;
    }

    /**
     * @param flowOwner the FlowOwner to set
     */
    public void setFlowOwner(String flowOwner) {
        this.flowOwner = flowOwner;
    }

    /**
     * @return the platforms
     */
    public String getPlatforms() {
        return platforms;
    }

    /**
     * @param platforms the platforms to set
     */
    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    /**
     * @return the creationDate
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
    
    
    
}
