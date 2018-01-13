package de.ramota.reclamu;

import java.util.ArrayList;

/**
 *
 * @author Mathies Gräske
 */
public class PlayGroup {
    private ArrayList<Instrument> Instruments = new ArrayList<>();
    public boolean IsSilenced;
    
    public void AddInstrument(Instrument instrument) {
        Instruments.add(instrument);
        instrument.Group = this;
    }
}
