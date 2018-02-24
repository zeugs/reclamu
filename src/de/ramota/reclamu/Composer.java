package de.ramota.reclamu;

import de.ramota.reclamu.composers.MidiDataEnhancer;
import de.ramota.reclamu.composers.MaeanderAccompanimentComposer;
import de.ramota.reclamu.composers.FreeFormTrackComposer;
import de.ramota.reclamu.composers.PlainAccompanimentComposer;
import de.ramota.reclamu.composers.PlainTrackComposer;
import de.ramota.reclamu.composers.TrackComposer;
import de.ramota.reclamu.configuration.PieceConfiguration;
import org.apache.commons.math3.random.MersenneTwister;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Composer {
    public static int MIN_SEQUENCE_LENGTH =  100;
    public static int MAX_SEQUENCE_LENGTH = 10000;
    public static double MAX_OFFSET = 4;

    public Composer() {
        compose();
    }

    private void compose() {
        this.fetchInstruments();
        List<TrackComposer> composers = this.fetchComposers();
        this.fetchTracks(composers);
        
        /*List<AbstractTrack> tracks = getPatterns().Tracks;
        int counter = 0;

        for (AbstractTrack track : tracks) {
            Pattern p1 = new Pattern(track.toString());
            System.out.println(track);

            try {
                MidiFileManager.savePatternToMidi(p1, new File("test" + String.valueOf(counter++) + ".mid"));
            } catch (IOException e) {
            }
        }*/
    }
           
    protected List<ScaleItem> GetStandardAccompItems() {
        List<ScaleItem> intendedScaleItems = new ArrayList<>();
        
        AccompanimentItem accompItem;
        MajorScaleAccompaniment simpleAccomp = new MajorScaleAccompaniment();
        // I
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        accompItem.Weight = 25;
        simpleAccomp.addAccompanimentItem(accompItem);
        // maj6
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(9);
        accompItem.Weight = 10;
        simpleAccomp.addAccompanimentItem(accompItem);
        // maj7
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(11);
        accompItem.Weight = 10;
        simpleAccomp.addAccompanimentItem(accompItem);
        // maj9
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(11);
        accompItem.Offsets.add(14);
        accompItem.Weight = 10;
        simpleAccomp.addAccompanimentItem(accompItem);
        // IV
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(5);
        accompItem.Offsets.add(9);
        accompItem.Offsets.add(12);
        accompItem.Weight = 30;
        simpleAccomp.addAccompanimentItem(accompItem);
        // V
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(11);
        accompItem.Offsets.add(14);
        accompItem.Weight = 30;
        simpleAccomp.addAccompanimentItem(accompItem);
        // VI
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(9);
        accompItem.Offsets.add(13);
        accompItem.Offsets.add(16);
        accompItem.Weight = 10;
        intendedScaleItems.add(simpleAccomp);
        // contains i, iv, v, vi, ii
        MinorScaleAccompaniment simpleAccomp2 = new MinorScaleAccompaniment();
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(3);
        accompItem.Offsets.add(7);
        accompItem.Weight = 10;
        simpleAccomp2.addAccompanimentItem(accompItem);
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(5);
        accompItem.Offsets.add(8);
        accompItem.Offsets.add(12);
        accompItem.Weight = 20;
        simpleAccomp2.addAccompanimentItem(accompItem);
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(10);
        accompItem.Offsets.add(14);
        accompItem.Weight = 10;
        simpleAccomp2.addAccompanimentItem(accompItem);
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(8);
        accompItem.Offsets.add(12);
        accompItem.Offsets.add(16);
        accompItem.Weight = 30;
        simpleAccomp2.addAccompanimentItem(accompItem);
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(2);
        accompItem.Offsets.add(5);
        accompItem.Offsets.add(9);
        accompItem.Weight = 30;
        simpleAccomp2.addAccompanimentItem(accompItem);
        intendedScaleItems.add(simpleAccomp2);
        
        return intendedScaleItems;
    }

    /*public AbstractTrack getFreeFormTrack(Piece piece, Instrument instrument, List<ScaleItem> intendedScaleItems) {
        MersenneTwister twister = new MersenneTwister();
        
        FreeFormTrackComposer trackComposer = new FreeFormTrackComposer();
        trackComposer.initialize(instrument, intendedScaleItems);
        int numberOfSequences = twister.nextInt(240) + 150;
        AbstractTrack track = trackComposer.generateTrack(numberOfSequences);
        piece.Tracks.add(track);
        
        return track;
    }
    
    public AbstractTrack getPlainTrack(Piece piece, Instrument instrument, List<ScaleItem> intendedScaleItems) {
        MersenneTwister twister = new MersenneTwister();
        
        PlainTrackComposer trackComposer = new PlainTrackComposer();
        trackComposer.initialize(instrument, intendedScaleItems);
        int numberOfSequences = twister.nextInt(20) + 10;
        AbstractTrack track = trackComposer.generateTrack(numberOfSequences);
        piece.Tracks.add(track);
        
        return track;
    }    

    private AbstractTrack getTrackFromFile(Piece piece, Instrument instrument, ScaleItem interpretationItem, String fileName, int midiTrack) {
        
        MidiDataEnhancer enhancer = new MidiDataEnhancer(interpretationItem);
        enhancer.setFileName(fileName);
        enhancer.setMidiTrack(midiTrack);
        enhancer.setScaleItem(interpretationItem);
        AbstractTrack track = enhancer.generateTrack(1);
        piece.Tracks.add(track);
        
        return track;
    }*/

    private List<Instrument> fetchInstruments() {
        List<Instrument> instruments = new ArrayList<>();
        JSONArray instrumentData = PieceConfiguration.getInstance().getInstruments();
        for (Iterator it = instrumentData.iterator(); it.hasNext();) {
            JSONObject instrumentObject = (JSONObject)it.next();
            String name = instrumentObject.get("name").toString();
            int minNote = Integer.parseInt(instrumentObject.get("min_note").toString());
            int maxNote = Integer.parseInt(instrumentObject.get("max_note").toString());
            double variationGrip = Double.parseDouble(instrumentObject.get("variation_grip").toString());
            Instrument instrument = new Instrument(minNote, maxNote, name, variationGrip);
            instruments.add(instrument);
        }
        
        return instruments;
    }

    private List<TrackComposer> fetchComposers() {
        List<TrackComposer> composers = new ArrayList<>();
        JSONArray composerData = PieceConfiguration.getInstance().getComposers();
        for (Iterator it = composerData.iterator(); it.hasNext();) {
            JSONObject instrumentObject = (JSONObject)it.next();
            String name = instrumentObject.get("name").toString();
            String type = instrumentObject.get("type").toString();
            switch (type) {
                case "PlainTrackComposer" : 
                    PlainTrackComposer composer = new PlainTrackComposer(name);
                    composers.add(composer); 
                    break;
                default: 
                    break;
            }
        }
        
        return composers;
    }

    private List<AbstractTrack> fetchTracks(List<TrackComposer> composers) {
        List<AbstractTrack> tracks = new ArrayList<>();
        JSONArray trackData = PieceConfiguration.getInstance().getTracks();
        for (Iterator it = trackData.iterator(); it.hasNext();) {
            JSONObject instrumentObject = (JSONObject)it.next();
            String name = instrumentObject.get("name").toString();
            String type = instrumentObject.get("type").toString();
            int trackNum = Integer.parseInt(instrumentObject.get("tracks_num").toString());
            int mirroredTrackNum = Integer.parseInt(instrumentObject.get("mirrored_tracks_num").toString());
            String input = instrumentObject.get("type").toString();
            AbstractTrack track = new AbstractTrack();
            tracks.add(track);
        }
        
        return tracks;
    }
}