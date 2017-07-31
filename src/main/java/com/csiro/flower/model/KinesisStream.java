/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.model;

/**
 *
 * @author alireza
 */
public class KinesisStream {
    private String streamName;
    private int openShardsNo;
    private int closedShardsNo;
    
    public KinesisStream(String streamName, int openShardNo, int closedShardNo){
        this.streamName = streamName;
        this.openShardsNo = openShardNo;
        this.closedShardsNo = closedShardNo;
    }

    public String getStreamName() {
        return streamName;
    }

    public int getClosedShardsNo(){
        return closedShardsNo;
    }

    public int getOpenShardsNo() {
        return openShardsNo;
    }

}
