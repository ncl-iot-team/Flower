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
public class CloudServiceRegionUtil {

    final static String amazonEndpointPattern = "%s.%s.amazonaws.com";

    public static String resolveEndpoint(String provider, String serviceName, String region) {
        String endpoint = null;
        if (provider.equals("Amazon")) {
            endpoint = String.format(amazonEndpointPattern, serviceName, region);
        }
        return endpoint;
    }
}
