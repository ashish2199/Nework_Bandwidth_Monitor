package snmp.unused;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Ashish Padalkar
 */
public class Ping {
    
    public static boolean isReachableByPing(String host,boolean show_output) {
        try{
                String cmd = "";
                if(System.getProperty("os.name").startsWith("Windows")) {	
                        // For Windows
                //-n Determines the number of echo requests to send.Defaul=4.
                        cmd = "ping -w 90 -n 1 " + host;
                } else {
                        // For Linux and OSX
                    // -c Stop after sending count ECHO_REQUEST packets.
                        cmd = "ping -c 1 " + host;
                }
                
                //Process myProcess = new ProcessBuilder(cmd, host).start();
                
                
                
                Process myProcess = Runtime.getRuntime().exec(cmd);
                
                myProcess.waitFor();
                
        if(show_output == true){
        
                InputStream output = myProcess.getInputStream();
                
                StringBuilder sb = new StringBuilder();
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
                System.out.println(""+sb.toString());
        }
                if(myProcess.exitValue() == 0) {
                        //System.out.println("Function executed successfully");
                        return true;
                } else {
                    //System.out.println("Function returned value which is not 0");
                        return false;
                }

        } catch( Exception e ) {

                e.printStackTrace();
                return false;
        }
}
    
    public static void main(String args[]){
        isReachableByPing("127.0.0.1",true);
    }
    
}
