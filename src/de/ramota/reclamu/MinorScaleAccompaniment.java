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
        
        int secondffset = 12;
        minorChordOffset.add(secondffset + 0);
        minorChordOffset.add(secondffset + 2);
        minorChordOffset.add(secondffset + 3);
        minorChordOffset.add(secondffset + 5);
        minorChordOffset.add(secondffset + 7);
        minorChordOffset.add(secondffset + 8);
        minorChordOffset.add(secondffset + 10);
    }
    
    @Override
    public String toString() {
        return "Minor";
    }
}
