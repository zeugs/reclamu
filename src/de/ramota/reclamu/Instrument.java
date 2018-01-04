package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;

public class Instrument {
    public int MinNoteIndex;
    public int MaxNoteIndex;
    public double VariationGrip;
    public String Name;
    public int DefaultLengthIndex;
    
    public Instrument() {
        MersenneTwister twister = new MersenneTwister();
        this.DefaultLengthIndex = twister.nextInt(5) + 1;
    }
}
