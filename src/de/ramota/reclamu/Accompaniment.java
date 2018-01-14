package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author Mathies Gräske
 */
public class Accompaniment {
    protected List<AccompanimentItem> Items = new ArrayList<>();
    protected int counter = 0;
    
    public ArrayList<Integer> GetMapping() {
        return null;
    }

    public void AddAccompanimentItem(AccompanimentItem item) {
        Items.add(item);
    }
    
    public ArrayList<Integer> GetItemOffsets() {
        MersenneTwister twister = new MersenneTwister();
        int itemCount = Items.size();
        
        if (itemCount == 1) {
            return Items.get(0).Offsets;
        } else if (itemCount > 1) {
            ArrayList<Integer> offsets = Items.get(counter).Offsets;
            
            if (twister.nextInt(40) == 0) {
                counter++;
            }
            
            if (counter >= itemCount) {
                counter = 0;
            }
            return offsets;
        }
        
        return null;
    }
}
