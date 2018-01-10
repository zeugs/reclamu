package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;

public class Instrument {
    public int MinNoteIndex;
    public int MaxNoteIndex;
    public double VariationGrip;
    public String Name;
    public int DefaultLength;
    public int ValueOffset;
    
    public Instrument() {
        MersenneTwister twister = new MersenneTwister();
        this.DefaultLength = twister.nextInt(Note.MAX_LENGTH - Note.MIN_LENGTH) + Note.MIN_LENGTH;
    }
}
