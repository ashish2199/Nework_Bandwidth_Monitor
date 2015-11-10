package snmp.unused;

import java.util.ArrayList;
import snmp.unused.Walk_mac_address;


public class walk {
    String mac[];
    public static void main(String args[]){
                String ip = "10.2.51.60";
		String community = "public";
		String targetOid = ".1.3.6.1.2.1.17.4.3.1.1";//".1.3.6.1.2.1.1";
		Walk_mac_address.snmpWalk(ip, community, targetOid);
                
                int len = snmp.unused.Walk_mac_address.snmp_mac_address.size();
                
                System.out.println("length= "+len);
                int no_of_macs = len;
                String mac[]=new String[no_of_macs];
                
                for(int i=0;i<no_of_macs;i++){
                    mac[i]=snmp.unused.Walk_mac_address.snmp_mac_address.get(i);
                    mac[i]=mac[i].replaceAll(":", "-");
                    //System.out.println(""+mac[i]);
                }
                
                ArrayList<String> macs = snmp.unused.Walk_command.snmpWalk(ip, community, targetOid);
                ArrayList<String> ports = snmp.unused.Walk_command.snmpWalk(ip, community, targetOid);
                
                
                String result_arp[][]=process.arp();
                for(int i =0;i<result_arp.length;i++){
                    //System.out.println(""+result_arp[i][1]);
                }
                System.out.println("got data");
                for(int i =0;i<result_arp.length;i++){
                    for(int j=0;j<mac.length;j++){
                        
                        if(mac[j].equals(result_arp[i][1])){
                            System.out.println("THE IP for the mac  "+mac[j]+"  is  "+result_arp[i][0] );
                        }
                    }
                }
                System.out.println("arp len="+result_arp.length+" mac from snmp="+mac.length);
    }    
}
