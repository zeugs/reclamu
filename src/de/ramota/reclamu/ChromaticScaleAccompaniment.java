package de.ramota.reclamu;

import java.util.ArrayList;

/**
 *
 * @author Mathies Gräske
 */
public class ChromaticScaleAccompaniment extends ScaleItem {
    private final ArrayList<Integer> majorChordOffsets = new ArrayList<>();

    @Override
    public ArrayList<Integer> GetMapping() {
        return majorChordOffsets;
    }
    
    public ChromaticScaleAccompaniment() {
        majorChordOffsets.add(0);
        majorChordOffsets.add(1);
        majorChordOffsets.add(2);
        majorChordOffsets.add(3);
        majorChordOffsets.add(4);
        majorChordOffsets.add(5);
        majorChordOffsets.add(6);
        majorChordOffsets.add(7);
        majorChordOffsets.add(8);
        majorChordOffsets.add(9);
        majorChordOffsets.add(10);
        majorChordOffsets.add(11);

        int secondffset = 12;
        majorChordOffsets.add(secondffset + 0);
        majorChordOffsets.add(secondffset + 1);
        majorChordOffsets.add(secondffset + 2);
        majorChordOffsets.add(secondffset + 3);
        majorChordOffsets.add(secondffset + 4);
        majorChordOffsets.add(secondffset + 5);
        majorChordOffsets.add(secondffset + 6);
        majorChordOffsets.add(secondffset + 7);
        majorChordOffsets.add(secondffset + 8);
        majorChordOffsets.add(secondffset + 9);
        majorChordOffsets.add(secondffset + 10);
        majorChordOffsets.add(secondffset + 11);
    }
    
    @Override
    public String toString() {
        return "Chromatic";
    }    
}
