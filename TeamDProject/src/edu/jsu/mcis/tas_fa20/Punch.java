package edu.jsu.mcis.tas_fa20;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Punch {
    
    private int id, terminalid, punchtypeid;
    
    private Badge badge;
                    
    private String adjustmentType;
    
    private long originaltimestamp, adjustedtimestamp;

    public static final int CLOCK_OUT_PUNCH = 0;
    public static final int CLOCK_IN_PUNCH = 1;
    public static final int TIME_OUT_PUNCH = 2;
    
    public Punch(int id, int terminalid, Badge badge, Timestamp date, int punchtypeid) {
        
        this.id = id;
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtypeid = punchtypeid;

        originaltimestamp = adjustedtimestamp = date.getTime();
       
    }
    
    public Punch(Badge badge, int terminalid, int eventtypeid) {
        
        this.badge = badge;
        this.terminalid = terminalid;
        this.punchtypeid = eventtypeid;
        this.adjustmentType = "";
        
        GregorianCalendar gc = new GregorianCalendar();
        originaltimestamp = adjustedtimestamp = gc.getTimeInMillis();
        
    }
    
    public void adjust(Shift s) {
        
        boolean adjusted = false;
        
        int shiftinterval = s.getInterval();
        
        /* INITIALIZE ORIGINAL / ADJUSTED TIMESTAMP OBJECTS */
        
        GregorianCalendar ots = new GregorianCalendar();
        GregorianCalendar ats = new GregorianCalendar();
        
        ots.setTimeInMillis(originaltimestamp);
        ats.setTimeInMillis(originaltimestamp);
        
        adjustedtimestamp = originaltimestamp;
        
        /* GENERATE TIMESTAMP OBJECTS */
        
        int originalminute = ots.get(Calendar.MINUTE);
        int adjustedminute;

        /* Shift Start */
        
        GregorianCalendar tsShiftStart = new GregorianCalendar();
        tsShiftStart.setTimeInMillis(originaltimestamp);
        tsShiftStart.set(Calendar.HOUR_OF_DAY, s.getShiftStartHour());
        tsShiftStart.set(Calendar.MINUTE, s.getShiftStartMinute());
        tsShiftStart.set(Calendar.SECOND, 0);
        
        long shiftstart = tsShiftStart.getTimeInMillis();
        
        /* Shift Stop */
        
        GregorianCalendar tsShiftStop = new GregorianCalendar();
        tsShiftStop.setTimeInMillis(originaltimestamp);
        tsShiftStop.set(Calendar.HOUR_OF_DAY, s.getShiftStopHour());
        tsShiftStop.set(Calendar.MINUTE, s.getShiftStopMinute());
        tsShiftStop.set(Calendar.SECOND, 0);
        
        long shiftstop = tsShiftStop.getTimeInMillis();
        
        /* Lunch Start */
        
        GregorianCalendar tsLunchStart = new GregorianCalendar();
        tsLunchStart.setTimeInMillis(originaltimestamp);
        tsLunchStart.set(Calendar.HOUR_OF_DAY, s.getLunchStartHour());
        tsLunchStart.set(Calendar.MINUTE, s.getLunchStartMinute());
        tsLunchStart.set(Calendar.SECOND, 0);
        
        long lunchstart = tsLunchStart.getTimeInMillis();
        
        /* Lunch Stop */
        
        GregorianCalendar tsLunchStop = new GregorianCalendar();
        tsLunchStop.setTimeInMillis(originaltimestamp);
        tsLunchStop.set(Calendar.HOUR_OF_DAY, s.getLunchStopHour());
        tsLunchStop.set(Calendar.MINUTE, s.getLunchStopMinute());
        tsLunchStop.set(Calendar.SECOND, 0);
        
        long lunchstop = tsLunchStop.getTimeInMillis();
        
        /* Interval (Shift Start) */
        
        GregorianCalendar tsIntervalStart = new GregorianCalendar();
        tsIntervalStart.setTimeInMillis(tsShiftStart.getTimeInMillis());
        tsIntervalStart.add(Calendar.MINUTE, -(s.getInterval()));
        
        long intervalstart = tsIntervalStart.getTimeInMillis();
        
        /* Grace (Shift Start) */
        
        GregorianCalendar tsGraceStart = new GregorianCalendar();
        tsGraceStart.setTimeInMillis(tsShiftStart.getTimeInMillis());
        tsGraceStart.add(Calendar.MINUTE, s.getGraceperiod());
        
        long gracestart = tsGraceStart.getTimeInMillis();
        
        /* Dock (Shift Start) */
        
        GregorianCalendar tsDockStart = new GregorianCalendar();
        tsDockStart.setTimeInMillis(tsShiftStart.getTimeInMillis());
        tsDockStart.add(Calendar.MINUTE, s.getDock());
        
        long dockstart = tsDockStart.getTimeInMillis();
        
        /* Interval (Shift Stop) */
        
        GregorianCalendar tsIntervalStop = new GregorianCalendar();
        tsIntervalStop.setTimeInMillis(tsShiftStop.getTimeInMillis());
        tsIntervalStop.add(Calendar.MINUTE, s.getInterval());
        
        long intervalstop = tsIntervalStop.getTimeInMillis();
        
        /* Grace (Shift Stop) */
        
        GregorianCalendar tsGraceStop = new GregorianCalendar();
        tsGraceStop.setTimeInMillis(tsShiftStop.getTimeInMillis());
        tsGraceStop.add(Calendar.MINUTE, -(s.getGraceperiod()));
        
        long gracestop = tsGraceStop.getTimeInMillis();
        
        /* Dock (Shift Stop) */
        
        GregorianCalendar tsDockStop = new GregorianCalendar();
        tsDockStop.setTimeInMillis(tsShiftStop.getTimeInMillis());
        tsDockStop.add(Calendar.MINUTE, -(s.getDock()));
        
        long dockstop = tsDockStop.getTimeInMillis();
        
        /* PERFORM CLOCK IN ADJUSTMENTS */
        
        if ( (ots.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) && (ots.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) ) {
            
            /* Clock In Punches */
        
            if (punchtypeid == CLOCK_IN_PUNCH) {

                /* Interval Start */

                if ((originaltimestamp >= intervalstart) && (originaltimestamp <= shiftstart)) {
                    
                    adjustedtimestamp = shiftstart;
                    adjustmentType = "Shift Start";
                    adjusted = true;
                    
                }

                /* Grace Start */

                else if ((originaltimestamp > shiftstart) && (originaltimestamp <= gracestart)) {
                    
                    adjustedtimestamp = shiftstart;
                    adjustmentType = "Shift Start";
                    adjusted = true;
                    
                }

                /* Dock Start */

                else if ((originaltimestamp > gracestart) && (originaltimestamp <= dockstart)) {
                    
                    adjustedtimestamp = dockstart;
                    adjustmentType = "Shift Dock";
                    adjusted = true;
                    
                }

                /* Lunch Stop */

                else if ((originaltimestamp >= lunchstart) && (originaltimestamp <= lunchstop)) {
                    
                    adjustedtimestamp = lunchstop;
                    adjustmentType = "Lunch Stop";
                    adjusted = true;
                    
                }
                
            }
            
            /* Clock Out Punches */

            else if (punchtypeid == CLOCK_OUT_PUNCH) {

                /* Lunch Start */

                if ((originaltimestamp >= lunchstart) && (originaltimestamp <= lunchstop)) {
                    
                    adjustedtimestamp = lunchstart;
                    adjustmentType = "Lunch Start";
                    adjusted = true;
                    
                }

                /* Dock Stop */

                else if ((originaltimestamp >= dockstop) && (originaltimestamp < gracestop)) {
                    
                    adjustedtimestamp = dockstop;
                    adjustmentType = "Shift Dock";
                    adjusted = true;
                    
                }

                /* Grace Stop */

                else if ((originaltimestamp >= gracestop) && (originaltimestamp < shiftstop)) {
                    
                    adjustedtimestamp = shiftstop;
                    adjustmentType = "Shift Stop";
                    adjusted = true;
                    
                }

                /* Interval Stop */

                else if ((originaltimestamp >= shiftstop) && (originaltimestamp <= intervalstop)) {
                    
                    adjustedtimestamp = shiftstop;
                    adjustmentType = "Shift Stop";
                    adjusted = true;
                    
                }
                
            }
            
        }
        
        /* If none of the other rules apply ... */
        
        if ( !adjusted ) {
            
            /* ... perform an round to the nearest interval increment as needed ... */

            if ( originalminute % shiftinterval != 0 ) {
                
                // Round Down

                if ((originalminute % shiftinterval) < (shiftinterval / 2)) {
                    adjustedminute = (Math.round(originalminute / shiftinterval) * shiftinterval);
                }
                
                // Round Up

                else {
                    adjustedminute = (Math.round(originalminute / shiftinterval) * shiftinterval) + shiftinterval;
                }
                
                ats.add(Calendar.MINUTE, (adjustedminute - originalminute));
                ats.set(Calendar.SECOND, 0);
                
                adjustedtimestamp = ats.getTimeInMillis();
                adjustmentType = "Interval Round";

            }
            
            /* ... or else, leave the punch alone */

            else {
                
                ats.set(Calendar.SECOND, 0);
                adjustedtimestamp = ats.getTimeInMillis();
                adjustmentType = "None";
                
            }

        }
        
    }
    
    public String printOriginalTimestamp() {
        
        GregorianCalendar ots = new GregorianCalendar();
        ots.setTimeInMillis(originaltimestamp);
        
        StringBuilder s = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("EEE MM/dd/yyyy HH:mm:ss");

        s.append("#").append(badge.getId()).append(" ");

        switch ( punchtypeid ) {

            case CLOCK_OUT_PUNCH:
                s.append("CLOCKED OUT");
                break;
            case CLOCK_IN_PUNCH:
                s.append("CLOCKED IN");
                break;
            case TIME_OUT_PUNCH:
                s.append("TIMED OUT");
                break;
	}

        s.append(": ").append(format.format(ots.getTime()).toUpperCase());

        return s.toString();

    }

    public String printAdjustedTimestamp() {
        
        GregorianCalendar ots = new GregorianCalendar();
        GregorianCalendar ats = new GregorianCalendar();
        ots.setTimeInMillis(originaltimestamp);
        ats.setTimeInMillis(adjustedtimestamp);
        
        StringBuilder s = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("EEE MM/dd/yyyy HH:mm:ss");

        s.append("#").append(badge.getId()).append(" ");

        switch ( punchtypeid ) {

            case CLOCK_OUT_PUNCH:
                s.append("CLOCKED OUT");
                break;
            case CLOCK_IN_PUNCH:
                s.append("CLOCKED IN");
                break;
            case TIME_OUT_PUNCH:
                s.append("TIMED OUT");
                break;
	}
        
        s.append(": ").append(format.format(ats.getTime()).toUpperCase());
        
        s.append(" (").append(adjustmentType).append(")");

        return s.toString();

    }
    
    /* GETTERS */

    public int getId() {
        return id;
    }

    public int getTerminalId() {
        return terminalid;
    }
    
    public int getTerminalid() {
        return terminalid;
    }

    public int getEventtypeid() {
        return punchtypeid;
    }

    public Badge getBadge() {
        return badge;
    }
    
    public String getBadgeid() {
        return badge.getId();
    }

    public String getEventdata() {
        return adjustmentType;
    }

    public long getOriginaltimestamp() {
        return originaltimestamp;
    }

    public long getAdjustedtimestamp() {
        return adjustedtimestamp;
    }

    public int getPunchTypeId() {
        return punchtypeid;
    }
    
    public int getPunchtypeid() {
        return punchtypeid;
    }
    
    /* SETTERS */

    public void setId(int id) {
        this.id = id;
    }

    public void setTerminalid(int terminalid) {
        this.terminalid = terminalid;
    }

    public void setEventtypeid(int eventtypeid) {
        this.punchtypeid = eventtypeid;
    }

    public void setEventdata(String eventdata) {
        this.adjustmentType = eventdata;
    }

    public void setOriginaltimestamp(long timestamp) {
        this.originaltimestamp = timestamp;
    }

    public void setAdjustedtimestamp(long timestamp) {
        this.adjustedtimestamp = timestamp;
    }
    
    public Timestamp getTimestamp() {
        
        return (new Timestamp(originaltimestamp));
        
    }
    public String getAdjustedType(){
        return adjustmentType;
    }
    
}
