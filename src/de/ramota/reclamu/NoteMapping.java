/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ramota.reclamu;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luser
 */
public class NoteMapping {
    protected Map<Integer, MappingItem> items;
    public int HalfToneRange = 12;
    
    public NoteMapping() {
        this.items = new HashMap<>();
    }
    
    public MappingItem GetMappingItem(int index) {
        return items.get(index);
    }    
    
    int findIndexByRelativePosition(MappingItem mappingItemToFind) {
        for (Map.Entry<Integer, MappingItem> item : items.entrySet()) {
            MappingItem currentItem = (MappingItem)item.getValue();
            
            if (currentItem.RelativePosition == mappingItemToFind.RelativePosition) {
                return item.getKey();
            }
        }
        
        return -1;
    }    
}
