/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.Ctrl;
import java.util.List;

/**
 *
 * @author kho01f
 */
public interface CtrlDao {

    public void save(Ctrl ctrl);

    public List<Ctrl> get(int flowId);

    public void update(Ctrl ctrl);

    public int getPkId(int flowId, String ctrlName,String resource, String measurementTarget);

    public Ctrl get(int flowId, String ctrlName, String resource, String measurementTarget);

}
