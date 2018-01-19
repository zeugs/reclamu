package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.ScaleItem;
import de.ramota.reclamu.Sequence;
import de.ramota.reclamu.composers.TrackComposer;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author Mathies Gräske
 */
public class PlainTrackComposer extends TrackComposer {

    public PlainTrackComposer(Instrument instrument, List<ScaleItem> intendedAccomps) {
        super(instrument, intendedAccomps);
    }

    @Override
    public Sequence getSequence(Instrument instrument) {
        return null;
    }    
}
