package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;

public class Instrument {
    public int MinNoteIndex;
    public int MaxNoteIndex;
    public double VariationGrip;
    public String Name;
    public int DefaultLength;
    public PlayGroup Group;
    
    public Instrument() {
        MersenneTwister twister = new MersenneTwister();
        this.DefaultLength = 800;
    }

    Instrument getCopy() {
        Instrument instrument = new Instrument();
        instrument.MinNoteIndex = MinNoteIndex;
        instrument.MaxNoteIndex = MaxNoteIndex;
        instrument.VariationGrip = VariationGrip;
        instrument.Name = Name;
        instrument.DefaultLength = DefaultLength;
        instrument.Group = Group;
        return instrument;
    }
}
