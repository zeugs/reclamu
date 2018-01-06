package de.ramota.reclamu;

import java.util.ArrayList;

public class Note {
    public double Length;
    private int Value;
    public boolean IsRest;
    public int BaseNote;
    public IntendedAccompaniment IntendedAccomp;
    public static double MAX_LENGTH = 1.0;
    public static double MIN_LENGTH = 0.1;
    public int Attack;
    
    private final ArrayList<Integer> majorChordOffset = new ArrayList<>();
    private final ArrayList<Integer> minorChordOffset = new ArrayList<>();
    
    public void SetValue(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 127) {
            value = 127;
        }
        
        this.Value = value;        
    }
    
    public int GetValue() {
        return this.Value;
    }
    
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

    public int addValue(double valueToAdd, Instrument instrument, boolean adapt) {
        Value = (int)(Value + valueToAdd);
        
        while (Value < instrument.MinNoteIndex) {
            Value += 12;
        } 
        
        while (Value > instrument.MaxNoteIndex) {
            Value -= 12;
        }
        
        ArrayList<Integer> mappings = null;
        if (IntendedAccomp == IntendedAccompaniment.MAJOR) {
            mappings = majorChordOffset;
        } else if (IntendedAccomp == IntendedAccompaniment.MINOR) {
            mappings = minorChordOffset;
        }
        
        if (adapt) {
            setValueInRange(mappings);
        }
        
        return Value;
    }

    @Override
    public String toString() {
        String convertedString = "";
        try {
            if (IsRest) {
                convertedString = "R/" + Length;
            } else {
                convertedString = (this.Value + 12) + String.valueOf("/" + Length) + "a" + Attack;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return convertedString;
    }

    Note getCopy() {
        Note note = new Note(this.Value);
        note.Attack = this.Attack;
        note.BaseNote = this.BaseNote;
        note.Length = this.Length;
        note.IntendedAccomp = this.IntendedAccomp;
        note.IsRest = this.IsRest;
 
        return note;
    }
}
