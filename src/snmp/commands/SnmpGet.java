package snmp.commands;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import snmp.Gui_fxmlController;
public class SnmpGet {

	public static final int DEFAULT_VERSION = SnmpConstants.version2c;
	public static final String DEFAULT_PROTOCOL = "udp";
	public static final int DEFAULT_PORT = 161;
	public static final long DEFAULT_TIMEOUT = 3 * 1000L;
	public static final int DEFAULT_RETRY = 3;

	public static CommunityTarget createDefault(String ip, String community) {
		Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip
				+ "/" + DEFAULT_PORT);
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(community));
		target.setAddress(address);
		target.setVersion(DEFAULT_VERSION);
		target.setTimeout(DEFAULT_TIMEOUT); // milliseconds
		target.setRetries(DEFAULT_RETRY);
		return target;
	}

	public static PDU snmpGet(String ip, String community, String oid) throws IOException {

		CommunityTarget target = createDefault(ip, community);
		Snmp snmp = null;
                PDU response = null;
		try {
			PDU pdu = new PDU();
			// pdu.add(new VariableBinding(new OID(new int[]
			// {1,3,6,1,2,1,1,2})));
			pdu.add(new VariableBinding(new OID(oid)));

			DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			snmp.listen();
			
			pdu.setType(PDU.GET);
			ResponseEvent respEvent = snmp.send(pdu, target);
			//System.out.println("PeerAddress:" + respEvent.getPeerAddress());
			response = respEvent.getResponse();

			if (response == null) {
                                Gui_fxmlController.create_dialog("SNMP Agent sent a null response");
				System.out.println("response is null, request time out");
			}
                        
		} 
                catch (Exception e) {
			e.printStackTrace();
			System.out.println("SNMP Get Exception:" + e);
		} 
                finally {
			if (snmp != null) {
                            snmp.close();
			}
		}
            return response;
	}

        public static long getPDUvalue(PDU response){
           long IfInOctets  = -1;
            //System.out.println("response pdu size is " + response.size());
            for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    String value = vb.getVariable().toString();
                    if(!value.equals("Null")){
                    IfInOctets  = Long.parseLong(value);
                    }else{
                    IfInOctets=NULL_DATA_RECIEVED;
                    }
            }
            return IfInOctets;
        }
        
    public static final int NULL_DATA_RECIEVED = -10;
        
        public static String getPDUStringvalue(PDU response){
                String response_str="";
                //System.out.println("response pdu size is " + response.size());
                for (int i = 0; i < response.size(); i++) {
                        VariableBinding vb = response.get(i);
                        //System.out.println(vb.getOid() + " = " + vb.getVariable());
                        response_str+=vb.getVariable().toString();
                        //System.out.println(vb.getVariable());
                }
            return response_str;
        }
}
