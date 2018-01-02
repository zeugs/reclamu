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
        int i = 0;
        int octave = 5;
        int currentNote = 0;
        double variationGrip = 0.7;
        List<Integer> noteLengths = new ArrayList<>();
        noteLengths.add(16);
        noteLengths.add(32);
        noteLengths.add(64);

        Piece piece = new Piece();
        Track track = new Track();
        Sequence sequence = new Sequence();

        MersenneTwister twister = new MersenneTwister();
        int lengthRange = MAX_LENGTH - MIN_LENGTH;
        int patternLength = twister.nextInt(lengthRange);
        int targetLength = patternLength + MIN_LENGTH;

        while (i < targetLength) {
            int lengthPlus = noteLengths.get(twister.nextInt(3));

            double baseOffset = twister.nextInt((int)MAX_OFFSET + 1);
            double adjustedOffset = baseOffset - MAX_OFFSET / 2;

            Note note = new Note();
            note.Value = currentNote;
            note.Octave = octave;
            note.Length = lengthPlus;

            note.addValue(adjustedOffset * variationGrip);

            currentNote = note.Value;

            i += lengthPlus;

            note.IsRest = twister.nextInt(10) == 0;
            sequence.addNote(note);
        }

        track.addSequence(sequence);
        piece.addTrack(track);

        return piece;
    }
}
