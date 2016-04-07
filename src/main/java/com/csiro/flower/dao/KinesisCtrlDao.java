/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.KinesisCtrl;
import java.util.List;

/**
 *
 * @author kho01f
 */
public interface KinesisCtrlDao {

    public void save(KinesisCtrl kinesisCtrl);

    public void delete(int id);

    public List<KinesisCtrl> get(int flowId);

    public void update(KinesisCtrl kinesisCtrl);

}
