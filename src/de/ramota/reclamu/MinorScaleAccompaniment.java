package de.ramota.reclamu;

import java.util.ArrayList;

/**
 *
 * @author Mathies Gräske
 */
public class MinorScaleAccompaniment extends ScaleItem {
    ArrayList<Integer> minorChordOffset = new ArrayList<>();

    @Override
    public ArrayList<Integer> GetMapping() {
        return minorChordOffset;
    }
    
    public MinorScaleAccompaniment() {
        minorChordOffset.add(0);
        minorChordOffset.add(2);
        minorChordOffset.add(3);
        minorChordOffset.add(5);
        minorChordOffset.add(7);
        minorChordOffset.add(8);
        minorChordOffset.add(10);
        
        minorChordOffset.add(12);        
        minorChordOffset.add(14);        
        minorChordOffset.add(15);        
        minorChordOffset.add(17);        
        minorChordOffset.add(19);        
        minorChordOffset.add(21);        
        minorChordOffset.add(23);        
    }
    
    @Override
    public String toString() {
        return "Minor";
    }
}
