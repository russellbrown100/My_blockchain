/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;




class StringUtils {
    
    
    public static List<TempItem> SortUnmodifiable(List<TempItem> list)
    {
        
        List<TempItem> list2 = new ArrayList<TempItem>();
        for (int i = 0; i < list.size(); i++)
        {
            list2.add(list.get(i));
        }
        
        
        Collections.sort(list2, new Comparator<TempItem>() {

            public int compare(TempItem item, TempItem item2) {

                int result = 0;

                if (item.GetID() < item2.GetID()) result = -1;
                else if (item.GetID() > item2.GetID()) result = 1;

                return result;

            }
        });
        
        list2 = Collections.unmodifiableList(list2); 
        
        return list2;
        
    }
    
    
    public static List<TempItem> AddUnmodifiable(List<TempItem> list, TempItem item)
    {
        List<TempItem> list2 = new ArrayList<TempItem>();
        for (int i = 0; i < list.size(); i++)
        {
            list2.add(list.get(i));
        }
        list2.add(item);
        
        //list2.add(new byte[][]{data});
        
        list2 = Collections.unmodifiableList(list2); 
              
        return list2;
    }
    
    public static List<byte[]> AddUnmodifiable(List<byte[]> list, byte[] data)
    {
        List<byte[]> list2 = new ArrayList<byte[]>();
        for (int i = 0; i < list.size(); i++)
        {
            list2.add(list.get(i));
        }
        list2.add(data);
        
        //list2.add(new byte[][]{data});
        
        list2 = Collections.unmodifiableList(list2); 
              
        return list2;
    }
    
    
    public static String GetString(byte[] data)
    {
        return new String(data);
    }
    
}
