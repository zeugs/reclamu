package de.ramota.reclamu;

import java.util.ArrayList;

public class Note {
    public double Length;
    public int Value;
    public boolean IsRest;
    public int BaseNote;
    public IntendedAccompaniment IntendedAccomp;

    private final ArrayList<Integer> majorChordOffset = new ArrayList<>();
    private final ArrayList<Integer> minorChordOffset = new ArrayList<>();
    
    public Note(int value) {
        this.Value = value;
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
    }
    
    private void setValueInRange(ArrayList<Integer> offsets) {
        int start;
        
        if (Value > BaseNote) {
            start = Value - (Value - BaseNote) % 12;
        } else {
            start = Value - (12 + (Value - BaseNote) % 12);
        }
        
        int distance = 1000;
        int valueToUse = 0;
        
        for (int offset : offsets) {
            int currentDist = Math.abs(start + offset - Value);
            if (currentDist < distance) {
                distance = currentDist;
                valueToUse = start + offset;
            }
        }
        
        Value = valueToUse;
    }

    public int addValue(double valueToAdd, Instrument instrument) {
        Value = (int)(Value + valueToAdd);
        
        if (Value < instrument.MinNoteIndex) {
            Value += 12;
        } else if (Value > instrument.MaxNoteIndex) {
            Value -= 12;
        }
        
        ArrayList<Integer> mappings = null;
        if (IntendedAccomp == IntendedAccompaniment.MAJOR) {
            mappings = majorChordOffset;
        } else if (IntendedAccomp == IntendedAccompaniment.MINOR) {
            mappings = minorChordOffset;
        }
        
        setValueInRange(mappings);

        return Value;
    }

    @Override
    public String toString() {
        String convertedString = "";
        try {
            if (IsRest) {
                convertedString = "R/" + Length;
            } else {
                convertedString = (this.Value + 12) + String.valueOf("/" + Length);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return convertedString;
    }
}
