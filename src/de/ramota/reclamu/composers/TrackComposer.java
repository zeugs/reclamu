package de.ramota.reclamu.composers;

import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.ScaleItem;
import de.ramota.reclamu.AbstractSequence;
import de.ramota.reclamu.AbstractTrack;
import de.ramota.reclamu.configuration.PieceConfiguration;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author Mathies Gräske
 */
public class TrackComposer {
    public int ScaleOffset;
    protected final MersenneTwister twister;
    protected List<ScaleItem> intendedAccomps = null;
    protected ScaleItem currentAccomp;
    protected int instrumentRange;
    protected int currentValue;
    private final ArrayList<Integer> allowedScaleOffsets;
    
    public String Name;

    public TrackComposer(String name) {
        this.Name = name;
        this.twister = new MersenneTwister();
        this.allowedScaleOffsets = PieceConfiguration.getInstance().getAllowedScaleOffsets();
    }
    
    public void initialize(Instrument instrument, List<ScaleItem> intendedAccomps) {
        this.intendedAccomps = intendedAccomps;
        
        this.findNoteValue(instrument);
        this.findAccompaniment();
        this.findScale();        
    }

    public AbstractSequence getSequence(Instrument instrument, double tempo) {
        return null;
    }
    
    public AbstractTrack generateTrack(Instrument instrument, String name, int sequenceNum) {
        return null;
    }
    
    public void findNoteValue(Instrument instrument) {
        instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        currentValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;        
    }
    
    public void findScale() {
        ScaleOffset = twister.nextInt(allowedScaleOffsets.size());        
        System.out.println(String.format("Scale changed to %d", ScaleOffset));
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
