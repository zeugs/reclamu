package de.ramota.reclamu.configuration;

import org.json.simple.JSONArray;

import java.util.ArrayList;

public interface IPieceConfiguration {
    JSONArray getInstruments();

    JSONArray getParameters();

    JSONArray getComposers();

    JSONArray getTracks();

    JSONArray getAccompaniments();

    JSONArray getAccompanimentItems();

    JSONArray getScaleItems();

    ArrayList<Integer> getAllowedScaleOffsets();
}
