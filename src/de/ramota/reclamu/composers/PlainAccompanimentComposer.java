package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.Note;
import de.ramota.reclamu.Piece;
import de.ramota.reclamu.Sequence;
import de.ramota.reclamu.Track;

/**
 *
 * @author Mathies Gräske
 */
public class PlainAccompanimentComposer extends AccompanimentComposer {

    public PlainAccompanimentComposer(Piece piece) {
        super(piece);
    }
    
    @Override
    protected Track getAccompanimentTrack(Track masterTrack, Instrument instrument) {    
        Track track = new Track();
        
        for (Sequence refSequence: masterTrack.Sequences) {
            Sequence sequence = new Sequence();
            
            for (Note note: refSequence.getNotes()) {
                sequence.addNote(note.getCopy());
            }
            track.addSequence(sequence);
        }
        
        return track;
    }
}
