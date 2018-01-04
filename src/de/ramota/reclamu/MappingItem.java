/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ramota.reclamu;

/**
 *
 * @author luser
 */
public class MappingItem {
    public int RelativePosition;
    public String Representation;    
    
    public MappingItem(int position, String representation) {
        this.RelativePosition = position;
        this.Representation = representation;
    }
}
