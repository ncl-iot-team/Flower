/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.dao;

import com.csiro.flower.model.CloudSetting;

/**
 *
 * @author kho01f
 */
public interface CloudSettingDao {

    public void save(CloudSetting cloudSetting);

    public void delete(int id);

    public CloudSetting get(int id);

    public void update(CloudSetting cloudSetting);

}
