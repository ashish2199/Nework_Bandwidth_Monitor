package snmp.unused;

import java.util.ArrayList;
import java.util.Iterator;
import snmp.unused.Walk_mac_address;


public class Switch_device_finder {
    String mac[];
    public static void main(String args[]){
        
                String ip = "10.2.51.56";
		String community = "public";
		String mac_targetOid = "1.3.6.1.2.1.17.4.3.1.1";//".1.3.6.1.2.1.1";
		
                Walk_mac_address.snmpWalk(ip, community, mac_targetOid);
                
                int len = snmp.unused.Walk_mac_address.snmp_mac_address.size();
                
                System.out.println("length= "+len);
                int no_of_macs = len;
                String mac[]=new String[no_of_macs];
                
                for(int i=0;i<no_of_macs;i++){
                    mac[i]=snmp.unused.Walk_mac_address.snmp_mac_address.get(i);
                    mac[i]=mac[i].replaceAll(":", "-");
                    //System.out.println(""+mac[i]);
                }
                
                ArrayList<String> macs = snmp.unused.Walk_command.snmpWalk(ip, community, mac_targetOid);
                
                
                String port_targetOid = "1.3.6.1.2.1.17.4.3.1.2";
                ArrayList<String> ports = snmp.unused.Walk_command.snmpWalk(ip, community, port_targetOid);
                
                Iterator<String> it=macs.iterator();
                
                int ind = 0;
                int len_port=ports.size();
                while(it.hasNext()){
                    if(ind>(len_port-1)){break;}
                    macs.get(ind).replaceAll(":", "-");
                    //System.out.println(""+macs.get(ind));
                    //System.out.print(""+ports.get(ind)+"\n");
                    ind++;
                }
                
                
                String result_arp[][]=process.arp();
                for(int i =0;i<result_arp.length;i++){
                    //System.out.println("from arp "+result_arp[i][1]);
                }
                System.out.println("got data");
                for(int i =0;i<result_arp.length;i++){
                    for(int j=0;j<macs.size();j++){
                        //System.out.println("macs.get(j)="+macs.get(j));
                        if(macs.get(j).replaceAll(":", "-").equals(result_arp[i][1])){
                            System.out.println("THE IP for the mac  "+macs.get(j)+"  is  "+result_arp[i][0] +" and the port is"+ports.get(j));
                        }
                    }
                }
                
                System.out.println("arp len="+result_arp.length+" mac from snmp="+mac.length);
    }    
}
