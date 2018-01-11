package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;

public class Track {
    public List<Sequence> Sequences = new ArrayList<>();
    public List<PlayGroup> PlayGroups = new ArrayList<>();

    public void addSequence(Sequence sequence) {
        Sequences.add(sequence);
    }

    @Override
    public String toString() {
        String sequenceInfo = "";

        for (Sequence sequence : Sequences) {
            sequenceInfo += sequence.toString();
        }

        return sequenceInfo;
    }
}
