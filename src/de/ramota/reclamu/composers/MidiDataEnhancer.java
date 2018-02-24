package de.ramota.reclamu.composers;

import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.MidiParserListener;
import de.ramota.reclamu.ScaleItem;
import de.ramota.reclamu.AbstractTrack;
import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import org.jfugue.midi.MidiParser;

/**
 *
 * @author Mathies Gräske
 */
public class MidiDataEnhancer extends TrackComposer {
    private String fileName;
    private int midiTrack;
    
    public MidiDataEnhancer(String name) {
        super(name);
    }

    @Override
    public AbstractTrack generateTrack(int sequenceNum) {
        MidiParser parser = new MidiParser(); 
        MidiParserListener listener = new MidiParserListener(currentAccomp, midiTrack);

        try {
            parser.addParserListener(listener);
            parser.parse(MidiSystem.getSequence(new File(fileName)));
        } catch (IOException | InvalidMidiDataException ex) {
            
        }
        
        AbstractTrack track = listener.getMidiTrack();
                
        for (AbstractNote n: track.Sequences.get(0).getNotes()) {
            if (twister.nextInt(6) == 0) {
                currentAccomp.findNewOffset();
            }
            n.IntendedScaleType = currentAccomp;
            n.detectOffset();
            //n.setValueInRange();
        }

        findAttackValues(track);
        
        return track;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setMidiTrack(int midiTrack) {
        this.midiTrack = midiTrack;
    }
    
    public void setScaleItem(ScaleItem scaleItem) {
        this.currentAccomp = scaleItem;
    }
}
