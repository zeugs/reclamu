package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.Piece;
import de.ramota.reclamu.Track;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author Mathies Gräske
 */
public class AccompanimentComposer {
    protected final Piece piece;
    
    public AccompanimentComposer(Piece piece) {
        this.piece = piece;
    }

    public void generateTrack(Track track, Instrument instrument, int trackNum, int mirroredTrackNum) {
        List<Track> newTracks = new ArrayList<>();
        MersenneTwister twister = new MersenneTwister();
        
        for (int i = 0; i < trackNum - mirroredTrackNum; i++) {
            Track accompTrack = this.getAccompanimentTrack(track, instrument);
            piece.addTrack(accompTrack);
            newTracks.add(accompTrack);
        }
        for (int i = 0; i < mirroredTrackNum; i++) {
            Track accompTrack = newTracks.get(twister.nextInt(newTracks.size()));
            Track mirrorTrack = accompTrack.getCopy();
            piece.addTrack(mirrorTrack);
        }
    }
    
    protected Track getAccompanimentTrack(Track masterTrack, Instrument instrument) {
        return null;
    }    
}
