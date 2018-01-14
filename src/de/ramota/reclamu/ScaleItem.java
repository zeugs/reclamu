package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author Mathies Gräske
 */
public class ScaleItem {
    protected List<AccompanimentItem> Items = new ArrayList<>();
    private final MersenneTwister twister = new MersenneTwister();

    protected int currentOffset = 0;
    
    public ArrayList<Integer> GetMapping() {
        return null;
    }

    public void AddAccompanimentItem(AccompanimentItem item) {
        Items.add(item);
    }
    
    public void SetNewOffset() {
        if (Items.size() > 0) {
            currentOffset = twister.nextInt(Items.size());
        }
    }
    
    public ArrayList<Integer> GetItemOffsets() {
        ArrayList<Integer> offsets = Items.get(currentOffset).Offsets;
        return offsets;
    }
}
