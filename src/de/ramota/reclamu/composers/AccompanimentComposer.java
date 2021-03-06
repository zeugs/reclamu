package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.AbstractSequence;
import de.ramota.reclamu.AbstractTrack;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author Mathies Gräske
 */
public class AccompanimentComposer {
    protected MersenneTwister twister = new MersenneTwister();
    public String Name;
    public Instrument Instrument;

    public AccompanimentComposer(String name) {
        this.Name = name;
    }
    
    public List<AbstractTrack> generateTracks(String name, AbstractTrack masterTrack, Instrument instrument, int trackNum, int mirroredTrackNum) {
        List<AbstractTrack> newTracks = new ArrayList<>();
        List<AbstractTrack> mirroredTracks = new ArrayList<>();

        for (int i = 0; i < trackNum - mirroredTrackNum; i++) {
            AbstractTrack accompTrack = this.getAccompanimentTrack(name, masterTrack, instrument);
            newTracks.add(accompTrack);
        }
        for (int i = 0; i < mirroredTrackNum; i++) {
            AbstractTrack accompTrack = newTracks.get(twister.nextInt(newTracks.size()));
            AbstractTrack mirrorTrack = accompTrack.getCopy();
            mirroredTracks.add(mirrorTrack);
        }
        
        newTracks.addAll(mirroredTracks);
        
        return newTracks;
    }

    protected AbstractTrack getAccompanimentTrack(String name, AbstractTrack masterTrack, Instrument instrument) {
        this.Instrument = instrument;
        return null;
    }   
    
    protected void silenceSequence(AbstractSequence refSequence, AbstractSequence sequence, AbstractTrack track) {
        for (int j = 0; j < refSequence.getNotes().size(); j++) {
            AbstractNote noteCopy = refSequence.getNotes().get(j).getCopy();
            noteCopy.IsRest = true;
            sequence.getNotes().add(noteCopy);
        }
        track.Sequences.add(sequence);
    }
    
    protected int findNoteDiff(Instrument instrument, AbstractSequence refSequence) {
        int noteDiff = 0;
        int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        if (instrumentRange > 0) {
            int absoluteValue = twister.nextInt(instrumentRange) + instrument.MinNoteIndex;
            absoluteValue -= absoluteValue % 12;
            if (refSequence.getNotes().size() > 0) {
                noteDiff = absoluteValue - refSequence.getNotes().get(0).getValue();
            }
        }
        return -noteDiff;
    }

    public AbstractNote getNextNote(int noteDiff, boolean mirrorsMaster, AbstractNote refNote) {
        int delayLength = twister.nextInt(4) + 2;
        int noteVal;

        if (!mirrorsMaster) {
            noteVal = (refNote.getValue() - refNote.getValue() % 12) - (noteDiff - noteDiff % 12) + refNote.ScaleOffset;
        } else {
            noteVal = refNote.getValue() - (noteDiff - noteDiff % 12);
        }

        AbstractNote note = refNote.getCopy();
        note.setValue(noteVal);
        note.setAttack(refNote.getAttack() + twister.nextInt(40) - 20);
        note.DelayLength = delayLength;

        return note;
    }
}
