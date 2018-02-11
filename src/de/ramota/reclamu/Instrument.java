package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;

public class Instrument {
    public int MinNoteIndex;
    public int MaxNoteIndex;
    public double VariationGrip;
    public String Name;
    public int DefaultLength;
    
    public Instrument(int minNoteIndex, int maxNoteIndex, String name, double variationGrip) {
        MersenneTwister twister = new MersenneTwister();
        this.DefaultLength = 800;

        this.MinNoteIndex = minNoteIndex;
        this.MaxNoteIndex = maxNoteIndex;
        this.Name = name;
        this.VariationGrip = variationGrip;
    }

    Instrument getCopy() {
        Instrument instrument = new Instrument(this.MinNoteIndex, this.MaxNoteIndex, this.Name, this.VariationGrip);
        instrument.DefaultLength = DefaultLength;
        return instrument;
    }
}
