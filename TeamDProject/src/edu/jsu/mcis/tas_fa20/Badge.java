/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jsu.mcis.tas_fa20;

/**
 *
 * @author penguin
 */


public class Badge {
    
     private String id, name;
    
    
    // the constructor for all badges
    public Badge(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    //Making all return statements
    public String getId()
    {
        return this.id;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    
    //changing the configuration of the badge to a string
    @Override
    public String toString()
    {
        String s = "#" + id + " (" + name + ")";
        return s;
    }
  
}
