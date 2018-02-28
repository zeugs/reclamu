package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.ScaleItem;
import de.ramota.reclamu.AbstractSequence;
import de.ramota.reclamu.AbstractTrack;
import de.ramota.reclamu.configuration.PieceConfiguration;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathies Gräske
 */
public class PlainAccompanimentComposer extends AccompanimentComposer {

    public PlainAccompanimentComposer(String name) {
        super(name);
    }
    
    @Override
    protected AbstractTrack getAccompanimentTrack(String name, AbstractTrack masterTrack, Instrument instrument) {
        AbstractTrack track = new AbstractTrack(name);
        PieceConfiguration conf = PieceConfiguration.getInstance();
        
        int audibleMinFloor = conf.getValueAsInt("Audible Min Floor");
        int audibleMinAdded = conf.getValueAsInt("Audible Min Added");
        int noteDiffCalcMax = conf.getValueAsInt("Max Note Difference Calculation Delta");
        int restUsageDelta = conf.getValueAsInt("Max Rest Usage Delta");
        int maxNoteSkipDelta = conf.getValueAsInt("Max Skip Delta");
        int maxNoteSkip = conf.getValueAsInt("Max Note Skip");
        int maxNoMirrorSequenceDelta = conf.getValueAsInt("Max No Mirror Delta");
        
        int noteDiff = -1;
        int audibleMin = twister.nextInt(audibleMinAdded) + audibleMinFloor;
        System.out.println(String.format("Track loudness: %d", audibleMin));

        for (AbstractSequence refSequence: masterTrack.Sequences) {
            AbstractSequence sequence = new AbstractSequence();
            sequence.setTempo(refSequence.getTempo());
            
            boolean mirrorsMaster = (twister.nextInt(maxNoMirrorSequenceDelta) == 0);
            
            int maxRestDelayRange = conf.getValueAsInt("Max Rest Delay Range");
            int maxRestStartRange = conf.getValueAsInt("Max Rest Delay Start");

            if (noteDiff == -1) {
                noteDiff = findNoteDiff(instrument, refSequence);
            }
            
            int valueIndex = -1;
            
            for (int i = 0; i < refSequence.getNotes().size(); i++) {
    
                if (twister.nextInt(noteDiffCalcMax) == 0) {
                    noteDiff = this.findNoteDiff(instrument, refSequence);
                }
                
                AbstractNote refNote = refSequence.getNotes().get(i);
                
                int delayLength = this.addNoteHumanized(sequence);
                int noteVal;
                
                if (!mirrorsMaster) {
                    noteVal = (refNote.getValue() - refNote.getValue() % 12) - (noteDiff - noteDiff % 12) + refNote.ScaleOffset;
                } else {
                    noteVal = refNote.getValue() - (noteDiff - noteDiff % 12);
                }
                
                AbstractNote note = new AbstractNote(noteVal);
                note.setAttack(refNote.getAttack() + twister.nextInt(40) - 20);
                note.ScaleOffset = refNote.ScaleOffset;
                note.setLength(refNote.getLength() - delayLength, false);
                note.IntendedScaleType = refNote.IntendedScaleType;

                if (!mirrorsMaster) {
                    ArrayList<Integer> offsets = refNote.IntendedScaleType.GetItemOffsets();
                
                    if (valueIndex == -1) {
                        valueIndex = twister.nextInt(offsets.size());
                    } else if (twister.nextInt(5) == 0) {
                        valueIndex += twister.nextInt(3) - 1;
                        if (valueIndex > offsets.size() - 1) {
                            valueIndex = offsets.size() - 1;
                        } else if (valueIndex < 0) {
                            valueIndex = 0;
                        }
                    }
                    
                    int valueToAdd = offsets.get(valueIndex);

                    note.addValue(valueToAdd, instrument);
                } else {
                    note.addValue(0, instrument);
                }

                List<AbstractNote> notes = sequence.getNotes();
                List<AbstractNote> refNotes = refSequence.getNotes();

                if (refNotes.get(i).getAttack() >= audibleMin && (!refNotes.get(i).IsRest || twister.nextInt(restUsageDelta) == 0)) {
                    if (notes.size() > 0 && sequence.getNotes().get(sequence.getNotes().size() - 1).IsRest) {
                        note.IsRest = true;
                        if (twister.nextInt(maxRestDelayRange) == 0) {
                            note.IsRest = false;
                        }
                    } else {
                        note.IsRest = !(twister.nextInt(maxRestStartRange) == 0);
                    }                    

                    if (twister.nextInt(maxNoteSkipDelta) == 0) {
                        int skip = twister.nextInt(maxNoteSkip);
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
        if (startPos >= refNotes.size() - 1) {
            return i;
        }

        ScaleItem item = refNotes.get(startPos).IntendedScaleType;
        
        for (int j = startPos; j < startPos + skip; j++) {
            if (j < refNotes.size() && item == refNotes.get(j).IntendedScaleType) {
                note.setLength(note.getLength() + refNotes.get(j).getLength(), false);
                i++;
            } else {
                break;
            }
        }
        return i;
    }    
}
