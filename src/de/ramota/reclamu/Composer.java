package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Composer {
    public static int MIN_LENGTH = 200;
    public static int MAX_LENGTH = 1500;
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
        int patternRange = twister.nextInt(20) + 20;
        IntendedAccompaniment intendedAccomp = IntendedAccompaniment.values()[twister.nextInt(1)];
        
        while (i < targetLength) {
            boolean adaptLength = (twister.nextInt(8) == 0);
            boolean switchLength = (twister.nextInt(30) == 0);
            double actualLength = baseLength;
            
            if (twister.nextInt(patternRange) == 0 && sequence.notes.size() > 0) {
                System.out.println("Pattern!");
                int patternStart = twister.nextInt(sequence.notes.size());
                int patternOffset = twister.nextInt(50) + 30;
                
                if (patternStart + patternOffset > sequence.notes.size() - 1) {
                    patternOffset = sequence.notes.size() - patternStart - 1;
                }
                
                for (int j = patternStart; j < patternStart + patternOffset; j++) {
                    Note refNote = sequence.notes.get(j);
                    Note note = new Note(refNote.Value);
                    note.Attack = refNote.Attack;
                    note.BaseNote = refNote.BaseNote;
                    note.IntendedAccomp = refNote.IntendedAccomp;
                    note.IsRest = refNote.IsRest;
                    note.Length = refNote.Length;
                    
                    sequence.notes.add(note);
                    i += note.Length;
                }
            }
            
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
                usedBaseNote = twister.nextInt(12);
                actualLength = baseLength;
            }
            
            double baseOffset = twister.nextInt((int)MAX_OFFSET + 1);
            double adjustedOffset = (baseOffset - MAX_OFFSET / 2) + 1;
            
            Note note = new Note(currentValue);
            note.Attack = twister.nextInt(70) + 40;
            note.BaseNote = usedBaseNote;
            note.Length = actualLength;

            if (twister.nextInt(5) == 0) {
                intendedAccomp = IntendedAccompaniment.values()[twister.nextInt(1)];
            }
            
            note.IntendedAccomp = intendedAccomp;
            
            double actualGrip = instrument.VariationGrip;
            if (actualLength < 0.3) {
                actualGrip = actualGrip / 2;
            }
            
            currentValue = note.addValue(adjustedOffset * actualGrip, instrument, true);

            i += actualLength;

            if (sequence.notes.size() > 0) {
                if (sequence.notes.get(sequence.notes.size() - 1).IsRest) {
                    note.IsRest = true;
                    if (twister.nextInt(5) == 0) {
                        note.IsRest = false;
                    }
                } else {
                    note.IsRest = twister.nextInt(4) == 0;
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
        
        for (int i = 0; i < master.notes.size(); i++) {
            Note refNote = master.notes.get(i);
            Note note = new Note(refNote.Value + instrument.ValueOffset);
            note.Attack = twister.nextInt(50) + 40;
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
                    if (twister.nextInt(5) == 0) {
                        note.IsRest = false;
                    }
                } else {
                    note.IsRest = twister.nextInt(3) == 0;
                }
            }
            
            if (twister.nextInt(7) == 0) {
                int skip = twister.nextInt(5) + 1;
                int startPos = i + 1;
                
                for (int j = startPos; j < startPos + skip; j++) {
                    if (j < master.notes.size()) {
                        note.Length += master.notes.get(j).Length;
                        i++;
                    } else {
                        break;
                    }
                }
            }
            sequence.addNote(note);
        }
        
        return sequence;
    }
}
