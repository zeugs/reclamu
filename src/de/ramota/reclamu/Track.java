package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;

public class Track {
    public List<Sequence> sequences = new ArrayList<>();

    public void addSequence(Sequence sequence) {
        sequences.add(sequence);
    }

    @Override
    public String toString() {
        String sequenceInfo = "";

        for (Sequence sequence : sequences) {
            sequenceInfo += sequence.toString();
        }

        return sequenceInfo;
    }
}
