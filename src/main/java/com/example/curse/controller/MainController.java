package com.example.curse.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
@Controller
public class MainController {

    private String url = "jdbc:postgresql://localhost:5432/parse_res", user = "tester",
            password = "qwerty", proc = "proc_info", mem = "mem_info", disk = "disk_info", intfc = "intfc_info";
    @GetMapping("/")
    public String getProcChart(Model model) {
        Map<String, Integer> cpu_occup = new TreeMap<>();
        Map<String, Integer> cpu_max = new TreeMap<>();
        try {
            Connection c = DriverManager.getConnection(this.url, this.user, this.password);
            try {
                Class.forName("org.postgresql.Driver");
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("select * from " + this.proc);
                while(rs.next()){
                    String temp = rs.getString("occupancy");
                    float value = Float.valueOf(temp);
                    int value_fin = Math.round(value);
                    cpu_occup.put(rs.getString("date"), value_fin);
                    cpu_max.put(rs.getString("date"), 100);
                }
                stmt.close();
            }
            finally {
                c.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        model.addAttribute("cpu_occup", cpu_occup);
        model.addAttribute("cpu_max", cpu_max);
        return "CPU_Occup";
    }

    @GetMapping("/disk")
    public String getDiskChart(Model model) {
        Map<String, Integer> disk_usage = new TreeMap<>();
        Map<String, Integer> disk_total = new TreeMap<>();
        try {
            Connection c = DriverManager.getConnection(this.url, this.user, this.password);
            try {
                Class.forName("org.postgresql.Driver");
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("select * from " + this.disk);
                while(rs.next()){
                    String temp = rs.getString("used_memory");
                    float value = Float.valueOf(temp);
                    int value_fin = Math.round(value);
                    disk_usage.put(rs.getString("date"), value_fin);

                    temp = rs.getString("total_memory");
                    value = Float.valueOf(temp);
                    value_fin = Math.round(value);
                    disk_total.put(rs.getString("date"), value_fin);
                }
                stmt.close();
            }
            finally {
                c.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        model.addAttribute("disk_usage", disk_usage);
        model.addAttribute("disk_total", disk_total);
        return "Disk_Usage";
    }

    @GetMapping("/ram")
    public String getMemChart(Model model) {
        Map<String, Integer> mem_usage = new TreeMap<>();
        Map<String, Integer> mem_total = new TreeMap<>();
        try {
            Connection c = DriverManager.getConnection(this.url, this.user, this.password);
            try {
                Class.forName("org.postgresql.Driver");
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("select * from " + this.mem);
                while(rs.next()){
                    String temp = rs.getString("used_memory");
                    float value = Float.valueOf(temp);
                    int value_fin = Math.round(value);
                    mem_usage.put(rs.getString("date"), value_fin);

                    temp = rs.getString("total_memory");
                    value = Float.valueOf(temp);
                    value_fin = Math.round(value);
                    mem_total.put(rs.getString("date"), value_fin);
                }
                stmt.close();
            }
            finally {
                c.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        model.addAttribute("mem_usage", mem_usage);
        model.addAttribute("mem_total", mem_total);
        return "Memory_Usage";
    }

    @GetMapping("/intfc")
    public String getIntfcChart(Model model) {
        Map<String, Integer> lo_rx = new TreeMap<>();
        Map<String, Integer> eth0_rx = new TreeMap<>();
        Map<String, Integer> lo_tx = new TreeMap<>();
        Map<String, Integer> eth0_tx = new TreeMap<>();

        try {
            Connection c = DriverManager.getConnection(this.url, this.user, this.password);
            try {
                Class.forName("org.postgresql.Driver");
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("select * from " + this.intfc);
                while(rs.next()){
                    eth0_rx.put(rs.getString("date"), rs.getInt("received_new"));
                    eth0_tx.put(rs.getString("date"), rs.getInt("transferred_new"));

                    rs.next();
                    lo_rx.put(rs.getString("date"), rs.getInt("received_new"));
                    lo_tx.put(rs.getString("date"), rs.getInt("transferred_new"));
                }
                stmt.close();
            }
            finally {
                c.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        model.addAttribute("lo_rx", lo_rx);
        model.addAttribute("eth0_rx", eth0_rx);
        model.addAttribute("lo_tx", lo_tx);
        model.addAttribute("eth0_tx", eth0_tx);
        return "Interfaces";
    }
}