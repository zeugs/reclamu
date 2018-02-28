package de.ramota.reclamu.configuration;

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
        return reader.getArrayForItem("accompaniments");
    }
    
    public String getValue(String key) {
        JSONArray values = reader.getArrayForItem("values");
        
        for (Iterator it = values.iterator(); it.hasNext();) {
            JSONObject valueObject = (JSONObject)it.next();
            String currentKey = valueObject.get("key").toString();
            String currentValue = valueObject.get("value").toString();
            
            if (currentKey.equals(key)) {
                return currentValue;
            }
        }
        
        return null;
    }

    public int getValueAsInt(String key) {
        JSONArray values = reader.getArrayForItem("values");
        
        for (Iterator it = values.iterator(); it.hasNext();) {
            JSONObject valueObject = (JSONObject)it.next();
            String currentKey = valueObject.get("key").toString();
            int currentValue = Integer.parseInt(valueObject.get("value").toString());
            
            if (currentKey.equals(key)) {
                return currentValue;
            }
        }
        
        return -1;
    }
}
