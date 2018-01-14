package de.ramota.reclamu;

import java.util.ArrayList;

/**
 *
 * @author Mathies Gräske
 */
public class MajorScaleAccompaniment extends Accompaniment {
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
    }
}
