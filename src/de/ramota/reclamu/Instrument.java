package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;

public class Instrument {
    public int MinNoteIndex;
    public int MaxNoteIndex;
    public double VariationGrip;
    public String Name;
    public double DefaultLength;
    public int ValueOffset;
    
    public Instrument() {
        MersenneTwister twister = new MersenneTwister(3456);
        this.DefaultLength = twister.nextDouble() * Note.MAX_LENGTH / 2 + Note.MIN_LENGTH; // 1/16
    }
}
