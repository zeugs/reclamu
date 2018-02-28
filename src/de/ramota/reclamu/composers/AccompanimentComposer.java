package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.AbstractNote;
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
public class AccompanimentComposer {
    protected MersenneTwister twister = new MersenneTwister();
    public String Name;
    
    public AccompanimentComposer(String name) {
        this.Name = name;
    }
    
    public List<AbstractTrack> generateTracks(String name, AbstractTrack masterTrack, Instrument instrument, int trackNum, int mirroredTrackNum) {
        List<AbstractTrack> newTracks = new ArrayList<>();
        List<AbstractTrack> mirroredTracks = new ArrayList<>();
        
        for (int i = 0; i < trackNum - mirroredTrackNum; i++) {
            AbstractTrack accompTrack = this.getAccompanimentTrack(name, masterTrack, instrument);
            newTracks.add(accompTrack);
        }
        for (int i = 0; i < mirroredTrackNum; i++) {
            AbstractTrack accompTrack = newTracks.get(twister.nextInt(newTracks.size()));
            AbstractTrack mirrorTrack = accompTrack.getCopy();
            mirroredTracks.add(mirrorTrack);
        }
        
        newTracks.addAll(mirroredTracks);
        
        return newTracks;
    }
    
    protected int addNoteHumanized(AbstractSequence sequence) {
        int minHumanization = PieceConfiguration.getInstance().getValueAsInt("Min Humanization");
        int humanizationScope = PieceConfiguration.getInstance().getValueAsInt("Humanization Scope");
        
        int delayLength = twister.nextInt(humanizationScope) + minHumanization;

        if (delayLength > 0) {
            AbstractNote delayPseudoNote = new AbstractNote(70);
            delayPseudoNote.IsRest = true;
            delayPseudoNote.setLength(delayLength, false);
            sequence.addNote(delayPseudoNote);
        }
        
        return delayLength;
    }
    
    protected AbstractTrack getAccompanimentTrack(String name, AbstractTrack masterTrack, Instrument instrument) {
        return null;
    }   
    
    protected void silenceSequence(AbstractSequence refSequence, AbstractSequence sequence, AbstractTrack track) {
        for (int j = 0; j < refSequence.getNotes().size(); j++) {
            AbstractNote noteCopy = refSequence.getNotes().get(j).getCopy();
            noteCopy.IsRest = true;
            sequence.getNotes().add(noteCopy);
        }
        track.Sequences.add(sequence);
    }
    
    protected int findNoteDiff(Instrument instrument, AbstractSequence refSequence) {
        int noteDiff = 0;
        int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        int absoluteValue = twister.nextInt(instrumentRange) + instrument.MinNoteIndex;
        absoluteValue -= absoluteValue % 12;
        if (refSequence.getNotes().size() > 0) {
            noteDiff = absoluteValue - refSequence.getNotes().get(0).getValue();
        }
        return -noteDiff;
    }    
}
