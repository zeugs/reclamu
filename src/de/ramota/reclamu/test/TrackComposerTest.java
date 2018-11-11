package de.ramota.reclamu.test;

import de.ramota.reclamu.composers.TrackComposer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.jupiter.api.Assertions;

class TrackComposerTest {

    @org.junit.jupiter.api.Test
    void TrackComposer_ArgumentsNull_NoError() {
        TrackComposer composer = new TrackComposer(null, null);
    }

    @org.junit.jupiter.api.Test
    void initialize_ArgumentsNull_NoError() {
        MersenneTwister twister = new MersenneTwister();
        TrackComposer composer = new TrackComposer("Test", twister);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            composer.initialize(null, null); }
            );
    }

    @org.junit.jupiter.api.Test
    void getSequence() {
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