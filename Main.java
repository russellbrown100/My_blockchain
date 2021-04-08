/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Main {

    
    
    public static void main(String[] args) {
        
        
        
        
        try
        {
            
            
            
            
            System.out.println("preparing test data");
            
            List<TempItem> data_list = new ArrayList<TempItem>();            
            int lim = 100;
            for (int i = 0; i < lim; i++)
            {
                String string = i + "hello world - good morning everyone - hello world - good morning everyone";
                
                Encryptor e = new Encryptor();
                String key = e.GenerateKey(8);
                
                byte[] data = e.encrypt(string, key);
                
                data = Base64.getEncoder().encode(data);
                
               // byte[] data = string.getBytes();               
               
                TempItem tr = new TempItem(data_list.size(), data);
                data_list.add(tr);
                
                
            }
            
            
           // int[] sizes = new int[]{84, 62, 70, 65, 57};
         
           // int[] sizes = new int[]{85 , 62 , 85 , 55 , 75 };
          
           //int[] sizes = new int[]{58 , 57 , 55 , 59 , 56 };
           
           int[] sizes = new int[]{53 , 49 , 26 , 47 , 42 };
            
            
            System.out.println("sizes = " + Arrays.toString(sizes));
            
            System.out.println("loading nodes");
            
            
            ArrayList ips = DnsServer.GetIPData();            
            
            ArrayList nodes = new ArrayList();
            
            for (int i = 0; i < ips.size(); i++)
            {
                String name = ("node_" + i).toString();
                
                String adr = ((Object[])ips.get(i))[0].toString();
                int prt = (int)((Object[])ips.get(i))[1];
                Node2 node = new Node2(i, name, adr, prt);
                node.LoadTestItems(data_list, sizes[i]);
                nodes.add(node);
                Thread.sleep(100);

            }
            
            
            
            
            
            
            System.out.println("starting node servers");
            
            for (int i = 0; i < nodes.size(); i++)
            {
                Node2 n = (Node2)nodes.get(i);
                
                if (i != 2)
                n.StartServer();
                
                
                Thread.sleep(100);
            }
            System.out.println();
            
            
            
            
            
            
            
            
            new Thread(() -> {
            
                try
                {
                    int cnt = 0;
                    
                    while (true)
                    {         
                        System.out.println("cnt = " + cnt);
                        
                        if (cnt == 2)
                        {
                          
                            for (int i = 0; i < 1; i++)
                            {
                                Client c = new Client("client100", "localhost", 200 + i); 

      
                                String string = String.valueOf(i) + "hello world - this is a custom transaction for everyone in the world";

                                Encryptor e = new Encryptor();
                                String key = e.GenerateKey(8);

                                byte[] data = e.encrypt(string, key);

                                data = Base64.getEncoder().encode(data);
                                
                                c.AddTransaction(data);

                                
                            }
                            
                            
                        }
                        
                        
                        
                        
                        for (int i = 0; i < nodes.size(); i++)
                        {
                            Node2 n = (Node2)nodes.get(i);
                            n.PrintItemsSize();
                        }
                        System.out.println();
                        Thread.sleep(2000);
                        
                        cnt++;
                        
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
