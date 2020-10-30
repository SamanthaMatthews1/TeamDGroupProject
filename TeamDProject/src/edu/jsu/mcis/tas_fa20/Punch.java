
package edu.jsu.mcis.tas_fa20;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
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
    private Timestamp adjusted;
    private String lastAdjusted;

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
        date = new Timestamp(System.currentTimeMillis());
    }
    
    public int getTerminalId() {
        return terminalid;
    }
    
    public int getTerminalid(){
        return terminalid;
    }
    
    public int getId() {
        return id;
    }
    
    public Badge getBadge() {
        return badge;
    }
    
    public String getBadgeid(){
        return badge.getId();
    }
    
    public int getPunchTypeId() {
        return punchtypeid;
    }
    
    public int getPunchtypeid(){
        return punchtypeid;
    }
    
    public Timestamp getTimestamp() {
        return date;
    }
    
    public long getOriginaltimestamp(){
        return date.getTime();
    }
    
    public long getAdjustedtimestamp(){
        return adjusted.getTime();
    }
    
    
    
    public void adjust(Shift s){
        long earliestStart = s.getStart() - s.getGraceperiod();
        long start = s.getStart();
        long latestStart = s.getStart() + s.getGraceperiod();
        long earliestStop = s.getStop() - s.getGraceperiod();
        long stop = s.getStop();
        long latestStop = s.getStop() + s.getGraceperiod();
        long earliestLunchStart = s.getLunchstart() - s.getGraceperiod();
        long lunchStart = s.getLunchstart();
        long latestLunchStart = s.getLunchstart() + s.getGraceperiod();
        long earliestLunchEnd = s.getLunchend() - s.getGraceperiod();
        long lunchEnd = s.getLunchend();
        long latestLunchEnd = s.getLunchend() + s.getGraceperiod();
        
        long time = date.getTime();
        
        if(punchtypeid == 0){
            if(Math.abs(time-lunchStart) < Math.abs(time-stop)){
                if(time < latestLunchStart && time > earliestLunchStart){
                    if(time - lunchStart < 60000 && time - lunchStart > 0){ // less than a minute apart
                        time = lunchStart;
                        adjusted = new Timestamp(time);
                        lastAdjusted = " (None)";
                    }else{
                        time = lunchStart;
                        adjusted = new Timestamp(time);
                        lastAdjusted = " (Lunch Start)";
                    }
                }
                else{
                    int mult = 1;
                    if(time < latestLunchStart) mult=-1;
                    time = time/60000;
                    time = time - time%1;
                    time = time - time % 15;
                    time = time * 60000;
                    adjusted = new Timestamp(time);
                    lastAdjusted = " (Interval Round)";
                }
            }else{
                if(time < latestStop  && time > earliestStop){
                    if(Math.abs(time - stop) < 60000){
                        time = stop;
                        adjusted = new Timestamp(time);
                        lastAdjusted = " (None)";
                    }else{
                        time = stop;
                        adjusted = new Timestamp(time);
                        lastAdjusted = " (Shift Stop)";
                    }
                }else{
                    int mult = 1;
                    if(time < latestStop) mult=-1;
                    time = time/60000;
                    time = time - time%1;
                    time = time - mult*time % 15;
                    time = time * 60000;
                    adjusted = new Timestamp(time);
                    lastAdjusted = " (Interval Round)";
                } 
            }
        }else if(punchtypeid == 1){
            if(Math.abs(time-lunchEnd) < Math.abs(time-start)){
                if(time < latestLunchEnd && time > earliestLunchEnd){
                    if(Math.abs(time - lunchEnd) < 60000){
                        time = lunchEnd;
                        adjusted = new Timestamp(time);
                        lastAdjusted = " (None)";
                    }else{
                        time = lunchEnd;
                        adjusted = new Timestamp(time);
                        lastAdjusted = " (Lunch Stop)";
                    }
                    
                }
                else{
                    int mult = 1;
                    if(time < latestLunchEnd) mult=-1;
                    time = time/60000;
                    time = time - time%1;
                    time = time - time % 15;
                    time = time * 60000;
                    adjusted = new Timestamp(time);
                    lastAdjusted = " (Interval Round)";
                }
            }else{
                if(time < latestStart  && time > earliestStart){
                    if(Math.abs(time-start) < 60000){
                        time = start;
                        adjusted = new Timestamp(time);
                        lastAdjusted = " (None)";
                    }else{
                        time = start;
                        adjusted = new Timestamp(time);
                        lastAdjusted = " (Shift Start)";
                    }
                }else{
                    int mult = 1;
                    if(time < latestStart) mult=-1;
                    time = time/60000;
                    time = time - time%1;
                    time = time - mult*time % 15;
                    time = time * 60000;
                    adjusted = new Timestamp(time);
                    lastAdjusted = " (Interval Round)";
                }
            }
        }
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
        Date dt = new Date(date.getTime() + (60*60*5)*1000);
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
    
    public String printAdjustedTimestamp(){
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
        Date dt = new Date(adjusted.getTime() + (60*60*5)*1000);
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
        str.append(lastAdjusted);
        
        return str.toString();
    }
}
