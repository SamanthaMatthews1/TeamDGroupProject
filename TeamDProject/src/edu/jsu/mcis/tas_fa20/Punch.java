
package edu.jsu.mcis.tas_fa20;

import java.util.Date;
import java.sql.*;
/**
 *
 * @author penguin
 */
public class Punch {
    
    private int terminalid;
    private int punchtypeid;
    private Badge badge;
    private int id;
    private Date date;

    Punch(int id, int terminalid, Badge badge, Date date, int punchtypeid) {
       this.id = id;
       this.terminalid = terminalid;
       this.badge = badge;
       this.date = date;
       this.punchtypeid = punchtypeid;
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
    
    public Date getDate() {
        return date;
    }
}
