
package edu.jsu.mcis.tas_fa20;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
/**
 *
 * @author penguin
 */
public class Punch {
    
    private int terminalid;
    private int punchtypeid;
    private Badge badge;
    private int id;
    private Timestamp date;

    Punch(int id, int terminalid, Badge badge, Timestamp date, int punchtypeid) {
       this.id = id;
       this.terminalid = terminalid;
       this.badge = badge;
       this.date = date;
       this.punchtypeid = punchtypeid;
    }
    
    Punch(Badge badge, int terminalId, int punchType){
        this.badge = badge;
        this.terminalid = terminalId;
        this.punchtypeid = punchType;
    }
    
    public int getTerminalId() {
        return terminalid;
    }
    
    public int getId() {
        return id;
    }
    
    public Badge getBadge() {
        return badge;
    }
    
    public int getPunchTypeId() {
        return punchtypeid;
    }
    
    public Timestamp getTimestamp() {
        return date;
    }
    
    public String printOriginalTimestamp(){
        StringBuilder str = new StringBuilder();
        str.append("#").append(badge.getId()).append(" ");
        if(punchtypeid == 1){
            str.append("CLOCKED IN: ");
        }
        else if (punchtypeid == 0){
            str.append("CLOCKED OUT: ");
        }
        else{
            str.append("TIMED OUT: ");
        }
        Date dt = new Date(date.getTime()+TimeUnit.HOURS.toMillis(5));
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String day = sdf.format(dt);
        day = day.toUpperCase();
        str.append(day).append(" ");
        sdf = new SimpleDateFormat("MM/dd/yyyy");
        day = sdf.format(dt);
        str.append(day).append(" ");
        sdf = new SimpleDateFormat("HH:mm:ss");
        day = sdf.format(dt);
        str.append(day);
        return str.toString();
    }
}
