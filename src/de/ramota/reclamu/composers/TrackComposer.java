package de.ramota.reclamu.composers;

import de.ramota.reclamu.*;
import de.ramota.reclamu.configuration.PieceConfiguration;
import java.util.ArrayList;
import java.util.List;

import jdk.jshell.spi.ExecutionControl;
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

    int currentVal = 0;

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

    public AbstractSequence getSequence(Instrument instrument) {
        return null;
    }
    
    public AbstractTrack generateTrack(Instrument instrument, String name, int sequenceNum) {
        return null;
    }

    public AbstractNote getNextNote(AbstractSequence sequence, int lengthenStartRange, int currentLength) { return null; }

    public void findNoteValue(Instrument instrument) {
        instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        currentValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;        
    }
    
    public void findScale() {
        ScaleOffset = twister.nextInt(allowedScaleOffsets.size());        
        System.out.println(String.format("Scale changed to %d", ScaleOffset));
    }

    /*
    gets a new accompaniment based on the weighting defined
    in the configuration file.

    Takes the accumulated weights of the configured scale items to
    decide, which scale item to use. Then uses the weights of the item to
    find an actual accompaniment
     */
    public void findAccompaniment() {
        int fullWeight = 0;

        for (ScaleItem item: intendedAccomps) {
            fullWeight += item.FullWeight;
        }

        int val = twister.nextInt(fullWeight);
        int counter = 0;

        for (int i = 0; i < intendedAccomps.size(); i++) {
            ScaleItem tempItem = intendedAccomps.get(i);
            if (val > counter) {
                if (counter + tempItem.FullWeight > val) {
                    currentAccomp = intendedAccomps.get(i);
                    currentAccomp.findNewOffset();
                    System.out.println(String.format("Intended Accomp changed to %s!", currentAccomp.toString()));
                    break;
                }
            }
            counter += intendedAccomps.get(i).FullWeight;
        }
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
