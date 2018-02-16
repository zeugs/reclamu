package de.ramota.reclamu;

import de.ramota.reclamu.composers.MidiDataEnhancer;
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
        List<AbstractTrack> tracks = getPatterns().Tracks;
        int counter = 0;

        for (AbstractTrack track : tracks) {
            Pattern p1 = new Pattern(track.toString());
            System.out.println(track);

            try {
                MidiFileManager.savePatternToMidi(p1, new File("test" + String.valueOf(counter++) + ".mid"));
            } catch (IOException e) {
            }
        }
    }

    private Piece getPatterns() {
        Piece piece = GenerateFullOrchestraPiece();
        //Piece piece = GenerateChamberOrchestra();
        
        return piece;
    }

    private Piece GenerateChamberOrchestra() {
        Instrument piano = new Instrument(24, 108, "Piano", 0.8);
        Instrument piano_sec = new Instrument(24, 108, "Piano_Sec", 0.8);
        Instrument viola = new Instrument(36, 76, "Viola", 0.7);
        Instrument cello = new Instrument(24, 96, "Cello", 0.8);
        Instrument violin = new Instrument(45, 96, "Violin", 0.8);
        Instrument contrabass = new Instrument(24, 60, "Contrabass", 0.7);
        Instrument flute = new Instrument(48, 86, "Flute", 0.8);
        Instrument clarinet = new Instrument(40, 84, "Clarinet", 0.7);
        Instrument bassoon = new Instrument(22, 64, "Bassoon", 0.7);
        Instrument oboe = new Instrument(46, 81, "Oboe", 0.7);
        //Instrument englishHorn = new Instrument(47, 79, "English Horn", 0.7);
        Instrument frenchHorn = new Instrument(29, 72, "French Horn", 0.7);
        Instrument trumpet = new Instrument(42, 74, "Trumpet", 0.7);
        Instrument trombone = new Instrument(33, 67, "Oboe", 0.6);
        Instrument tuba = new Instrument(14, 53, "Tuba", 0.6);
        Instrument harp = new Instrument(22, 90, "Harp", 0.8);
        Instrument bells = new Instrument(43, 72, "Bells", 0.7);
        //Instrument drums = new Instrument(24, 53, "Drums", 0.9);
        //Instrument drums2 = new Instrument(24, 53, "Drums2", 0.9);

        List<ScaleItem> intendedScaleItems = GetStandardAccompItems();
        
        Piece piece = new Piece();

        AbstractTrack track1 = this.getFreeFormTrack(piece, piano, intendedScaleItems);

        /*AddPlayGroup(drums, track1);
        AddPlayGroup(drums2, track1);*/

        PlainAccompanimentComposer composer = new PlainAccompanimentComposer(piece);
        //MaeanderAccompanimentComposer composer = new MaeanderAccompanimentComposer(piece);

        composer.generateTrack(track1, piano_sec, 1, 0);

        composer.generateTrack(track1, viola, 4, 2);
        composer.generateTrack(track1, cello, 4, 2);
        composer.generateTrack(track1, violin, 4, 2);
        composer.generateTrack(track1, violin, 4, 2);
        composer.generateTrack(track1, contrabass, 6, 3);
        
        composer.generateTrack(track1, flute, 2, 1);
        composer.generateTrack(track1, clarinet, 2, 1);
        composer.generateTrack(track1, oboe, 2, 1);
        composer.generateTrack(track1, bassoon, 2, 1);
        composer.generateTrack(track1, frenchHorn, 3, 1);

        composer.generateTrack(track1, trumpet, 3, 1);
        composer.generateTrack(track1, trombone, 3, 1);
        composer.generateTrack(track1, tuba, 2, 1);        

        /*piece.AddAccompTrack(track1, drums, 1, 0);
        piece.AddAccompTrack(track1, drums2, 1, 0);*/

        return piece;                
    }
    
    private Piece GenerateFullOrchestraPiece() {
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
        Instrument piano = new Instrument(24, 108, "Piano", 0.8);
        Instrument piano_sec = new Instrument(24, 108, "Piano_Sec", 0.8);
        Instrument viola = new Instrument(36, 76, "Viola", 0.7);
        Instrument cello = new Instrument(24, 96, "Cello", 0.8);
        Instrument violin = new Instrument(45, 96, "Violin", 0.8);
        Instrument contrabass = new Instrument(24, 60, "Contrabass", 0.7);
        Instrument flute = new Instrument(48, 86, "Flute", 0.8);
        Instrument clarinet = new Instrument(40, 84, "Clarinet", 0.7);
        Instrument bassoon = new Instrument(22, 64, "Bassoon", 0.7);
        Instrument oboe = new Instrument(46, 81, "Oboe", 0.7);
        //Instrument englishHorn = new Instrument(47, 79, "English Horn", 0.7);
        Instrument frenchHorn = new Instrument(29, 72, "French Horn", 0.7);
        Instrument trumpet = new Instrument(42, 74, "Trumpet", 0.7);
        Instrument trombone = new Instrument(33, 67, "Oboe", 0.6);
        Instrument tuba = new Instrument(14, 53, "Tuba", 0.6);
        Instrument harp = new Instrument(22, 90, "Harp", 0.8);
        Instrument bells = new Instrument(43, 72, "Bells", 0.7);
        //Instrument drums = new Instrument(24, 53, "Drums", 0.9);
        //Instrument drums2 = new Instrument(24, 53, "Drums2", 0.9);

        List<ScaleItem> intendedScaleItems = GetStandardAccompItems();
        
        Piece piece = new Piece();

        AbstractTrack track1 = this.getPlainTrack(piece, piano, intendedScaleItems);
        //Track track1 = this.getFreeFormTrack(piece, piano, intendedScaleItems);
        //AbstractTrack track1 = this.getTrackFromFile(piece, piano, intendedScaleItems.get(0), "jingle_bells.mid", 2);

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
        
        return piece;
    }
    
    private Piece GenerateFullOrchestraMidiPiece() {
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
        Instrument piano = new Instrument(24, 108, "Piano", 0.8);
        Instrument piano_sec = new Instrument(24, 108, "Piano_Sec", 0.8);
        Instrument viola = new Instrument(36, 76, "Viola", 0.7);
        Instrument cello = new Instrument(24, 96, "Cello", 0.8);
        Instrument violin = new Instrument(45, 96, "Violin", 0.8);
        Instrument contrabass = new Instrument(24, 60, "Contrabass", 0.7);
        Instrument flute = new Instrument(48, 86, "Flute", 0.8);
        Instrument clarinet = new Instrument(40, 84, "Clarinet", 0.7);
        Instrument bassoon = new Instrument(22, 64, "Bassoon", 0.7);
        Instrument oboe = new Instrument(46, 81, "Oboe", 0.7);
        //Instrument englishHorn = new Instrument(47, 79, "English Horn", 0.7);
        Instrument frenchHorn = new Instrument(29, 72, "French Horn", 0.7);
        Instrument trumpet = new Instrument(42, 74, "Trumpet", 0.7);
        Instrument trombone = new Instrument(33, 67, "Oboe", 0.6);
        Instrument tuba = new Instrument(14, 53, "Tuba", 0.6);
        Instrument harp = new Instrument(22, 90, "Harp", 0.8);
        Instrument bells = new Instrument(43, 72, "Bells", 0.7);
        //Instrument drums = new Instrument(24, 53, "Drums", 0.9);
        //Instrument drums2 = new Instrument(24, 53, "Drums2", 0.9);

        List<ScaleItem> intendedScaleItems = GetStandardAccompItems();
        
        Piece piece = new Piece();

        //Track track1 = this.getPlainTrack(piece, piano, intendedScaleItems);
        //Track track1 = this.getFreeFormTrack(piece, piano, intendedScaleItems);
        ScaleItem interpretationItem = intendedScaleItems.get(1);
        
        AbstractTrack track1 = this.getTrackFromFile(piece, piano, interpretationItem, "sho8_2.mid", 0);
        AbstractTrack track2 = this.getTrackFromFile(piece, piano, interpretationItem, "sho8_2.mid", 1);
        AbstractTrack track3 = this.getTrackFromFile(piece, piano, interpretationItem, "sho8_2.mid", 2);
        AbstractTrack track4 = this.getTrackFromFile(piece, piano, interpretationItem, "sho8_2.mid", 3);

        /*AddPlayGroup(drums, track1);
        AddPlayGroup(drums2, track1);*/

        PlainAccompanimentComposer composer = new PlainAccompanimentComposer(piece);
        //MaeanderAccompanimentComposer composer = new MaeanderAccompanimentComposer(piece);

        composer.generateTrack(track1, piano_sec, 1, 0);

        composer.generateTrack(track1, viola, 3, 1);
        composer.generateTrack(track2, viola, 3, 1);
        composer.generateTrack(track3, viola, 3, 1);
        composer.generateTrack(track4, viola, 3, 1);

        composer.generateTrack(track1, cello, 2, 1);
        composer.generateTrack(track2, cello, 2, 1);
        composer.generateTrack(track3, cello, 3, 2);
        composer.generateTrack(track4, cello, 3, 2);

        composer.generateTrack(track1, violin, 4, 2);
        composer.generateTrack(track2, violin, 4, 2);
        composer.generateTrack(track3, violin, 4, 2);
        composer.generateTrack(track4, violin, 4, 2);

        composer.generateTrack(track1, violin, 4, 2);
        composer.generateTrack(track1, violin, 4, 2);
        composer.generateTrack(track1, violin, 3, 1);
        composer.generateTrack(track1, violin, 3, 1);

        composer.generateTrack(track1, contrabass, 2, 1);
        composer.generateTrack(track1, contrabass, 2, 1);
        composer.generateTrack(track1, contrabass, 2, 1);
        composer.generateTrack(track1, contrabass, 2, 1);
        
        composer.generateTrack(track1, flute, 3, 1);
        composer.generateTrack(track2, clarinet, 3, 1);
        composer.generateTrack(track3, oboe, 3, 1);
        composer.generateTrack(track4, bassoon, 3, 1);
        composer.generateTrack(track1, frenchHorn, 6, 3);

        composer.generateTrack(track1, trumpet, 5, 2);
        composer.generateTrack(track2, trombone, 5, 2);
        composer.generateTrack(track3, tuba, 2, 1);        
        composer.generateTrack(track4, harp, 2, 0);
        composer.generateTrack(track1, bells, 1, 0);
        
        /*composer.generateTrack(track1, piano_sec, 1, 0);

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
        composer.generateTrack(track1, bells, 1, 0);*/

        /*piece.AddAccompTrack(track1, drums, 1, 0);
        piece.AddAccompTrack(track1, drums2, 1, 0);*/

        return piece;        
    }

    protected List<ScaleItem> GetStandardAccompItems() {
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
        
        return intendedScaleItems;
    }

    public AbstractTrack getFreeFormTrack(Piece piece, Instrument instrument, List<ScaleItem> intendedScaleItems) {
        MersenneTwister twister = new MersenneTwister();
        
        FreeFormTrackComposer trackComposer = new FreeFormTrackComposer(instrument, intendedScaleItems);
        trackComposer.initialize(intendedScaleItems);
        int numberOfSequences = twister.nextInt(240) + 150;
        AbstractTrack track = trackComposer.generateTrack(numberOfSequences);
        piece.Tracks.add(track);
        
        return track;
    }
    
    public AbstractTrack getPlainTrack(Piece piece, Instrument instrument, List<ScaleItem> intendedScaleItems) {
        MersenneTwister twister = new MersenneTwister();
        
        PlainTrackComposer trackComposer = new PlainTrackComposer(instrument, intendedScaleItems);
        trackComposer.initialize(intendedScaleItems);
        int numberOfSequences = twister.nextInt(10) + 10;
        AbstractTrack track = trackComposer.generateTrack(numberOfSequences);
        piece.Tracks.add(track);
        
        return track;
    }    

    private AbstractTrack getTrackFromFile(Piece piece, Instrument instrument, ScaleItem interpretationItem, String fileName, int midiTrack) {
        
        MidiDataEnhancer enhancer = new MidiDataEnhancer(instrument, interpretationItem);
        enhancer.setFileName(fileName);
        enhancer.setMidiTrack(midiTrack);
        enhancer.setScaleItem(interpretationItem);
        AbstractTrack track = enhancer.generateTrack(1);
        piece.Tracks.add(track);
        
        return track;
    }
}