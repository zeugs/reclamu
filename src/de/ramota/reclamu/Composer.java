package de.ramota.reclamu;

import de.ramota.reclamu.composers.*;
import de.ramota.reclamu.configuration.PieceConfiguration;
import de.ramota.reclamu.properties.ComposerProperties;
import de.ramota.reclamu.properties.PlainComposerProperties;
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
        List<ScaleItem> intendedAccomps = this.GetScaleData();
        List<Instrument> instruments = this.fetchInstruments();
        List<ComposerProperties> composerProperties = this.fetchComposerProperties();
        List<TrackComposer> composers = this.fetchComposers();
        List<AccompanimentComposer> accompComposers = this.fetchAccompaniments();
        List<AbstractTrack> tracks = this.fetchTracks(composers, accompComposers, instruments, intendedAccomps);
        Piece piece = new Piece();
        piece.Tracks = tracks;

        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for (AbstractTrack track : tracks) {
            if (i == 9) {
                i++;
            }
            String trackData = "V" + Integer.toString(i++) + track.toString();
            buffer.append(trackData);
            System.out.println(trackData);
        }

        try {
            Pattern p1 = new Pattern(buffer.toString());
            MidiFileManager.savePatternToMidi(p1, new File("test.mid"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<ComposerProperties> fetchComposerProperties() {
        List<ComposerProperties> composersProperties = new ArrayList<>();
        JSONArray composerPropertyData = PieceConfiguration.getInstance().getComposerProperties();

        for (Iterator it = composerPropertyData.iterator(); it.hasNext();) {
            JSONObject instrumentObject = (JSONObject)it.next();
            String name = instrumentObject.get("name").toString();
            String type = instrumentObject.get("type").toString();

            switch (type) {
                case "PlainComposerProperties" :
                    composersProperties.add(new PlainComposerProperties(name));
                    break;
                default:
                    break;
            }
        }

        return composersProperties;
    }

    protected List<ScaleItem> GetScaleData() {
        List<ScaleItem> intendedScaleItems = new ArrayList<>();
        
        JSONArray scaleData = PieceConfiguration.getInstance().getScaleItems();
        for (Iterator it = scaleData.iterator(); it.hasNext();) {
            JSONObject scaleObject = (JSONObject)it.next();
            String name = scaleObject.get("name").toString();
            String[] offsetStrings = scaleObject.get("offsets").toString().split(",");
            
            ScaleItem scaleItem = new ScaleItem(name);
            ArrayList<Integer> offsets = new ArrayList<>();

            for (String offset : offsetStrings) {
                offsets.add(Integer.parseInt(offset));
            }
            
            scaleItem.Offsets = offsets;
            intendedScaleItems.add(scaleItem);
        }
        
        JSONArray accompData = PieceConfiguration.getInstance().getAccompanimentItems();
        for (Iterator it = accompData.iterator(); it.hasNext();) {
            JSONObject scaleObject = (JSONObject)it.next();
            String name = scaleObject.get("name").toString();
            String scaleItemRef = scaleObject.get("scale item").toString();
            Integer weight = Integer.parseInt(scaleObject.get("weight").toString());
            String[] offsetStrings = scaleObject.get("offsets").toString().split(",");
            
            ScaleItem scaleItem = this.GetScaleItemByName(intendedScaleItems, scaleItemRef);
            
            if (scaleItem != null) {
                AccompanimentItem accompItem = new AccompanimentItem(name);
                accompItem.Weight = weight;
                ArrayList<Integer> offsets = new ArrayList<>();

                for (String offset : offsetStrings) {
                    offsets.add(Integer.parseInt(offset));
                }

                accompItem.Offsets = offsets;
                scaleItem.addAccompanimentItem(accompItem);
            }
        }
        
        return intendedScaleItems;
    }

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
            Object input = instrumentObject.get("input");
            
            switch (type) {
                case "FreeFormTrackComposer" : 
                    composers.add(new FreeFormTrackComposer(name)); 
                    break;
                case "PlainTrackComposer" : 
                    composers.add(new PlainTrackComposer(name));
                    break;
                case "SineWaveTrackComposer" : 
                    composers.add(new SineWaveTrackComposer(name)); 
                    break;
                case "StairTrackComposer" :
                    int startDirection = Integer.parseInt(instrumentObject.get("startDirection").toString());
                    composers.add(new StairTrackComposer(name, startDirection));
                    break;
                case "MidiDataEnhancer" :
                    MidiDataEnhancer enhancer = new MidiDataEnhancer(name);
                    enhancer.setFileName(input.toString());
                    enhancer.setMidiTrack(3);
                    enhancer.setScale(4);                    
                    composers.add(enhancer); 
                    break;
                default: 
                    break;
            }
        }
        
        return composers;
    }

    private List<AccompanimentComposer> fetchAccompaniments() {
        List<AccompanimentComposer> composers = new ArrayList<>();
        JSONArray composerData = PieceConfiguration.getInstance().getAccompaniments();
        for (Iterator it = composerData.iterator(); it.hasNext();) {
            JSONObject instrumentObject = (JSONObject)it.next();
            String name = instrumentObject.get("name").toString();
            String type = instrumentObject.get("type").toString();
            
            switch (type) {
                case "PlainAccompanimentComposer" : 
                    composers.add(new PlainAccompanimentComposer(name)); 
                    break;
                default:
                    break;
            }
        }
        
        return composers;
    }

    private List<AbstractTrack> fetchTracks(List<TrackComposer> composers, List<AccompanimentComposer> accompComposers, List<Instrument> instruments, List<ScaleItem> intendedAccomps) {
        List<AbstractTrack> tracks = new ArrayList<>();
        List<AbstractTrack> refTracks = new ArrayList<>();
        JSONArray trackData = PieceConfiguration.getInstance().getTracks();
        for (Iterator it = trackData.iterator(); it.hasNext();) {
            JSONObject instrumentObject = (JSONObject)it.next();
            String name = instrumentObject.get("name").toString();
            String type = instrumentObject.get("type").toString();
            String instr = instrumentObject.get("instrument").toString();
            int trackNum = Integer.parseInt(instrumentObject.get("tracks_num").toString());
            int mirroredTrackNum = Integer.parseInt(instrumentObject.get("mirrored_tracks_num").toString());
            String input = instrumentObject.get("input").toString();
            
            for (TrackComposer composer : composers) {
                if (composer.Name.equals(type)) {
                    Instrument refInstrument = getInstrumentByName(instruments, instr);
                    composer.initialize(refInstrument, intendedAccomps);
                    AbstractTrack track = composer.generateTrack(refInstrument, name, 10);
                    tracks.add(track);
                    refTracks.add(track);
                }
            }

            for (AccompanimentComposer composer : accompComposers) {
                if (composer.Name.equals(type)) {
                    AbstractTrack refTrack = getTrackByName(refTracks, input);
                    Instrument refInstrument = getInstrumentByName(instruments, instr);
                    List<AbstractTrack> accompTracks = composer.generateTracks(name, refTrack, refInstrument, trackNum, mirroredTrackNum);
                    tracks.addAll(accompTracks);
                }
            }
        }
        
        return tracks;
    }
    
    private Instrument getInstrumentByName(List<Instrument> instruments, String name) {
        for (Instrument instrument : instruments) {
            if (instrument.Name.equals(name)) {
                return instrument;
            }
        }
        
        return null;
    }

    private AbstractTrack getTrackByName(List<AbstractTrack> tracks, String name) {
        for (AbstractTrack track : tracks) {
            if (track.Name.equals(name)) {
                return track;
            }
        }
        
        return null;
    }

    private ScaleItem GetScaleItemByName(List<ScaleItem> intendedScaleItems, String name) {
        for (ScaleItem item: intendedScaleItems) {
            if (item.Name.equals(name)) {
                return item;
            }
        }
        
        return null;
    }
}