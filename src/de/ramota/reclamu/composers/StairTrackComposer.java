package de.ramota.reclamu.composers;

import de.ramota.reclamu.*;
import de.ramota.reclamu.configuration.PieceConfiguration;
import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author Mathies Gräske
 */
public class StairTrackComposer extends TrackComposer {

    private final int startDirection;

    public StairTrackComposer(String name, int startDirection, MersenneTwister twister) {
        super(name, twister);
        this.startDirection = startDirection;
        this.readAllowedScaleOffsets(PieceConfiguration.getInstance());
    }
        
    @Override
    public AbstractSequence getSequence(Instrument instrument) {
        AbstractSequence sequence = new AbstractSequence();

        int currentLength = instrument.DefaultLength / 2;
        int dirChanges = 0;
        boolean up = false;

        if (startDirection == 1) {
            currentVal = instrument.MinNoteIndex;
        } else {
            currentVal = instrument.MaxNoteIndex;
            up = true;
        }

        while (true) {
            currentLength += instrument.DefaultLength / 30 * (twister.nextBoolean() ? 1 : -1);

            if (currentLength > AbstractNote.MAX_LENGTH) {
                currentLength = AbstractNote.MAX_LENGTH;
            } else if (currentLength < AbstractNote.MIN_LENGTH * 2) {
                currentLength = AbstractNote.MIN_LENGTH * 2;
            }

            AbstractNote note = new AbstractNote(currentVal);

            note.setLength(currentLength, true);
            note.IntendedScaleType = currentAccomp;
            note.IsRest = false;

            if (currentVal == instrument.MaxNoteIndex || currentVal == instrument.MinNoteIndex) {
                dirChanges++;
                up = !up;
            }

            sequence.addNote(note);
            currentVal = up ? currentVal + 1 : currentVal - 1;

            if (dirChanges == 3) {
                break;
            }
        }

        return sequence;
    }    
        
    @Override
    public AbstractTrack generateTrack(Instrument instrument, String name, int sequenceNum) {
        AbstractTrack track = new AbstractTrack(name, instrument);
        
        for (int i = 0; i < sequenceNum; i++) {
            AbstractSequence sequence;
            sequence = this.getSequence(instrument);
            track.addSequence(sequence);            
        }
                
        this.findAccompaniment();
        this.findScale();
        
        double currentAttackOffset = 0;
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

                note.IntendedScaleType = currentAccomp;
                note.ScaleOffset = ScaleOffset;
                note.setAttack((int) ((Math.sin(currentAttackOffset)) * 40 + 70));
                note.setValueInRange();
    
                currentAttackOffset += Math.PI / 70;
                noteCount++;
            }
            
            this.currentAccomp = oldAccomp;
            this.currentAccomp.setNewOffset(oldOffset);

            sequenceCount++;
        }
        
        return track;
    }    
}
