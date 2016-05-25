/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.model;

/**
 *
 * @author kho01f
 */
public class CtrlInternalSetting {

    private double epsilon;
    private double upperK0;
    private double upInitK0;
    private double lowInitK0;
    private double lowerK0;
    private double k_init;
    private double gamma;

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public double getUpperK0() {
        return upperK0;
    }

    public void setUpperK0(double upperK0) {
        this.upperK0 = upperK0;
    }

    public double getUpInitK0() {
        return upInitK0;
    }

    public void setUpInitK0(double upInitK0) {
        this.upInitK0 = upInitK0;
    }

    public double getLowInitK0() {
        return lowInitK0;
    }

    public void setLowInitK0(double lowInitK0) {
        this.lowInitK0 = lowInitK0;
    }

    public double getLowerK0() {
        return lowerK0;
    }

    public void setLowerK0(double lowerK0) {
        this.lowerK0 = lowerK0;
    }

    public double getK_init() {
        return k_init;
    }

    public void setK_init(double k_init) {
        this.k_init = k_init;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }
    
    
}
