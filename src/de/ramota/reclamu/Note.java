package de.ramota.reclamu;

import java.util.HashMap;
import java.util.Map;

public class Note {
    public int Length;
    public int Octave;
    public int Value;
    public boolean IsRest;

    Map noteMappings;
    Map noteLengths;

    public Note() {
        this.noteLengths = new HashMap<>();
        this.noteMappings = new HashMap<>();
        
        noteMappings.put(0, "C");
        noteMappings.put(1, "D");
        noteMappings.put(2, "E");
        noteMappings.put(3, "F");
        noteMappings.put(4, "G");
        noteMappings.put(5, "A");
        noteMappings.put(6, "B");

        noteLengths.put(16, "q");
        noteLengths.put(32, "h");
        noteLengths.put(64, "w");
    }

    public void addValue(double valueToAdd) {
        int updatedNote = (int)(Value + valueToAdd);

        if (updatedNote < 0) {
            Octave--;
            updatedNote = 7 + updatedNote;
        } else if (updatedNote > 6) {
            Octave++;
            updatedNote = updatedNote - 6;
        }

        if (Octave < 1) {
            Octave = 1;
        } else if (Octave > 7) {
            Octave = 7;
        }

        Value = updatedNote;
    }

    @Override
    public String toString() {
        return IsRest ? "R" + noteLengths.get(Length) : noteMappings.get(Value).toString() + Octave + noteLengths.get(Length);
    }
}
