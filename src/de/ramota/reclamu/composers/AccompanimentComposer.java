package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.Note;
import de.ramota.reclamu.Piece;
import de.ramota.reclamu.Sequence;
import de.ramota.reclamu.Track;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author Mathies Gräske
 */
public class AccompanimentComposer {
    protected final Piece piece;
    protected MersenneTwister twister = new MersenneTwister();

    public AccompanimentComposer(Piece piece) {
        this.piece = piece;
    }

    public void generateTrack(Track track, Instrument instrument, int trackNum, int mirroredTrackNum) {
        List<Track> newTracks = new ArrayList<>();
        
        for (int i = 0; i < trackNum - mirroredTrackNum; i++) {
            Track accompTrack = this.getAccompanimentTrack(track, instrument);
            piece.addTrack(accompTrack);
            newTracks.add(accompTrack);
        }
        for (int i = 0; i < mirroredTrackNum; i++) {
            Track accompTrack = newTracks.get(twister.nextInt(newTracks.size()));
            Track mirrorTrack = accompTrack.getCopy();
            piece.addTrack(mirrorTrack);
        }
    }
    
    protected int addNoteHumanized(Sequence sequence) {
        int delayLength = twister.nextInt(15) + 5;

        if (delayLength > 0) {
            Note delayPseudoNote = new Note(70);
            delayPseudoNote.IsRest = true;
            delayPseudoNote.SetLength(delayLength, false);
            sequence.addNote(delayPseudoNote);
        }
        
        return delayLength;
    }
    
    protected Track getAccompanimentTrack(Track masterTrack, Instrument instrument) {
        return null;
    }   
    
    protected void silenceSequence(Sequence refSequence, Sequence sequence, Track track) {
        for (int j = 0; j < refSequence.getNotes().size(); j++) {
            Note noteCopy = refSequence.getNotes().get(j).getCopy();
            noteCopy.IsRest = true;
            sequence.getNotes().add(noteCopy);
        }
        track.Sequences.add(sequence);
    }
    
    protected int findNoteDiff(Instrument instrument, Sequence refSequence) {
        int noteDiff = 0;
        int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        int absoluteValue = twister.nextInt(instrumentRange) + instrument.MinNoteIndex;
        absoluteValue -= absoluteValue % 12;
        if (refSequence.getNotes().size() > 0) {
            noteDiff = absoluteValue - refSequence.getNotes().get(0).GetValue();
        }
        return -noteDiff;
    }    
}
