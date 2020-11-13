/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jsu.mcis.tas_fa20;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class TeamDProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // this file is for testing, not for functionality
        TASDatabase db = new TASDatabase();
        Shift s1 = db.getShift(1);
        System.out.println(s1.getStart());
        Date date = new Date(s1.getStart());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+1"));
        String formattedDate = sdf.format(date);
        String [] splits = formattedDate.split(":");
        int hours = Integer.parseInt(splits[0]);
        int minutes = Integer.parseInt(splits[1]);
        System.out.println(hours);
        System.out.println(minutes);
        Punch p1 = db.getPunch(3634);
        p1.adjust(s1);
        
        
		
        /* Adjust Punches According to Shift Rulesets */
        
        p1.adjust(s1);
        
        
    }
    
}
