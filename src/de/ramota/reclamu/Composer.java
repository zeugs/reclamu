package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Composer {
    public static int MIN_LENGTH = 1;
    public static int MAX_LENGTH = 25;
    public static double MAX_OFFSET = 4;

    public Composer() {
        compose();
    }

    private void compose() {
        List<Track> tracks = getPatterns().Tracks;
        int counter = 0;

        for (Track track : tracks) {
            Pattern p1 = new Pattern(track.toString());
            System.out.println(track);

            try {
                MidiFileManager.savePatternToMidi(p1, new File("test" + String.valueOf(counter++) + ".mid"));
            } catch (IOException e) {
            }
        }
    }

    private Piece getPatterns() {
        Instrument piano = new Instrument();
        piano.MinNoteIndex = 24;
        piano.MaxNoteIndex = 108;
        piano.Name = "Piano";
        piano.VariationGrip = 0.7;
        
        Instrument piano_sec = new Instrument();
        piano_sec.ValueOffset = -12;
        piano_sec.MinNoteIndex = 24;
        piano_sec.MaxNoteIndex = 108;
        piano_sec.Name = "Piano_Sec";
        piano_sec.VariationGrip = 0.8;

        Instrument cello = new Instrument();
        cello.ValueOffset = -24;
        cello.MinNoteIndex = 24;
        cello.MaxNoteIndex = 96;
        cello.Name = "Cello";
        cello.VariationGrip = 0.8;

        Instrument violin = new Instrument();
        violin.ValueOffset = 0;
        violin.MinNoteIndex = 45;
        violin.MaxNoteIndex = 96;
        violin.Name = "Violin";
        violin.VariationGrip = 0.8;

        Instrument violin2 = new Instrument();
        violin2.ValueOffset = -12;
        violin2.MinNoteIndex = 45;
        violin2.MaxNoteIndex = 96;
        violin2.Name = "Violin2";
        violin2.VariationGrip = 0.7;

        Instrument contrabass = new Instrument();
        contrabass.ValueOffset = -36;
        contrabass.MinNoteIndex = 24;
        contrabass.MaxNoteIndex = 60;
        contrabass.Name = "Contrabass";
        contrabass.VariationGrip = 0.7;

        Instrument flute = new Instrument();
        flute.ValueOffset = 0;
        flute.MinNoteIndex = 48;
        flute.MaxNoteIndex = 86;
        flute.Name = "Flute";
        flute.VariationGrip = 0.7;

        Instrument clarinet = new Instrument();
        clarinet.ValueOffset = -12;
        clarinet.MinNoteIndex = 40;
        clarinet.MaxNoteIndex = 84;
        clarinet.Name = "Clarinet";
        clarinet.VariationGrip = 0.7;

        Instrument oboe = new Instrument();
        oboe.ValueOffset = -12;
        oboe.MinNoteIndex = 46;
        oboe.MaxNoteIndex = 81;
        oboe.Name = "Oboe";
        oboe.VariationGrip = 0.7;

        Instrument englishHorn = new Instrument();
        englishHorn.ValueOffset = -36;
        englishHorn.MinNoteIndex = 47;
        englishHorn.MaxNoteIndex = 79;
        englishHorn.Name = "English Horn";
        englishHorn.VariationGrip = 0.7;

        Instrument frenchHorn = new Instrument();
        frenchHorn.ValueOffset = -24;
        frenchHorn.MinNoteIndex = 29;
        frenchHorn.MaxNoteIndex = 72;
        frenchHorn.Name = "French Horn";
        frenchHorn.VariationGrip = 0.7;

        Instrument trombone = new Instrument();
        trombone.ValueOffset = -24;
        trombone.MinNoteIndex = 33;
        trombone.MaxNoteIndex = 67;
        trombone.Name = "Oboe";
        trombone.VariationGrip = 0.7;

        Instrument tuba = new Instrument();
        tuba.ValueOffset = -36;
        tuba.MinNoteIndex = 14;
        tuba.MaxNoteIndex = 53;
        tuba.Name = "Tuba";
        tuba.VariationGrip = 0.7;

        Piece piece = new Piece();

        Track track1 = this.getTrack(piano);

        Track track5 = this.getAccompanimentTrack(track1, piano_sec);
        Track track2 = this.getAccompanimentTrack(track1, cello);
        Track track3 = this.getAccompanimentTrack(track1, violin);
        Track track6 = this.getAccompanimentTrack(track1, violin2);
        Track track4 = this.getAccompanimentTrack(track1, contrabass);
        Track track7 = this.getAccompanimentTrack(track1, flute);
        Track track8 = this.getAccompanimentTrack(track1, clarinet);
        Track track9 = this.getAccompanimentTrack(track1, oboe);
        Track track10 = this.getAccompanimentTrack(track1, englishHorn);
        Track track11 = this.getAccompanimentTrack(track1, frenchHorn);
        Track track12 = this.getAccompanimentTrack(track1, trombone);
        Track track13 = this.getAccompanimentTrack(track1, tuba);

        piece.addTrack(track1);
        piece.addTrack(track5);
        piece.addTrack(track2);
        piece.addTrack(track3);
        piece.addTrack(track6);
        piece.addTrack(track4);
        piece.addTrack(track7);
        piece.addTrack(track8);
        piece.addTrack(track9);
        piece.addTrack(track10);
        piece.addTrack(track11);
        piece.addTrack(track12);
        piece.addTrack(track13);

        return piece;
    }

    private Track getTrack(Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        
        Track track = new Track();
        int maxRepNum = twister.nextInt(10) + 1;
        int adaptRange = twister.nextInt(4) + 1;
        int restRange = twister.nextInt(7) + 1;        
        int numberOfSequences = twister.nextInt(20);
        
        Sequence adaptedSequence;
        Sequence sequenceToAdd;
        for (int i = 0; i < numberOfSequences; i++) {
            Sequence sequence = getSequence(instrument);   
            
            int repetitions = twister.nextInt(maxRepNum);
            System.out.println(String.format("Number of repetitions: %d", repetitions));
            for (int j = 0; j < repetitions; j++) {
                if (twister.nextInt(adaptRange) == 0) {
                    adaptedSequence = sequence.getCopy();
                    boolean transposeUp = twister.nextBoolean();
                    adaptedSequence.notes.forEach(note -> {
                        note.addValue(transposeUp ? 12 : -12, instrument, false);
                    });
                    track.sequences.add(adaptedSequence);
                    sequenceToAdd = adaptedSequence;
                } else {
                    sequenceToAdd = sequence;
                }
                
                if (twister.nextInt(restRange) == 0) {
                    sequenceToAdd.notes.forEach(note -> {
                        note.IsRest = true;
                    });                    
                }
                track.sequences.add(sequenceToAdd);
            }
        }
                
        return track;
    }

    private Track getAccompanimentTrack(Track masterTrack, Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        Track track = new Track();
        int restRange = twister.nextInt(7) + 1;

        for (Sequence refSequence: masterTrack.sequences) {
            Sequence sequence = new Sequence();
    
            boolean addRest = twister.nextInt(restRange) == 0;

            if (addRest) {
                for (int j = 0; j < refSequence.notes.size(); j++) {
                    Note noteCopy = refSequence.notes.get(j).getCopy();
                    noteCopy.IsRest = true;
                    sequence.notes.add(noteCopy);
                }    
                track.sequences.add(sequence);
                
                continue;
            }                
            
            int attackRange = twister.nextInt(90) + 1;
            int restDelayRange = twister.nextInt(7) + 1;
            int restStartRange = twister.nextInt(12) + 1;
            int noteLengthenRange = twister.nextInt(12) + 1;
            int noteSkipRange = twister.nextInt(8) + 1;
            
            for (int i = 0; i < refSequence.notes.size(); i++) {
                Note refNote = refSequence.notes.get(i);
                Note note = new Note(refNote.GetValue() + instrument.ValueOffset);
                note.Attack = twister.nextInt(attackRange) + 30;
                note.BaseNote = refNote.BaseNote;
                note.Length = refNote.Length;
                note.IntendedAccomp = refNote.IntendedAccomp;

                int rand = 0;
                int valueToAdd = 0;

                if (note.Attack > 127) {
                    note.Attack = 115;
                } else if (note.Attack < 0) {
                    note.Attack = 15;
                }

                if (refNote.IntendedAccomp == IntendedAccompaniment.MAJOR) {
                    twister.nextInt(3);
                    switch (rand) {
                        case 0: valueToAdd = 0; break;
                        case 1: valueToAdd = 4; break;
                        case 2: valueToAdd = 7; break;
                        default: break;
                    }                
                }

                if (refNote.IntendedAccomp == IntendedAccompaniment.MINOR) {
                    twister.nextInt(3);
                    switch (rand) {
                        case 0: valueToAdd = 0; break;
                        case 1: valueToAdd = 3; break;
                        case 2: valueToAdd = 7; break;
                        default: break;
                    }                
                }

                note.addValue(valueToAdd, instrument, true);

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

                if (twister.nextInt(noteLengthenRange) == 0) {
                    int skip = twister.nextInt(noteSkipRange) + 1;
                    int startPos = i + 1;

                    for (int j = startPos; j < startPos + skip; j++) {
                        if (j < refSequence.notes.size()) {
                            note.Length += refSequence.notes.get(j).Length;
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                
                sequence.addNote(note);                
            }
            
            track.sequences.add(sequence);
        }
        
        return track;
    }

    private Sequence getSequence(Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        Sequence sequence = new Sequence();

        int lengthRange = MAX_LENGTH - MIN_LENGTH;
        int patternLength = twister.nextInt(lengthRange);
        int targetLength = patternLength + MIN_LENGTH;
        double baseLength = instrument.DefaultLength;
        int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        int currentValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;
        int usedBaseNote = twister.nextInt(12);
        double maxOffset = twister.nextInt(14) + 2;
        int changeBaseNoteRange = twister.nextInt(20) + 5;
        
        IntendedAccompaniment intendedAccomp = IntendedAccompaniment.values()[twister.nextInt(1)];

        double i = 0;
        int attackRange = twister.nextInt(90) + 1;
        int changeAccompRange = twister.nextInt(10) + 1;
        int restDelayRange = twister.nextInt(7) + 1;
        int restStartRange = twister.nextInt(12) + 1;

        while (i < targetLength) {
            boolean adaptLength = (twister.nextInt(8) == 0);
            boolean switchLength = (twister.nextInt(30) == 0);
            double actualLength = baseLength;
                        
            if (adaptLength) {
                double lengthDelta = twister.nextDouble() * (baseLength * 0.3); // max 30% change
                boolean subtractDelta = twister.nextBoolean();
                
                if (subtractDelta) {
                    lengthDelta *= -1;
                }
                baseLength += lengthDelta;
                
                if (baseLength < Note.MIN_LENGTH) {
                    baseLength = Note.MIN_LENGTH;
                } else if (baseLength > Note.MAX_LENGTH) {
                    baseLength = Note.MAX_LENGTH - twister.nextDouble() * 0.5;
                }
            }
            
            if (switchLength) {
                baseLength = twister.nextDouble() * Note.MAX_LENGTH / 2 + Note.MIN_LENGTH;
                actualLength = baseLength;
            }
            
            if (twister.nextInt(changeBaseNoteRange) == 0) {
                usedBaseNote = twister.nextInt(12);
                actualLength = baseLength;
            }
            
            double baseOffset = twister.nextInt((int)maxOffset + 1);
            double adjustedOffset = (baseOffset - maxOffset / 2) + 1;
            
            Note note = new Note(currentValue);
            note.Attack = twister.nextInt(attackRange) + 30;
            note.BaseNote = usedBaseNote;
            note.Length = actualLength;

            if (twister.nextInt(changeAccompRange) == 0) {
                intendedAccomp = IntendedAccompaniment.values()[twister.nextInt(1)];
                System.out.println("Intended Accomp changed!");
            }
            
            note.IntendedAccomp = intendedAccomp;
            
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
}
