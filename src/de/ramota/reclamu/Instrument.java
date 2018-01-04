package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;

public class Instrument {
    public int MinOctave;
    public int MaxOctave;
    public double VariationGrip;
    public String Name;
    public int DefaultLength;
    
    public Instrument() {
        MersenneTwister twister = new MersenneTwister();
        this.DefaultLength = twister.nextInt(5) + 1;
    }
}
