/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mathies Gräske
 */
public class IntendedAccompaniment {
    private final AccompanimentType type;
    private final ArrayList<Integer> majorChordOffset = new ArrayList<>();
    private final ArrayList<Integer> minorChordOffset = new ArrayList<>();
    
    private final Map<AccompanimentType, ArrayList<Integer>> accompMapping = new HashMap<>();
    
    public ArrayList<Integer> GetMapping() {
        return accompMapping.get(this.type);
    }
    
    public AccompanimentType GetType() {
        return this.type;
    }
    
    public IntendedAccompaniment(AccompanimentType type) {
        this.type = type;
        
        majorChordOffset.add(0);
        majorChordOffset.add(2);
        majorChordOffset.add(4);
        majorChordOffset.add(5);
        majorChordOffset.add(7);
        majorChordOffset.add(9);
        majorChordOffset.add(11);
        majorChordOffset.add(12);

        minorChordOffset.add(0);
        minorChordOffset.add(2);
        minorChordOffset.add(3);
        minorChordOffset.add(5);
        minorChordOffset.add(7);
        minorChordOffset.add(8);
        minorChordOffset.add(10);
        minorChordOffset.add(12);        
        
        accompMapping.put(AccompanimentType.TYPE_MAJOR, majorChordOffset);
        accompMapping.put(AccompanimentType.TYPE_MINOR, minorChordOffset);
    }
}
