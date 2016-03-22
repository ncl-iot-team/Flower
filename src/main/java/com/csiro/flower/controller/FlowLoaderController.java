/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csiro.flower.controller;

//import java.util.Map;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sun.util.calendar.CalendarUtils;
//import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author kho01f
 */
@Controller
public class FlowLoaderController {

    Connection con = null;
    Datasource ds = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    @RequestMapping("/flowload")
    public String viewFlowLoadPage() {
        return "flowload";
    }

    @RequestMapping(value = "/submitFlowFormSetting", method = {RequestMethod.POST})
    public String submitFlowSetting(@RequestParam Map<String, String> reqPar) {
        String flowName = reqPar.get("flowName");
        String owner = reqPar.get("owner");
        String platforms = reqPar.get("list");

        Date date = new Date();
        long milis = date.getTime();
        Calendar calendarUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        ds = new Datasource();
        con = ds.openCon();

        try {
            ps = con.prepareStatement("INSERT INTO flow_tbl (flow_name, flow_owner, "
                    + "platforms, date_created) VALUES (?,?,?,?)");
            ps.setString(1, flowName);
            ps.setString(2, owner);
            ps.setString(3, platforms);
            ps.setTimestamp(4, new Timestamp(milis), calendarUTC);
            ps.executeUpdate();
            System.out.println(platforms);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Datasource.class.getName());
            lgr.log(Level.WARNING, ex.getMessage(), ex);
        }

        ds.closeCon(rs, ps, con);

        return "redirect:stepform";
    }

    @RequestMapping(value= "/stepform", method={RequestMethod.GET})
    public String viewStepFormPage(){
        return "stepform"; 
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
