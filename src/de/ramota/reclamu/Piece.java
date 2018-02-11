package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;

public class Piece {
    public List<AbstractTrack> Tracks = new ArrayList<>();
    
    public void addTrack(AbstractTrack track) {
        Tracks.add(track);
    }
}
