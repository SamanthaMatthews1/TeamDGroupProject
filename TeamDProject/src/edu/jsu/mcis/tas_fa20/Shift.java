
package edu.jsu.mcis.tas_fa20;


import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class Shift {
    
    private int id;
    private String description;
    private long  start;
    private long stop;
    private int interval;
    private int graceperiod;
    private int dock;
    private long lunchstart;
    private long lunchend;
    private int lunchdeduct;

    public Shift(int id, String description, long start, long stop, int interval, int graceperiod, int dock, long lunchstart, long lunchend, int lunchdeduct) {
        this.id = id;
        this.description = description;
        this.start = start;
        this.stop = stop;
        this.interval = interval;
        this.graceperiod = graceperiod;
        this.dock = dock;
        this.lunchstart = lunchstart;
        this.lunchend = lunchend;
        this.lunchdeduct = lunchdeduct;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public long getStart() {
        return start;
    }

    public long getStop() {
        return stop;
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

    public long getLunchstart() {
        return lunchstart;
    }

    public long getLunchend() {
        return lunchend;
    }

    public int getLunchdeduct() {
        return lunchdeduct;
    }
    

    
    

    @Override
    public String toString(){
        
        StringBuilder s = new StringBuilder();
        
        String fstart = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(start),
            TimeUnit.MILLISECONDS.toMinutes(start) % TimeUnit.HOURS.toMinutes(1));
        String fStop = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(stop),
            TimeUnit.MILLISECONDS.toMinutes(stop) % TimeUnit.HOURS.toMinutes(1));
        String fLunchstart = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(lunchstart),
            TimeUnit.MILLISECONDS.toMinutes(lunchstart) % TimeUnit.HOURS.toMinutes(1));
        String fLunchend = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(lunchend),
            TimeUnit.MILLISECONDS.toMinutes(lunchend) % TimeUnit.HOURS.toMinutes(1));
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