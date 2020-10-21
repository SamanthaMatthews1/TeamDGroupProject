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
        System.out.println(db.getShift(db.getBadge("76E920D9")).toString());
        
        
    }
    
}
