package de.ramota.reclamu;

import java.util.ArrayList;

/**
 *
 * @author Mathies Gräske
 */
public class PlayGroup {
    private ArrayList<Instrument> Instruments = new ArrayList<>();
    public boolean isSilenced;
    
    public void AddInstrument(Instrument instrument) {
        Instruments.add(instrument);
        instrument.Group = this;
    }

    PlayGroup getCopy() {
        PlayGroup group = new PlayGroup();
        group.isSilenced = isSilenced;
        for (Instrument instrument: Instruments) {
            group.Instruments.add(instrument.getCopy());
        }
        
        return group;
    }
}
