package de.ramota.reclamu.test;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.ScaleItem;
import de.ramota.reclamu.composers.TrackComposer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

class TrackComposerTest {

    @org.junit.jupiter.api.Test
    void TrackComposer_ArgumentsNull_NoError() {
        TrackComposer composer = new TrackComposer(null, null, null);
    }

    @org.junit.jupiter.api.Test
    void initialize_InstrumentNull_NoUnexpectedError() {
        MersenneTwister twister = new MersenneTwister();

        List<ScaleItem> scaleItems = new ArrayList<>();
        TrackComposer composer = new TrackComposer("Test", twister, null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            composer.initialize(null); }
        );
    }

    @org.junit.jupiter.api.Test
    void initialize_IntendedAccompsNull_NoUnexpectedError() {
        MersenneTwister twister = new MersenneTwister();

        Instrument instrument = new Instrument(0, 128, "TestInstr", 1);
        TrackComposer composer = new TrackComposer("Test", twister, null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            composer.initialize(instrument); }
        );
    }

    @org.junit.jupiter.api.Test
    void initialize_ArgumentsSimple_NoError() {
        MersenneTwister twister = new MersenneTwister();
        List<ScaleItem> scaleItems = new ArrayList<>();

        TrackComposer composer = new TrackComposer("Test", twister, scaleItems);
        Instrument instrument = new Instrument(0, 128, "TestInstr", 1);
        composer.initialize(instrument);
    }

    @org.junit.jupiter.api.Test
    void getSequence_ArgumentsSimple_NoError() {
        MersenneTwister twister = new MersenneTwister();
        List<ScaleItem> scaleItems = new ArrayList<>();

        TrackComposer composer = new TrackComposer("Test", twister, scaleItems);
        Instrument instrument = new Instrument(0, 128, "TestInstr", 1);
        composer.initialize(instrument);

        try {
            composer.getSequence();
        } catch (Exception ex) {
            System.out.println("Sequence get error!");
        }
    }

    @org.junit.jupiter.api.Test
    void getSequence_NotInitialized_NoUnexpectedError() {
        MersenneTwister twister = new MersenneTwister();
        List<ScaleItem> scaleItems = new ArrayList<>();
        TrackComposer composer = new TrackComposer("Test", twister, scaleItems);

        try {
            composer.getSequence();
        } catch (Exception ex) {
            System.out.println("Sequence get error!");
        }
    }

    @org.junit.jupiter.api.Test
    void generateTrack() {
    }

    @org.junit.jupiter.api.Test
    void getNextNote() {
    }

    @org.junit.jupiter.api.Test
    void findNoteValue() {
    }

    @org.junit.jupiter.api.Test
    void findScale() {
    }

    @org.junit.jupiter.api.Test
    void findAccompaniment() {
    }

    @org.junit.jupiter.api.Test
    void findAttackValues() {
    }
}