/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class Shrink {
    
    public static List<TempItem> items_removed;
    
    public static List<TempItem> ShrinkUnmodifiable(List<TempItem> list, int start_id)
    {
        items_removed = new ArrayList<TempItem>();
        for (int i = 0; i < start_id; i++)
        {
            items_removed.add(list.get(i));
        }        
        
        List<TempItem> list2 = new ArrayList<TempItem>();
        for (int i = start_id; i < list.size(); i++)
        {
            list2.add(list.get(i));
        }
        
        list2 = Collections.unmodifiableList(list2); 
              
        return list2;
    }
    
}
