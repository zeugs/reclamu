package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Composer {
    public static int MIN_SEQUENCE_LENGTH =  100;
    public static int MAX_SEQUENCE_LENGTH = 20000;
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
        /* https://en.wikipedia.org/wiki/Orchestra#Expanded_instrumentation
        
            Woodwinds
                2–4 flutes (1 doubling piccolo)
                2–4 oboes (1 doubling cor anglais)
                2–4 clarinets (1–2 doubling bass clarinet and/or E♭ Clarinet)
                2–4 bassoons (1 doubling contrabassoon)
                (1 or more saxophones of various types)

            Brass
                4–8 German (usually double) horns in F/B♭ (in France: French horns; in Vienna: Vienna horns)
                3–6 trumpets in B♭, C
                3–6 trombones (1–2 bass trombones)
                1–2 tubas
                (1 or more baritone horns/euphoniums)
                (1 or more Wagner tubas)

            Keyboards
                1 piano
                1 celesta
                (pipe organ in some works)

            Strings
                16 violins 1
                14 violins 2
                12 violas
                10 cellos
                8 double basses
                1–2 harps 
        */
        Instrument piano = new Instrument();
        piano.MinNoteIndex = 24;
        piano.MaxNoteIndex = 108;
        piano.Name = "Piano";
        piano.VariationGrip = 0.7;
        
        Instrument piano_sec = new Instrument();
        piano_sec.MinNoteIndex = 24;
        piano_sec.MaxNoteIndex = 108;
        piano_sec.Name = "Piano_Sec";
        piano_sec.VariationGrip = 0.8;

        Instrument viola = new Instrument();
        viola.MinNoteIndex = 36;
        viola.MaxNoteIndex = 76;
        viola.Name = "Viola";
        viola.VariationGrip = 0.8;

        Instrument cello = new Instrument();
        cello.MinNoteIndex = 24;
        cello.MaxNoteIndex = 96;
        cello.Name = "Cello";
        cello.VariationGrip = 0.8;

        Instrument violin = new Instrument();
        violin.MinNoteIndex = 45;
        violin.MaxNoteIndex = 96;
        violin.Name = "Violin";
        violin.VariationGrip = 0.8;

        Instrument contrabass = new Instrument();
        contrabass.MinNoteIndex = 24;
        contrabass.MaxNoteIndex = 60;
        contrabass.Name = "Contrabass";
        contrabass.VariationGrip = 0.7;

        Instrument flute = new Instrument();
        flute.MinNoteIndex = 48;
        flute.MaxNoteIndex = 86;
        flute.Name = "Flute";
        flute.VariationGrip = 0.7;

        Instrument clarinet = new Instrument();
        clarinet.MinNoteIndex = 40;
        clarinet.MaxNoteIndex = 84;
        clarinet.Name = "Clarinet";
        clarinet.VariationGrip = 0.7;

        Instrument bassoon = new Instrument();
        bassoon.MinNoteIndex = 22;
        bassoon.MaxNoteIndex = 64;
        bassoon.Name = "Bassoon";
        bassoon.VariationGrip = 0.7;

        Instrument oboe = new Instrument();
        oboe.MinNoteIndex = 46;
        oboe.MaxNoteIndex = 81;
        oboe.Name = "Oboe";
        oboe.VariationGrip = 0.7;

        Instrument englishHorn = new Instrument();
        englishHorn.MinNoteIndex = 47;
        englishHorn.MaxNoteIndex = 79;
        englishHorn.Name = "English Horn";
        englishHorn.VariationGrip = 0.7;

        Instrument frenchHorn = new Instrument();
        frenchHorn.MinNoteIndex = 29;
        frenchHorn.MaxNoteIndex = 72;
        frenchHorn.Name = "French Horn";
        frenchHorn.VariationGrip = 0.7;

        Instrument trumpet = new Instrument();
        trumpet.MinNoteIndex = 42;
        trumpet.MaxNoteIndex = 74;
        trumpet.Name = "Trumpet";
        trumpet.VariationGrip = 0.7;

        Instrument trombone = new Instrument();
        trombone.MinNoteIndex = 33;
        trombone.MaxNoteIndex = 67;
        trombone.Name = "Oboe";
        trombone.VariationGrip = 0.7;

        Instrument tuba = new Instrument();
        tuba.MinNoteIndex = 14;
        tuba.MaxNoteIndex = 53;
        tuba.Name = "Tuba";
        tuba.VariationGrip = 0.7;

        Piece piece = new Piece();

        Track track1 = this.getTrack(piano);

        PlayGroup playGroup = new PlayGroup();
        playGroup.AddInstrument(viola);
        track1.PlayGroups.add(playGroup);
        
        playGroup = new PlayGroup();
        playGroup.AddInstrument(cello);
        track1.PlayGroups.add(playGroup);

        playGroup = new PlayGroup();
        playGroup.AddInstrument(violin);
        track1.PlayGroups.add(playGroup);

        playGroup = new PlayGroup();
        playGroup.AddInstrument(contrabass);
        track1.PlayGroups.add(playGroup);
        
        playGroup = new PlayGroup();
        playGroup.AddInstrument(flute);
        track1.PlayGroups.add(playGroup);

        playGroup = new PlayGroup();
        playGroup.AddInstrument(clarinet);
        track1.PlayGroups.add(playGroup);

        playGroup = new PlayGroup();
        playGroup.AddInstrument(oboe);
        track1.PlayGroups.add(playGroup);

        playGroup = new PlayGroup();
        playGroup.AddInstrument(bassoon);
        track1.PlayGroups.add(playGroup);
        
        playGroup = new PlayGroup();
        playGroup.AddInstrument(frenchHorn);
        track1.PlayGroups.add(playGroup);

        playGroup = new PlayGroup();
        playGroup.AddInstrument(trumpet);
        track1.PlayGroups.add(playGroup);

        playGroup = new PlayGroup();
        playGroup.AddInstrument(trombone);
        track1.PlayGroups.add(playGroup);

        playGroup = new PlayGroup();
        playGroup.AddInstrument(tuba);
        track1.PlayGroups.add(playGroup);
        
        MersenneTwister twister = new MersenneTwister();
        track1.Sequences.forEach((sequence) -> {
            track1.PlayGroups.forEach((group) -> {
                boolean insertSilence = twister.nextInt(5) == 0;
                if (insertSilence) {
                    sequence.SilencedGroups.add(group);
                }
            });
        });

        Track track2 = this.getAccompanimentTrack(track1, piano_sec);

        piece.addTrack(track1);
        piece.addTrack(track2);
        
        for (int i = 0; i < 12; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, viola);
            piece.addTrack(accompTrack);
        }

        for (int i = 0; i < 10; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, cello);
            piece.addTrack(accompTrack);
        }

        for (int i = 0; i < 16; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, violin);
            piece.addTrack(accompTrack);
        }
        
        for (int i = 0; i < 14; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, violin);
            piece.addTrack(accompTrack);
        }

        for (int i = 0; i < 8; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, contrabass);
            piece.addTrack(accompTrack);
        }

        for (int i = 0; i < 3; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, flute);
            piece.addTrack(accompTrack);
        }

        for (int i = 0; i < 3; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, clarinet);
            piece.addTrack(accompTrack);
        }

        for (int i = 0; i < 3; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, oboe);
            piece.addTrack(accompTrack);
        }

        for (int i = 0; i < 3; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, bassoon);
            piece.addTrack(accompTrack);
        }

        for (int i = 0; i < 6; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, frenchHorn);
            piece.addTrack(accompTrack);
        }
        
        for (int i = 0; i < 5; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, trumpet);
            piece.addTrack(accompTrack);
        }

        for (int i = 0; i < 5; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, trombone);
            piece.addTrack(accompTrack);
        }

        for (int i = 0; i < 2; i++) {
            Track accompTrack = this.getAccompanimentTrack(track1, tuba);
            piece.addTrack(accompTrack);
        }

        return piece;
    }

    private Track getTrack(Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        
        Track track = new Track();
        int numberOfSequences = twister.nextInt(20);
        
        Sequence adaptedSequence;
        Sequence sequenceToAdd;
        for (int i = 0; i < numberOfSequences; i++) {
            Sequence sequence = getSequence(instrument);   
            
            int repetitions = twister.nextInt(10);
            System.out.println(String.format("Number of repetitions: %d", repetitions));
            for (int j = 0; j < repetitions; j++) {
                if (twister.nextInt(5) == 0) {
                    adaptedSequence = sequence.getCopy();
                    boolean transposeUp = twister.nextBoolean();
                    adaptedSequence.notes.forEach(note -> {
                        note.addValue(transposeUp ? 12 : -12, instrument, false);
                    });
                    track.Sequences.add(adaptedSequence);
                    sequenceToAdd = adaptedSequence;
                } else {
                    sequenceToAdd = sequence;
                }
                
                if (twister.nextInt(7) == 0) {
                    sequenceToAdd.notes.forEach(note -> {
                        note.IsRest = true;
                    });                    
                }
                track.Sequences.add(sequenceToAdd);
            }
        }
                
        return track;
    }

    private Track getAccompanimentTrack(Track masterTrack, Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        Track track = new Track();
        int restRange = twister.nextInt(7) + 1;

        for (Sequence refSequence: masterTrack.Sequences) {
            Sequence sequence = new Sequence();
    
            boolean addRest = (twister.nextInt(restRange) == 0);
            boolean silence = refSequence.SilencedGroups.contains(instrument.Group);
            
            if (addRest || silence) {
                for (int j = 0; j < refSequence.notes.size(); j++) {
                    Note noteCopy = refSequence.notes.get(j).getCopy();
                    noteCopy.IsRest = true;
                    sequence.notes.add(noteCopy);
                }    
                track.Sequences.add(sequence);
                
                continue;
            }                
            
            int attackRange = twister.nextInt(90) + 1;
            int restDelayRange = twister.nextInt(7) + 1;
            int restStartRange = twister.nextInt(12) + 1;
            int noteLengthRange = twister.nextInt(12) + 1;
            int noteSkipRange = twister.nextInt(8) + 1;
            int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
            int sequenceOffset = twister.nextInt(instrumentRange) - instrumentRange / 2;
            sequenceOffset = sequenceOffset - sequenceOffset % 12;
                    
            for (int i = 0; i < refSequence.notes.size(); i++) {
                int delayLength = twister.nextInt(15) + 5;
                Note refNote = refSequence.notes.get(i);
                Note note = new Note(refNote.GetValue() + sequenceOffset + refNote.BaseNote);
                note.Attack = twister.nextInt(attackRange) + 30;
                note.BaseNote = refNote.BaseNote;
                note.SetLength(refNote.GetLength() - delayLength, false);
                note.IntendedAccomp = refNote.IntendedAccomp;

                if (delayLength > 0) {
                    Note delayPseudoNote = new Note(70);
                    delayPseudoNote.IsRest = true;
                    delayPseudoNote.SetLength(delayLength, false);
                    sequence.addNote(delayPseudoNote);     
                }
                
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

                if (twister.nextInt(noteLengthRange) == 0) {
                    int skip = twister.nextInt(noteSkipRange) + 1;
                    int startPos = i + 1;

                    for (int j = startPos; j < startPos + skip; j++) {
                        if (j < refSequence.notes.size()) {
                            note.SetLength(note.GetLength() + refSequence.notes.get(j).GetLength(), false);
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                
                sequence.addNote(note);                
            }
            
            track.Sequences.add(sequence);
        }
        
        return track;
    }

    private Sequence getSequence(Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        Sequence sequence = new Sequence();

        int lengthRange = MAX_SEQUENCE_LENGTH - MIN_SEQUENCE_LENGTH;
        int patternLength = twister.nextInt(lengthRange);
        int targetLength = patternLength + MIN_SEQUENCE_LENGTH;
        int baseLength = instrument.DefaultLength;
        int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        int currentValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;
        int usedBaseNote = twister.nextInt(12);
        double maxOffset = twister.nextInt(14) + 2;
        int changeBaseNoteRange = twister.nextInt(20) + 5;
        
        IntendedAccompaniment intendedAccomp = IntendedAccompaniment.values()[twister.nextInt(1)];

        int i = 0;
        int attackRange = twister.nextInt(90) + 1;
        int changeAccompRange = twister.nextInt(10) + 1;
        int restDelayRange = twister.nextInt(7) + 1;
        int restStartRange = twister.nextInt(12) + 1;

        while (i < targetLength) {
            boolean adaptLength = twister.nextInt(8) == 0;
            boolean switchLength = twister.nextInt(30) == 0;
            int actualLength = baseLength;
                        
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
                    baseLength = Note.MAX_LENGTH - twister.nextInt(Note.MAX_LENGTH) / 2;
                }
            }
            
            if (switchLength) {
                baseLength = twister.nextInt(Note.MAX_LENGTH) * Note.MAX_LENGTH / 2 + Note.MIN_LENGTH;
                actualLength = baseLength;
            }
            
            if (twister.nextInt(changeBaseNoteRange) == 0) {
                usedBaseNote = twister.nextInt(12);
            }
            
            double baseOffset = twister.nextInt((int)maxOffset + 1);
            double adjustedOffset = (baseOffset - maxOffset / 2) + 1;
            
            Note note = new Note(currentValue);
            note.Attack = twister.nextInt(attackRange) + 30;
            note.BaseNote = usedBaseNote;
            actualLength = note.SetLength(actualLength, true);

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
