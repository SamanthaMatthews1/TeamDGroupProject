/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jsu.mcis.tas_fa20;

import java.sql.*;

public class TASDatabase {
    
    private Connection con;
    private String username;
    private String password;
    
    
    TASDatabase(){ // create database interface
        username = "tasuser";
        password = "PASSWORD";
        try{
            // CHANGE THIS IF NEEDED
            Class.forName("com.mysql.jdbc.Driver"); // EVERYONE ELSE
            con = DriverManager.getConnection("jdbc:mysql://localhost/tas", username, password);
            
            
            // DO NOT CHANGE THE NEXT TWO LINES
            //Class.forName("com.mysql.cj.jdbc.Driver"); // WES'S COMPUTER
            //con = DriverManager.getConnection("jdbc:mysql://localhost/tas?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
        
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    
    public Badge getBadge(String id){
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM badge");
            while(rs.next()){
                if(rs.getString("id").equals(id)){
                    // make new badge, return new badge
                    return new Badge(rs.getString("id"), 
                            rs.getString("description"));
                }
            }
        }catch(Exception e){
            return null;
        }
        return null;
    }
    
    
    public Punch getPunch(int id){
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM punch");
            while(rs.next()){
                if(rs.getInt("id") == (id)){
                    // make new punch, return new punch
                    // punch id int, terminal id int, badge id string
                    // timestamp (long int), punch id type int
                    return new Punch(rs.getInt("id"),  // punch number id
                            rs.getInt("terminalid"),  // terminal of punch
                            rs.getString("badgeid"),  // badge of punch
                            rs.getString("originaltimestamp"),  // timestamp of punch
                            rs.getInt("punchtypeid")); // punched auto or manual
                }
            }
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
        return null;
    }
    
    
    
    public Shift getShift(Badge bag){
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM shift");
            while(rs.next()){
                if(bag.getShift() == rs.getInt("id")){
                    // get the shift from the badge's shift id, return the shift
                    return new Shift(rs.getInt("id"),  // shift id type
                            rs.getString("description"), // string of description of shift
                            rs.getString("start"),  // time
                            rs.getString("stop"),  // time 
                            rs.getInt("interval"), // minutes not time
                            rs.getInt("graceperiod"),  // minutes not time
                            rs.getInt("dock"),  // minutes not time
                            rs.getString("lunchstart"),  // time
                            rs.getString("lunchend"),  // time
                            rs.getInt("lunchdeduct")); // minutes not time
                }
            }
        }catch(Exception e){
            return null;
        }
        return null;
    }
    
    
    
    
    
}
