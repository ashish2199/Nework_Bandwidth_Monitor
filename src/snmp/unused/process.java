package snmp.unused;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Ashish Padalkar
 */
public class process {
    
    public static void ping_all(String subnet){
           int timeout=1000;
            for (int i=120;i<255;i++){
                String host=subnet + "." + i;
                System.out.println("Pinging "+host);
                if (Ping.isReachableByPing(host,false)){
                    Ping.isReachableByPing(host,true);
                    System.out.println(host + " is reachable");
                }
            }
    }
    
    public static String[][] arp(){
        
        //calculate length of subnet mask
        
        /*InetAddress localHost;
        int subnetlength;
        try {
        localHost = Inet4Address.getLocalHost();
        
        NetworkInterface networkInterface;
        
        networkInterface = NetworkInterface.getByInetAddress(localHost);
        subnetlength=networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
        }
        catch (UnknownHostException ex) {
        Logger.getLogger(process.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SocketException ex) {
        Logger.getLogger(process.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        
        /*
        InetAddress ip = null;
        try {
        ip = Inet4Address.getLocalHost();
        for (Byte b : ip.getAddress()) {
        System.out.println(""+Integer.toBinaryString(b)); // Get your binary string 1110011101010110
        }
        } catch (UnknownHostException ex) {
        Logger.getLogger(process.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        String ping_result = process("ping 10.2.51.255",true);
        String result = process("arp -a",false);
        String arp_data[]=result.split(System.getProperty("line.separator"));
        //first line is blank,second line is heading and third line is interface name
        int len=arp_data.length-3;
        String[][] ip_mac=new String[len][3];
        for(int i=3;i<arp_data.length;i++){
            //split based on consecutive spaces
            String single_arp_row[]=arp_data[i].split("\\s+");
            //first is "" so start from 1
                for(int j=1;j<single_arp_row.length;j++){
                    ip_mac[i-3][j-1]=single_arp_row[j];
                    //System.out.println(""+single_arp_row[j]);
                }
        }
        
        
        return ip_mac;
    }
    
    public static void main(String args[]){
        process("arp -a",true);
    }
    
    public static String process(String cmd,boolean show_output ) {
        String output_process="";
        StringBuilder sb = new StringBuilder();
        
        try{    
                Process myProcess = Runtime.getRuntime().exec(cmd);
                //System.out.println("starting to wait");
                //myProcess.waitFor();
                
                //System.out.println("completed process");
                
                InputStream output = myProcess.getInputStream();
                
                BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(output));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + System.getProperty("line.separator"));
			}
		} 
                finally {
			br.close();
		}
                if(show_output==true){
                    System.out.println(""+sb.toString());
                }
                
                /*if(myProcess.exitValue() == 0) {
                //System.out.println("Process Exit value:0");
                
                } else {
                System.out.println("Process Exit value:not 0");
                }*/
                output_process = sb.toString();
        } catch( Exception e ) {
            e.printStackTrace();
        }
        return output_process;
}

}

