package edu.jsu.mcis.tas_fa20;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    private boolean isLunch = false;

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
        
        GregorianCalendar shiftStart = new GregorianCalendar();
        GregorianCalendar shiftStop = new GregorianCalendar();
        GregorianCalendar lunchStart = new GregorianCalendar();
        GregorianCalendar lunchStop = new GregorianCalendar();
        GregorianCalendar startInterval = new GregorianCalendar();
        GregorianCalendar stopInterval = new GregorianCalendar();
        GregorianCalendar startGrace = new GregorianCalendar();
        GregorianCalendar stopGrace = new GregorianCalendar();
        GregorianCalendar startDock= new GregorianCalendar();
        GregorianCalendar stopDock = new GregorianCalendar();
                   
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(date.getTime());
        long originalTimeStampInMillis = cal.getTimeInMillis();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal2.setTimeInMillis(originalTimeStampInMillis);
        
        shiftStart.setTimeInMillis(originalTimeStampInMillis);

        shiftStart.set(Calendar.HOUR_OF_DAY, (int)s.getStart());
        shiftStart.set(Calendar.MINUTE, (int)((int)(s.getStart()%60*60*1000)-(s.getStart()-s.getStart()%(60*1000))));
        shiftStart.set(Calendar.SECOND, 0);
        long shiftStartInMillis = shiftStart.getTimeInMillis();

        startInterval.setTimeInMillis(shiftStartInMillis);
        startInterval.add(Calendar.MINUTE, -s.getInterval());
        long startIntervalInMillis = startInterval.getTimeInMillis();
        
        startGrace.setTimeInMillis(shiftStartInMillis);
        startGrace.add(Calendar.MINUTE, s.getGraceperiod());
        long startGraceInMillis = startGrace.getTimeInMillis();
        
        startDock.setTimeInMillis(shiftStartInMillis);
        startDock.add(Calendar.MINUTE, s.getDock());
        long startDockInMillis = startDock.getTimeInMillis();
        
        lunchStart.setTimeInMillis(originalTimeStampInMillis);
        lunchStart.set(Calendar.HOUR_OF_DAY, (int)((int)(s.getLunchstart()%60*60*24*1000)-(s.getLunchstart()-s.getLunchstart()%(60*60*1000))));
        lunchStart.set(Calendar.MINUTE, (int)((int)(s.getLunchstart()%60*60*1000)-(s.getLunchstart()-s.getLunchstart()%(60*1000))));
        lunchStart.set(Calendar.SECOND, 0);
        long lunchStartInMillis = lunchStart.getTimeInMillis();
        
        shiftStop.setTimeInMillis(originalTimeStampInMillis);
        shiftStop.set(Calendar.HOUR_OF_DAY, (int)((int)(s.getStop()%60*60*24*1000)-(s.getStop()-s.getStop()%(60*60*1000))));
        shiftStop.set(Calendar.MINUTE, (int)((int)(s.getStop()%60*60*1000)-(s.getStop()-s.getStop()%(60*1000))));
        shiftStop.set(Calendar.SECOND, 0);
        long shiftStopInMillis = shiftStop.getTimeInMillis();
        
        stopInterval.setTimeInMillis(shiftStopInMillis);
        stopInterval.add(Calendar.MINUTE, s.getInterval());
        long stopIntervalInMillis = stopInterval.getTimeInMillis();
        
        stopGrace.setTimeInMillis(shiftStopInMillis);
        stopGrace.add(Calendar.MINUTE, -s.getGraceperiod());
        long stopGraceInMillis = stopGrace.getTimeInMillis();
        
        stopDock.setTimeInMillis(shiftStopInMillis);
        stopDock.add(Calendar.MINUTE, -s.getDock());
        long stopDockInMillis = stopDock.getTimeInMillis();
        
        lunchStop.setTimeInMillis(shiftStopInMillis);
        lunchStop.set(Calendar.HOUR_OF_DAY, (int)((int)(s.getLunchend()%60*60*24*1000)-(s.getLunchend()-s.getLunchend()%(60*60*1000))));
        lunchStop.set(Calendar.MINUTE, (int)((int)(s.getLunchend()%60*60*1000)-(int)(s.getLunchend()-s.getLunchend()%(60*1000))));
        lunchStop.set(Calendar.SECOND, 0);
        long lunchStopInMillis = lunchStop.getTimeInMillis();
        
        int interval = s.getInterval();
        
        if(shiftStart.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || shiftStart.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            
            if(punchtypeid == 1){
                if(originalTimeStampInMillis >= startIntervalInMillis && originalTimeStampInMillis <= shiftStartInMillis + (s.getInterval() * 60000)){
                    lastAdjusted = " (None)";
                }
                else{
                    if(cal.get(Calendar.MINUTE) % interval <= interval /2){
                        cal2.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - (cal.get(Calendar.MINUTE) % interval));
                        cal2.set(Calendar.SECOND, 0);
                    }
                    else{
                        cal2.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + (interval - (cal.get(Calendar.MINUTE) % interval)));
                        cal2.set(Calendar.SECOND, 0);
                    }
                    lastAdjusted = " (Interval Round)";
                }
            }
        
        
            else if(punchtypeid == 0){
                if(originalTimeStampInMillis <= stopIntervalInMillis && originalTimeStampInMillis >= shiftStopInMillis + (s.getInterval() * 60000)){
                    lastAdjusted = " (None)";
                }
                else{
                    if(cal.get(Calendar.MINUTE) % interval >= interval / 2){
                        cal2.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + (interval - (cal.get(Calendar.MINUTE) % interval)));
                        cal2.set(Calendar.SECOND, 0);
                    }
                    else{
                        cal2.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - (cal.get(Calendar.MINUTE) % interval));
                        cal2.set(Calendar.SECOND, 0);
                    }
                    lastAdjusted = " (Interval Round)";
                }
            }
        }    
        else{
            //Handling Clocking In on Weekdays
            if(punchtypeid == 1){
                if(originalTimeStampInMillis <= shiftStartInMillis && originalTimeStampInMillis >= startIntervalInMillis){
                    cal2.setTimeInMillis(shiftStartInMillis);
                    lastAdjusted = " (Shift Start)";
                }
                else if(originalTimeStampInMillis >= shiftStartInMillis && originalTimeStampInMillis <= startGraceInMillis){
                    cal2.setTimeInMillis(shiftStartInMillis);
                    
                    lastAdjusted =  " (Shift Start)";
                }
                else if(originalTimeStampInMillis >= lunchStartInMillis && originalTimeStampInMillis <= lunchStopInMillis){
                    cal2.setTimeInMillis(lunchStopInMillis);
                    lastAdjusted = " (Lunch Stop)";
                    isLunch = true;
                }
                else if(originalTimeStampInMillis > startGraceInMillis && cal.get(Calendar.MINUTE) % interval > interval /2){
                    cal2.setTimeInMillis(startDockInMillis);
                    lastAdjusted = " (Shift Dock)";
                }
                else if(cal.get(Calendar.HOUR_OF_DAY) == shiftStart.get(Calendar.HOUR_OF_DAY) + 1 && cal.get(Calendar.MINUTE) == shiftStart.get(Calendar.MINUTE)){
                        lastAdjusted = " (None)";
                }
                else{
                    if(cal.get(Calendar.MINUTE) % interval <= interval / 2){
                        cal2.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - (cal.get(Calendar.MINUTE) % interval));
                        cal2.set(Calendar.SECOND, 0);
                    }
                    else{
                        cal2.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + (cal.get(Calendar.MINUTE) % interval));
                        cal2.set(Calendar.SECOND, 0);
                    }
                    lastAdjusted = " (Interval Round)";
                }
            }
            
            else if(punchtypeid == 0){
                //Handling Clocking Out on Weekdays
                if(originalTimeStampInMillis >= stopGraceInMillis && originalTimeStampInMillis <= shiftStopInMillis){
                    cal2.setTimeInMillis(shiftStopInMillis);
                    lastAdjusted = " (Shift Stop)";
                }
                else if(originalTimeStampInMillis <= stopIntervalInMillis && originalTimeStampInMillis >= shiftStopInMillis){
                    cal2.setTimeInMillis(shiftStopInMillis);
                    
                    lastAdjusted = " (Shift Stop)";
                }
                else if(originalTimeStampInMillis >= lunchStartInMillis && originalTimeStampInMillis < lunchStopInMillis){
                    cal2.setTimeInMillis(lunchStartInMillis);
                    lastAdjusted = " (Lunch Start)";
                    isLunch = true;
                }
                else if(originalTimeStampInMillis < stopGraceInMillis && cal.get(Calendar.MINUTE) % interval < interval /2){
                    cal2.setTimeInMillis(stopDockInMillis);
                    lastAdjusted = " (Shift Dock)";
                }
                else if(cal.get(Calendar.HOUR_OF_DAY) == shiftStop.get(Calendar.HOUR_OF_DAY) + 1 && cal.get(Calendar.MINUTE) == shiftStop.get(Calendar.MINUTE)){
                        lastAdjusted = " (None)";
                        cal2.set(Calendar.SECOND,0);
                }
                else{
                    if(cal.get(Calendar.MINUTE) % interval >= interval / 2){
                        cal2.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + (cal.get(Calendar.MINUTE) % interval)+1);
                        cal2.set(Calendar.SECOND, 0);
                    }
                    else{
                        cal2.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - (cal.get(Calendar.MINUTE) % interval));
                        cal2.set(Calendar.SECOND, 0);
                    }
                    lastAdjusted = " (Interval Round)";
                }
            }
        }
        
        adjusted = new Timestamp (cal2.getTimeInMillis());
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
