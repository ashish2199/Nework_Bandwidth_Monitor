package snmp;
import java.text.DecimalFormat;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static snmp.database.truncate;

/**
 * Model class for a Person.
 *
 * @author Marco Jakob
 */
public class gui_model_record {

    public SimpleStringProperty ip;
    public SimpleStringProperty dscr;
    public SimpleDoubleProperty inbw;
    public SimpleDoubleProperty outbw;
    public SimpleDoubleProperty datain;
    public SimpleDoubleProperty dataout;
    public SimpleDoubleProperty dataratein;
    public SimpleDoubleProperty datarateout;
    
    public SimpleDoubleProperty totalbw;
    public SimpleDoubleProperty totaldata;
    public SimpleDoubleProperty totalrate;
    
    public SimpleDoubleProperty speed;
    

    public static ObservableList<gui_model_record> recordData = FXCollections.observableArrayList();
  
    /**
     * Constructor with some initial data.
     * 
     * @param firstName
     * @param lastName
     */
    
    
    gui_model_record(String ip,String user_dscr,String snmp_dscr,double inbw,double outbw,
            double datain,double dataout,double speed,double indatarate,double outdatarate)
    {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(6);
        
        this.ip = new SimpleStringProperty(ip);
        String description;
        if(user_dscr.equals("")){
            description=snmp_dscr;
        }else{
            description=user_dscr+"\n"+snmp_dscr;
        }
        
        this.dscr=new SimpleStringProperty(description);
        this.inbw = new SimpleDoubleProperty(inbw);
        this.outbw = new SimpleDoubleProperty(outbw);
        this.datain = new SimpleDoubleProperty(datain);
        this.dataout = new SimpleDoubleProperty(dataout);
        this.speed = new SimpleDoubleProperty(speed);
        this.dataratein = new SimpleDoubleProperty(indatarate);
        this.datarateout = new SimpleDoubleProperty(outdatarate);
        
        this.totalbw=new SimpleDoubleProperty(truncate(inbw+outbw,database.places_truncation)); 
        this.totaldata=new SimpleDoubleProperty(truncate(datain+dataout,database.places_truncation)); 
        this.totalrate=new SimpleDoubleProperty(truncate(indatarate+outdatarate,database.places_truncation)); 
        
        
    }
    
    gui_model_record(gui_model_record r)
    {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(6);
        
        this.ip = r.ip;
        
        this.dscr=r.dscr;
        this.inbw = r.inbw;
        this.outbw = r.outbw;
        this.datain = r.datain;
        this.dataout = r.dataout;
        this.dataratein = r.dataratein;
        this.datarateout = r.datarateout;
        
        this.totalbw=r.totalbw;
        this.totaldata=r.totaldata;
        this.totalrate=r.totalrate;
        
        
    }
    
    public String getIp() {
            return this.ip.get();
        }
    public void setIp(String Ip) {
            this.ip.set(Ip);
        }
   
    public String getDscr() {
            return this.dscr.get();
        }
    public void setDscr(String Ip) {
            this.dscr.set(Ip);
        }
    public Double getInbw() {
            return this.inbw.get();
        }
    public void setInbw(double Inbw) {
            this.inbw.set(Inbw);
        }
    public Double getOutbw() {
            return this.outbw.get();
        }
    public void setOutbw(double Outbw) {
            this.outbw.set(Outbw);
        }
    public Double getDatain() {
            return this.datain.get();
        }
    public void setDatain(double Datain) {
            this.datain.set(Datain);
        }
    public Double getDataout() {
            return this.dataout.get();
        }
    public void setDataout(double Dataout) {
            this.dataout.set(Dataout);
        }
    
    
    public double getSpeed() {
            return this.speed.get();
        }
    public void setSpeed(double speed) {
            this.speed.set(speed);
        }
    
    public Double getDataratein() {
            return this.dataratein.get();
        }
    public void setDataratein(double Indatarate) {
            this.dataratein.set(Indatarate);
        }
    public Double getDatarateout() {
            return this.datarateout.get();
        }
    public void setDatarateout(double Outdatarate) {
            this.datarateout.set(Outdatarate);
        }
    
    
    public Double getTotalbw() {
            return this.totalbw.get();
        }
    public void setTotalbw(double Totalbw) {
            this.totalbw.set(Totalbw);
        }
    public Double getTotaldata() {
            return this.totaldata.get();
        }
    public void setTotaldata(double Totaldata) {
            this.totaldata.set(Totaldata);
        }
    public Double getTotalrate() {
            return this.totalrate.get();
        }
    public void setTotalrate(double Totalrate) {
            this.totalrate.set(Totalrate);
        }
    
}