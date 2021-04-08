/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.ArrayList;

class DnsServer
{
    public static ArrayList GetIPData()
    {
        ArrayList ips = new ArrayList();
        
        for (int p = 80; p < 85; p++)
        {
            ips.add(new Object[]{"localhost", p});
        }
        
        
        
        return ips;
    }
    
    public DnsServer()
    {
        
    }
    
}
