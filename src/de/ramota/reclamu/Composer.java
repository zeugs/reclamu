package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Composer {
    public static int MIN_LENGTH = 2;
    public static int MAX_LENGTH = 500;
    public static double MAX_OFFSET = 14;

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
        violin.ValueOffset = 12;
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
        contrabass.MaxNoteIndex = 84;
        contrabass.Name = "Contrabass";
        contrabass.VariationGrip = 0.7;

        Piece piece = new Piece();
        Track track1 = new Track();
        Track track5 = new Track();
        Track track2 = new Track();
        Track track3 = new Track();
        Track track6 = new Track();
        Track track4 = new Track();

        Sequence sequence1 = this.getSequence(piano);
        track1.addSequence(sequence1);

        Sequence sequence5 = this.getAccompanimentSequence(sequence1, piano_sec);
        track5.addSequence(sequence5);

        Sequence sequence2 = this.getAccompanimentSequence(sequence1, cello);
        track2.addSequence(sequence2);

        Sequence sequence3 = this.getAccompanimentSequence(sequence1, violin);
        track3.addSequence(sequence3);

        Sequence sequence6 = this.getAccompanimentSequence(sequence1, violin2);
        track6.addSequence(sequence6);

        Sequence sequence4 = this.getAccompanimentSequence(sequence1, contrabass);
        track4.addSequence(sequence4);

        piece.addTrack(track1);
        piece.addTrack(track5);
        piece.addTrack(track2);
        piece.addTrack(track3);
        piece.addTrack(track6);
        piece.addTrack(track4);

        return piece;
    }

    private Sequence getSequence(Instrument instrument) {
        MersenneTwister twister = new MersenneTwister(36345);
        
        double i = 0;

        Sequence sequence = new Sequence();

        int lengthRange = MAX_LENGTH - MIN_LENGTH;
        int patternLength = twister.nextInt(lengthRange);
        int targetLength = patternLength + MIN_LENGTH;
        double baseLength = instrument.DefaultLength;
        int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        int currentValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;
        int usedBaseNote = twister.nextInt(12);
        
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
            
            if (twister.nextInt(20) == 0) {
                System.out.println("Switched BaseNote");
                usedBaseNote = twister.nextInt(12);
            }
            
            double baseOffset = twister.nextInt((int)MAX_OFFSET + 1);
            double adjustedOffset = (baseOffset - MAX_OFFSET / 2) + 1;
            
            Note note = new Note(currentValue);
            note.Attack = twister.nextInt(60) + 40;
            note.BaseNote = usedBaseNote;
            note.IntendedAccomp = IntendedAccompaniment.values()[twister.nextInt(1)];
            note.Length = actualLength;

            double actualGrip = instrument.VariationGrip;
            if (actualLength < 0.3) {
                actualGrip = actualGrip / 2;
            }
            
            currentValue = note.addValue(adjustedOffset * actualGrip, instrument, true);

            i += actualLength;

            if (sequence.notes.size() > 0) {
                if (sequence.notes.get(sequence.notes.size() - 1).IsRest) {
                    note.IsRest = twister.nextBoolean();
                } else {
                    note.IsRest = twister.nextInt(5) == 0;
                }
            }
            
            sequence.addNote(note);
        }    
        
        System.out.println(String.format("Sequence note num: %s", sequence.notes.size()));
        return sequence;
    }

    private Sequence getAccompanimentSequence(Sequence master, Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        Sequence sequence = new Sequence();
        
        for (Note refNote : master.notes) {
            Note note = new Note(refNote.Value + instrument.ValueOffset);
            note.Attack = refNote.Attack + twister.nextInt(30) - 15;
            note.BaseNote = refNote.BaseNote;
            note.Length = refNote.Length;
            note.IntendedAccomp = refNote.IntendedAccomp;
            
            int rand = 0;
            int valueToAdd = 0;
            
            if (refNote.IntendedAccomp == IntendedAccompaniment.MAJOR) {
                twister.nextInt(2);
                switch (rand) {
                    case 0: valueToAdd = 4; break;
                    case 1: valueToAdd = 7; break;
                    default: break;
                }                
            }
            
            if (refNote.IntendedAccomp == IntendedAccompaniment.MINOR) {
                twister.nextInt(2);
                switch (rand) {
                    case 0: valueToAdd = 3; break;
                    case 1: valueToAdd = 7; break;
                    default: break;
                }                
            }

            note.addValue(valueToAdd, instrument, true);
            
            if (sequence.notes.size() > 0) {
                if (sequence.notes.get(sequence.notes.size() - 1).IsRest) {
                    note.IsRest = twister.nextBoolean();
                } else {
                    note.IsRest = twister.nextInt(5) == 0;
                }
            }
            
            sequence.addNote(note);
        }
        
        return sequence;
    }
}
