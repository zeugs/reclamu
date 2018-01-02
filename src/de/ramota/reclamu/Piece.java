package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;

public class Piece {
    public List<Track> Tracks = new ArrayList<>();

    public void addTrack(Track track) {
        Tracks.add(track);
    }
}
