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
    public ArrayList<Integer> Offsets = new ArrayList<>();
    public int FullWeight;

    protected int currentOffset = 0;

    public String Name;
    
    public ScaleItem(String name) {
        this.Name = name;
    }
    
    public ArrayList<Integer> GetMapping() {
        return items.get(currentOffset).Offsets;
    }

    public void addAccompanimentItem(AccompanimentItem item) {
        items.add(item);

        FullWeight = 0;
        items.forEach((accompItem) -> FullWeight += accompItem.Weight);
    }

    public void findNewOffset() {
        if (items.size() > 0) {
            int val = twister.nextInt(FullWeight);
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

    @Override
    public String toString() {
        return Name + " - " + items.get(currentOffset).Name;
    }
}
