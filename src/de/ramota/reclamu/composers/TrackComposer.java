package de.ramota.reclamu.composers;

import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.ScaleItem;
import de.ramota.reclamu.AbstractSequence;
import de.ramota.reclamu.AbstractTrack;
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

    public AbstractSequence getSequence(Instrument instrument, double tempo) {
        return null;
    }
    
    public AbstractTrack generateTrack(int sequenceNum) {
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
        currentAccomp.findNewOffset();
        System.out.println(String.format("Intended Accomp changed to %s!", currentAccomp.toString()));
    }  
    
    protected void findAttackValues(AbstractTrack track) throws IllegalArgumentException {
        int currentAttack = twister.nextInt(80) + 40;
        
        for (AbstractSequence sequence: track.Sequences) {
            for (AbstractNote note: sequence.getNotes()) {
                if (twister.nextInt(150) == 0) {
                    currentAttack = twister.nextInt(80) + 40;
                }
                note.setAttack(currentAttack);
                currentAttack += twister.nextInt(36) - 18;
                
                if (currentAttack < 40) {
                    currentAttack = 41;
                } else if (currentAttack > 120) {
                    currentAttack = 119;
                }
            }
        }
    }    
}
