package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.Note;
import de.ramota.reclamu.ScaleItem;
import de.ramota.reclamu.Sequence;
import de.ramota.reclamu.Track;
import java.util.List;

/**
 *
 * @author Mathies Gräske
 */
public class PlainTrackComposer extends TrackComposer {

    public PlainTrackComposer(Instrument instrument, List<ScaleItem> intendedAccomps) {
        super(instrument, intendedAccomps);        
    }

    @Override
    public Sequence getSequence(Instrument instrument) {
        Sequence sequence = new Sequence();
        
        int currentVal = twister.nextInt(instrument.MaxNoteIndex - instrument.MinNoteIndex) + instrument.MinNoteIndex;
        int sequenceLength = twister.nextInt(10) + 3;
        sequenceLength -= sequenceLength % 4 + 4;
        
        int subNoteCount = twister.nextInt(5);
 
        for (int i = 0; i < sequenceLength; i++) {
            if (twister.nextInt(2) == 0) {
                subNoteCount = twister.nextInt(5);
            }
            
            for (int j = 0; j < subNoteCount; j++) {
                Note note = new Note(currentVal);
                note.SetAttack(twister.nextInt(80) + 30);
                note.SetLength(instrument.DefaultLength / subNoteCount, true);
                note.IntendedScaleType = currentAccomp;
                sequence.addNote(note);

                currentVal += twister.nextInt(6) - 3;                
                if (currentVal < instrument.MinNoteIndex) {
                    currentVal = instrument.MinNoteIndex;
                } else if (currentVal > instrument.MaxNoteIndex) {
                    currentVal = instrument.MaxNoteIndex;
                }
            }
        }
        
        return sequence;
    }    
    
    @Override
    public Track generateTrack(int sequenceNum) {
        Track track = new Track();
        
        for (int i = 0; i < sequenceNum; i++) {
            Sequence sequence = this.getSequence(this.instrument);
            track.addSequence(sequence);
            
            int repetitions = twister.nextInt(10);
            for (int j = 0; j < repetitions; j++) {
                track.addSequence(sequence.getCopy());
            }
        }
        
        int currentAttack = twister.nextInt(80) + 40;
        
        for (Sequence sequence: track.Sequences) {
            for (Note note: sequence.getNotes()) {
                if (twister.nextInt(150) == 0) {
                    currentAttack = twister.nextInt(80) + 40;
                }
                note.SetAttack(currentAttack);
                currentAttack += twister.nextInt(36) - 18;
                
                if (currentAttack < 40) {
                    currentAttack = 41;
                } else if (currentAttack > 120) {
                    currentAttack = 119;
                }
            }
        }
        
        return track;
    }    
}
