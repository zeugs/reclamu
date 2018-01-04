package de.ramota.reclamu;

import java.util.HashMap;
import java.util.Map;

public class Note {
    public int Length;
    public int Value;
    public boolean IsRest;
    public IntendedAccompaniment IntendedAccomp;
    Map noteLengths;

    public Note(int value) {
        this.Value = value;
        this.noteLengths = new HashMap<>();

        noteLengths.put(2, "t");
        noteLengths.put(4, "s");
        noteLengths.put(8, "i");
        noteLengths.put(16, "q");
        noteLengths.put(32, "h");
        noteLengths.put(64, "w");
    }

    public int addValue(double valueToAdd, Instrument instrument) {
        int updatedValue = (int)(Value + valueToAdd);
        
        if (updatedValue < instrument.MinNoteIndex) {
            updatedValue += 12;
        } else if (updatedValue > instrument.MaxNoteIndex) {
            updatedValue -= 12;
        }

        Value = updatedValue;
        return updatedValue;
    }

    @Override
    public String toString() {
        String convertedString = "";
        try {
            if (IsRest) {
                convertedString = "R" + noteLengths.get(Length);
            } else {
                convertedString = (this.Value + 12) + String.valueOf(noteLengths.get(Length));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return convertedString;
    }
}
