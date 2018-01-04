/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ramota.reclamu;

/**
 *
 * @author Mathies Gräske
 */
public class ChromaticNoteMapping extends NoteMapping {
    public ChromaticNoteMapping() {
        super();
        items.put(0, new MappingItem(0, "C"));
        items.put(1, new MappingItem(1, "C#"));
        items.put(2, new MappingItem(2, "D"));
        items.put(3, new MappingItem(3, "D#"));
        items.put(4, new MappingItem(4, "E"));
        items.put(5, new MappingItem(5, "F"));
        items.put(6, new MappingItem(6, "F#"));
        items.put(7, new MappingItem(7, "G"));
        items.put(8, new MappingItem(8, "G#"));
        items.put(9, new MappingItem(9, "A"));  
        items.put(10, new MappingItem(10, "A#"));
        items.put(11, new MappingItem(11, "B"));
    }
}
