/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csiro.flower.util;


/**
 *
 * @author kho01f
 */
public interface CloudServiceRegionMgmt {
    public String resolveEndpoint(String provider, String serviceName, String region);
}
