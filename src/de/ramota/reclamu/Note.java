package de.ramota.reclamu;

import java.util.ArrayList;

public class Note {
    private int Value;
    private int Length;
    public boolean IsRest;
    public int ScaleOffset;
    public ScaleItem IntendedScaleType;
    public static int MAX_LENGTH = 1000;
    public static int MIN_LENGTH = 100;
    private int Attack;
        
    public void SetValue(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 127) {
            value = 127;
        }
        
        this.Value = value;        
    }
    
    public void SetAttack(int value) {
        if (value < 10) {
            value = 10;
        } else if (value > 120) {
            value = 120;
        }
        
        this.Attack = value;        
    }

    public int SetLength(int length, boolean limit) {
        
        if (limit) {
            if (length < Note.MIN_LENGTH) {
                length = Note.MIN_LENGTH;
            } else if (length > Note.MAX_LENGTH) {
                length = Note.MAX_LENGTH;
            }
        }
        
        this.Length = length;    
        return this.Length;
    }

    public int GetLength() {
        return this.Length;
    }
    
    public int GetAttack() {
        return this.Attack;
    }

    public int GetValue() {
        return this.Value;
    }
    
    public Note(int value) {
        this.Value = value;
    }
    
    private void setValueInRange(ArrayList<Integer> offsets) {
        int relativeValue = (Value - ScaleOffset) % 12;
        
        int distance = 1000;
        int valueToUse = 0;
        
        for (int offset : offsets) {
            int currentDist = Math.abs(relativeValue - offset);
            if (currentDist < distance) {
                distance = currentDist;
                valueToUse = Value - relativeValue + offset;
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
        
        ArrayList<Integer> mappings = IntendedScaleType.GetMapping();
        
        if (adapt) {
            setValueInRange(mappings);
        }
        
        return Value;
    }

    @Override
    public String toString() {
        String convertedString = "";
        try {
            double outputLength = Length / (Note.MAX_LENGTH * 1.0);
            
            if (IsRest) {
                convertedString = "R/" + outputLength;
            } else {
                convertedString = (this.Value + 12) + String.valueOf("/" + outputLength + "a" + Attack);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return convertedString;
    }

    Note getCopy() {
        Note note = new Note(this.Value);
        note.Attack = this.Attack;
        note.ScaleOffset = this.ScaleOffset;
        note.Length = this.Length;
        note.IntendedScaleType = this.IntendedScaleType;
        note.IsRest = this.IsRest;
 
        return note;
    }
}
