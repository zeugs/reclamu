package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathies Gräske
 */
public class Accompaniment {
    protected List<AccompanimentItem> Items = new ArrayList<>();
    
    public ArrayList<Integer> GetMapping() {
        return null;
    }

    public void AddAccompanimentItem(AccompanimentItem item) {
        Items.add(item);
    }
    
    public ArrayList<Integer> GetItemOffsets() {
        return null;
    }
}
