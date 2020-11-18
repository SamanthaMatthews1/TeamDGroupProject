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
            if (punch.type == Punch.PunchType.ClockIn) {
                punchCount++;
                lastInstant = punch.getAdjustedInstant();
            } else if (punch.type == Punch.PunchType.ClockOut) {
                punchCount++;
                if (lastInstant != null) {
                    totalMinutes += TimeUnit.MINUTES.convert(Math.abs(punch.getAdjustedInstant().getTimeInMillis() - lastInstant.getTimeInMillis()), TimeUnit.MILLISECONDS);
                }
            }
        }

        if (punchCount == 2 && totalMinutes >= shift.shiftDuration) {
            totalMinutes -= shift.lunchDuration;
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
            punchData.put("punchdata", String.valueOf(punch.getAdjustmentType()));
            punchData.put("originaltimestamp", String.valueOf(punch.getOriginaltimestamp()));
            punchData.put("adjustedtimestamp", String.valueOf(punch.getAdjustedInstant().getTimeInMillis()));

            jsonData.add(punchData);
        }

        return JSONValue.toJSONString(jsonData);
    }
}