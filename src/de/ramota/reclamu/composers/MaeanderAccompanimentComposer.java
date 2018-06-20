package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.AbstractSequence;
import de.ramota.reclamu.AbstractTrack;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathies Gräske
 */
public class MaeanderAccompanimentComposer extends AccompanimentComposer {

    public MaeanderAccompanimentComposer(String name) {
        super(name);
    }
            
    @Override
    protected AbstractTrack getAccompanimentTrack(String name, AbstractTrack masterTrack, Instrument instrument) {
        AbstractTrack track = new AbstractTrack(name, instrument);
        int noteDiff = -1;
        int audibleMin = twister.nextInt(60) + 35;
        System.out.println(String.format("Track loudness: %d", audibleMin));

        for (AbstractSequence refSequence: masterTrack.Sequences) {
            AbstractSequence sequence = new AbstractSequence();
    
            boolean addRest = (twister.nextInt(7) == 0);
            
            if (addRest) {
                silenceSequence(refSequence, sequence, track);
                
                continue;
            }                
            
            int restDelayRange = twister.nextInt(3) + 2;
            int restStartRange = twister.nextInt(10) + 2;

            if (noteDiff == -1) {
                noteDiff = findNoteDiff(instrument, refSequence);
            }
            
            for (int i = 0; i < refSequence.getNotes().size(); i++) {
    
                if (twister.nextInt(30) == 0) {
                    noteDiff = this.findNoteDiff(instrument, refSequence);
                }
                
                AbstractNote refNote = refSequence.getNotes().get(i);

                AbstractNote note = getNextNote(noteDiff, false, refNote);

                ArrayList<Integer> offsets = refNote.IntendedScaleType.GetItemOffsets();

                int valueIndex = twister.nextInt(offsets.size());
                int valueToAdd = offsets.get(valueIndex);

                note.addValue(valueToAdd, instrument);

                List<AbstractNote> notes = sequence.getNotes();
                List<AbstractNote> refNotes = refSequence.getNotes();
                
                if (refNotes.get(i).getAttack() >= audibleMin) {
                    if (notes.size() > 0 && sequence.getNotes().get(sequence.getNotes().size() - 1).IsRest) {
                        note.IsRest = true;
                        if (twister.nextInt(restDelayRange) == 0) {
                            note.IsRest = false;
                        }
                    } else {
                        note.IsRest = twister.nextInt(restStartRange) == 0;
                    }                    

                    if (twister.nextInt(12) == 0) {
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

    private int lengthenNotes(int startPos, int skip, List<AbstractNote> refNotes, AbstractNote note, int i) {
        for (int j = startPos; j < startPos + skip; j++) {
            if (j < refNotes.size()) {
                note.setLength(note.getLength() + refNotes.get(j).getLength(), false);
                i++;
            } else {
                break;
            }
        }
        return i;
    }
}
