
package edu.jsu.mcis.tas_fa20;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
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
        long time = date.getTime();
        LocalTime ttime = new Timestamp(time).toLocalDateTime().toLocalTime();
        long tempTime = ttime.toSecondOfDay();
        
        long earliestStart = s.getStart() - s.getInterval() * 60;
        LocalTime es = new Timestamp(earliestStart).toLocalDateTime().toLocalTime();
        earliestStart = es.toSecondOfDay();
        
        long start = s.getStart();
        LocalTime sr = new Timestamp(start).toLocalDateTime().toLocalTime();
        start = sr.toSecondOfDay();
        long startGrace = s.getStart() + s.getGraceperiod() * 60;
        LocalTime sg = new Timestamp(startGrace).toLocalDateTime().toLocalTime();
        startGrace = sg.toSecondOfDay();
        
        long latestStart = s.getStart() + s.getInterval() * 60;
        LocalTime ls = new Timestamp(latestStart).toLocalDateTime().toLocalTime();
        latestStart = ls.toSecondOfDay();
        
        
        long earliestStop = s.getStop() - s.getInterval() * 60;
        LocalTime et = new Timestamp(earliestStop).toLocalDateTime().toLocalTime();
        earliestStop = et.toSecondOfDay();
        long stop = s.getStop();
        LocalTime st = new Timestamp(stop).toLocalDateTime().toLocalTime();
        stop = st.toSecondOfDay();
        
        long stopGrace = s.getStop() - s.getGraceperiod() * 60;
        LocalTime tg = new Timestamp(stopGrace).toLocalDateTime().toLocalTime();
        stopGrace = tg.toSecondOfDay();
        long latestStop = s.getStop() + s.getInterval() * 60;
        LocalTime lt = new Timestamp(latestStop).toLocalDateTime().toLocalTime();
        latestStop = lt.toSecondOfDay();
        
        
        long lunchStart = s.getLunchstart();
        LocalTime lunchs = new Timestamp(lunchStart).toLocalDateTime().toLocalTime();
        lunchStart = lunchs.toSecondOfDay();
        long lunchEnd = s.getLunchend();
        LocalTime lunche = new Timestamp(lunchEnd).toLocalDateTime().toLocalTime();
        lunchEnd = lunche.toSecondOfDay();
        
        
        
        Timestamp tadjusted = new Timestamp(round(time, s.getInterval() * 60000));
        
        if(time - time % 60000 == tadjusted.getTime()){
            lastAdjusted = " (None)";
            adjusted = tadjusted;
            return;
        }
        
        
        if(punchtypeid == 0){ // clocked out
            if(Math.abs(tempTime - lunchStart) < Math.abs(tempTime - stop)){ // lunch break
                adjusted = tadjusted;
                lastAdjusted = " (Interval Round)";

                if(tempTime > lunchStart){
                    lastAdjusted = " (Lunch Start)";
                }
            }
            else{ // clocking out
                if(tempTime > stop){
                    if(tempTime < latestStop){
                        lastAdjusted = " (Shift Stop)";
                        adjusted = roundDown(time, s.getInterval());
                    }
                    else{
                        lastAdjusted = " (Interval Round)";
                        adjusted = roundUp(time, s.getInterval());
                    }
                }
                else{
                    if(tempTime > stopGrace){
                        adjusted = roundUp(time, s.getInterval());
                        lastAdjusted = " (Shift Stop)";
                        
                    }else if(tempTime > earliestStop){
                        adjusted = roundDown(time, s.getInterval());
                        lastAdjusted = " (Shift Dock)";
                    }else{
                        adjusted = new Timestamp(round(time, s.getInterval()));
                        lastAdjusted = " (Interval Round)";
                    }
                }
            }
        }else if(punchtypeid == 1){ // clocked in
            if(Math.abs(time - lunchEnd) < Math.abs(tempTime - start)){ // lunch break over
                adjusted = new Timestamp(round(time, s.getInterval()));
                lastAdjusted = " (Lunch Stop)";
                if(tempTime > lunchEnd){
                    lastAdjusted = " (Interval Round)";
                }
            }else{
                if(tempTime > start){
                    if(tempTime < startGrace){
                        adjusted = roundDown(time, s.getInterval());
                        lastAdjusted = " (Shift Start)";
                    }
                    else if (tempTime < latestStart){
                        adjusted = roundDown(time, s.getInterval());
                        lastAdjusted = " (Shift Dock)";
                    }
                    else{
                        adjusted = roundUp(time, s.getInterval()); // not working?
                        lastAdjusted = " (Shift Start)";
                    }
                }else{
                    if(tempTime > earliestStart){ // round up no matter what
                        adjusted = roundUp(time, s.getInterval());
                        lastAdjusted = " (Shift Start)";
                    }else{
                        lastAdjusted = " (Interval Round)";
                        adjusted = roundUp(time, s.getInterval());
                    }
                }
            }
        }else{ // timed out... worst case senario i guess
            adjusted = new Timestamp(round(time, s.getInterval()));
            lastAdjusted = " (Interval Round)";
        }

        
        
        
        /*
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
        }*/
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
    
    private long round(long time, long interval){
        if(time%interval > interval/2){
            time += interval - time%interval;
            return time;
        }
        else{
            time -= time%interval;
        }
        return time;
    }
    
    private Timestamp roundUp(long time, long interval){
        Timestamp temp = roundDown(time, interval*60000);
        return new Timestamp(temp.getTime() + 15*60*1000);
    }
    private Timestamp roundDown(long time, long interval){
        time -= time%(interval);
        return new Timestamp(time);
    }
}
