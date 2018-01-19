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
public class MaeanderAccompanimentComposer extends AccompanimentComposer {

    public MaeanderAccompanimentComposer(Piece piece) {
        super(piece);
    }
        
    private int findNoteDiff(Instrument instrument, MersenneTwister twister, Sequence refSequence) throws IllegalArgumentException {
        int noteDiff = 0;
        int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        int absoluteValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;
        absoluteValue -= absoluteValue % 12;
        if (refSequence.getNotes().size() > 0) {
            noteDiff = absoluteValue - refSequence.getNotes().get(0).GetValue();
        }
        return noteDiff;
    }
    
    @Override
    protected Track getAccompanimentTrack(Track masterTrack, Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        Track track = new Track();
        int restRange = twister.nextInt(7) + 1;
        int noteDiff = -1;
        
        for (Sequence refSequence: masterTrack.Sequences) {
            Sequence sequence = new Sequence();
    
            boolean addRest = (twister.nextInt(restRange) == 0);
            boolean silence = refSequence.SilencedGroups.contains(instrument.Group);
            
            if (addRest || silence) {
                for (int j = 0; j < refSequence.getNotes().size(); j++) {
                    Note noteCopy = refSequence.getNotes().get(j).getCopy();
                    noteCopy.IsRest = true;
                    sequence.getNotes().add(noteCopy);
                }    
                track.Sequences.add(sequence);
                
                continue;
            }                
            
            int restDelayRange = twister.nextInt(7) + 2;
            int restStartRange = twister.nextInt(10) + 2;

            if (noteDiff == -1) {
                noteDiff = findNoteDiff(instrument, twister, refSequence);
            }
            
            for (int i = 0; i < refSequence.getNotes().size(); i++) {
                int delayLength = twister.nextInt(15) + 5;
    
                if (twister.nextInt(30) == 0) {
                    noteDiff = this.findNoteDiff(instrument, twister, refSequence);
                }
                
                Note refNote = refSequence.getNotes().get(i);
                
                int noteVal = (refNote.GetValue() - refNote.GetValue() % 12) - (noteDiff - noteDiff % 12) + refNote.ScaleOffset;
                Note note = new Note(noteVal);
                note.SetAttack(refNote.GetAttack() + twister.nextInt(40) - 20);
                note.ScaleOffset = refNote.ScaleOffset;
                note.SetLength(refNote.GetLength() - delayLength, false);
                note.IntendedScaleType = refNote.IntendedScaleType;

                if (delayLength > 0) {
                    Note delayPseudoNote = new Note(70);
                    delayPseudoNote.IsRest = true;
                    delayPseudoNote.SetLength(delayLength, false);
                    sequence.addNote(delayPseudoNote);     
                }

                ArrayList<Integer> offsets = refNote.IntendedScaleType.GetItemOffsets();

                int valueIndex = twister.nextInt(offsets.size());
                int valueToAdd = offsets.get(valueIndex);

                note.addValue(valueToAdd, instrument, false);

                List<Note> notes = sequence.getNotes();
                
                if (notes.size() > 0 && sequence.getNotes().get(sequence.getNotes().size() - 1).IsRest) {
                    note.IsRest = true;
                    if (twister.nextInt(restDelayRange) == 0) {
                        note.IsRest = false;
                    }
                } else {
                    note.IsRest = twister.nextInt(restStartRange) == 0;
                }                    

                List<Note> refNotes = refSequence.getNotes();

                if (twister.nextInt(12) == 0) {
                    int skip = twister.nextInt(5);
                    int startPos = i + 1;

                    for (int j = startPos; j < startPos + skip; j++) {
                        if (j < refNotes.size()) {
                            note.SetLength(note.GetLength() + refNotes.get(j).GetLength(), false);
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                
                sequence.addNote(note);                
            }
            
            track.Sequences.add(sequence);
        }
        
        return track;
    }        
}
