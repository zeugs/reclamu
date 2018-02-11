package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;

public final class AbstractTrack {
    public List<AbstractSequence> Sequences = new ArrayList<>();

    public void addSequence(AbstractSequence sequence) {
        Sequences.add(sequence);
    }
        
    @Override
    public String toString() {
        String sequenceInfo = "";

        for (AbstractSequence sequence : Sequences) {
            sequenceInfo += sequence.toString();
        }

        return sequenceInfo;
    }

    public AbstractTrack getCopy() {
        AbstractTrack mirrorTrack = new AbstractTrack();
        
        Sequences.forEach((sequence) -> {
            mirrorTrack.Sequences.add(sequence.getCopy());
        });
        
        return mirrorTrack;
    }
}
