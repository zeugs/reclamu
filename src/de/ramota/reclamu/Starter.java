package de.ramota.reclamu;

import de.ramota.reclamu.configuration.PieceConfiguration;

public class Starter {

    public static void main(String[] args) {
        PieceConfiguration.getInstance().setInputFile("default.json");
        Composer composer = new Composer();
    }
}
