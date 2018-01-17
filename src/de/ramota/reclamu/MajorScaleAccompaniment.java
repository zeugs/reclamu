package de.ramota.reclamu;

import java.util.ArrayList;

/**
 *
 * @author Mathies Gräske
 */
public class MajorScaleAccompaniment extends ScaleItem {
    private final ArrayList<Integer> majorChordOffsets = new ArrayList<>();

    @Override
    public ArrayList<Integer> GetMapping() {
        return majorChordOffsets;
    }
    
    public MajorScaleAccompaniment() {
        majorChordOffsets.add(0);
        majorChordOffsets.add(2);
        majorChordOffsets.add(4);
        majorChordOffsets.add(5);
        majorChordOffsets.add(7);
        majorChordOffsets.add(9);
        majorChordOffsets.add(11);

        majorChordOffsets.add(12);                
        majorChordOffsets.add(14);                
        majorChordOffsets.add(16);                
        majorChordOffsets.add(17);                
        majorChordOffsets.add(19);                
        majorChordOffsets.add(21);                
        majorChordOffsets.add(23);                
    }
    
    @Override
    public String toString() {
        return "Major";
    }    
}
