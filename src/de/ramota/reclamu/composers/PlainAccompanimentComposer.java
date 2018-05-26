package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.AbstractSequence;
import de.ramota.reclamu.AbstractTrack;
import de.ramota.reclamu.ScaleItem;
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
        int noteDiff = -1;

        for (AbstractSequence refSequence: masterTrack.Sequences) {
            AbstractSequence sequence = new AbstractSequence();
            
            boolean mirrorsMaster = (twister.nextInt(2) == 0);
            int restDelayRange = twister.nextInt(2);
            int restStartRange = twister.nextInt(40);

            boolean silence = twister.nextInt(16) == 0;
            
            if (silence) {
                for (int j = 0; j < refSequence.getNotes().size(); j++) {
                    AbstractNote noteCopy = refSequence.getNotes().get(j).getCopy();
                    noteCopy.IsRest = true;
                    sequence.getNotes().add(noteCopy);
                }    
                track.Sequences.add(sequence);
                
                continue;
            }

            if (twister.nextInt(3) != 0 && refSequence.getParentSequence() != null) {
                boolean sequenceFound = false;
                for (AbstractSequence accompSequence: track.Sequences) {
                    if (accompSequence.getParentSequence() == refSequence.getParentSequence()) {
                        track.Sequences.add(accompSequence.getCopy());
                        sequenceFound = true;
                        break;
                    }
                }
                if (sequenceFound) {
                    continue;
                }
            }

            if (noteDiff == -1) {
                noteDiff = findNoteDiff(instrument, refSequence);
            }
            
            int valueIndex = -1;

            int divisionSustainRange = twister.nextInt(5) + 1;
            int lengthenSustainRange = twister.nextInt(5) + 1;
            boolean lengthening = false;
            boolean dividing = false;
            int divisions = 0;

            for (int i = 0; i < refSequence.getNotes().size(); i++) {
    
                if (twister.nextInt(15) == 0) {
                    noteDiff = this.findNoteDiff(instrument, refSequence);
                }

                AbstractNote refNote = refSequence.getNotes().get(i);

                AbstractNote note = generateNote(noteDiff, mirrorsMaster, refNote);

                valueIndex = setValueToAdd(instrument, mirrorsMaster, valueIndex, refNote, note);

                List<AbstractNote> notes = sequence.getNotes();
                List<AbstractNote> refNotes = refSequence.getNotes();

                if (!refNotes.get(i).IsRest || twister.nextInt(5) == 0) {
                    if (notes.size() > 0 && sequence.getNotes().get(sequence.getNotes().size() - 1).IsRest) {
                        note.IsRest = true;
                        if (twister.nextInt(restDelayRange) == 0) {
                            note.IsRest = false;
                        }
                    } else {
                        note.IsRest = !(twister.nextInt(restStartRange) == 0);
                    }                    

                    if (!note.IsRest) {
                        if (!note.AttackSet) {
                            if (refNote.LengtheningAllowed || lengthening) {
                                if (!dividing || twister.nextInt(1) == 0) {
                                    int skip = twister.nextInt(4) + 1;
                                    int startPos = i + 1;

                                    i = lengthenNotes(startPos, skip, refNotes, note, i);

                                    if (twister.nextInt(4) == 0) {
                                        lengthenSustainRange = twister.nextInt(5) + 1;
                                    }

                                    if (!lengthening) {
                                        lengthening = true;
                                    }

                                    if (twister.nextInt(lengthenSustainRange) == 0) {
                                        lengthening = false;
                                    }
                                }
                            }

                            if (refNote.DividingAllowed || dividing) {
                                if (refNote.DividingAllowed) {
                                    divisions = refNote.SubDivisions;
                                    if (note.getLength() < AbstractNote.MIN_LENGTH * 2) {
                                        divisions = 2;
                                    }
                                }

                                if (!lengthening || twister.nextInt(1) == 0) {
                                    int oldLength = note.getLength();

                                    note.setLength(note.getLength() / divisions, false);

                                    AbstractNote extraNote = null;
                                    for (int j = 0; j < divisions - 1; j++) {
                                        extraNote = generateNote(noteDiff, mirrorsMaster, note);
                                        valueIndex = setValueToAdd(instrument, mirrorsMaster, valueIndex, note, extraNote);
                                        extraNote.IsRest = !(twister.nextInt(restStartRange) == 0);

                                        sequence.addNote(extraNote);
                                    }
                                    extraNote.setLength(extraNote.getLength() + oldLength % divisions, false);

                                    if (twister.nextInt(4) == 0) {
                                        divisionSustainRange = twister.nextInt(5) + 1;
                                    }

                                    if (!dividing) {
                                        dividing = true;
                                    }

                                    if (twister.nextInt(divisionSustainRange) == 0) {
                                        dividing = false;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    note.IsRest = true;
                }
                
                sequence.addNote(note);                
            }

            sequence.setParentSequence(refSequence);
            track.Sequences.add(sequence);
        }

        postProcessTrack(track);

        return track;
    }

    private int setValueToAdd(Instrument instrument, boolean mirrorsMaster, int valueIndex, AbstractNote refNote, AbstractNote note) {
        if (!mirrorsMaster && !note.IsRest) {
            ArrayList<Integer> offsets = refNote.IntendedScaleType.GetItemOffsets();

            if (valueIndex == -1) {
                valueIndex = twister.nextInt(offsets.size());
            } else if (twister.nextInt(5) == 0) {
                valueIndex += twister.nextInt(3) - 1;
            }

            if (valueIndex > offsets.size() - 1) {
                valueIndex = offsets.size() - 1;
            } else if (valueIndex < 0) {
                valueIndex = 0;
            }

            int valueToAdd = offsets.get(valueIndex);

            note.addValue(valueToAdd, instrument);
        } else {
            note.addValue(0, instrument);
        }
        return valueIndex;
    }

    private void postProcessTrack(AbstractTrack track) {
        int audibleMinRange = 50;

        for (AbstractSequence sequence: track.Sequences) {
            int audibleMin = twister.nextInt(audibleMinRange) + 20;
            for (AbstractNote note: sequence.getNotes()) {
                if (note.getAttack() < audibleMin) {
                    note.IsRest = true;
                }
            }
        }
    }
    
    private int lengthenNotes(int startPos, int skip, List<AbstractNote> refNotes, AbstractNote note, int i) {
        if (startPos >= refNotes.size() - 1) {
            return i;
        }

        Integer scaleOffset = refNotes.get(startPos).ScaleOffset;
        ScaleItem scaleType = refNotes.get(startPos).IntendedScaleType;
        
        for (int j = startPos; j < startPos + skip; j++) {
            if (j >= refNotes.size()) {
                break;
            }
            
            AbstractNote refNote = refNotes.get(j);
            boolean noteFits = (refNote.IntendedScaleType == scaleType && refNote.ScaleOffset == scaleOffset);
            
            if (noteFits) {
                note.setLength(note.getLength() + refNote.getLength(), false);
                i++;
            } 
        }
        return i;
    }    
}
