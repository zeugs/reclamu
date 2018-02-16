package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.Locale;

public class AbstractNote {
    private int Value;
    private int Length;
    public boolean IsRest;
    public int ScaleOffset;
    public ScaleItem IntendedScaleType;
    public static int MAX_LENGTH = 1500;
    public static int MIN_LENGTH = 200;
    private int attack;
    public int RelativeOffset = 0;

    public void setValue(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 127) {
            value = 127;
        }
        
        this.Value = value;        
    }
    
    public void setAttack(int value) {
        if (value < 10) {
            value = 10;
        } else if (value > 120) {
            value = 120;
        }
        
        this.attack = value;        
    }

    public int setLength(int length, boolean limit) {
        
        if (limit) {
            length -= length % AbstractNote.MIN_LENGTH;
            
            if (length < AbstractNote.MIN_LENGTH) {
                length = AbstractNote.MIN_LENGTH;
            } else if (length > AbstractNote.MAX_LENGTH) {
                length = AbstractNote.MAX_LENGTH;
            }
        }
        
        this.Length = length;    
        return this.Length;
    }

    public int getLength() {
        return this.Length;
    }
    
    public int getAttack() {
        return this.attack;
    }

    public int getValue() {
        return this.Value;
    }
    
    public AbstractNote(int value) {
        this.Value = value;
    }
    
    public void setValueInRange() {
        ArrayList<Integer> offsets = IntendedScaleType.GetMapping();
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
        
        Value = valueToUse + RelativeOffset;
    }

    public int addValue(double valueToAdd, Instrument instrument) {
        Value = (int)(Value + valueToAdd);
        
        while (Value < instrument.MinNoteIndex) {
            Value += 12;
        } 
        
        while (Value > instrument.MaxNoteIndex) {
            Value -= 12;
        }
        
        return Value;
    }

    @Override
    public String toString() {
        String convertedString = "";
        try {
            String outputLength = String.format(Locale.US, "%.4f", this.Length / (AbstractNote.MAX_LENGTH * 1.0));
            
            if (IsRest) {
                convertedString = "R/" + outputLength;
            } else {
                convertedString = (this.Value + 12) + String.valueOf("/" + outputLength + "a" + attack);                
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return convertedString;
    }

    public AbstractNote getCopy() {
        AbstractNote note = new AbstractNote(this.Value);
        note.attack = this.attack;
        note.ScaleOffset = this.ScaleOffset;
        note.Length = this.Length;
        note.IntendedScaleType = this.IntendedScaleType;
        note.IsRest = this.IsRest;
        
        return note;
    }

    public void detectOffset() {
        ArrayList<Integer> offsets = IntendedScaleType.GetMapping();
        int relativeValue = (Value - ScaleOffset) % 12;
        int distOffset = 0;
        int distance = 1000;
        
        for (int offset : offsets) {
            int currentDist = Math.abs(relativeValue - offset);
            if (currentDist < distance) {
                distance = currentDist;
                distOffset = relativeValue - offset;
            }
        }
        
        if (distance > 0) {
            this.RelativeOffset = distOffset;
        }
    }
}
