package de.ramota.reclamu.composers;

import de.ramota.reclamu.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;

import de.ramota.reclamu.configuration.PieceConfiguration;
import org.apache.commons.math3.random.MersenneTwister;
import org.jfugue.midi.MidiParser;

/**
 *
 * @author Mathies Gräske
 */
public class MidiDataEnhancer extends TrackComposer {
    private String fileName;
    private int midiTrack;
    private int refScale;
    
    public MidiDataEnhancer(String name, MersenneTwister twister, List<ScaleItem> intendedAccomps) {
        super(name, twister, intendedAccomps);
        this.ScaleOffset = 4;
        this.readAllowedScaleOffsets(PieceConfiguration.getInstance());
    }

    @Override
    public AbstractTrack generateTrack(String name, int sequenceNum) {
        MidiParser parser = new MidiParser(); 
        MidiParserListener listener = new MidiParserListener(midiTrack);
        
        try {
            parser.addParserListener(listener);
            parser.parse(MidiSystem.getSequence(new File(fileName)));
        } catch (IOException | InvalidMidiDataException ex) {
            
        }
        
        AbstractTrack track = listener.getMidiTrack(name, instrument);
                
        for (AbstractNote n: track.Sequences.get(0).getNotes()) {
            if (twister.nextInt(6) == 0) {
                //this.findScale();
            }
            n.IntendedScaleType = currentAccomp;
            n.detectOffset();
            n.addValue((ScaleOffset - refScale), instrument);
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
    
    public void setScale(int scale) {
        this.refScale = scale;
    }
}
