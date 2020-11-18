package edu.jsu.mcis.tas_fa20;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TASDatabase {
    
    private Connection con;
    
    private String username;
    private String password;
    
    public TASDatabase() { // create database interface
        
        username = "tasuser";
        password = "PASSWORD";
        
        try {

            // CHANGE THIS IF NEEDED
            //Class.forName("com.mysql.jdbc.Driver"); // EVERYONE ELSE
            //con = DriverManager.getConnection("jdbc:mysql://localhost/tas?serverTimezone=UTC", username, password);
            
            // DO NOT CHANGE THE NEXT TWO LINES
            Class.forName("com.mysql.cj.jdbc.Driver"); // WES'S COMPUTER
            //con = DriverManager.getConnection("jdbc:mysql://localhost/tas?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
            con = DriverManager.getConnection("jdbc:mysql://localhost/tas?useUnicode=true&autoReconnect=true&useSSL=false", username, password);
        
        }
        catch(Exception e){ e.printStackTrace(); }
        
    }    
    
    public Badge getBadge(String id) {
        
        Badge b = null;
        
        try {
            
            String query = "SELECT * FROM badge WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, id);
            
            ResultSet res = ps.executeQuery();
            
            if (res.next()) {                
                b = new Badge(id, res.getString("description"));
            }
            
        }
        catch(Exception e){ e.printStackTrace(); }
        
        return b;
        
    }
    
    public Punch getPunch(int id) {
        
        Punch p = null;
        
        try {
            
            String query = "SELECT terminalid, badgeid, punchtypeid, UNIX_TIMESTAMP(`originaltimestamp`) * 1000 AS originaltimestamp FROM punch WHERE punch.id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            
            ResultSet res = ps.executeQuery();

            if (res.next()) {
                
                Badge badge = getBadge(res.getString("badgeid"));
                int terminalid = res.getInt("terminalid");
                int punchtypeid = res.getInt("punchtypeid");                
                long originaltimestamp = res.getLong("originaltimestamp");
                
                Timestamp date = new Timestamp(originaltimestamp);
                
                p = new Punch(id, terminalid, badge, date, punchtypeid);
                    
            }

            res.close();

        }
        catch (Exception e) { e.printStackTrace(); }
        
        return p;
        
    }
    
    public Shift getShift(Badge badge) {

        String id = badge.getId();
        Shift s = null;
        
        try {

            String query = "SELECT * FROM employee WHERE badgeid = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, id);
            
            ResultSet res = ps.executeQuery();

            if (res.next()) {
                
                int shiftid = res.getInt("shiftid");
                s = getShift(shiftid);
                    
            }

            res.close();

        }
        catch (Exception e) { e.printStackTrace(); }
        
        return s;
        
    }
    
    public Shift getShift(int id) {
        
        Shift s = null;
        
        try {
            
            String query = "SELECT * FROM shift WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            
            ResultSet res = ps.executeQuery();
            
            if (res.next()) {
                s = new Shift(res.getInt("id"), // shift id type
                              res.getString("description"), // string of description of shift
                              res.getTime("start").getTime(),  // long (miliseconds after 12 am)
                              res.getTime("stop").getTime(),  // long (miliseconds after 12 am) 
                              res.getInt("interval"), // minutes not time
                              res.getInt("graceperiod"),  // minutes not time
                              res.getInt("dock"),  // minutes not time
                              res.getTime("lunchstart").getTime(),  // long (miliseconds after 12 am)
                              res.getTime("lunchstop").getTime(),  // long (miliseconds after 12 am)
                              res.getInt("lunchdeduct")); // minutes not time
            }
            
            res.close();
            
        }
        catch(Exception e) { e.printStackTrace(); }
        
        return s;
        
    }
    
    public int insertPunch(Punch p) {

        int key = 0;

        try {

            /* Get Punch Parameters */

            String badgeid = p.getBadgeid();
            int terminalid = p.getTerminalid();
            int punchtypeid = p.getPunchtypeid();

            /* Get Punch Timestamp as String */

            GregorianCalendar ots = new GregorianCalendar();
            ots.setTimeInMillis(p.getOriginaltimestamp());
            String otsString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(ots.getTime());

            /* Parametrize Query */

            String query = "INSERT INTO punch (terminalid, badgeid, originaltimestamp, punchtypeid) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, terminalid);
            ps.setString(2, badgeid);
            ps.setString(3, otsString);
            ps.setInt(4, punchtypeid);

            /* Execute Query */

            int result = ps.executeUpdate();

            /* Was insertion successful?  If so, get generated key */

            if (result == 1) {

                ResultSet keys = ps.getGeneratedKeys();

                if (keys.next()) {
                    key = keys.getInt(1);
                }

            }

        } // End try{}

        catch (Exception e) { e.printStackTrace(); }

        return key;

    }
    
    public ArrayList<Punch> getDailyPunchList(Badge bag, long ts){
        ArrayList<Punch> punches = new ArrayList<Punch>();
        
        //help here D:
        String badgeid = bag.getId();
        
        boolean hasresults;
        
        ResultSet rs = null;
        PreparedStatement pstSelect = null, pstUpdate = null;
        ResultSetMetaData metadata = null;
        int columnCount, resultCount, updateCount = 0;
        String key, query;
        
        
        //Creating Gregorian Calendar no idea! uhh can comment it out to test other things.. 
        GregorianCalendar gcStart = new GregorianCalendar();
        gcStart.setTimeInMillis(ts);
        gcStart.set(Calendar.HOUR_OF_DAY, 0);
        gcStart.set(Calendar.MINUTE, 0);
        gcStart.set(Calendar.SECOND, 0);
        
        GregorianCalendar gcStop = new GregorianCalendar();
        gcStop.setTimeInMillis(ts);
        gcStop.set(Calendar.HOUR_OF_DAY, 23);
        gcStop.set(Calendar.MINUTE, 59);
        gcStop.set(Calendar.SECOND, 59);
        
        try{
            
            if(con.isValid(0)){
                
                query = "SELECT *, UNIX_TIMESTAMP(`ORIGINALTIMESTAMP`) * 1000 AS ts\n"
               + "FROM punch\n" 
               + "WHERE badgeid = ?\n"
               + "HAVING ts >= ?\n"
               + "AND ts <= ?\n";
                pstSelect = con.prepareStatement(query);
                pstSelect.setString(1, badgeid);
                pstSelect.setLong(2, gcStart.getTimeInMillis());
                pstSelect.setLong(3, gcStop.getTimeInMillis());
                
                
                System.out.println("Submitting Query...");
                hasresults = pstSelect.execute();
                
                //Getting Final Results
                
                while(hasresults || pstSelect.getUpdateCount() != -1){
                    
                    
                    if(hasresults){
                        
                        rs = pstSelect.getResultSet();
                        metadata = rs.getMetaData();
                        columnCount = metadata.getColumnCount();
                        
                        while(rs.next()){
                            
                            int id = rs.getInt("id");
                            punches.add(getPunch(id));
                        }         
                    }
                    else{
                        
                        resultCount = pstSelect.getUpdateCount();
                        
                        if(resultCount == -1){
                            break;
                            
                        }
                    }
                    
                    hasresults = pstSelect.getMoreResults();
                }
                
            }

        
        }catch(Exception e){
            
        }
        
        finally{
            //i might change pstSelect to ps and pstUpdate to pu for shorter variables
            if (rs != null){ try { rs.close(); rs = null; } catch (Exception e){}}
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }
            
            if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; } catch (Exception e) {} }
            
        }
        return punches;
        
    }

}