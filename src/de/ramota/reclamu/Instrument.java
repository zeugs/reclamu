package de.ramota.reclamu;

public class Instrument {
    public int MinNoteIndex;
    public int MaxNoteIndex;
    public double VariationGrip;
    public String Name;
    public int DefaultLength;
    
    public Instrument(int minNoteIndex, int maxNoteIndex, String name, double variationGrip) {
        this.DefaultLength = 200;

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
