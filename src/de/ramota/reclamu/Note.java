package de.ramota.reclamu;

import java.util.HashMap;
import java.util.Map;

public class Note {
    public int Length;
    public int Octave;
    public int Value;
    public boolean IsRest;
    public static int NOTE_BOUND = 12;

    Map noteMappings;
    Map noteLengths;

    public Note() {
        this.noteLengths = new HashMap<>();
        this.noteMappings = new HashMap<>();
        
        noteMappings.put(0, "C");
        noteMappings.put(1, "C#");
        noteMappings.put(2, "D");
        noteMappings.put(3, "D#");
        noteMappings.put(4, "E");
        noteMappings.put(5, "F");
        noteMappings.put(6, "F#");
        noteMappings.put(7, "G");
        noteMappings.put(8, "G#");
        noteMappings.put(9, "A");
        noteMappings.put(10, "A#");
        noteMappings.put(11, "B");

        noteLengths.put(8, "w");
        noteLengths.put(16, "q");
        noteLengths.put(32, "h");
        noteLengths.put(64, "w");
    }

    public void addValue(double valueToAdd, Instrument instrument) {
        int updatedNote = (int)(Value + valueToAdd);

        if (updatedNote < 0) {
            Octave--;
            updatedNote = NOTE_BOUND + updatedNote;
        } else if (updatedNote > NOTE_BOUND - 1) {
            Octave++;
            updatedNote = updatedNote - NOTE_BOUND;
        }

        if (Octave < instrument.MinOctave) {
            Octave = instrument.MinOctave;
        } else if (Octave > instrument.MaxOctave) {
            Octave = instrument.MaxOctave;
        }

        Value = updatedNote;
    }

    @Override
    public String toString() {
        String convertedString = "";
        try {
            convertedString = IsRest ? "R" + noteLengths.get(Length) : noteMappings.get(Value).toString() + Octave + noteLengths.get(Length);  
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return convertedString;
    }
}
