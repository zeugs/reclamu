package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.AbstractSequence;
import de.ramota.reclamu.AbstractTrack;
import static de.ramota.reclamu.Composer.MAX_SEQUENCE_LENGTH;
import static de.ramota.reclamu.Composer.MIN_SEQUENCE_LENGTH;
import de.ramota.reclamu.ScaleItem;
import java.util.List;

/**
 *
 * @author Mathies Gräske
 */
public class FreeFormTrackComposer extends TrackComposer {

    public FreeFormTrackComposer(Instrument instrument, List<ScaleItem> intendedAccomps) {
        super(instrument);        
        this.intendedAccomps = intendedAccomps;
    }
            
    @Override
    public AbstractSequence getSequence(Instrument instrument, double tempo) {
        AbstractSequence sequence = new AbstractSequence();

        sequence.setTempo(1);
        
        int lengthRange = MAX_SEQUENCE_LENGTH - MIN_SEQUENCE_LENGTH;
        int patternLength = twister.nextInt(lengthRange);
        int targetLength = patternLength + MIN_SEQUENCE_LENGTH;
        int baseLength = instrument.DefaultLength;
        double maxOffset = twister.nextInt(14) + 2;
        
        int i = 0;
        int restDelayRange = twister.nextInt(7) + 1;
        int restStartRange = twister.nextInt(12) + 1;

        while (i < targetLength) {
            boolean adaptLength = twister.nextInt(8) == 0;
            boolean switchLength = twister.nextInt(30) == 0;
            int actualLength = baseLength;
                        
            if (adaptLength || switchLength) {
                double lengthDelta = twister.nextDouble() * (baseLength * (adaptLength ? 0.5 : 0.7) + 0.1);
                boolean subtractDelta = twister.nextBoolean();
                
                if (subtractDelta) {
                    lengthDelta *= -1;
                }

                baseLength += lengthDelta;
                
                if (baseLength < AbstractNote.MIN_LENGTH) {
                    baseLength = AbstractNote.MIN_LENGTH;
                } else if (baseLength > AbstractNote.MAX_LENGTH) {
                    baseLength = AbstractNote.MAX_LENGTH;
                }
            }
                        
            if (twister.nextInt(25) == 0) {
                this.findScale();
            }
            
            double baseOffset = twister.nextInt((int)maxOffset + 1);
            double adjustedOffset = (baseOffset - maxOffset / 2) + 1;
            
            AbstractNote note = new AbstractNote(currentValue);
            note.ScaleOffset = scaleOffset;
            actualLength = note.setLength(actualLength, true);

            if (twister.nextInt(2) == 0) {
                this.findAccompaniment(intendedAccomps);
            }
            
            note.IntendedScaleType = currentAccomp;
            
            double actualGrip = instrument.VariationGrip;
            
            note.addValue(adjustedOffset * actualGrip, instrument);
            note.setValueInRange();
            currentValue = note.getValue();

            i += actualLength;

            List<AbstractNote> notes = sequence.getNotes();
            if (notes.size() > 0 && notes.get(notes.size() - 1).IsRest) {
                note.IsRest = true;
                if (twister.nextInt(restDelayRange) == 0) {
                    note.IsRest = false;
                }
            } else {
                note.IsRest = twister.nextInt(restStartRange) == 0;
            }
            
            sequence.addNote(note);
        }    
        
        System.out.println(String.format("Sequence note num: %s", sequence.getNotes().size()));
        
        return sequence;
    }    

    @Override
    public AbstractTrack generateTrack(int sequenceNum) {
        AbstractTrack track = new AbstractTrack();
        AbstractSequence adaptedSequence;
        AbstractSequence sequenceToAdd;
        
        for (int i = 0; i < sequenceNum; i++) {

            AbstractSequence sequence;
            if (i > 5 && twister.nextInt(3) == 0) {
                int itemToCopy = twister.nextInt(track.Sequences.size());
                sequence = track.Sequences.get(itemToCopy).getCopy();
                System.out.println(String.format("Just copied sequence %d", itemToCopy));
            } else {
                sequence = this.getSequence(instrument, 1);   
            }
            
            int repetitions = twister.nextInt(6);
            System.out.println(String.format("Number of repetitions: %d", repetitions));
            for (int j = 0; j < repetitions; j++) {
                if (twister.nextInt(5) == 0) {
                    adaptedSequence = sequence.getCopy();
                    boolean transposeUp = twister.nextBoolean();
                    adaptedSequence.getNotes().forEach(note -> {
                        note.addValue(transposeUp ? 12 : -12, instrument);
                    });
                    
                    track.Sequences.add(adaptedSequence);
                    continue;
                } else {
                    sequenceToAdd = sequence;
                }
                
                boolean modifyNoteValues = twister.nextBoolean();
                if (modifyNoteValues) {
                    adaptedSequence = sequence.getCopy();
                    adaptedSequence.getNotes().forEach(note -> {
                        if (twister.nextInt(12) == 0) {
                            note.addValue(twister.nextInt(8) - 3, instrument);
                            note.setValueInRange();
                        }
                    });
                    sequenceToAdd = adaptedSequence;
                }

                if (twister.nextInt(7) == 0) {
                    sequenceToAdd.getNotes().forEach(note -> {
                        note.IsRest = true;
                    });                    
                }
                track.Sequences.add(sequenceToAdd);
            }
        }

        findAttackValues(track);
        
        return track;
    }
}
