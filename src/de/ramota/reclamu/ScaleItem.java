package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

/**
 *
 * @author Mathies Gräske
 */
public class ScaleItem {
    private final List<AccompanimentItem> items = new ArrayList<>();
    private final MersenneTwister twister = new MersenneTwister();

    protected int currentOffset = 0;
    private int fullWeight;
    
    public ArrayList<Integer> GetMapping() {
        return null;
    }

    public void addAccompanimentItem(AccompanimentItem item) {
        items.add(item);
        
        fullWeight = 0;
        items.forEach((accompItem) -> {
            fullWeight += accompItem.Weight;
        });
    }
    
    public void findNewOffset() {
        if (items.size() > 0) {
            int val = twister.nextInt(fullWeight);
            int counter = 0;
            for (int i = 0; i < items.size(); i++) {
                AccompanimentItem tempItem = items.get(i);
                if (val > counter) {
                    if (counter + tempItem.Weight > val) {
                        currentOffset = i;
                        break;
                    }
                }
                counter += items.get(i).Weight;
            }
        }
    }
    
    public void setNewOffset(int offset) {
        currentOffset = offset;
    }
    
    public ArrayList<Integer> GetItemOffsets() {
        ArrayList<Integer> offsets = items.get(currentOffset).Offsets;
        return offsets;
    }

    public void setOffset(int offset) {
        currentOffset = offset;
    }
    
    public int getOffset() {
        return currentOffset;
    }
}
