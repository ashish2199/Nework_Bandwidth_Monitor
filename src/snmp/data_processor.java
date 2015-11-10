package snmp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class data_processor {
    
    ArrayList<record[][]> list_record = new ArrayList<record[][]>();
    
    public static void main(String args[]){
        
    }    
    
    boolean initialise;
    static int startindex;
    static ArrayList<record> current_data=new ArrayList();
    static int list_index;
    
    /*public static ArrayList<record[][]> getdata(){
    ArrayList<record[][]> data=null;
    data=snmp.Datacollector.get_tmp_recordings();
    if(data==null){
    System.out.println("No data returned by datacollector");
    }
    return data;
    }*/
    
    public static void show_raw_data(ArrayList<record> data){
        for(record r:data){
            System.out.println("Port= "+r.port+"  inbw= "+r.in_bw+"  out_bw= "+
            r.out_bw+" data_in_mb="+r.data_in_mb+" data_out_mb"+r.data_out_mb +
            " datarate_in"+r.data_datarate_in_mbps+" datarate_out"+r.data_datarate_out_mbps);
                       
        }   
    }
    
    //temporary buffer for storing the data which is to be written to database
    public static String database_data[][];
    //MySql date time datatype format 
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static void prepare_data(ArrayList<record> data){
        
        int no_of_records=data.size();
        database_data=new String[no_of_records][9];
        int index=0;
        for(record r:data){
            database_data[index][0]=r.ip;
            
            database_data[index][1]=r.port;
            
            database_data[index][2]=sdf.format(r.date);
            
            Double inbw = r.in_bw;
            Double rounded_inbw= Math.floor(inbw * 10000000) / 10000000;
            database_data[index][3]=rounded_inbw.toString();
            
            Double outbw = r.out_bw;
            Double rounded_outbw= Math.floor(outbw * 10000000) / 10000000;
            database_data[index][4]=rounded_outbw.toString();
            
            Double datain = r.data_in_mb;
            Double rounded_datain= Math.floor(datain * 10000000) / 10000000;
            database_data[index][5]=rounded_datain.toString();
            
            Double dataout = r.data_out_mb;
            Double rounded_dataout= Math.floor(dataout * 10000000) / 10000000;
            database_data[index][6]=rounded_dataout.toString();
            
            Double dataratein = r.data_datarate_in_mbps;
            Double rounded_dataratein= Math.floor(dataratein * 10000000) / 10000000;
            database_data[index][7]=rounded_dataratein.toString();
            
            Double datarateout = r.data_datarate_out_mbps;
            Double rounded_datarateout= Math.floor(datarateout * 10000000) / 10000000;
            database_data[index][8]=rounded_datarateout.toString();
            
            index++;
        }
        
    }
    
    public static String[][] get_database_data(){
        if(database_data==null){
            System.out.println(" NULL data ");
        }
        return database_data;
    }
    
    public static void show_processed_data(){
        System.out.println("");
        for(int i=0;i<database_data.length;i++){
            for(int j=0;j<database_data[i].length;j++){
                System.out.print(""+database_data[i][j]+"   ");
            }
            System.out.println("");
        }
    }
    /*    for(record[][] k:data){
            for(Integer rm:Datacollector.list_of_ports){
                record[] tmp = k[rm];
                for(int j = Datacollector.startindex-1;j<=Datacollector.endindex;j++){
                    if(j<0){j=0;}
                    record r = tmp[j];
                    System.out.println("Port= "+r.port+"  inbw= "+r.in_bw+"  out_bw= "+r.out_bw+"   date= "+r.date.toString());
                }
                
            }
        }
    */
    
    
    
    
    /*class repeat_function implements Runnable{
    int time_sec=0;
    long startInoctets =0;
    long prevInoctets =0;
    long prevOutoctets =0;
    @Override
    public void run(){
    try {
    if(initialise){
    startindex=0;
    current_data.clear();
    }
    snmp.record r = new snmp.record("3");
    
    }
    catch (IOException ex) {
    Logger.getLogger(data_processor.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    }*/
    
    
}
