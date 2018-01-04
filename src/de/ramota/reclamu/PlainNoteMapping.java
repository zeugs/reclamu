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
public class PlainNoteMapping extends NoteMapping {
    public PlainNoteMapping() {
        super();
        items.put(0, new MappingItem(0, "C"));
        items.put(1, new MappingItem(2, "D"));
        items.put(2, new MappingItem(4, "E"));
        items.put(3, new MappingItem(5, "F"));
        items.put(4, new MappingItem(7, "G"));
        items.put(5, new MappingItem(9, "A"));
        items.put(6, new MappingItem(11, "B"));
    }    
}
