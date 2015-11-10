package snmp;
 
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.snmp4j.PDU;
import snmp.commands.SnmpGet;

    
class record{
int index;
long inoctets;
long outoctets;
long speed;
Date date;
String ip;
double in_bw;
double out_bw;
String port;
double data_in_mb;
double data_out_mb;
double data_datarate_in_mbps;
double data_datarate_out_mbps;

public String community = "public";
static String oid_IFindexes="1.3.6.1.2.1.2.2.1.1";
static String oid_speed = "1.3.6.1.2.1.2.2.1.5.";
static String oid_status= "1.3.6.1.2.1.2.2.1.8.";
static String oid_interface_dscr="1.3.6.1.2.1.2.2.1.2.";

static String oid_basic_inoctetes = "1.3.6.1.2.1.2.2.1.10.";
static String oid_basic_outoctetes = "1.3.6.1.2.1.2.2.1.16.";

static String oid_inoctetes;
static String oid_outoctetes;


static String oid_HCInoctetes = " 1.3.6.1.2.1.31.1.1.1.6.";
static String oid_HCOutoctetes = " 1.3.6.1.2.1.31.1.1.1.10.";

record(String ip,String port,String community) throws IOException{
    
    this.port = port;
    this.ip=ip;
    
    date = new Date();
                              
    String InOctets_oidval = oid_inoctetes+port;
    PDU In_resp_from_get = SnmpGet.snmpGet(ip, community, InOctets_oidval);
    inoctets = SnmpGet.getPDUvalue(In_resp_from_get);
    
    String OutOctets_oidval = oid_outoctetes+port;
    PDU Out_resp_from_get = SnmpGet.snmpGet(ip, community, OutOctets_oidval);
    outoctets = SnmpGet.getPDUvalue(Out_resp_from_get);
                            
    String IfSpeed_oidval = oid_speed+port;
    PDU speed_resp_from_get = SnmpGet.snmpGet(ip, community, IfSpeed_oidval);
    speed = SnmpGet.getPDUvalue(speed_resp_from_get);
    
    
    
}
record(record r){
    this.ip=r.ip;
    this.data_datarate_in_mbps=r.data_datarate_in_mbps;
    this.data_datarate_out_mbps=r.data_datarate_out_mbps;
    this.data_in_mb=r.data_in_mb;
    this.data_out_mb=r.data_out_mb;
    this.date=r.date;
    this.in_bw=r.in_bw;
    this.index=r.index;
    this.inoctets=r.inoctets;
    this.out_bw=r.out_bw;
    this.outoctets=r.outoctets;
    this.port=r.port;
    this.speed=r.speed;
}

    

}




public class Datacollector {
    
    static void check_if_HC_octets_present(String ip,String port,String community) throws IOException{
        
        System.out.println("Checking the type of InOctets");
        
        System.out.println("First using HCOctets");
        String oid_HCInoctetes = " 1.3.6.1.2.1.31.1.1.1.6.";
        String oid_HCOutoctetes = " 1.3.6.1.2.1.31.1.1.1.10.";
        
        String InOctets_oidval = oid_HCInoctetes+port;
        PDU In_resp_from_get = SnmpGet.snmpGet(ip, community, InOctets_oidval);
        long inoctets = SnmpGet.getPDUvalue(In_resp_from_get);

        String OutOctets_oidval = oid_HCOutoctetes+port;
        PDU Out_resp_from_get = SnmpGet.snmpGet(ip, community, OutOctets_oidval);
        long outoctets = SnmpGet.getPDUvalue(Out_resp_from_get);
        System.out.println("Got value outoctets:"+outoctets+" inoctets:"+inoctets);
        
        String oid_inoctetes = "1.3.6.1.2.1.2.2.1.10.";
        String oid_outoctetes = "1.3.6.1.2.1.2.2.1.16.";
        
        record.oid_inoctetes=oid_HCInoctetes;
        record.oid_outoctetes=oid_HCOutoctetes;
        
        System.out.println("setting oid to inoctets= "+record.oid_inoctetes+" and outoctets to"+record.oid_outoctetes);
        
        if(inoctets==-10&&outoctets==-10){
        
            System.out.println("Changing octets oid");
            
            InOctets_oidval = oid_inoctetes+port;
            In_resp_from_get = SnmpGet.snmpGet(ip, community, InOctets_oidval);
            inoctets = SnmpGet.getPDUvalue(In_resp_from_get);
            
            OutOctets_oidval = oid_outoctetes+port;
            Out_resp_from_get = SnmpGet.snmpGet(ip, community, OutOctets_oidval);
            outoctets = SnmpGet.getPDUvalue(Out_resp_from_get);
            
            record.oid_inoctetes=oid_inoctetes;
            record.oid_outoctetes=oid_outoctetes;
            
            System.out.println("changed oid to inoctets= "+record.oid_inoctetes+" and outoctets to"+record.oid_outoctetes);
        }
        
        
        
    }
    
    
    //used for circular array
    static int startindex;
    static int endindex;
    
    //size of the circular array
    static int no_of_slots=200;
    
    //size of circular array queue
    static int size_record_queue=10;
    
    static record tmp_recordings[][]; 
    
    static ArrayList<Integer> list_of_ports=new ArrayList<Integer>();
    static int tmp_index;
    
    //after how many seconds shoulf we update
    static final int update_freq = 12;
    //after how many updates should we calculate bandwidth
    static final int calc_freq=1;
    //after how many calculations of bandwidth should the data be stored in database
    static final int data_store_freq = 1;
    
    //for storing the data stored
    static ArrayList<record> data_collected=new ArrayList<record>();
    
    //used for GUI
    static ArrayList<record> data_collected_copy_for_gui=new ArrayList<record>();
    
    //used for labels in gui
    static double current_inbw;
    static double current_outbw;
    
    //used for mapping index to port and ip
    static String mapping_table[][];
    
    //slots are used as we car add and remove various devices so 
    //we need a mechanism to add so that we can allocate slot for device to be added
    static ArrayList<Integer> freeslots=new ArrayList();
    static ArrayList<Integer> usedslots=new ArrayList();
    
    static ArrayList<Integer> portslist=new ArrayList();
    static ArrayList<String> iplist=new ArrayList();
    static int index=0;
    
    public static void add(String ip,int port,String community){
        if(!iplist.contains(ip)||!portslist.contains(port)){
                if(freeslots.size()!=0){
                    index=freeslots.get(0);
                }
                else{
                    System.out.println("error No free slots;");
                }
                freeslots.remove((Integer)index);
                usedslots.add((Integer)index);
                portslist.add(port);
                iplist.add(ip);
                mapping_table[index][0]=ip;
                mapping_table[index][1]=""+port;
                mapping_table[index][2]=community;
        }
    }
    
    public static void remove(String ip,int port){
            if(iplist.contains(ip)&&portslist.contains(port)){
                for(int i:usedslots){
                    if(mapping_table[i][0].equals(ip)&&mapping_table[i][1].equals(""+port)){
                        mapping_table[i]=null;
                        usedslots.remove((Integer)i);
                        freeslots.add((Integer)i);
                        portslist.remove((Integer)port);
                        iplist.remove(ip);
                        break;
                    }
                }
            }
    }
    

    
    //sent to gui
    public static ArrayList get_current_recordings(){
        System.out.println("sending arraylist of size"+data_collected_copy_for_gui.size());
        return data_collected_copy_for_gui;
    }
    
    //copies to create 
    public static void copyArrayList(){
        //both the copies are referring to same object as only the object references 
        //get copied and not the object themselves
        try{
            for(record r:data_collected){
                record new_r = new record(r);
                data_collected_copy_for_gui.add(new_r);
            }
        }
        catch (Exception ex) {
            gui_javafx.pst.println("Error in copying arraylist for gui Datacollector.copyArrayList()");
            ex.printStackTrace(gui_javafx.pst);
        }
        //data_collected_copy_for_gui.addAll(data_collected);
        
    }
    
    public static void clear_copyArrayList(Date last_shown){
    
        Iterator<record> iter = data_collected_copy_for_gui.iterator();

        while (iter.hasNext()) {
            record r = iter.next();

            if(r.date.before(last_shown)){
                iter.remove();
            }
                
        }
        
        
        //concurrent modification exception
        /*for(record r:data_collected_copy_for_gui){
        if(r.date.before(last_shown)){
        data_collected_copy_for_gui.remove(r);
        }
        }*/
    }
    
    //not used now 
    public static void addports(int port){
        if(!list_of_ports.contains(port)){
            list_of_ports.add(port);
        }
        else{
            System.out.println("The port you are trying to add is already added");
        }
    }
    
    static int recordindex=0;
    
    //not used now    
    public static void add_device(String ip,int port){
    
        if(!list_of_ports.contains(port)){
            list_of_ports.add(port);
        }
        else{
            System.out.println("The port you are trying to add is already added");
        }
        
    }
    
    
    
    
    public static void calc_bandwidth(record data[][],int start,int end){
        int i,x,j;
        int loop;
        
        if(start<end){
            loop=end-start;
        }
        else{
            loop=(size_record_queue-start)+end;
        }
        


        //System.out.println("start="+start+" end="+end+" loopsize="+loop);
        for(Integer p:usedslots){
            
            
           // System.out.println("for Port = "+p);
                                 
            for(j=0,i=start+1;j<loop;i++,j++){
            
                if(data[p][(i-1)%size_record_queue]==null)
                {
                    continue;
                }
            
            //System.out.println("subtracting "+(i%size_record_queue)+" from "+((i-1)%size_record_queue));
            long delta_inoctets=(data[p][i%size_record_queue].inoctets-data[p][(i-1)%size_record_queue].inoctets);
            long delta_outoctets=(data[p][i%size_record_queue].outoctets-data[p][(i-1)%size_record_queue].outoctets);
            
            
            
            
            long ifspeed = data[p][i%size_record_queue].speed;
            long time_difference=(data[p][i%size_record_queue].date.getTime()-data[p][(i-1)%size_record_queue].date.getTime());
            long delta_seconds = TimeUnit.MILLISECONDS.toSeconds(time_difference);
            
            if(delta_seconds<update_freq){
                delta_seconds=update_freq;
            }
            
            //System.out.println("delta inoctets= "+delta_inoctets+" delta_outoctets "+delta_outoctets);
            //System.out.println("delta_seconds= "+delta_seconds);
            
            data[p][i%size_record_queue].data_in_mb=((double)delta_inoctets)/(1024*1024); 
            data[p][i%size_record_queue].data_out_mb=((double)delta_outoctets)/(1024*1024);
            data[p][i%size_record_queue].data_datarate_in_mbps=(data[p][i%size_record_queue].data_in_mb/(double)delta_seconds)*8;
            data[p][i%size_record_queue].data_datarate_out_mbps=(data[p][i%size_record_queue].data_out_mb/(double)delta_seconds)*8;
            
            //System.out.println("data_in_mb "+data[p][i%size_record_queue].data_in_mb+"data_out_mb "+data[p][i%size_record_queue].data_out_mb);
            
            double in_num=(double)delta_inoctets*8*100;
            double in_denom=(double)delta_seconds*ifspeed;
            double inbw=in_num/in_denom;
            
            double out_num=(double)delta_outoctets*8*100;
            double out_denom=(double)delta_seconds*ifspeed;
            double outbw=out_num/out_denom;
            
            inbw= Math.floor(inbw * 100000) / 100000;
            outbw= Math.floor(outbw * 100000) / 100000;
            
            
            data[p][i%size_record_queue].in_bw=inbw;
            data[p][i%size_record_queue].out_bw=outbw;
            
            //leave counter wraps
            if(delta_inoctets>=0&&delta_outoctets>=0){
                data_collected.add(data[p][i%size_record_queue]);
            }
            
            
            current_inbw=inbw;
            current_outbw=outbw;
            
            
            //System.out.println("inbw = "+inbw+" outbw = "+outbw);
            //System.out.printf("inbw = %.4f  outbw = %.4f \n",inbw,outbw);
        }
    
            
        }
            //current_data.add(data);
            //list_index++;
    }
    
    public static void initialise_mapping(){
        mapping_table=new String[no_of_slots][3];
        usedslots.clear();
        freeslots.clear();
        for(int j=0;j<no_of_slots;j++){
            freeslots.add(j);
        }
    }
    //called in data model for records
    public static String get_snmp_description(String ip,int port,String community) throws IOException{
        String oid = record.oid_interface_dscr+port;
        PDU In_resp_from_get = SnmpGet.snmpGet(ip,community, oid);
        String hexresponse=SnmpGet.getPDUStringvalue(In_resp_from_get);
        //can also be in hexadecimal 
        Pattern pattern =Pattern.compile("[0-9a-fA-f][0-9a-fA-f]:[0-9a-fA-f][0-9a-fA-f]");
                                        Matcher matcher = pattern.matcher(hexresponse);
                                        
                                       String description=hexresponse;
                                       
                                       if(matcher.find()){
                                       description="";    
                                       String newhexstring = hexresponse.replaceAll(":"," ");
                                            String str[]=newhexstring.split(" ");

                                            for(String s:str){
                                                    int num = hex2decimal(s);
                                                    description += (char)num;
                                                }
                                        }
        
        return description;
        
    }
    
    public static int hex2decimal(String s) {
             String digits = "0123456789ABCDEF";
             s = s.toUpperCase();
             int val = 0;
             for (int i = 0; i < s.length(); i++) {
                 char c = s.charAt(i);
                 int d = digits.indexOf(c);
                 val = 16*val + d;
             }
             return val;
         }
    public static String get_snmp_status(String ip,int port,String community) throws IOException{
        String status="";
        String oid = record.oid_status+port;
        PDU In_resp_from_get = SnmpGet.snmpGet(ip,community, oid);
        long status_code=SnmpGet.getPDUvalue(In_resp_from_get);
        if(status_code==1){status="up";}
        if(status_code==2){status="down";}
        if(status_code==3){status="testing";}
        if(status_code==5){status="dormant";}
        if(status_code==6){status="notpresent";}
        
        return status;
    }
    
    public static Double get_snmp_speed(String ip,int port,String community) throws IOException{
        String oid = record.oid_speed+port;
        PDU In_resp_from_get = SnmpGet.snmpGet(ip,community, oid);
        long speed_in_bps=SnmpGet.getPDUvalue(In_resp_from_get);
        Double speed = ((double)speed_in_bps)/(1024*1024);
        return speed;
    }
    
    static ScheduledExecutorService scheduler;
    
	public static void main(String[] args) throws IOException {
                //snmp.process.process("ping 127.0.0.1",true);
                System.out.println("new circular array based implementation");
                Timer timer = new Timer();
                
                //list_index=0;
                
                // regular interval at which the task will be performed 100000000
                int interval = update_freq;
                
                //after how many seconds it will start
                int delay = 0;
                
                initialise_mapping();
                
                add("127.0.0.1",7,"public");
                add("127.0.0.1",36,"public");
                add("127.0.0.1",37,"public");
                add("127.0.0.1",39,"public");
                add("127.0.0.1",41,"public");
                add("127.0.0.1",42,"public");
                add("127.0.0.1",43,"public");
                add("127.0.0.1",44,"public");
                add("127.0.0.1",46,"public");
                add("127.0.0.1",47,"public");
                
                
                
                repeatfunctions tt= new repeatfunctions();
                scheduler = Executors.newScheduledThreadPool(1);
                scheduler.scheduleWithFixedDelay(tt, delay, interval, TimeUnit.SECONDS);
                
	}
        
        static int time_sec=0;
        
        static int timewrap=2147483647-(2147483647%(update_freq*3));
        
        static boolean initialise=true;
        static boolean pause=false;
        static Boolean initialise_in_octets_type=true;
    
        public static void initialise_data_collection(){
            try{
                                if(initialise){
                                    startindex=0;
                                    endindex=-1;
                                    initialise_mapping();
                                    tmp_recordings = new record[no_of_slots][size_record_queue];
                                    System.out.println("Table created");
                                    initialise=false;
                                }
                            }
                            catch (Exception ex) {
                                            gui_javafx.pst.println("Error in Initialising Datacollector.repeatfunction()");
                                            ex.printStackTrace(gui_javafx.pst);
                            }
        }
        public static class repeatfunctions implements Runnable{
    
                    
                    long startInoctets =0;
                    long prevInoctets =0;
                    long prevOutoctets =0;
                    public void run() {
                        
                        
                    try {
                            System.out.println("Inside repeat function");
                            
                            if(initialise_in_octets_type){
                                if(!usedslots.isEmpty())
                                {   
                                    Iterator<Integer> iter = usedslots.iterator();
                                    while (iter.hasNext()) {
                                        
                                        Integer i = iter.next();
                                        check_if_HC_octets_present(mapping_table[i][0],mapping_table[i][1],mapping_table[i][2]);
                                        break;
                                    }
                                    
                                    initialise_in_octets_type=false;
                                
                                }
                            }
                            
                            
                            if(!pause){
                                
                                try{
                                    
                                    Iterator<Integer> iter = usedslots.iterator();
                                    while (iter.hasNext()) {
                                        Integer i = iter.next();
                                        tmp_recordings[i][tmp_index%size_record_queue]=new record(mapping_table[i][0],mapping_table[i][1],mapping_table[i][2]);
                                    }
                                    //giving concurrent modification exception
                                    /*
                                        for(Integer i:usedslots){                                       //ip,port,coomunity
                                        tmp_recordings[i][tmp_index%size_record_queue]=new record(mapping_table[i][0],mapping_table[i][1],mapping_table[i][2]);
                                        }
                                    */
                                }
                                catch (Exception ex) {
                                    gui_javafx.pst.println("Error in creating temporary records array Datacollector.repeatfunction() at time:"+time_sec);
                                    ex.printStackTrace(gui_javafx.pst);
                                }
                                
                                    try{
                                        System.out.println("time:"+time_sec);
                                        tmp_index++;
                                        endindex++;
                                        endindex=endindex%size_record_queue;
                                    }
                                    catch (Exception ex) {
                                        gui_javafx.pst.println("Error in modifying indexes for circular array Datacollector.repeatfunction()");
                                        ex.printStackTrace(gui_javafx.pst);
                                    }
                                
                                try{
                                    if(time_sec%(update_freq*calc_freq)==0&&time_sec!=0){
                                            calc_bandwidth(tmp_recordings, startindex, endindex);
                                            startindex=endindex;
                                            copyArrayList();
                                    }
                                }
                                catch (Exception ex) {
                                    gui_javafx.pst.println("Error in calc_bandwidth  Datacollector.repeatfunction()");
                                    ex.printStackTrace(gui_javafx.pst);

                                }
                            }
                            
                            if(time_sec%(update_freq*calc_freq*data_store_freq)==0&&time_sec!=0){
                               try{
                                    data_processor.show_raw_data(data_collected);
                                    
                                    try{
                                        data_processor.prepare_data(data_collected);
                                    }
                                    catch (Exception ex) {
                                        gui_javafx.pst.println("Error in preparing data for database Datacollector.repeatfunction()");
                                        ex.printStackTrace(gui_javafx.pst);
                                    }
                                    
                                    String data[][] = null;
                                    try{
                                        data=data_processor.get_database_data();
                                    }
                                    catch (Exception ex) {
                                        gui_javafx.pst.println("Error in preparing data for database Datacollector.repeatfunction()");
                                        ex.printStackTrace(gui_javafx.pst);
                                    }
                                    data_processor.show_processed_data();
                                    
                                    try{
                                        if(data==null){
                                            System.out.println("NO DATA ( NULL )TO BE INSERTED IN DATABASE");
                                        }
                                        else{
                                            database.insert_data(data);
                                        }
                                    }
                                    catch (Exception ex) {
                                        gui_javafx.pst.println("Error in inserting data to database Database.insert_data() in Datacollector.repeatfunction()");
                                        ex.printStackTrace(gui_javafx.pst);
                                    }
                                    data_collected.clear();
                               }
                               catch (Exception ex) {
                                gui_javafx.pst.println("Error in sending data to database Datacollector.repeatfunction()");
                                ex.printStackTrace(gui_javafx.pst);
                               }
                               
                            }
                        
                            time_sec+=update_freq;  
                            if(time_sec>(timewrap)){
                               time_sec=2147483647%update_freq;
                            }
                            
                        } 
                        catch (Exception ex) {
                            gui_javafx.pst.println("Error in Data collecting and processing Datacollector.repeatfunction()");
                            ex.printStackTrace(gui_javafx.pst);

                        }
                        
                    }
                }

}
