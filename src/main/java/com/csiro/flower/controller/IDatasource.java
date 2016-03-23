/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csiro.flower.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author kho01f
 */
public interface IDatasource {
    
    public Connection openCon();
    public void closeCon(ResultSet rs, Statement st, Connection con);
    
}
