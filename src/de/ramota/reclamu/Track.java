package de.ramota.reclamu;

import static de.ramota.reclamu.Composer.MAX_SEQUENCE_LENGTH;
import static de.ramota.reclamu.Composer.MIN_SEQUENCE_LENGTH;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

public final class Track {
    public List<Sequence> Sequences = new ArrayList<>();
    public List<PlayGroup> PlayGroups = new ArrayList<>();
    private final Instrument instrument;
    private ScaleItem currentAccomp;
    private int instrumentRange;
    private int currentValue;
    private int scaleOffset;
    private final MersenneTwister twister = new MersenneTwister();
    private final List<ScaleItem> intendedAccomps;

    public Track(Instrument instrument, List<ScaleItem> intendedAccompaniments) {
        this.instrument = instrument;
        this.intendedAccomps = intendedAccompaniments;
        this.findNoteValue();
        this.findAccompaniment();
        this.findScale();
    }

    public Track(Instrument instrument) {
        this.instrument = instrument;
        this.intendedAccomps = null;
    }
    
    public void findNoteValue() {
        instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        currentValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;        
    }
    
    public void findScale() {
        scaleOffset = twister.nextInt(12);        
        System.out.println(String.format("Scale changed to %d", scaleOffset));
    }
    
    public int GetCurrentValue() {
        return currentValue;
    }
    
    public int GetScaleOffset() {
        return scaleOffset;
    }

    public void SetCurrentValue(int value) {
        currentValue = value;
    }

    public void addSequence(Sequence sequence) {
        Sequences.add(sequence);
    }

    public Sequence getSequence(Instrument instrument) {
        Sequence sequence = new Sequence();

        int lengthRange = MAX_SEQUENCE_LENGTH - MIN_SEQUENCE_LENGTH;
        int patternLength = twister.nextInt(lengthRange);
        int targetLength = patternLength + MIN_SEQUENCE_LENGTH;
        int baseLength = instrument.DefaultLength;
        double maxOffset = twister.nextInt(14) + 2;
        
        int i = 0;
        int attackRange = twister.nextInt(90) + 1;
        int restDelayRange = twister.nextInt(7) + 1;
        int restStartRange = twister.nextInt(12) + 1;

        while (i < targetLength) {
            boolean adaptLength = twister.nextInt(8) == 0;
            boolean switchLength = twister.nextInt(30) == 0;
            int actualLength = baseLength;
                        
            if (adaptLength || switchLength) {
                double lengthDelta = twister.nextDouble() * (baseLength * (adaptLength ? 0.3 : 0.6)); // max 30% change on adapt, 50% otherwise
                boolean subtractDelta = twister.nextBoolean();
                
                if (subtractDelta) {
                    lengthDelta *= -1;
                }
                baseLength += lengthDelta;
                
                if (baseLength < Note.MIN_LENGTH) {
                    baseLength = Note.MIN_LENGTH;
                } else if (baseLength > Note.MAX_LENGTH) {
                    baseLength = Note.MAX_LENGTH - twister.nextInt(Note.MAX_LENGTH) / 2;
                }
            }
                        
            if (twister.nextInt(25) == 0) {
                this.findScale();
            }
            
            double baseOffset = twister.nextInt((int)maxOffset + 1);
            double adjustedOffset = (baseOffset - maxOffset / 2) + 1;
            
            Note note = new Note(currentValue);
            note.Attack = twister.nextInt(attackRange) + 30;
            note.ScaleOffset = scaleOffset;
            actualLength = note.SetLength(actualLength, true);

            if (twister.nextInt(10) == 0) {
                this.findAccompaniment();
            }
            
            note.IntendedScaleType = currentAccomp;
            
            double actualGrip = instrument.VariationGrip;
            
            currentValue = note.addValue(adjustedOffset * actualGrip, instrument, true);

            i += actualLength;

            if (sequence.notes.size() > 0) {
                if (sequence.notes.get(sequence.notes.size() - 1).IsRest) {
                    note.IsRest = true;
                    if (twister.nextInt(restDelayRange) == 0) {
                        note.IsRest = false;
                    }
                } else {
                    note.IsRest = twister.nextInt(restStartRange) == 0;
                }
            }
            
            sequence.addNote(note);
        }    
        
        System.out.println(String.format("Sequence note num: %s", sequence.notes.size()));
        
        return sequence;
    }
        
    @Override
    public String toString() {
        String sequenceInfo = "";

        for (Sequence sequence : Sequences) {
            sequenceInfo += sequence.toString();
        }

        return sequenceInfo;
    }

    public Track getCopy() {
        Track mirrorTrack = new Track(this.instrument);
        
        for (Sequence sequence : Sequences) {
            mirrorTrack.Sequences.add(sequence.getCopy());
        }
        
        return mirrorTrack;
    }

    private void findAccompaniment() {
        currentAccomp = intendedAccomps.get(twister.nextInt(intendedAccomps.size()));
        currentAccomp.SetNewOffset();
        System.out.println(String.format("Intended Accomp changed to %s!", currentAccomp.toString()));
    }
}
