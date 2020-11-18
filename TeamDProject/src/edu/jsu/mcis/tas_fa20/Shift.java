package edu.jsu.mcis.tas_fa20;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
    
public class Shift {
    
    private int id;
    private String description;
    
    private long start;
    private long stop;
    private int interval;
    private int graceperiod;
    private int dock;
    private long lunchStart;
    private long lunchStop;
    private int lunchDeduct;
    
    private int shiftStartHour, shiftStopHour, lunchStartHour, lunchStopHour;
    private int shiftStartMinute, shiftStopMinute, lunchStartMinute, lunchStopMinute;
    private int shiftDuration, lunchDuration;

    public Shift(int id, String description, long start, long stop, int interval, int graceperiod, int dock, long lunchstart, long lunchstop, int lunchdeduct) {
        
        this.id = id;
        this.description = description;
        this.start = start;
        this.stop = stop;
        this.interval = interval;
        this.graceperiod = graceperiod;
        this.dock = dock;
        this.lunchStart = lunchstart;
        this.lunchStop = lunchstop;
        this.lunchDeduct = lunchdeduct;
        
        /* New Shift Fields (addad 11/13/20) */
        
        GregorianCalendar gcShiftStart = new GregorianCalendar();
        gcShiftStart.setTimeInMillis(start);
        
        GregorianCalendar gcShiftStop = new GregorianCalendar();
        gcShiftStop.setTimeInMillis(stop);
        
        shiftStartHour = gcShiftStart.get(Calendar.HOUR_OF_DAY);
        shiftStopHour = gcShiftStop.get(Calendar.HOUR_OF_DAY);
        shiftStartMinute = gcShiftStart.get(Calendar.MINUTE);
        shiftStopMinute = gcShiftStop.get(Calendar.MINUTE);
        
        /* New Lunch Fields (addad 11/13/20) */
        
        GregorianCalendar gcLunchStart = new GregorianCalendar();
        gcLunchStart.setTimeInMillis(lunchstart);
        
        GregorianCalendar gcLunchStop = new GregorianCalendar();
        gcLunchStop.setTimeInMillis(lunchstop);
        
        lunchStartHour = gcLunchStart.get(Calendar.HOUR_OF_DAY);
        lunchStopHour = gcLunchStop.get(Calendar.HOUR_OF_DAY);
        lunchStartMinute = gcLunchStart.get(Calendar.MINUTE);
        lunchStopMinute = gcLunchStop.get(Calendar.MINUTE);
        
        /* New Duration Fields (added 11/13/20) */
        
        LocalTime lcShiftStart = LocalTime.of(shiftStartHour, shiftStartMinute);
        LocalTime lcShiftStop = LocalTime.of(shiftStopHour, shiftStopMinute);        
        shiftDuration = (int)(ChronoUnit.MINUTES.between(lcShiftStart, lcShiftStop));
        
        LocalTime lcLunchStart = LocalTime.of(lunchStartHour, lunchStartMinute);
        LocalTime lcLunchStop = LocalTime.of(lunchStopHour, lunchStopMinute);        
        lunchDuration = (int)(ChronoUnit.MINUTES.between(lcLunchStart, lcLunchStop));
        
    }
    
    /* Added 11/13/20 */
    
    @Override
    public String toString() {
        
        String s = description + ": ";
        s += String.format("%02d", shiftStartHour) + ":" + String.format("%02d", shiftStartMinute) + " - ";
        s += String.format("%02d", shiftStopHour) + ":" + String.format("%02d", shiftStopMinute);
        s += " (" + shiftDuration + " minutes)";
        
        s += "; Lunch: " + String.format("%02d", lunchStartHour) + ":" + String.format("%02d", lunchStartMinute);
        s += " - " + String.format("%02d", lunchStopHour) + ":" + String.format("%02d", lunchStopMinute);
        s += " (" + lunchDuration + " minutes)";
        
        return s;
        
    }

    /* GETTER METHODS (generated 11/13/20) */

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getInterval() {
        return interval;
    }

    public int getGraceperiod() {
        return graceperiod;
    }

    public int getDock() {
        return dock;
    }

    public int getLunchDeduct() {
        return lunchDeduct;
    }

    public int getShiftStartHour() {
        return shiftStartHour;
    }

    public int getShiftStopHour() {
        return shiftStopHour;
    }

    public int getLunchStartHour() {
        return lunchStartHour;
    }

    public int getLunchStopHour() {
        return lunchStopHour;
    }

    public int getShiftStartMinute() {
        return shiftStartMinute;
    }

    public int getShiftStopMinute() {
        return shiftStopMinute;
    }

    public int getLunchStartMinute() {
        return lunchStartMinute;
    }

    public int getLunchStopMinute() {
        return lunchStopMinute;
    }

    public int getShiftDuration() {
        return shiftDuration;
    }

    public int getLunchDuration() {
        return lunchDuration;
    }
    
    public String oldToString() {
        
        StringBuilder s = new StringBuilder();
        
        String fstart = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(start),
            TimeUnit.MILLISECONDS.toMinutes(start) % TimeUnit.HOURS.toMinutes(1));
        String fStop = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(stop),
            TimeUnit.MILLISECONDS.toMinutes(stop) % TimeUnit.HOURS.toMinutes(1));
        String fLunchstart = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(lunchStart),
            TimeUnit.MILLISECONDS.toMinutes(lunchStart) % TimeUnit.HOURS.toMinutes(1));
        String fLunchend = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(lunchStop),
            TimeUnit.MILLISECONDS.toMinutes(lunchStop) % TimeUnit.HOURS.toMinutes(1));
        //"Shift 1: 07:00 - 15:30 (510 minutes); Lunch: 12:00 - 12:30 (30 minutes)"
        
        s.append(description);
        s.append(": ").append(fstart).append(" - ");
        s.append(fStop).append(" (510 minutes); ");
        s.append("Lunch: ").append(fLunchstart).append(" - ");
        s.append(fLunchend).append(" (").append("30");
        s.append(" minutes)");
        
        return ( s.toString());
        
    }
    
}