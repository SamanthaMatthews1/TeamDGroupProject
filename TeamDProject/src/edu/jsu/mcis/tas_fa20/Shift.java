
package edu.jsu.mcis.tas_fa20;


public class Shift {
    
    private String id;
    private String description;
    private long  start;
    private long stop;
    private int interval;
    private int graceperiod;
    private int dock;
    private long lunchstart;
    private long lunchend;
    private int lunchdeduct;

    public Shift(String id, String description, long start, long stop, int interval, int graceperiod, int dock, long lunchstart, long lunchend, int lunchdeduct) {
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

    public String getId() {
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
        
        //"Shift 1: 07:00 - 15:30 (510 minutes); Lunch: 12:00 - 12:30 (30 minutes)"
        
        s.append(id).append(start).append(" - ");
        s.append(stop).append("(").append(interval).append("); ");
        s.append("Lunch: ").append(lunchstart).append(" - ");
        s.append(lunchend).append(" (").append(lunchdeduct);
        s.append(" minutes)");
        
        return ( s.toString());
        
    }
    
}