package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.AbstractSequence;
import de.ramota.reclamu.AbstractTrack;
import de.ramota.reclamu.ScaleItem;

/**
 *
 * @author Mathies Gräske
 */
public class SimpleTrackComposer extends TrackComposer {

    public SimpleTrackComposer(String name) {
        super(name);
    }
        
    @Override
    public AbstractSequence getSequence(Instrument instrument, double tempo) {
        AbstractSequence sequence = new AbstractSequence();
        
        sequence.setTempo(tempo);
        
        if (currentVal == 0) {
            currentVal = twister.nextInt((int) ((instrument.MaxNoteIndex - instrument.MinNoteIndex) * 0.75)) + instrument.MinNoteIndex;
        }
        
        int sequenceLength = (twister.nextInt(2) + 1) * 4 * AbstractNote.MIN_LENGTH;
        
        int currentNoteLength = twister.nextInt(4) * AbstractNote.MIN_LENGTH;
        
        boolean finished = false;
        int lengthSummed = 0;
        
        while (!finished) {
            if (twister.nextInt(3) == 0) {
                currentNoteLength += (twister.nextInt(4) - 2) * AbstractNote.MIN_LENGTH;
                if (currentNoteLength >= AbstractNote.MAX_LENGTH) {
                    currentNoteLength = AbstractNote.MAX_LENGTH;
                } else if (currentNoteLength < AbstractNote.MIN_LENGTH) {
                    currentNoteLength = AbstractNote.MIN_LENGTH;
                }
                
                if (lengthSummed + currentNoteLength >= sequenceLength) {
                    currentNoteLength = sequenceLength - lengthSummed;
                    finished = true;
                }
                
                lengthSummed += currentNoteLength;
            }
            
            AbstractNote note = generateNote(currentNoteLength, sequence);

            sequence.addNote(note);

            currentVal += (twister.nextInt(8) - 4) * instrument.VariationGrip;                
            
            if (currentVal < instrument.MinNoteIndex) {
                currentVal = instrument.MinNoteIndex;
            } else if (currentVal > instrument.MaxNoteIndex) {
                currentVal = instrument.MaxNoteIndex;
            }
        }
        
        return sequence;
    }    

    protected AbstractNote generateNote(int currentNoteLength, AbstractSequence sequence) throws IllegalArgumentException {
        AbstractNote note = new AbstractNote(currentVal);
        note.setAttack(twister.nextInt(80) + 30);
        note.setLength((int)(currentNoteLength / sequence.getTempo()), true);
        note.IntendedScaleType = currentAccomp;
        if (twister.nextInt(3) == 0) {
            note.RelativeOffset += twister.nextInt(3) - 1;
        }
        if (sequence.getNotes().size() > 0) {
            if (sequence.getNotes().get(sequence.getNotes().size() - 1).IsRest) {
                note.IsRest = twister.nextBoolean();
            } else if (twister.nextInt(4) == 0) {
                note.IsRest = true;
            }
        }
        return note;
    }
    
    @Override
    public AbstractTrack generateTrack(Instrument instrument, String name, int sequenceNum) {
        AbstractTrack track = new AbstractTrack(name);
        
        double currentTempo = twister.nextDouble() * 2 + 0.1;

        for (int i = 0; i < sequenceNum; i++) {
            if (twister.nextInt(3) == 0) {
                currentTempo += (twister.nextDouble() - 0.5) / 2;

                if (currentTempo < 0) {
                    currentTempo = 0;
                } else if (currentTempo > 2) {
                    currentTempo = 2;
                }
                System.out.println(String.format("Tempo: %d", (int)(currentTempo * 100)));
            }
                        
            if (twister.nextInt(20) != 0 && i > 0) {
                AbstractSequence sequenceToCopy = track.Sequences.get(twister.nextInt(track.Sequences.size()));
                if (twister.nextInt(5) != 0) {
                    sequenceToCopy = track.Sequences.get(track.Sequences.size() - 1);
                }
                AbstractSequence adaptedSequence = sequenceToCopy.getCopy();

                boolean alterSequence = twister.nextInt(5) == 0;
                if (alterSequence) {
                    boolean transpose = twister.nextInt(4) == 0;
                    boolean transposeUp = twister.nextInt(4) == 0;
                    for (AbstractNote note: adaptedSequence.getNotes()) {
                        if (twister.nextInt(4) == 0) {
                            note = this.generateNote(note.getValue(), sequenceToCopy);
                        }
                        if (transpose) {
                            note.addValue(transposeUp ? 12 : -12, instrument);
                        }
                    }
                    adaptedSequence.setParentSequence(null);
                }
                
                track.addSequence(adaptedSequence);
            } else {
                AbstractSequence sequence;
                sequence = this.getSequence(instrument, currentTempo);   
                track.addSequence(sequence);                
            }
        }
        
        int currentAttack = twister.nextInt(80) + 40;
        
        this.findAccompaniment();
        this.findScale();
        
        int sequenceCount = 0;
        for (AbstractSequence sequence: track.Sequences) {
                        
            if (twister.nextInt(12) == 0) {
                this.findScale();
            }

            int noteCount = 0;
            boolean terminate = twister.nextInt(4) == 0 || sequenceCount == track.Sequences.size() - 1;
            int terminateIntro = twister.nextBoolean() ? 1 : 2;
            ScaleItem oldAccomp = this.currentAccomp;
            int oldOffset = currentAccomp.getOffset();

            for (AbstractNote note: sequence.getNotes()) {
                
                if (terminate && noteCount > sequence.getNotes().size() * 0.9) {
                    this.currentAccomp = intendedAccomps.get(0);
                    this.currentAccomp.setNewOffset(0);                    
                    System.out.println("!!!-!");
                } else if (terminate && noteCount > sequence.getNotes().size() * 0.8) {
                    oldAccomp = this.currentAccomp;
                    oldOffset = currentAccomp.getOffset();
                    this.currentAccomp = intendedAccomps.get(0);
                    this.currentAccomp.setNewOffset(terminateIntro);
                    System.out.println("!-!");
                } else if (twister.nextInt(28) == 0) {
                    this.findAccompaniment();
                }

                if (twister.nextInt(150) == 0) {
                    currentAttack = twister.nextInt(80) + 40;
                }
                note.setAttack(currentAttack);
                note.IntendedScaleType = currentAccomp;
                note.ScaleOffset = ScaleOffset;
                
                if (twister.nextInt(7) != 0) {
                    note.setValueInRange();
                }
                
                currentAttack += twister.nextInt(30) - 15;
                
                if (currentAttack < 25) {
                    currentAttack = 26;
                } else if (currentAttack > 120) {
                    currentAttack = 119;
                }    
                
                noteCount++;
            }
            
            this.currentAccomp = oldAccomp;
            this.currentAccomp.setNewOffset(oldOffset);

            sequenceCount++;
        }
        
        return track;
    }    
}
