package de.ramota.reclamu.configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Mathies Gräske
 */
public class JsonReader {
    private JSONParser parser;
    private JSONObject mainObject;
    
    public JsonReader(String filePath) {
        parser = new JSONParser();
        try {
            mainObject = (JSONObject) parser.parse(new FileReader(filePath));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(JsonReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public JSONArray getArrayForItem(String itemName) {
        return (JSONArray) mainObject.get(itemName);
    }

    public String getStringForItem(String itemName) {
        return (String) mainObject.get(itemName);
    }
}
