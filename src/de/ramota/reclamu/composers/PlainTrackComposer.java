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
public class PlainTrackComposer extends TrackComposer {

    public PlainTrackComposer(String name) {
        super(name);
    }
        
    @Override
    public AbstractSequence getSequence(Instrument instrument, double tempo) {
        AbstractSequence sequence = new AbstractSequence();
        
        sequence.setTempo(tempo);
        
        if (currentVal == 0) {
            currentVal = twister.nextInt((int) ((instrument.MaxNoteIndex - instrument.MinNoteIndex) * 0.75)) + instrument.MinNoteIndex;
        }
        
        int sequenceLength = twister.nextInt(16) + 4;
        sequenceLength -= sequenceLength % 4 + 4;
        
        int currentLength = twister.nextInt(instrument.DefaultLength / 2) + instrument.DefaultLength / 4;
        
        for (int i = 0; i < sequenceLength; i++) {
            if (twister.nextInt(2) == 0) {
                currentLength += twister.nextInt(instrument.DefaultLength / 4) - instrument.DefaultLength / 8;        
            } else if (twister.nextInt(10) == 0) {
                currentLength = twister.nextInt(instrument.DefaultLength / 2) + instrument.DefaultLength / 4;        
            }
            
            AbstractNote note = new AbstractNote(currentVal);

            note.setAttack(twister.nextInt(80) + 30);
            note.setLength((int)(currentLength / sequence.getTempo()), true);
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
            
            AbstractSequence sequence;
            if (i > 5 && twister.nextInt(2) == 0) {
                int itemToCopy = twister.nextInt(track.Sequences.size());
                sequence = track.Sequences.get(itemToCopy).getCopy();
                                
                System.out.println(String.format("Just copied sequence %d", itemToCopy));
            } else {
                sequence = this.getSequence(instrument, currentTempo);   
            }
            track.addSequence(sequence);
            
            int repetitions = twister.nextInt(20);
            for (int j = 0; j < repetitions; j++) {
                AbstractSequence adaptedSequence = sequence.getCopy();
                boolean transposeUp = twister.nextInt(4) == 0;
                sequence.getNotes().forEach(note -> {
                    note.addValue(transposeUp ? 12 : -12, instrument);
                });
                
                track.addSequence(adaptedSequence);
            }
        }
        
        int currentAttack = twister.nextInt(80) + 40;
        
        this.findAccompaniment();
        this.findScale();
        
        int sequenceCount = 0;
        for (AbstractSequence sequence: track.Sequences) {
                        
            if (twister.nextInt(6) == 0) {
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
                } else if (twister.nextInt(14) == 0) {
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
