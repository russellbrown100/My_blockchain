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
import java.util.ArrayList;


class Client {
    
    private String name;
    private String address;
    private int port;
    private int validation_count;
    private int timer_count;
    private ServerSocket client_server;
    private boolean client_closed;
    
    public Client(String name, String address, int port)
    {
        this.name = name;
        this.address = address;
        this.port = port;
    }
    
    private void StartTimer()
    {
        new Thread(() -> {

            try
            {
                while (true)
                {
//                    System.out.println(timer_count);

                    if (timer_count >= 5)
                    {
                        client_server.close();
                    }

                    if (client_closed == true)
                    {
                       // System.out.println("stopping timer");
                      //  System.out.println("validation count = " + validation_count);
                        break;
                    }

                    timer_count++;
                    Thread.sleep(1000);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }


        }).start();
    }
    
    private void StartListener()
    {
        
            
        new Thread(() -> {


            try
            {
            //    System.out.println("starting client server");

                while (true)
                {

                    client_server = new ServerSocket(port, 0, InetAddress.getByName(address));


                    System.out.println(name + ": client server started [" + client_server.toString() + "]");


                    while (true)
                    {                            
                        try
                        {

                            Socket socket = client_server.accept(); 


                            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                            Object obj = ois.readObject();
                            String obj_name = obj.getClass().getName();

                            if (obj_name.contains("SocketData"))
                            {
                                SocketData sd = (SocketData)obj;

                                if (StringUtils.GetString(sd.message).equals("transaction validated"))
                                {
                                    validation_count++;
                                    if (validation_count > 2)
                                    {
                                        System.out.println(this.name + ":  transaction validated");
                                        client_closed = true;
                                        break;
                                    }
                                }

                            }

                        }
                        catch (Exception ex)
                        {
                            if (ex.getMessage().equals("Socket is closed"))
                            {
                                client_closed = true;
//                                System.out.println("socket is closed 1");
                                break;
                            }

                        }

                    }

                    if (client_closed == true)
                    {
//                        System.out.println("socket is closed 2");
                        break;
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
    
    
    
    public void AddTransaction(byte[] data)
    {
        System.out.println("client:  add transaction");
            
        try
        {
                        
            StartTimer();
            StartListener();
                        
            System.out.println("client:  broadcasting transaction");
            
            ArrayList ips = DnsServer.GetIPData();

            for (int i = 0; i < ips.size(); i++)
            {            
                String adr = ((Object[])ips.get(i))[0].toString();
                int prt = (int)((Object[])ips.get(i))[1];
                SocketData socket_data = new SocketData("add-with-announcement".getBytes(), data, null, null, 0);
                if (SocketUtils.SocketConnected(adr, prt) == true)
                {
                    SocketUtils.SendData(adr, prt, socket_data);
                    Thread.sleep(10); 
                }
            }

            
            
            validation_count = 0;
            timer_count = 0;
            client_closed = false;
            
            
                        
            System.out.println("client:  broadcasting validation");
            
            for (int i = 0; i < ips.size(); i++)
            {            
                String adr = ((Object[])ips.get(i))[0].toString();
                int prt = (int)((Object[])ips.get(i))[1];
                if (SocketUtils.SocketConnected(adr, prt) == true)
                {
                    SocketData socket_data = new SocketData("validate".getBytes(), data, name.getBytes(), address.getBytes(), port);
                    SocketUtils.SendData(adr, prt, socket_data);
                }
                Thread.sleep(10); 
            }
            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    
}
