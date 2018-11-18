package de.ramota.reclamu.composers;

import de.ramota.reclamu.*;
import de.ramota.reclamu.configuration.IPieceConfiguration;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author Mathies Gräske
 */
public class TrackComposer {
    protected final MersenneTwister twister;
    public int ScaleOffset;
    protected List<ScaleItem> intendedAccomps = null;
    protected ScaleItem currentAccomp;
    protected Instrument instrument;
    protected int instrumentRange;
    protected int currentValue;
    private ArrayList<Integer> allowedScaleOffsets;

    int currentVal = 0;

    public String Name;
    private boolean initialized;

    public TrackComposer(String name, MersenneTwister twister, List<ScaleItem> intendedAccomps) {
        this.twister = twister;
        this.Name = name;
        this.allowedScaleOffsets = new ArrayList<>();
        this.intendedAccomps = intendedAccomps;
    }

    public void readAllowedScaleOffsets(IPieceConfiguration configuration) {
        this.allowedScaleOffsets = configuration.getAllowedScaleOffsets();
    }
    
    public void initialize(Instrument instrument) {
        if (instrument == null) {
            throw new IllegalArgumentException("Instrument is null! Cannot initialize!");
        }
        if (intendedAccomps == null) {
            throw new IllegalArgumentException("ScaleItems are null! Cannot initialize!");
        }
        this.instrument = instrument;
        this.findNoteValue(instrument);
        this.findAccompaniment();
        this.findScale();
        this.initialized = true;
    }

    public AbstractSequence getSequence() throws Exception {
        if (!initialized) {
            throw new Exception("Object not initialized!");
        }
        return null;
    }
    
    public AbstractTrack generateTrack(String name, int sequenceNum) {
        return null;
    }

    public AbstractNote getNextNote(AbstractSequence sequence, int lengthenStartRange, int currentLength) { return null; }

    public void findNoteValue(Instrument instrument) {
        instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        currentValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;        
    }
    
    public void findScale() {
        if (allowedScaleOffsets.size() > 0) {
            ScaleOffset = twister.nextInt(allowedScaleOffsets.size());
            System.out.println(String.format("Scale changed to %d", ScaleOffset));
        } else {
            System.out.println("There are no scale offsets defined!");
        }
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

        int val = fullWeight > 0 ? twister.nextInt(fullWeight) : 0;
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
