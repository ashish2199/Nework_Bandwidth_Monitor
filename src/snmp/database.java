package snmp;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class database {
 
    public static void main(String[] args) {
     
    }
    
    //for data coming from database
    static int places_truncation = 3;
    
    //after how many days should the records be deleted
    static int delete_freq=gui_javafx.db_del_freq_days;
    
    static String database_name=gui_javafx.dbname;
    
    // by default localhost
    static String db_ip=gui_javafx.dbloc;
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://"+db_ip+"/"+database_name;

    //  Database credentials
    static final String USER = gui_javafx.dbusr;
    static final String PASS = gui_javafx.dbpwd;
    
    static String tablename_records = "recorded_data";
    static String tablename_devces = "device_data";
    
    static Connection conn;
    static Statement stmt;
    
    static void check_for_database(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String MySQL_loc = "jdbc:mysql://"+db_ip+"/";
            System.out.println(""+MySQL_loc);
            conn = DriverManager.getConnection(MySQL_loc,USER,PASS);
            System.out.println("the database name is"+database_name);
            String create_db_sql = "CREATE DATABASE IF NOT EXISTS "+database_name+";";
            stmt = conn.createStatement();
            stmt.executeUpdate(create_db_sql);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) 
        {
                gui_javafx.pst.println("Error in checking for database database.check_for_database()");
                ex.printStackTrace(gui_javafx.pst);
        }
    }
    
    public static void create_event_deletion() throws SQLException{
        
        create_connection();
         
        stmt = conn.createStatement();
        
        String turn_on_scheduler = "SET GLOBAL event_scheduler = ON";
        stmt.executeUpdate(turn_on_scheduler);
        
        String drop_event ="DROP EVENT if EXISTS delete_old_records;";
        stmt.executeUpdate(drop_event);
        
        String create_event = "CREATE EVENT IF NOT EXISTS delete_old_records "
                            + "ON SCHEDULE EVERY 1 DAY "
                            + "DO delete from "+database_name+"."+tablename_records+" where "
                            + "DATE_SUB(NOW(), INTERVAL "+delete_freq+" DAY) > "+database_name+"."+tablename_records+".record_date;";
        System.out.println(""+create_event);
        stmt.executeUpdate(create_event);

    }
    
    public static void check_tables(){
     try {

         check_for_database();
         
         create_event_deletion();
         
         create_connection();
         
         stmt = conn.createStatement();
         
         String sql[]=new String[2];
         
         sql[0] =   "CREATE TABLE IF NOT EXISTS "+database_name+"."+tablename_records+"("
                 + "record_id INT PRIMARY KEY AUTO_INCREMENT," +
                        "ip varchar(15)," +
                        "port INT," +
                        "record_date DATETIME," +
                        "inbw DOUBLE(12,8)," +
                        "outbw DOUBLE(12,8)," +
                        "datain DOUBLE(20,8)," +
                        "dataout DOUBLE(20,8)," +
                        "indatarate DOUBLE(12,8)," +
                        "outdatarate DOUBLE(12,8));";
         
        sql[1]  =   "CREATE TABLE IF NOT EXISTS "+database_name+"."+tablename_devces+"(" +
                    "record_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "device_ip varchar(15)," +
                    "device_port smallint(6)," +
                    "community varchar(50)," +
                    "device_speed DOUBLE(12,8),"  +
                    "user_description varchar(255)," +
                    "snmp_description varchar(255)" +
                    ");";
         
        
        for(String s:sql){
            stmt.executeUpdate(s);
        }
        
        /*
        drop trigger if exists bmn.delete_old;

        CREATE trigger bmn.delete_old after insert ON bmn.recorded_data
        FOR EACH ROW

        delete from bmn.recorded_data where DATE_SUB(NOW(), INTERVAL 60 SECOND) > bmn.recorded_data.record_date;
        */
        
        
        }
        catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) 
        {
                   gui_javafx.pst.println("Error in checking fortables database.check_tables()");
                   ex.printStackTrace(gui_javafx.pst);
        }
    }
    
    public static void create_connection(){
        try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);

           /*//STEP 4: Execute a query
           System.out.println("Creating statement...");
           stmt = conn.createStatement();
           String sql;
           sql = "SELECT * FROM emp";
           ResultSet rs = stmt.executeQuery(sql);
           
           //STEP 5: Extract data from result set
           while(rs.next()){
           //Retrieve by column name
           int id  = rs.getInt("id");
           Object o = rs.getObject("name");
           
           //Display values
           System.out.println("ID: " + id);
           System.out.print("Name: " + o.toString());
           }
           //STEP 6: Clean-up environment
           rs.close();
           stmt.close();
           conn.close();
           */
        }catch(SQLException se){
           //Handle errors for JDBC
           se.printStackTrace();
        }catch(Exception e){
           //Handle errors for Class.forName
           e.printStackTrace();
        }
        /*        finally
        {
        //finally block used to close resources
        try{
        if(stmt!=null)
        stmt.close();
        }catch(SQLException se2){
        }// nothing we can do
        try{
        if(conn!=null)
        conn.close();
        }catch(SQLException se){
        se.printStackTrace();
        }//end finally try
        }//end try*/
        System.out.println("END");
    }
    
    public static String[] get_max_min_dates(){
        String dates[]=new String[2];
        try {
            
         create_connection();
         stmt = conn.createStatement();
         //start
         String sql="select max(record_date) as max,min(record_date) as min from "+database_name+"."+tablename_records+";";
         
         ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()){
           //start 
           String min = rs.getString("min");
           //end
           String max = rs.getString("max");
           dates[0]=min;
           dates[1]=max;
            System.out.println("inside database"+dates[0]+"max"+dates[1]);
        }
        
        rs.close();
        stmt.close();
        conn.close();

//end
         
     } 
     catch (SQLException ex) {
         Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
     }
     catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
     }
     return dates;
    }
    
    
    
    
    
    
    
    public static void insert_data(String data[][]){
     try {
         create_connection();
         stmt = conn.createStatement();
         for(int i=0;i<data.length;i++){
                if(data[i][3].equals("Infinity")||data[i][4].equals("Infinity")){
                    System.out.println("Skipping as Infinite data recieved for "+data[i][0]+":"+data[i][1]);
                    continue;
                }
                if(data[i][3].equals("NaN")||data[i][4].equals("NaN")){
                    System.out.println("Skipping as NO data recieved for "+data[i][0]+":"+data[i][1]);
                    continue;
                }
                String sql = "INSERT INTO "+database_name+"."+tablename_records+"(ip,port,record_date,inbw,outbw,datain,dataout,indatarate,outdatarate) "
                        + "VALUES("+"'"+data[i][0]+"'"
                        +","+data[i][1]+",'"+data[i][2]+"',"+data[i][3]+","+
                        data[i][4]+","+data[i][5]+","+data[i][6]+","+data[i][7]
                        +","+data[i][8]+");";
                System.out.println(""+sql);
            stmt.executeUpdate(sql);   
         }
         
     } 
     catch (SQLException ex) {
         Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
     }
     catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
     }
    }
    
    public static ArrayList<String[]> get_port_dscr(String ip){
        ArrayList<String[]> output= new ArrayList<>();
        
        try {
        create_connection();
        stmt = conn.createStatement();
        
        String where_ip="where R.ip='"+ip+"';";
        if(ip.equals("ALL")){where_ip="";}
        String sql = "select distinct R.port,D.user_description,D.snmp_description from "
                +database_name+"."+tablename_records+" R INNER JOIN "+database_name+"."+tablename_devces+" D ON R.ip=D.device_ip and R.port=D.device_port "
                + where_ip;
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()){
            
           String port = rs.getString("port");
           String dscr = rs.getString("user_description");
           String snmp_dscr = rs.getString("snmp_description");
           if(dscr==null||dscr.equals("")){
               dscr=snmp_dscr;
           }
           String[] s ={port,dscr,snmp_dscr};
           output.add(s);
            //System.out.println(""+item);
        }
        
        rs.close();
        stmt.close();
        conn.close();
         
        } 
        catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
        }
        return output;
    }
    
    public static ArrayList<String> get_all_distinct(String items,String ip){
        
        ArrayList<String> output= new ArrayList<>();
        
        try {
        create_connection();
        stmt = conn.createStatement();
        String where_ip="where ip='"+ip+"'";
        if(ip.equals("")){where_ip="";}
        String sql = "select distinct "+items+" from "+database_name+"."+tablename_records+" "+where_ip+";";
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()){
            
           String item = rs.getString(items);
           output.add(item);
            //System.out.println(""+item);
        }
        
        rs.close();
        stmt.close();
        conn.close();
         
     } 
     catch (SQLException ex) {
         Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
     }
     catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
     }
     return output;
    }
    
    public static ArrayList<gui_model_record> get_data(String sql){
        
        ArrayList<gui_model_record> output= new ArrayList<>();
        
        try {
        create_connection();
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()){
            
            String ip = rs.getString("ip");
            String snmp_description=rs.getString("snmp_description");
            String user_description=rs.getString("user_description");
            Double inbw= rs.getDouble("inbw");
            Double outbw= rs.getDouble("outbw");
            Double datain= rs.getDouble("datain");
            Double dataout= rs.getDouble("dataout");
            Double speed = rs.getDouble("speed");
            Double indatarate= rs.getDouble("indatarate");
            Double outdatarate= rs.getDouble("outdatarate");
           
            if (inbw<0.001) {
                inbw=0.001;
            }
            inbw = truncate(inbw, places_truncation);
            
            if (outbw<0.001) {
                outbw=0.001;
            }
            outbw=truncate(outbw,places_truncation);
            
            if (datain<0.001) {
                datain=0.001;
            }
            datain=truncate(datain,places_truncation);
           
            if (dataout<0.001) {
                dataout=0.001;
            }
            dataout=truncate(dataout,places_truncation);
           
            if (indatarate<0.001) {
                indatarate=0.001;
            }
            indatarate=truncate(indatarate,places_truncation);
           
            if (outdatarate<0.001) {
                outdatarate=0.001;
            }
            outdatarate=truncate(outdatarate,places_truncation);
            
            if (speed<0.001) {
                speed=0.001;
            }
            speed=truncate(speed,places_truncation);
            
            if(snmp_description==null||snmp_description.equals("")){snmp_description="";}
            if(user_description==null||user_description.equals("")){user_description="";}
            
            gui_model_record r=new gui_model_record(ip,user_description,snmp_description, inbw, 
                   outbw, datain, dataout,speed,indatarate, outdatarate);
            output.add(r);
            //System.out.println(""+id+" "+ip+" "+port+""+date+""+inbw+" "+outbw
            //        +" "+datain+" "+dataout+" "+indatarate+" "+outdatarate);
        }
        
        rs.close();
        stmt.close();
        conn.close();
         
     } 
     catch (SQLException ex) {
         Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
     }
     catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
     }
     return output;
    }
    
    public static Double truncate(Double num,int places){
            
            Double tens = 1.0;
            
            for(int i=0;i<places;i++){
                tens *= 10;
            }
            
            double mult     = (num*tens);
            mult = Math.round(mult);
            double result  =  mult/tens;
            return result;
    }
    
    public static ArrayList<gui_model_device> get_device_data(String sql){
        
        ArrayList<gui_model_device> output= new ArrayList<>();
        
        try {
            
            create_connection();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){

               int id  = rs.getInt("record_id");
               String ip = rs.getString("device_ip");
               int port  = rs.getInt("device_port");
               
               String community = rs.getString("community");
               String user_description = rs.getString("user_description");
               String snmp_dscr = rs.getString("snmp_description");
               gui_model_device d=new gui_model_device(ip, port, community,user_description);
               output.add(d);
            }

            rs.close();
            stmt.close();
            conn.close();
         
     } 
     catch (SQLException ex) {
         Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
     }
     catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
     }
     return output;
    }
    
    public static void add_device(gui_model_device d){
        
        try {
            create_connection();
            stmt = conn.createStatement();
        
            String ip = d.getIp();
                    //rs.getString("ip");
            int port  = d.getPort();
                    //rs.getInt("port");
            String community = d.getCommunity();
                    //rs.getString("community");
            Double speed = d.getSpeed();
            String user_description = d.getUser_description();
                    //rs.getString("user_description");
            String snmp_description = d.getSnmp_description();

            String sql="INSERT INTO  "+database_name+"."+tablename_devces+" (device_ip,device_port,community,device_speed,user_description,snmp_description) VALUES('"+ip+"',"+
                    port+",'"+community+"',"+speed+",'"+user_description+"'"+",'"+snmp_description+"');";
        
           stmt.executeUpdate(sql); 
        
           stmt.close();
           conn.close();
         
     } 
     catch (SQLException ex) {
         Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
     }
     catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
     }
    }
    public static void remove_device(gui_model_device d){
        
        try {
            
        create_connection();
        stmt = conn.createStatement();
        
            String ip = d.getIp();
                   //rs.getString("ip");
            int port  = d.getPort();
                   //rs.getInt("port");
            String sql = "Delete from "+database_name+"."+tablename_devces+" where device_ip='"+ip+"' and device_port="+port+";";
        
           
           stmt.executeUpdate(sql); 
        
           stmt.close();
           conn.close();
         
     } 
     catch (SQLException ex) {
         Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
     }
     catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
     }
    }
}

