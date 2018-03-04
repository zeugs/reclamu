package de.ramota.reclamu;

import java.util.ArrayList;

/**
 *
 * @author Mathies Gräske
 */
public class AccompanimentItem {
    public int OrderId;
    public int Weight;
    public ArrayList<Integer> Offsets;
    public String Name;
    
    public AccompanimentItem(String name) {
        this.Name = name;
    }

    @Override
    public String toString() {
        return Name;
    }
}
