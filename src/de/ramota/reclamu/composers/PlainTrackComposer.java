package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.ScaleItem;
import de.ramota.reclamu.AbstractSequence;
import de.ramota.reclamu.AbstractTrack;
import java.util.List;

/**
 *
 * @author Mathies Gräske
 */
public class PlainTrackComposer extends TrackComposer {
    
    public PlainTrackComposer(Instrument instrument, List<ScaleItem> intendedAccomps) {
        super(instrument);        
        this.intendedAccomps = intendedAccomps;
    }

    @Override
    public AbstractSequence getSequence(Instrument instrument, double tempo) {
        AbstractSequence sequence = new AbstractSequence();
        
        sequence.setTempo(tempo);
        
        int currentVal = twister.nextInt(instrument.MaxNoteIndex - instrument.MinNoteIndex) + instrument.MinNoteIndex;
        int sequenceLength = twister.nextInt(15) + 6;
        sequenceLength -= sequenceLength % 4 + 4;
        
        int subNoteCountIndex = twister.nextInt(3);
        int currentSubNoteCount = 0;
        
        switch (subNoteCountIndex) {
            case 0: currentSubNoteCount = 1; break;
            case 1: currentSubNoteCount = 2; break;
            case 2: currentSubNoteCount = 4; break;
            case 3: currentSubNoteCount = 8; break;
        }
 
        for (int i = 0; i < sequenceLength; i++) {
            if (twister.nextInt(5) == 0) {
                int subNoteDelta = (twister.nextInt(3) - 1);
                subNoteCountIndex += subNoteDelta;
                if (subNoteCountIndex < 0) {
                    subNoteCountIndex = 0;
                } else if (subNoteCountIndex > 3) {
                    subNoteCountIndex = 3;
                }
                
                switch (subNoteCountIndex) {
                    case 0: currentSubNoteCount = 1; break;
                    case 1: currentSubNoteCount = 2; break;
                    case 2: currentSubNoteCount = 4; break;
                    case 3: currentSubNoteCount = 8; break;
                }
            }
            
            for (int j = 0; j < currentSubNoteCount; j++) {
                AbstractNote note = new AbstractNote(currentVal);
                
                note.setAttack(twister.nextInt(80) + 30);
                note.setLength((int)(instrument.DefaultLength / currentSubNoteCount / sequence.getTempo()), true);
                note.IntendedScaleType = currentAccomp;

                if (sequence.getNotes().size() > 0) {
                    if (sequence.getNotes().get(sequence.getNotes().size() - 1).IsRest) {
                        note.IsRest = twister.nextBoolean();
                    } else if (twister.nextInt(5) == 0) {
                        note.IsRest = true;
                    }
                }

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
    public AbstractTrack generateTrack(int sequenceNum) {
        AbstractTrack track = new AbstractTrack();
        
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
            if (i > 5 && twister.nextInt(3) == 0) {
                int itemToCopy = twister.nextInt(track.Sequences.size());
                sequence = track.Sequences.get(itemToCopy).getCopy();
                                
                System.out.println(String.format("Just copied sequence %d", itemToCopy));
            } else {
                sequence = this.getSequence(instrument, currentTempo);   
            }
            track.addSequence(sequence);
            
            int repetitions = twister.nextInt(10);
            for (int j = 0; j < repetitions; j++) {
                AbstractSequence adaptedSequence = sequence.getCopy();
                boolean transposeUp = twister.nextBoolean();
                sequence.getNotes().forEach(note -> {
                    note.addValue(transposeUp ? 12 : -12, instrument);
                });
                
                track.addSequence(adaptedSequence);
            }
        }
        
        int currentAttack = twister.nextInt(80) + 40;
        
        this.findAccompaniment(intendedAccomps);
        this.findScale();
        
        int sequenceCount = 0;
        for (AbstractSequence sequence: track.Sequences) {
                        
            if (twister.nextInt(3) == 0) {
                this.findScale();
            }

            int noteCount = 0;
            boolean terminate = twister.nextInt(4) == 0 || sequenceCount == track.Sequences.size() - 1;
            int terminateIntro = twister.nextBoolean() ? 4 : 5;
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
                } else if (twister.nextInt(9) == 0) {
                    this.findAccompaniment(intendedAccomps);
                    oldAccomp = this.currentAccomp;
                    oldOffset = this.currentAccomp.getOffset();
                }

                if (twister.nextInt(150) == 0) {
                    currentAttack = twister.nextInt(80) + 40;
                }
                note.setAttack(currentAttack);
                note.IntendedScaleType = currentAccomp;
                note.ScaleOffset = scaleOffset;
                note.setValueInRange();
                currentAttack += twister.nextInt(36) - 18;
                
                if (currentAttack < 40) {
                    currentAttack = 41;
                } else if (currentAttack > 120) {
                    currentAttack = 119;
                }
                
                noteCount++;
            }
            
            this.currentAccomp = oldAccomp;
            this.currentAccomp.setNewOffset(oldOffset);
        }
        
        sequenceCount++;
        
        return track;
    }    
}
