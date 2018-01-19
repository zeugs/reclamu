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
    protected int scaleOffset;
    protected ScaleItem currentAccomp;
    protected int instrumentRange;
    protected int currentValue;

    public TrackComposer(Instrument instrument, List<ScaleItem> intendedAccomps) {
        this.twister = new MersenneTwister();
        this.instrument = instrument;
        this.intendedAccomps = intendedAccomps;
    }
    
    public void initialize() {
        this.findNoteValue();
        this.findAccompaniment();
        this.findScale();        
    }

    public Sequence getSequence(Instrument instrument) {
        return null;
    }
    
    public Track generateTrack(int sequenceNum) {
        return null;
    }
    
    public void findNoteValue() {
        instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        currentValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;        
    }
    
    public void findScale() {
        scaleOffset = twister.nextInt(12);        
        System.out.println(String.format("Scale changed to %d", scaleOffset));
    }
    
    public void findAccompaniment() {
        currentAccomp = intendedAccomps.get(twister.nextInt(intendedAccomps.size()));
        currentAccomp.SetNewOffset();
        System.out.println(String.format("Intended Accomp changed to %s!", currentAccomp.toString()));
    }    
}
