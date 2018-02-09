package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.Note;
import de.ramota.reclamu.Piece;
import de.ramota.reclamu.Sequence;
import de.ramota.reclamu.Track;
import java.util.ArrayList;
import java.util.List;

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
        int noteDiff = -1;
        int audibleMin = twister.nextInt(60) + 35;
        System.out.println(String.format("Track loudness: %d", audibleMin));

        for (Sequence refSequence: masterTrack.Sequences) {
            Sequence sequence = new Sequence();
            sequence.setTempo(refSequence.getTempo());
            
            boolean mirrorsMaster = (twister.nextInt(3) == 0);
            boolean addRest = (twister.nextInt(2) == 0);
            
            if (addRest) {
                silenceSequence(refSequence, sequence, track);
                
                continue;
            }                
            
            int restDelayRange = twister.nextInt(2) + 1;
            int restStartRange = twister.nextInt(20) + 2;

            if (noteDiff == -1) {
                noteDiff = findNoteDiff(instrument, refSequence);
            }
            
            for (int i = 0; i < refSequence.getNotes().size(); i++) {
    
                if (twister.nextInt(15) == 0) {
                    noteDiff = this.findNoteDiff(instrument, refSequence);
                }
                
                Note refNote = refSequence.getNotes().get(i);
                
                int delayLength = this.addNoteHumanized(sequence);
                int noteVal;
                
                if (!mirrorsMaster) {
                    noteVal = (refNote.GetValue() - refNote.GetValue() % 12) - (noteDiff - noteDiff % 12) + refNote.ScaleOffset;
                } else {
                    noteVal = refNote.GetValue() - (noteDiff - noteDiff % 12);
                }
                
                Note note = new Note(noteVal);
                note.setAttack(refNote.getAttack() + twister.nextInt(40) - 20);
                note.ScaleOffset = refNote.ScaleOffset;
                note.SetLength(refNote.GetLength() - delayLength, false);
                note.IntendedScaleType = refNote.IntendedScaleType;

                if (!mirrorsMaster) {
                    ArrayList<Integer> offsets = refNote.IntendedScaleType.GetItemOffsets();
                
                    int valueIndex = twister.nextInt(offsets.size());
                    int valueToAdd = offsets.get(valueIndex);

                    note.addValue(valueToAdd, instrument);
                } else {
                    note.addValue(0, instrument);
                }

                List<Note> notes = sequence.getNotes();
                List<Note> refNotes = refSequence.getNotes();

                if (refNotes.get(i).getAttack() >= audibleMin) {
                    if (notes.size() > 0 && sequence.getNotes().get(sequence.getNotes().size() - 1).IsRest) {
                        note.IsRest = true;
                        if (twister.nextInt(restDelayRange) == 0) {
                            note.IsRest = false;
                        }
                    } else {
                        note.IsRest = twister.nextInt(restStartRange) == 0;
                    }                    

                    if (twister.nextInt(8) == 0) {
                        int skip = twister.nextInt(5);
                        int startPos = i + 1;

                        i = lengthenNotes(startPos, skip, refNotes, note, i);
                    }
                } else {
                    note.IsRest = true;
                }
                
                sequence.addNote(note);                
            }
            
            track.Sequences.add(sequence);
        }
        
        return track;
    }     
    
    private int lengthenNotes(int startPos, int skip, List<Note> refNotes, Note note, int i) {
        for (int j = startPos; j < startPos + skip; j++) {
            if (j < refNotes.size()) {
                note.SetLength(note.GetLength() + refNotes.get(j).GetLength(), false);
                i++;
            } else {
                break;
            }
        }
        return i;
    }    
}
