package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.ScaleItem;
import de.ramota.reclamu.Sequence;
import de.ramota.reclamu.Track;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author Mathies Gräske
 */
public class TrackComposer {
    protected final List<ScaleItem> intendedAccomps;
    protected final MersenneTwister twister;
    protected final Instrument instrument;

    public TrackComposer(Instrument instrument, List<ScaleItem> intendedAccomps) {
        this.twister = new MersenneTwister();
        this.instrument = instrument;
        this.intendedAccomps = intendedAccomps;
    }
    
    public Sequence getSequence(Instrument instrument) {
        return null;
    }
    
    public Track generateTrack(int sequenceNum) {
        return null;
    }
}
