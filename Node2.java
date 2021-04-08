/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;



class ItemsReceived  implements Serializable
{
    private int id;
    private List<TempItem> temp_items;
    private List<byte[]> block_items;
    
    public ItemsReceived(int id)
    {
        this.id = id;
        temp_items = new ArrayList<TempItem>();
        block_items = new ArrayList<byte[]>();
    }
    
    public int GetId()
    {
        return id;
    }
    
    public void AddTempItem(TempItem item)
    {
        temp_items.add(item);
    }
    
    public TempItem GetTransactionItem(int id)
    {
        return temp_items.get(id);
    }
    
    public int GetTempItemsSize()
    {
        return temp_items.size();
    }
    
    public void AddBlockItem(byte[] item)
    {
        block_items.add(item);
    }
    
    public byte[] GetBlockItem(int id)
    {
        return block_items.get(id);
    }
    
    public int GetBlockItemsSize()
    {
        return block_items.size();
    }
    
    
}



class TempItem  implements Serializable
{
    private int id;
    private byte[] data;
    
    public TempItem(int id, byte[] data)
    {
        this.id = id;
        this.data = data;
    }
    
    public int GetID()
    {
        return id;
    }
    
    public byte[] GetData()
    {
        return data;
    }
    
}


class Node2
{
    private final boolean enable_synchronization = true;
    private final int TIMER_PERIOD = 2;
    private int id;
    private String name;
    private String address;
    private int port;
    private List<TempItem> temp_items;
    private List<byte[]> block_items;
    private List<byte[]> temp_item_counts;
    private List<byte[]> block_item_counts;
    
    public Node2(int id, String name, String address, int port)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.port = port;
        this.temp_items = new ArrayList<TempItem>();
        this.block_items = new ArrayList<byte[]>();
    }
    
    
    
    public void LoadTestItems(List<TempItem> data_list, int size)
    {
        for (int i = 0; i < size; i++)
        {
            TempItem item = data_list.get(i);
        
            if (ContainsTransactionData(temp_items, item.GetData()) == false)
            {

                if (temp_items.size() >= 20)
                {
                    
                    
                    temp_items = StringUtils.SortUnmodifiable(temp_items);
                    
                    
                    temp_items = Shrink.ShrinkUnmodifiable(temp_items, 20);

                    StringBuilder sb = new StringBuilder();

                    for (int i2 = 0; i2 < Shrink.items_removed.size(); i2++)
                    {
                        sb = sb.append(StringUtils.GetString(Shrink.items_removed.get(i2).GetData()));                                                
                    }

                    block_items.add(sb.toString().getBytes());
                    
                    
                }

                temp_items = StringUtils.AddUnmodifiable(temp_items, item);                
            }
        
        }
        
        
        
        
    }
    
    
    public void PrintItems()
    {
        System.out.println(this.name + ": temp_items...");
        for (int i = 0; i < temp_items.size(); i++)
        {
            TempItem item = temp_items.get(i);
            System.out.println(item.GetID() + "  " + new String(item.GetData()));
        }
    }
    
    public void PrintItemsSize()
    {
        System.out.println(this.name + ": temp_items-size = " + this.temp_items.size() + "  block_items-size = " + block_items.size());
       // System.out.println(this.name + ": temp_items-size = " + this.temp_items.size() + "  validated_items-size = " + this.validated_items.size());
    }
    
    private boolean ContainsTransactionData(List<TempItem> list, byte[] data)
    {
        boolean result = false;
        
        for (int i = 0; i < list.size(); i++)
        { 
            byte[] data2 = list.get(i).GetData();
            
            if (data2.length == data.length)
            {
                boolean match = true;
                
                for (int i2 = 0; i2 < data2.length; i2++)
                {
                    if (data2[i2] != data[i2])
                    {
                        match = false;
                        break;
                    }
                }
                
                if (match == true)
                {
                    result = true;
                    break;
                }
                
            }
            
        }
        
        return result;
    }
    
    
    private boolean ContainsBlockData(List<byte[]> list, byte[] data)
    {
        boolean result = false;
        
        for (int i = 0; i < list.size(); i++)
        { 
            byte[] data2 = list.get(i);
            
            if (data2.length == data.length)
            {
                boolean match = true;
                
                for (int i2 = 0; i2 < data2.length; i2++)
                {
                    if (data2[i2] != data[i2])
                    {
                        match = false;
                        break;
                    }
                }
                
                if (match == true)
                {
                    result = true;
                    break;
                }
                
            }
            
        }
        
        return result;
    }
    
    
    
    
    
    
    private void perform_synchronization()
    {
        try
        {
            ArrayList ips = DnsServer.GetIPData();


            // prepare data vectors...

            temp_item_counts = new ArrayList<byte[]>();
            for (int i = 0; i < ips.size(); i++)
            {
                temp_item_counts.add(null);
            }
            temp_item_counts.set(this.id, String.valueOf(temp_items.size()).getBytes());


            block_item_counts = new ArrayList<byte[]>();
            for (int i = 0; i < ips.size(); i++)
            {
                block_item_counts.add(null);
            }
            block_item_counts.set(this.id, String.valueOf(block_items.size()).getBytes());

            Thread.sleep(500);

            // send synchronization request...

            String my_s = this.address + ":" + this.port;

            for (int i = 0; i < ips.size(); i++)
            {
                Object[] ip = (Object[])ips.get(i);
                String s = ip[0].toString() + ":" + (int)ip[1];
                if (s.equals(my_s) == false)
                {                      
                    
                    if (SocketUtils.SocketConnected(ip[0].toString(), (int)ip[1]) == true)
                    {
                        System.out.println(this.name + ": requesting items node " + i);
                        SocketData socket_data = new SocketData("request items - synchronization".getBytes(), null, this.name.getBytes(), this.address.getBytes(), this.port);                                    
                        SocketUtils.SendData(ip[0].toString(), (int)ip[1], socket_data);
                        
                    }

                        
                    
                }
                
                Thread.sleep(100);

            }
            
            
            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    private void AddDataToBlock()
    {
        try
        {
            // shrink items...
                            
            System.out.println(this.name + ": shrinking items...");

            PrintItemsSize();



            // sort the temp items by transaction id...
            temp_items = StringUtils.SortUnmodifiable(temp_items);

            int block_size = 10;

            while (true)
            {
                temp_items = Shrink.ShrinkUnmodifiable(temp_items, block_size);

                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < Shrink.items_removed.size(); i++)
                {
                    sb = sb.append(StringUtils.GetString(Shrink.items_removed.get(i).GetData()));                                                
                }

                int size = sb.toString().length();

                byte[] data = Compressor.compress(sb.toString().getBytes());

                int size2 = data.length;

                double ratio = 1 - ((double)size2 / (double)size);

                ratio = Math.round(ratio * 100.0) / 100.0;

                System.out.println(this.name + ": data compression ratio = " + size + "   " + size2 + "   " + ratio);

                block_items = StringUtils.AddUnmodifiable(block_items, data);

                if (temp_items.size() < block_size) break;
            }



            PrintItemsSize();

            System.out.println(this.name + ": shrinking items, complete!");

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    
    public void StartServer()
    {
        try
        {
            
            if (enable_synchronization == true)
            {
            
                Calendar next_date = Calendar.getInstance();
                next_date.set(Calendar.MINUTE, 0);
                next_date.set(Calendar.SECOND, 0);
                next_date.set(Calendar.MILLISECOND, 0);

                while (true)
                {
                    next_date.add(Calendar.MINUTE, TIMER_PERIOD);
                    if (next_date.compareTo(Calendar.getInstance()) > 0)
                    {
                        break;
                    }
                }
                


                new Thread(() -> {


                    try
                    {


                        System.out.println(name + ": starting timer");

                        while (true)
                        {
                            if (Calendar.getInstance().compareTo(next_date) >= 0)
                            {
                                
                                
                                perform_synchronization();
                                
                                Calendar target_date = Calendar.getInstance();
                                target_date.set(Calendar.MILLISECOND, 0);
                                target_date.add(Calendar.SECOND, 10);
                                boolean pass = false;
                                
                                while (true)
                                {
                                    Calendar date = Calendar.getInstance();
                                    date.set(Calendar.MILLISECOND, 0);

                                    int count = 0;
                                    for (int i = 0; i < temp_item_counts.size(); i++)
                                    {
                                        if (temp_item_counts.get(i) != null) count++;
                                    }
                                    
                                    double target = (double)temp_item_counts.size() * 0.75;
                                    
                                    if ((double)count >= target)
                                    {
                                        pass = true;
                                        break;
                                    }
                                    
                                    if (date.compareTo(target_date) >= 0)
                                    {
                                        break;
                                    }
                                
                                    Thread.sleep(100);
                                }
                                
                                if (pass == true)
                                {
                                    System.out.println(this.name + ": consensus achieved");
                                    
                                    AddDataToBlock();
                                }
                                            
            
                                //---------------------------------------------------

                                while (true)
                                {
                                    next_date.add(Calendar.MINUTE, TIMER_PERIOD);
                                    if (next_date.compareTo(Calendar.getInstance()) > 0)
                                    {
                                        break;
                                    }
                                }
                                
                                
                                


                            }

                            Thread.sleep(1000);
                        }


                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }).start();
                
                
                
            }
            
            
            new Thread(() -> {

                try
                {
                    
                    ServerSocket server = new ServerSocket(port, 0, InetAddress.getByName(address));

                    System.out.println(name + ": server started [" + server.toString() + "]");
                    
                    while (true)
                    {
                        Socket socket = server.accept(); 
                        
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        
                        Object obj = ois.readObject();
                        String obj_name = obj.getClass().getSimpleName();
                            
                        if (obj_name.equals("SocketData"))
                        {          
                            SocketData sd = (SocketData)obj;
                            
//                            System.out.println(this.name + ": " + StringUtils.GetString(sd.message));
                            
                            if (StringUtils.GetString(sd.message).equals("add-with-announcement"))
                            {

                                String my_s = this.address + ":" + this.port;

                                ArrayList ips = DnsServer.GetIPData();
                                for (int i = 0; i < ips.size(); i++)
                                {
                                    Object[] ip = (Object[])ips.get(i);
                                    String s = ip[0].toString() + ":" + (int)ip[1];
                                    if (s.equals(my_s) == false)
                                    {                                                  
                                        if (SocketUtils.SocketConnected(ip[0].toString(), (int)ip[1]) == true)
                                        {
                                            SocketData socket_data = new SocketData("request acknowledgement".getBytes(), sd.data, this.name.getBytes(), this.address.getBytes(), this.port);                                    
                                            SocketUtils.SendData(ip[0].toString(), (int)ip[1], socket_data);
                                        }
                                    }
                                }

                            }
                            else if (StringUtils.GetString(sd.message).equals("request acknowledgement"))
                            {                                    
                                if (ContainsTransactionData(temp_items, sd.data) == false)
                                {
                                    TempItem item = new TempItem(temp_items.size(), sd.data);
                                    temp_items = StringUtils.AddUnmodifiable(temp_items, item);                
                                }
                                
                                
                                if (SocketUtils.SocketConnected(StringUtils.GetString(sd.source_adr), sd.source_prt) == true)
                                {
                                    SocketData socket_data = new SocketData("data acknowledged".getBytes(), sd.data, null, null, 0);
                                    SocketUtils.SendData(StringUtils.GetString(sd.source_adr), sd.source_prt, socket_data);

                                }


                            }
                            else if (StringUtils.GetString(sd.message).equals("data acknowledged"))
                            {                                    
                                if (ContainsTransactionData(temp_items, sd.data) == false)
                                {
                                    TempItem item = new TempItem(temp_items.size(), sd.data);
                                    temp_items = StringUtils.AddUnmodifiable(temp_items, item);                  
                                }


                            }
                            else if (StringUtils.GetString(sd.message).equals("validate"))
                            {                                                                        
                                String last = new String(temp_items.get(temp_items.size() - 1).GetData());
                                String target = new String(sd.data);
                                if (last.equals(target))
                                {                 
                                    if (SocketUtils.SocketConnected(StringUtils.GetString(sd.source_adr), sd.source_prt) == true)
                                    {
                                        SocketData socket_data = new SocketData("transaction validated".getBytes(), null, null, null, 0);
                                        SocketUtils.SendData(StringUtils.GetString(sd.source_adr), sd.source_prt, socket_data);
                                    }
                                }

                            }
                            else if (StringUtils.GetString(sd.message).equals("request items - synchronization"))
                            {
                                
                                Thread.sleep(1000);
                                
                                if (SocketUtils.SocketConnected(StringUtils.GetString(sd.source_adr), sd.source_prt) == true)
                                {
                                    ItemsReceived items = new ItemsReceived(this.id);
                                    for (int i = 0; i < temp_items.size(); i++)
                                    {
                                        items.AddTempItem(temp_items.get(i));
                                    } 
                                    for (int i = 0; i < block_items.size(); i++)
                                    {
                                        items.AddBlockItem(block_items.get(i));
                                    }

                                    if (SocketUtils.SocketConnected(StringUtils.GetString(sd.source_adr), sd.source_prt) == true)
                                    {
                                        System.out.println(this.name + ": sending data...");
                                        SocketUtils.SendData(StringUtils.GetString(sd.source_adr), sd.source_prt, items);
                                    }

                                }
                                
                            }
                            
                        }                        
                        else if (obj_name.equals("ItemsReceived"))
                        {   
                            ItemsReceived items = (ItemsReceived)obj;   
                            
                            System.out.println(this.name + ": received from node " + items.GetId()); 
                            
                            temp_item_counts.set(items.GetId(), String.valueOf(items.GetTempItemsSize()).getBytes());

                            for (int i = 0; i < items.GetTempItemsSize(); i++)
                            {
                                if (ContainsTransactionData(temp_items, items.GetTransactionItem(i).GetData()) == false)
                                {
                                    temp_items = StringUtils.AddUnmodifiable(temp_items, items.GetTransactionItem(i));  
                                }                                    
                            }


                            block_item_counts.set(items.GetId(), String.valueOf(items.GetBlockItemsSize()).getBytes());

                            for (int i = 0; i < items.GetBlockItemsSize(); i++)
                            {
                                if (ContainsBlockData(block_items, items.GetBlockItem(i)) == false)
                                {
                                    block_items = StringUtils.AddUnmodifiable(block_items, items.GetBlockItem(i));  
                                }                                    
                            }
                                    
                            
                        }
                        
                        
                        
                    }
                    
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }).start();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    
}
