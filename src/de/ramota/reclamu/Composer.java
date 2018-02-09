package de.ramota.reclamu;

import de.ramota.reclamu.composers.MaeanderAccompanimentComposer;
import de.ramota.reclamu.composers.FreeFormTrackComposer;
import de.ramota.reclamu.composers.PlainAccompanimentComposer;
import de.ramota.reclamu.composers.PlainTrackComposer;
import org.apache.commons.math3.random.MersenneTwister;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Composer {
    public static int MIN_SEQUENCE_LENGTH =  100;
    public static int MAX_SEQUENCE_LENGTH = 10000;
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
        piano.VariationGrip = 0.8;
        
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

        Instrument drums = new Instrument();
        drums.MinNoteIndex = 24;
        drums.MaxNoteIndex = 53;
        drums.Name = "Drums";
        drums.VariationGrip = 0.9;

        Instrument drums2 = new Instrument();
        drums2.MinNoteIndex = 24;
        drums2.MaxNoteIndex = 53;
        drums2.Name = "Drums";
        drums2.VariationGrip = 0.9;

        List<ScaleItem> intendedScaleItems = new ArrayList<>();
        AccompanimentItem accompItem;

        MajorScaleAccompaniment simpleAccomp = new MajorScaleAccompaniment();
        // I
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        accompItem.Weight = 25;
        simpleAccomp.addAccompanimentItem(accompItem);
        // maj6
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(9);
        accompItem.Weight = 10;
        simpleAccomp.addAccompanimentItem(accompItem);
        // maj7
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(11);
        accompItem.Weight = 10;
        simpleAccomp.addAccompanimentItem(accompItem);
        // maj9
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(11);
        accompItem.Offsets.add(14);
        accompItem.Weight = 10;
        simpleAccomp.addAccompanimentItem(accompItem);
        // IV
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(5);
        accompItem.Offsets.add(9);
        accompItem.Offsets.add(12);
        accompItem.Weight = 30;
        simpleAccomp.addAccompanimentItem(accompItem);
        // V
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(11);
        accompItem.Offsets.add(14);
        accompItem.Weight = 30;
        simpleAccomp.addAccompanimentItem(accompItem);
        // VI
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(9);
        accompItem.Offsets.add(13);
        accompItem.Offsets.add(16);
        accompItem.Weight = 10;
        simpleAccomp.addAccompanimentItem(accompItem);

        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(4);
        accompItem.Weight = 8;
        simpleAccomp.addAccompanimentItem(accompItem);

        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(7);
        accompItem.Weight = 8;
        simpleAccomp.addAccompanimentItem(accompItem);

        intendedScaleItems.add(simpleAccomp);

        // contains i, iv, v, vi, ii
        MinorScaleAccompaniment simpleAccomp2 = new MinorScaleAccompaniment();
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(3);
        accompItem.Offsets.add(7);
        accompItem.Weight = 10;
        simpleAccomp2.addAccompanimentItem(accompItem);
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(5);
        accompItem.Offsets.add(8);
        accompItem.Offsets.add(12);
        accompItem.Weight = 20;
        simpleAccomp2.addAccompanimentItem(accompItem);
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(10);
        accompItem.Offsets.add(14);
        accompItem.Weight = 10;
        simpleAccomp2.addAccompanimentItem(accompItem);
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(8);
        accompItem.Offsets.add(12);
        accompItem.Offsets.add(16);
        accompItem.Weight = 30;
        simpleAccomp2.addAccompanimentItem(accompItem);
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(2);
        accompItem.Offsets.add(5);
        accompItem.Offsets.add(9);
        accompItem.Weight = 30;
        simpleAccomp2.addAccompanimentItem(accompItem);
        intendedScaleItems.add(simpleAccomp2);
        
        Piece piece = new Piece();

        //Track track1 = this.getFreeFormTrack(piece, piano, intendedScaleItems);
        Track track1 = this.getPlainTrack(piece, piano, intendedScaleItems);

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

        /*AddPlayGroup(drums, track1);
        AddPlayGroup(drums2, track1);*/

        PlainAccompanimentComposer composer = new PlainAccompanimentComposer(piece);
        //MaeanderAccompanimentComposer composer = new MaeanderAccompanimentComposer(piece);

        composer.generateTrack(track1, piano_sec, 1, 0);

        composer.generateTrack(track1, viola, 12, 6);
        composer.generateTrack(track1, cello, 10, 5);
        composer.generateTrack(track1, violin, 16, 8);
        composer.generateTrack(track1, violin, 14, 7);
        composer.generateTrack(track1, contrabass, 8, 4);
        
        composer.generateTrack(track1, flute, 3, 1);
        composer.generateTrack(track1, clarinet, 3, 1);
        composer.generateTrack(track1, oboe, 3, 1);
        composer.generateTrack(track1, bassoon, 3, 1);
        composer.generateTrack(track1, frenchHorn, 6, 3);

        composer.generateTrack(track1, trumpet, 5, 2);
        composer.generateTrack(track1, trombone, 5, 2);
        composer.generateTrack(track1, tuba, 2, 1);        
        composer.generateTrack(track1, harp, 2, 0);
        composer.generateTrack(track1, bells, 1, 0);

        /*piece.AddAccompTrack(track1, drums, 1, 0);
        piece.AddAccompTrack(track1, drums2, 1, 0);*/

        return piece;
    }

    public Track getFreeFormTrack(Piece piece, Instrument instrument, List<ScaleItem> intendedScaleItems) {
        MersenneTwister twister = new MersenneTwister();
        
        FreeFormTrackComposer trackComposer = new FreeFormTrackComposer(instrument, intendedScaleItems);
        trackComposer.initialize();
        int numberOfSequences = twister.nextInt(40) + 20;
        Track track = trackComposer.generateTrack(numberOfSequences);
        piece.Tracks.add(track);
        
        return track;
    }
    
    public Track getPlainTrack(Piece piece, Instrument instrument, List<ScaleItem> intendedScaleItems) {
        MersenneTwister twister = new MersenneTwister();
        
        PlainTrackComposer trackComposer = new PlainTrackComposer(instrument, intendedScaleItems);
        trackComposer.initialize();
        int numberOfSequences = twister.nextInt(40) + 10;
        Track track = trackComposer.generateTrack(numberOfSequences);
        piece.Tracks.add(track);
        
        return track;
    }    
    
    private void AddPlayGroup(Instrument viola, Track track1) {
        PlayGroup playGroup = new PlayGroup();
        playGroup.AddInstrument(viola);
        track1.PlayGroups.add(playGroup);
    }
}
