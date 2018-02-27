package de.ramota.reclamu.configuration;

import org.json.simple.JSONArray;

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
        return reader.getArrayForItem("accompaniments");
    }
}
