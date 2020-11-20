package edu.jsu.mcis.tas_fa20;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.*;

public class TASLogic {
    
    public static int calculateTotalMinutes(ArrayList<Punch> punchList, Shift shift) {
        int totalMinutes = 0;
        int punchCount = 0;
        GregorianCalendar lastInstant = null;

        for (Punch punch : punchList) {
            if (punch.getPunchtypeid() == 1) {
                punchCount++;
                lastInstant = new GregorianCalendar();
                lastInstant.setTime(new Date(punch.getAdjustedtimestamp()));
            } else if (punch.getPunchtypeid() == 0){
                if (lastInstant != null) {
                    totalMinutes += TimeUnit.MINUTES.convert(Math.abs(punch.getAdjustedtimestamp() - lastInstant.getTimeInMillis()), TimeUnit.MILLISECONDS);
                }
            }
        }

        if (punchCount == 2 && totalMinutes >= shift.getShiftDuration()) {
            totalMinutes -= shift.getLunchDuration();
        }

        return totalMinutes;
    }

    public static String getPunchListAsJSON(ArrayList<Punch> dailyPunchList) {
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();

        for(Punch punch : dailyPunchList) {
            HashMap<String, String> punchData = new HashMap<>();
            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", String.valueOf(punch.getBadgeid()));
            punchData.put("terminalid", String.valueOf(punch.getTerminalid()));
            punchData.put("punchtypeid", String.valueOf(punch.getPunchtypeid()));
            punchData.put("punchdata", String.valueOf(punch.getAdjustedType()));
            punchData.put("originaltimestamp", String.valueOf(punch.getOriginaltimestamp()));
            punchData.put("adjustedtimestamp", String.valueOf(punch.getAdjustedtimestamp()));

            jsonData.add(punchData);
        }
        
        return jsonData.toString();
        //return JSONValue.toJSONString(jsonData);
    }

}