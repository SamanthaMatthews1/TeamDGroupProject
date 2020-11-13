/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jsu.mcis.tas_fa20;


public class TeamDProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // this file is for testing, not for functionality
        TASDatabase db = new TASDatabase();
        Shift s1 = db.getShift(1);

        Punch p1 = db.getPunch(3634);
        Punch p2 = db.getPunch(3687);
        Punch p3 = db.getPunch(3688);
        Punch p4 = db.getPunch(3716);
        p1.adjust(s1);
        String s2 = p1.printOriginalTimestamp();
        String s3 = p1.printAdjustedTimestamp();
        System.out.println(s2);
        System.out.println(s3);
		
        /* Adjust Punches According to Shift Rulesets */
        
        p1.adjust(s1);
        
        
    }
    
}
