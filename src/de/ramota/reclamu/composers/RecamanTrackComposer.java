package de.ramota.reclamu.composers;

import de.ramota.reclamu.*;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathies Gräske
 */
public class RecamanTrackComposer extends TrackComposer {

    private int currentAdd = 0;
    private int currentNum = 0;
    private List<Integer> usedNumbers;

    public RecamanTrackComposer(String name, MersenneTwister twister) {
        super(name, twister);
        usedNumbers = new ArrayList<>();
    }
        
    @Override
    public AbstractSequence getSequence(Instrument instrument) {
        AbstractSequence sequence = new AbstractSequence();

        int currentLength = instrument.DefaultLength / 2;
        int nextNote = 0;

        while (nextNote < 6000) {
            nextNote = getNextNumber();
            currentVal = nextNote % (instrument.MaxNoteIndex - instrument.MinNoteIndex) + instrument.MinNoteIndex;

            AbstractNote note = new AbstractNote(currentVal);

            note.setLength(currentLength, true);
            note.IntendedScaleType = currentAccomp;
            note.IsRest = false;

            sequence.addNote(note);
        }

        return sequence;
    }

    private int getNextNumber() {
        int tryValue = currentNum - currentAdd;

        boolean found = false;
        for (int entry : usedNumbers) {
            if (entry == tryValue) {
                currentNum = currentNum + currentAdd;
                found = true;
                break;
            }
        }

        if (tryValue < 0) {
            currentNum = currentNum + currentAdd;
        } else if (!found) {
            currentNum = currentNum - currentAdd;
        }

        currentAdd++;
        usedNumbers.add(currentNum);
        System.out.println(String.format("%d", currentNum));
        return currentNum;
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
                //note.setValueInRange();
    
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
