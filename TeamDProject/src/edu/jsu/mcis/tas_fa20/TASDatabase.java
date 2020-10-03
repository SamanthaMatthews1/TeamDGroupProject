/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jsu.mcis.tas_fa20;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

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
                    //System.out.println(new Date(rs.getTimestamp("originaltimestamp").getTime()));
                    return new Punch(rs.getInt("id"),  // punch number id
                            rs.getInt("terminalid"),  // terminal of punch
                            rs.getString("badgeid"),  // badge of punch
                            new Date(rs.getTimestamp("originaltimestamp").getTime()),  // timestamp of punch
                            rs.getInt("punchtypeid")); // punched auto or manual
                }
            }
        }catch(Exception e){
            //System.out.println(e);
            return null;
        }
        return null;
    }
    
    public Shift getShift(Badge bag){
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM employee");
            while(rs.next()){
                if(bag.getId().equals(rs.getString("badgeid"))){
                    // get the shift from the badge's shift id, return the shift
                    Statement sta = con.createStatement();
                    ResultSet res = st.executeQuery("SELECT * FROM shift");
                    return new Shift(res.getInt("id"), // shift id type
                            res.getString("description"), // string of description of shift
                            res.getTime("start").getTime(),  // long (miliseconds after 12 am)
                            res.getTime("stop").getTime(),  // long (miliseconds after 12 am) 
                            res.getInt("interval"), // minutes not time
                            res.getInt("graceperiod"),  // minutes not time
                            res.getInt("dock"),  // minutes not time
                            res.getTime("lunchstart").getTime(),  // long (miliseconds after 12 am)
                            res.getTime("lunchend").getTime(),  // long (miliseconds after 12 am)
                            res.getInt("lunchdeduct")); // minutes not time
                }
            }
        }catch(Exception e){
            return null;
        }
        return null;
    }
    
    public Shift getShift(int id){
        try{
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM shift WHERE id="+id);
            res.next();
            return new Shift(res.getInt("id"), // shift id type
                            res.getString("description"), // string of description of shift
                            res.getTime("start").getTime(),  // long (miliseconds after 12 am)
                            res.getTime("stop").getTime(),  // long (miliseconds after 12 am) 
                            res.getInt("interval"), // minutes not time
                            res.getInt("graceperiod"),  // minutes not time
                            res.getInt("dock"),  // minutes not time
                            res.getTime("lunchstart").getTime(),  // long (miliseconds after 12 am)
                            res.getTime("lunchend").getTime(),  // long (miliseconds after 12 am)
                            res.getInt("lunchdeduct")); // minutes not time
        }
        catch(Exception e){
            return null;
        }
    }
    
    
    
    public int insertPunch(Punch p){// -1 output means error
        try{
            Statement st = con.createStatement();
            int id = -1;
            ResultSet rs = st.executeQuery("SELECT MAX(id) FROM punch");
            rs.next();
            id = rs.getInt(1) + 1;
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO punch VALUES (");
            query.append(id).append(", ").append(p.getTerminalId()).append(", ");
            query.append(p.getBadgeId()).append(", ").append(p.getTimeStamp());
            query.append(", ").append(p.getPunchType()).append(")");
            ResultSet res = st.executeQuery(query.toString());
            
            return id;
        } catch(Exception e){
            //System.out.println(e);   
        }
        return -1;
    }
    
    
    public ArrayList<Punch> getDailyPunchList(Badge bag, long ts){
        ArrayList<Punch> punches = new ArrayList<Punch>();
        
        try{
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM punch WHERE badgeid="+bag.getId());
        }catch(Exception e){
            //System.out.println(e);
        }
        return punches;
        
    }
    
}
