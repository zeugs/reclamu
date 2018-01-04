package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;

public class Instrument {
    public int MinNoteIndex;
    public int MaxNoteIndex;
    public double VariationGrip;
    public String Name;
    public double DefaultLength;
    
    public Instrument() {
        MersenneTwister twister = new MersenneTwister();
        this.DefaultLength = twister.nextDouble() * 1.5 + 0.0625; // 1/16
    }
}
