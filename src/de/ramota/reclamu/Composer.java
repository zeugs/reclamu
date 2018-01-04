package de.ramota.reclamu;

import org.apache.commons.math3.random.MersenneTwister;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Composer {
    public static int MIN_LENGTH = 4 * 64;
    public static int MAX_LENGTH = 16 * 64;
    public static double MAX_OFFSET = 14;

    public ChromaticNoteMapping chromaticMapping = new ChromaticNoteMapping();
    public PlainNoteMapping plainMapping = new PlainNoteMapping();
    
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
        piano.MinOctave = 2;
        piano.MaxOctave = 7;
        piano.Name = "Piano";
        piano.VariationGrip = 0.7;
        
        Instrument piano_sec = new Instrument();
        piano_sec.MinOctave = 2;
        piano_sec.MaxOctave = 7;
        piano_sec.Name = "Piano_Sec";
        piano_sec.VariationGrip = 0.8;

        Instrument cello = new Instrument();
        cello.MinOctave = 2;
        cello.MaxOctave = 6;
        cello.Name = "Cello";
        cello.VariationGrip = 0.8;

        Instrument violin = new Instrument();
        violin.MinOctave = 2;
        violin.MaxOctave = 6;
        violin.Name = "Violin";
        violin.VariationGrip = 0.8;

        Instrument violin2 = new Instrument();
        violin2.MinOctave = 2;
        violin2.MaxOctave = 6;
        violin2.Name = "Violin2";
        violin2.VariationGrip = 0.7;

        Instrument contrabass = new Instrument();
        contrabass.MinOctave = 2;
        contrabass.MaxOctave = 5;
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
        MersenneTwister twister = new MersenneTwister();
        NoteMapping mapping = this.chromaticMapping;
        
        int i = 0;
        int octave = twister.nextInt(instrument.MaxOctave - instrument.MinOctave + 1) + instrument.MinOctave;
        int mappingIndex = twister.nextInt(mapping.items.size());
        MappingItem item = mapping.GetMappingItem(mappingIndex);

        List<Integer> noteLengths = new ArrayList<>();
        noteLengths.add(8);
        noteLengths.add(16);
        noteLengths.add(32);
        noteLengths.add(64);

        Sequence sequence = new Sequence();

        int lengthRange = MAX_LENGTH - MIN_LENGTH;
        int patternLength = twister.nextInt(lengthRange);
        int targetLength = patternLength + MIN_LENGTH;

        while (i < targetLength) {
            int lengthPlus = noteLengths.get(twister.nextInt(4));

            double baseOffset = twister.nextInt((int)MAX_OFFSET + 1);
            double adjustedOffset = baseOffset - MAX_OFFSET / 2;

            Note note = new Note(item, octave);
            note.Length = lengthPlus;

            int currentIndex = note.addValue(adjustedOffset * instrument.VariationGrip, instrument, mapping, mappingIndex);

            item = mapping.GetMappingItem(currentIndex);

            i += lengthPlus;

            note.IsRest = twister.nextInt(10) == 0;
            sequence.addNote(note);
        }    
        
        System.out.println(String.format("Sequence note num: %s", sequence.notes.size()));
        return sequence;
    }

    private Sequence getAccompanimentSequence(Sequence master, Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        Sequence sequence = new Sequence();
        
        int octave = twister.nextInt(instrument.MaxOctave - instrument.MinOctave + 1) + instrument.MinOctave;
        boolean useHighOffset = twister.nextBoolean();

        for (Note refNote : master.notes) {
            int currentIndex = this.chromaticMapping.findIndexByRelativePosition(refNote.MappingItem);
            Note note = new Note(refNote.MappingItem, octave);
            note.Length = refNote.Length;
            note.IsRest = refNote.IsRest;
            note.addValue(useHighOffset ? 7 : 4, instrument, this.chromaticMapping, currentIndex);
            note.IsRest = twister.nextInt(10) == 0;
            
            sequence.addNote(note);
        }
        
        return sequence;
    }
}
