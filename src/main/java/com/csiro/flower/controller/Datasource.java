/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kho01f
 */
public class Datasource {

    Connection con = null;
    String url = "jdbc:postgresql://localhost:5433/flowerDB";
    String user = "postgres";
    String pass = "300";

    public Connection openCon() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException ex) {
            Logger lgr = Logger.getLogger(Datasource.class.getName());
            lgr.log(Level.WARNING, ex.getMessage(), ex);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Datasource.class.getName());
            lgr.log(Level.WARNING, ex.getMessage(), ex);
        }
        return con;
    }

    public void closeCon(ResultSet rs, Statement st, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Datasource.class.getName());
            lgr.log(Level.WARNING, ex.getMessage(), ex);
        }
    }
}
