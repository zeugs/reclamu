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
        contrabass.MaxNoteIndex = 84;
        contrabass.Name = "Contrabass";
        contrabass.VariationGrip = 0.7;

        Piece piece = new Piece();

        Track track1 = this.getTrack(piano);

        Track track5 = this.getAccompanimentTrack(track1, piano_sec);
        Track track2 = this.getAccompanimentTrack(track1, cello);
        Track track3 = this.getAccompanimentTrack(track1, violin);
        Track track6 = this.getAccompanimentTrack(track1, violin2);
        Track track4 = this.getAccompanimentTrack(track1, contrabass);

        piece.addTrack(track1);
        piece.addTrack(track5);
        piece.addTrack(track2);
        piece.addTrack(track3);
        piece.addTrack(track6);
        piece.addTrack(track4);

        return piece;
    }

    private Track getTrack(Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        
        Track track = new Track();
        int numberOfSequences = twister.nextInt(20);
        for (int i = 0; i < numberOfSequences; i++) {
            Sequence sequence = getSequence(instrument);   
            int repetitions = twister.nextInt(10);
            System.out.println(String.format("Number of repetitions: %d", repetitions));
            for (int j = 0; j < repetitions; j++) {
                if (twister.nextInt(3) == 0) {
                    Sequence adaptedSequence = sequence.getCopy();
                    boolean transposeUp = twister.nextBoolean();
                    adaptedSequence.notes.forEach(note -> {
                        note.addValue(transposeUp ? 12 : -12, instrument, false);
                    });
                    track.sequences.add(adaptedSequence);
                } else {
                    track.sequences.add(sequence);
                }
            }
        }
                
        return track;
    }

    private Track getAccompanimentTrack(Track masterTrack, Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        Track track = new Track();
        
        for (Sequence refSequence: masterTrack.sequences) {
            Sequence sequence = new Sequence();
    
            boolean addRest = twister.nextInt(3) == 0;

            if (addRest) {
                for (int j = 0; j < refSequence.notes.size(); j++) {
                    Note noteCopy = refSequence.notes.get(j).getCopy();
                    noteCopy.IsRest = true;
                    sequence.notes.add(noteCopy);
                }    
                track.sequences.add(sequence);
                
                continue;
            }                
            
            for (int i = 0; i < refSequence.notes.size(); i++) {
                Note refNote = refSequence.notes.get(i);
                Note note = new Note(refNote.GetValue() + instrument.ValueOffset);
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
                        if (twister.nextInt(3) == 0) {
                            note.IsRest = false;
                        }
                    } else {
                        note.IsRest = twister.nextInt(6) == 0;
                    }                    
                }

                if (twister.nextInt(7) == 0) {
                    int skip = twister.nextInt(5) + 1;
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
        IntendedAccompaniment intendedAccomp = IntendedAccompaniment.values()[twister.nextInt(1)];
        
        double i = 0;

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
                    if (twister.nextInt(3) == 0) {
                        note.IsRest = false;
                    }
                } else {
                    note.IsRest = twister.nextInt(6) == 0;
                }
            }
            
            sequence.addNote(note);
        }    
        
        System.out.println(String.format("Sequence note num: %s", sequence.notes.size()));
        
        return sequence;
    }
}
