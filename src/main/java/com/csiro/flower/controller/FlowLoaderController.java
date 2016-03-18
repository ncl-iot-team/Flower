/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

//import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author kho01f
 */
@Controller
public class FlowLoaderController {

    @RequestMapping("/flowload")
    public String viewPage() {
        return "flowload";
    }

    @RequestMapping("/submitFlowFormSetting")
    public ModelAndView submitFlowSetting(@RequestParam Map<String, String> reqPar) {
        String flowName = reqPar.get("flowName");
        String owner = reqPar.get("owner");
        String platforms = reqPar.get("list[]");

        ModelAndView model = new ModelAndView("stepform");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:postgresql://localhost:5433/flowerDB";
        String user = "postgres";
        String pass = "300";

        try {
            con = DriverManager.getConnection(url, user, pass);
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");
            model.addObject("msg", flowName);

//            if (rs.next()) {
//                System.out.println(rs.getString(1));
//            }
        } catch (SQLException ex) {

        } finally {
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

            }
        }

        return model;
    }

//    public ModelAndView t(@PathVariable Map<String, String> varMap) {
//        ModelAndView m = new ModelAndView("configform");
//        String fname = varMap.get("ali");
//        String lname = varMap.get("reza");
//
//        m.addObject("msg", "hello" + fname + lname);
//        return m;
//    }
}
