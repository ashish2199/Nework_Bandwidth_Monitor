/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snmp.unused;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.snmp4j.PDU;
import snmp.commands.SnmpGet;

/**
 *
 * @author Ashish Padalkar
 */
public class hc_tested {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
        
            check_if_HC_octets_present();
        
        } catch (IOException ex) {
            Logger.getLogger(hc_tested.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    static void check_if_HC_octets_present() throws IOException{
        String oid_HCInoctetes = " 1.3.6.1.2.1.31.1.1.1.6.";
        String oid_HCOutoctetes = " 1.3.6.1.2.1.31.1.1.1.10.";
        
        String ip="127.0.0.1";
        String community="public";
        String port ="3";
        
        String oid_inoctetes = "1.3.6.1.2.1.2.2.1.10.";
        String oid_outoctetes = "1.3.6.1.2.1.2.2.1.16.";
        
        String InOctets_oidval = oid_inoctetes+port;
        PDU In_resp_from_get = SnmpGet.snmpGet(ip, community, InOctets_oidval);
        long inoctets = SnmpGet.getPDUvalue(In_resp_from_get);

        String OutOctets_oidval = oid_outoctetes+port;
        PDU Out_resp_from_get = SnmpGet.snmpGet(ip, community, OutOctets_oidval);
        long outoctets = SnmpGet.getPDUvalue(Out_resp_from_get);
        
        System.out.println("Got value outoctets:"+outoctets+" inoctets:"+inoctets);
        
    }
}
