package de.ramota.reclamu.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Mathies Gräske
 */
public class PieceConfiguration {
 
    private static PieceConfiguration instance;
    private JsonReader reader;

    private PieceConfiguration () {}

    public static PieceConfiguration getInstance () {
      if (PieceConfiguration.instance == null) {
        PieceConfiguration.instance = new PieceConfiguration();
      }
      return PieceConfiguration.instance;
    }   

    public void setInputFile(String filePath) {
        reader = new JsonReader(filePath);
    }

    public JSONArray getInstruments() {
        return reader.getArrayForItem("instruments");
    }

    public JSONArray getComposers() {
        return reader.getArrayForItem("composers");
    }

    public JSONArray getTracks() {
        return reader.getArrayForItem("tracks");
    }

    public JSONArray getAccompaniments() {
        return reader.getArrayForItem("accompaniment composers");
    }
    
    public JSONArray getAccompanimentItems() {
        return reader.getArrayForItem("accompaniment items");
    }

    public JSONArray getScaleItems() {
        return reader.getArrayForItem("scale items");
    }

    public ArrayList<Integer> getAllowedScaleOffsets() {
        String[] scaleOffsetsData = reader.getStringForItem("allowed scale offsets").split(",");
        
        ArrayList<Integer> allowedOffsets = new ArrayList<>();
        for (String offset: scaleOffsetsData) {
            allowedOffsets.add(Integer.parseInt(offset));
        }
        
        return allowedOffsets;
    }

}
