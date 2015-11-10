package snmp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class gui_model_device {

    public SimpleStringProperty ip;
    public SimpleIntegerProperty port;
    public SimpleStringProperty community;
    public SimpleStringProperty device_status;
    public SimpleStringProperty snmp_description;
    public SimpleStringProperty user_description;
    public SimpleDoubleProperty speed;
    
    
    public static ObservableList<gui_model_device> deviceData = FXCollections.observableArrayList();

    public gui_model_device(String ip,int port,String community,String usr_dscr){
        try {
            this.ip = new SimpleStringProperty(ip);
            this.port = new SimpleIntegerProperty(port);
            this.community = new SimpleStringProperty(community);
            this.snmp_description=new SimpleStringProperty(snmp.Datacollector.get_snmp_description(ip, port, community));
            this.device_status=new SimpleStringProperty(snmp.Datacollector.get_snmp_status(ip, port, community));
            this.user_description=new SimpleStringProperty(usr_dscr);
            this.speed=new SimpleDoubleProperty(snmp.Datacollector.get_snmp_speed(ip, port, community));
            
        
        }catch(IOException ex){
            Logger.getLogger(gui_model_device.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getIp() {
            return this.ip.get();
        }
    public void setIp(String Ip) {
            this.ip.set(Ip);
        }
    public Integer getPort() {
            return this.port.getValue();
        }
    public void setPort(int port) {
            this.port.set(port);
        }
    public String getCommunity() {
            return this.community.get();
        }
    public void setCommunity(String Ip) {
            this.community.set(Ip);
        }
    public String getDevice_status() {
            return this.device_status.get();
        }
    public void setDevice_status(String status) {
            this.device_status.set(status);
        }
    public String getSnmp_description() {
            return this.snmp_description.get();
        }
    public void setSnmp_description(String description) {
            this.snmp_description.set(description);
        }
    public String getUser_description() {
            return this.user_description.get();
        }
    public void setUser_description(String description) {
            this.user_description.set(description);
        }
    
    public Double getSpeed() {
            return this.speed.get();
        }
    public void setSpeed(Double speed) {
            this.speed.set(speed);
        }
    
    public static void update_status(){
        for(gui_model_device d:gui_model_device.deviceData){
            try {
                //System.out.println("started status");
                d.device_status=new SimpleStringProperty(snmp.Datacollector.get_snmp_status(d.getIp(), d.getPort(), d.getCommunity()));
                
            } catch (IOException ex) {
                Logger.getLogger(gui_model_device.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //System.out.println("status");
        //snmp.Gui_fxmlController.add_images();
    }
    
}