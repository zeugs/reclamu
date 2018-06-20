package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;

public final class AbstractTrack {
    public List<AbstractSequence> Sequences = new ArrayList<>();
    public String Name;
    public Instrument Instrument;

    public AbstractTrack(String name, Instrument instrument) {
        this.Name = name;
        this.Instrument = instrument;
    }
    
    public void addSequence(AbstractSequence sequence) {
        Sequences.add(sequence);
    }
        
    @Override
    public String toString() {
        String sequenceInfo = " I[" + this.Instrument.Name + "] ";

        for (AbstractSequence sequence : Sequences) {
            sequenceInfo += sequence.toString();
        }

        return sequenceInfo;
    }

    public AbstractTrack getCopy() {
        AbstractTrack mirrorTrack = new AbstractTrack(this.Name, this.Instrument);
        
        Sequences.forEach((sequence) -> {
            mirrorTrack.Sequences.add(sequence.getCopy());
        });
        
        return mirrorTrack;
    }
}
