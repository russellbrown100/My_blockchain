/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

class NullData implements Serializable
{
    
}

class SocketData  implements Serializable
{
    public byte[] message;
    public byte[] data;
    public byte[] source_name;
    public byte[] source_adr;
    public int source_prt;
    
    public SocketData(byte[] message, byte[] data, byte[] source_name, byte[] source_adr, int source_prt)
    {
        this.message = message;
        this.data = data;
        this.source_name = source_name;
        this.source_adr = source_adr;
        this.source_prt = source_prt;
    }
}

class SocketUtils
{
    public static boolean SocketConnected(String address, int port)
    {
        boolean result = true;
        
        try
        {
            Socket socket = new Socket(address, port); 
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            NullData data = new NullData();
            oos.writeObject(data); 
            socket.close();            

        }
        catch (Exception ex)
        {
             if (ex.getMessage().equals("Connection refused: connect"))
             {
                 result = false;
             }
        }
        
        return result;
    }
    
    public static void SendData(String address, int port, SocketData data)
    {
       // if (SocketConnected(address, port) == true)
        {

            try
            {         

                Socket socket = new Socket(address, port); 
                OutputStream os = socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(data); 
                socket.close();            

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        
        }
        
        
    }
    
    public static void SendData(String address, int port, NullData data)
    {
       // if (SocketConnected(address, port) == true)
        {

            try
            {         

                Socket socket = new Socket(address, port); 
                OutputStream os = socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(data); 
                socket.close();            

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        
        }
        
        
    }
    
    public static void SendData(String address, int port, ItemsReceived items)
    {
       // if (SocketConnected(address, port) == true)
        {
            try
            {            
                Socket socket = new Socket(address, port); 
                OutputStream os = socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(items); 
                socket.close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        
    }
    
    
    
    
    
}