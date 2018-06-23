package de.ramota.reclamu.composers;

import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.AbstractSequence;
import de.ramota.reclamu.AbstractTrack;
import de.ramota.reclamu.Instrument;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathies Gräske
 */
public class InvertingAccompanimentComposer extends AccompanimentComposer {

    public InvertingAccompanimentComposer(String name) {
        super(name);
    }
    
    @Override
    protected AbstractTrack getAccompanimentTrack(String name, AbstractTrack masterTrack, Instrument instrument) {
        AbstractTrack track = new AbstractTrack(name, instrument);
        this.Instrument = instrument;

        for (AbstractSequence refSequence: masterTrack.Sequences) {
            AbstractSequence sequence = new AbstractSequence();

            for (int i = 0; i < refSequence.getNotes().size(); i++) {
    
                AbstractNote refNote = refSequence.getNotes().get(i);

                AbstractNote note = refNote.getCopy();
                int lowerBound = (refNote.getValue() - refNote.getValue() % 12) + refNote.ScaleOffset;
                int upperBound = lowerBound + 12;

                note.setValue(upperBound - (refNote.getValue() - lowerBound));

                sequence.addNote(note);
            }

            sequence.setParentSequence(refSequence);
            track.Sequences.add(sequence);
        }

        return track;
    }
}
