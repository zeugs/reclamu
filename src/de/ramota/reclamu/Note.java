package de.ramota.reclamu;

import java.util.HashMap;
import java.util.Map;

public class Note {
    public int Length;
    private int Octave;
    public boolean IsRest;
    public MappingItem MappingItem;
        
    Map noteLengths;

    public Note(MappingItem item, int octave) {
        this.MappingItem = item;
        this.Octave = octave;
        this.noteLengths = new HashMap<>();

        noteLengths.put(8, "w");
        noteLengths.put(16, "q");
        noteLengths.put(32, "h");
        noteLengths.put(64, "w");
    }

    public int addValue(double valueToAdd, Instrument instrument, NoteMapping mapping, int index) {
        int updatedIndex = (int)(index + valueToAdd);
        int mappingSize = mapping.items.size();
        
        if (updatedIndex < 0) {
            Octave--;
            updatedIndex = mappingSize + updatedIndex;
        } else if (updatedIndex > mappingSize - 1) {
            Octave++;
            updatedIndex = updatedIndex - mappingSize;
        }

        if (Octave < instrument.MinOctave) {
            Octave = instrument.MinOctave;
        } else if (Octave > instrument.MaxOctave) {
            Octave = instrument.MaxOctave;
        }

        this.MappingItem = mapping.GetMappingItem(updatedIndex);
        return updatedIndex;
    }

    @Override
    public String toString() {
        String convertedString = "";
        try {
            if (IsRest) {
                convertedString = "R" + noteLengths.get(Length);
            } else {
                convertedString = this.MappingItem.Representation + String.valueOf(Octave + 1) + String.valueOf(noteLengths.get(Length));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return convertedString;
    }
}
