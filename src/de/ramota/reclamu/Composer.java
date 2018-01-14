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
        viola.VariationGrip = 0.7;

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
        flute.VariationGrip = 0.8;

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
        trombone.VariationGrip = 0.6;

        Instrument tuba = new Instrument();
        tuba.MinNoteIndex = 14;
        tuba.MaxNoteIndex = 53;
        tuba.Name = "Tuba";
        tuba.VariationGrip = 0.6;

        Instrument harp = new Instrument();
        harp.MinNoteIndex = 22;
        harp.MaxNoteIndex = 90;
        harp.Name = "Harp";
        harp.VariationGrip = 0.8;

        Instrument bells = new Instrument();
        bells.MinNoteIndex = 43;
        bells.MaxNoteIndex = 72;
        bells.Name = "Bells";
        bells.VariationGrip = 0.7;

        Piece piece = new Piece();

        Track track1 = piece.getTrack(piano);

        AddPlayGroup(viola, track1);
        AddPlayGroup(cello, track1);
        AddPlayGroup(violin, track1);
        AddPlayGroup(contrabass, track1);
        
        AddPlayGroup(flute, track1);
        AddPlayGroup(clarinet, track1);
        AddPlayGroup(oboe, track1);
        AddPlayGroup(bassoon, track1);
        AddPlayGroup(frenchHorn, track1);
        
        AddPlayGroup(trumpet, track1);
        AddPlayGroup(trombone, track1);
        AddPlayGroup(tuba, track1);
        
        AddPlayGroup(harp, track1);
        AddPlayGroup(bells, track1);

        SetSilencedInstrumentsInSequences(track1);

        piece.AddAccompTrack(track1, piano_sec, 1, 0);

        piece.AddAccompTrack(track1, viola, 12, 6);
        piece.AddAccompTrack(track1, cello, 10, 5);
        piece.AddAccompTrack(track1, violin, 16, 8);
        piece.AddAccompTrack(track1, violin, 14, 7);
        piece.AddAccompTrack(track1, contrabass, 8, 4);
        
        piece.AddAccompTrack(track1, flute, 3, 2);
        piece.AddAccompTrack(track1, clarinet, 3, 1);
        piece.AddAccompTrack(track1, oboe, 3, 1);
        piece.AddAccompTrack(track1, bassoon, 3, 1);
        piece.AddAccompTrack(track1, frenchHorn, 6, 3);

        piece.AddAccompTrack(track1, trumpet, 5, 2);
        piece.AddAccompTrack(track1, trombone, 5, 2);
        piece.AddAccompTrack(track1, tuba, 2, 1);        
        piece.AddAccompTrack(track1, harp, 2, 0);
        piece.AddAccompTrack(track1, bells, 1, 0);

        return piece;
    }

    private void AddPlayGroup(Instrument viola, Track track1) {
        PlayGroup playGroup = new PlayGroup();
        playGroup.AddInstrument(viola);
        track1.PlayGroups.add(playGroup);
    }

    private void SetSilencedInstrumentsInSequences(Track track1) {
        MersenneTwister twister = new MersenneTwister();
        track1.Sequences.forEach((sequence) -> {
            track1.PlayGroups.forEach((group) -> {
                boolean insertSilence = twister.nextInt(7) == 0;
                if (insertSilence) {
                    sequence.SilencedGroups.add(group);
                }
            });
        });
    }}
