/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jsu.mcis.tas_fa20;

/**
 *
 * @author Sam
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;

import org.json.simple.JSONValue;

public class TASLogic {
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift){
        int total = 0;
        int day;
        long inTime = 0;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dailypunchlist.get(0).getOriginaltimestamp());
        day = cal.get(Calendar.DAY_OF_WEEK);

        for (Punch punch : dailypunchlist) {
            switch (punch.getPunchtypeid()) {
                case 0:
                    int minutes = (int)((punch.getAdjustedtimestamp() - inTime) / 60000);
                    if (minutes > shift.getLunchDeduct(day)) {
                        minutes -= shift.getLunchDuration(day);
                    }
                    total += minutes;
                    break;
                case 1:
                    inTime = punch.getAdjustedtimestamp();
                    break;
                case 2:
                    break;
            }
        }

        return total;
}
 
    //F5
public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist){
        HashMap<String, String> map;
        ArrayList<HashMap<String, String>> mapList = new ArrayList<>();

        for (Punch punch : dailypunchlist) {
            map = new HashMap<>();
            map.put("id", String.valueOf(punch.getId()));
            map.put("terminalid", String.valueOf(punch.getTerminalid()));
            map.put("punchtypeid", String.valueOf(punch.getPunchtypeid()));
            map.put("badgeid", punch.getBadgeid());
            map.put("originaltimestamp", String.valueOf(punch.getOriginaltimestamp()));
            map.put("adjustedtimestamp", String.valueOf(punch.getAdjustedtimestamp()));
            map.put("punchdata", punch.getAdjustmenttype());
            mapList.add(map);
        }

        return JSONValue.toJSONString(mapList);
    }

