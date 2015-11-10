package snmp;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.ImageIcon;
import snmp.Datacollector.repeatfunctions;

/**
 *
 * @author Ashish Padalkar
 */
public class gui_javafx extends Application {
    
@Override
    public void start(Stage stage) throws Exception {
            createloginscene(stage);
    }

    public void open_main_app(Stage stage) throws Exception{
        
     try{
        
            Parent root = FXMLLoader.load(getClass().getResource("gui_fxml.fxml"));
                                    //width,height
            Scene scene = new Scene(root, 1000, 700);
            stage.setTitle("Network Bandwidth Monitor");

            stage.setScene(scene);
            Image icon = new Image(Gui_fxmlController.class.getResourceAsStream("images/icon.png"));
            stage.getIcons().add(icon);
            stage.show();

            make_tray_icon(stage);
            
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override public void handle(WindowEvent t) {
                System.out.println("CLOSING");
                
                t.consume();
                
                create_exit_dialog(stage);
                
            }
            });
    }
     catch(Exception exp){
            pst.println("Error gui_javafx.in open_main_app()");
            exp.printStackTrace();
        }
    }
    public void make_tray_icon(Stage stg){
        if (SystemTray.isSupported()) {         
            SystemTray tray = SystemTray.getSystemTray();
            
            java.awt.Image icon = null;
            
                URL img_url = gui_javafx.class.getResource("images/icon_tray_24x24.png");
                icon = new ImageIcon(img_url).getImage();
            
            
            
            
            
            PopupMenu popup = new PopupMenu();
            MenuItem menu_item_exit = new MenuItem("Exit");
            MenuItem menu_item_show = new MenuItem("Show Application");
            MenuItem menu_item_hide = new MenuItem("Hide Application");

            popup.add(menu_item_show);
            popup.add(menu_item_hide);
            popup.addSeparator();
            popup.add(menu_item_exit);

            
            
            TrayIcon trayIcon = new TrayIcon(icon, " MANAS \nNetwork Bandwidth Monitor ", popup);
            trayIcon.setImageAutoSize(true);
            
            ActionListener menuitem_exit_listener = new ActionListener() {                
                @Override
                public void actionPerformed(java.awt.event.ActionEvent arg0) {
                    tray.remove(trayIcon);
                    close_app(stg);
                }               
            };                       

            ActionListener menuitem_hide_listener = new ActionListener() {                
                @Override
                public void actionPerformed(java.awt.event.ActionEvent arg0) {
                    Platform.runLater(new Runnable() {
                    @Override public void run() {
                    stg.hide();
                    }
                  });
                }                   
              };
                    
                    
            ActionListener menuitem_show_listener = new ActionListener() {                
                @Override
                public void actionPerformed(java.awt.event.ActionEvent arg0) {
                    Platform.runLater(new Runnable() {
                    @Override public void run() {
                    stg.show();
                    }
                  });
                }                   
              };
            
            ActionListener listenerTray = new ActionListener() {                
                @Override public void actionPerformed(java.awt.event.ActionEvent event) {
                  Platform.runLater(new Runnable() {
                    @Override public void run() {
                        System.out.println("clicked on tray icon");
                        if(!stg.isShowing()){
                            stg.show();
                        }
                        else{
                            stg.hide();
                        }
                        
                        
                    }
                  });
                }                   
              };                 

            trayIcon.addActionListener(listenerTray);
            menu_item_exit.addActionListener(menuitem_exit_listener);
            menu_item_show.addActionListener(menuitem_show_listener);
            menu_item_hide.addActionListener(menuitem_hide_listener);
            try{
                
                tray.add(trayIcon);
            
            }
            catch (Exception exp) {
                exp.printStackTrace(pst);
                System.err.println("Can't add to tray");
            }
        } 
        else
        {
            System.err.println("Tray unavailable");
        } 
    }
    public static void close_app(Stage stage){
        shutdown_data_collection();
        stage.close();
    }
    
    public static void create_exit_dialog(Stage stg){
        
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.setAlwaysOnTop(true);
                VBox vb = new VBox();
                vb.setAlignment(Pos.CENTER);
                vb.setPadding(new Insets(0, 0, 20, 10));
                vb.setMinSize(400, 150);
                vb.setSpacing(10);
                Text msg = new Text("Are you sure you want to exit the Application ?");
                msg.setFont(new Font(18));
                
                Button b_yes = new Button(" YES ");
                b_yes.setMinSize(75, 30);
                b_yes.setOnAction((ActionEvent event) -> {
                    close_app(stg);
                    dialogStage.close();
                });
                
                Button b_no = new Button(" NO ");
                b_no.setMinSize(75, 30);
                b_no.setOnAction((ActionEvent event) -> {
                    dialogStage.close();
                });
                
                Button b_minimise = new Button(" Minimise the application to System Tray ");
                b_minimise.setMinSize(75, 30);
                b_minimise.setOnAction((ActionEvent event) -> {
                    stg.hide();
                    dialogStage.close();
                });
                
                Image image_warning = new Image(Gui_fxmlController.class.getResourceAsStream("images/Warning.png"));
                ImageView img_warning=new ImageView(image_warning);
                img_warning.setFitHeight(75);
                img_warning.setFitWidth(75);
                dialogStage.getIcons().add(image_warning);
                HBox hb = new HBox(100);
                hb.setPadding(new Insets(10, 25, 20, 75));
                hb.setMinWidth(400);
                hb.getChildren().addAll(b_yes,b_no);
                vb.getChildren().addAll(img_warning,msg,hb,b_minimise);
                Scene scene = new Scene(vb);
                dialogStage.setScene(scene);
                dialogStage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    
    static PrintStream pst;
    
    
    
    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("errors.txt");
        FileOutputStream fos = new FileOutputStream(f);
        pst=new PrintStream(fos);
        
        //PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
        //System.setOut(out);
        
        Platform.setImplicitExit(false);
        launch(args);
                System.out.println("play");
    }
    
    static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    public static void start_data_collection() {
        
        try{
        // regular interval at which the task will be performed 100000000
        int interval = snmp.Datacollector.update_freq;
        //after how many seconds it will start
        int delay = 0;
        Datacollector.initialise=true;
        Datacollector.pause=false;
        
        
        repeatfunctions tt= new repeatfunctions();
        
        scheduler.scheduleWithFixedDelay(tt, delay, interval,TimeUnit.SECONDS);
        System.out.println("started scheduler ");
        
        }
        catch(RejectedExecutionException reject){
            pst.println("Error in gui_javafx.start_data_collection()");
            reject.printStackTrace(pst);
        }
        catch(Exception exp){
            pst.println("Error with scheduler");
            pst.println("Error in gui_javafx.start_data_collection()");
            exp.printStackTrace(pst);
        }
        
    }
    
    public static void shutdown_data_collection(){
            try {
                System.out.println("shutting down");
                scheduler.shutdownNow();
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println("Still waiting...");
                    System.exit(0);
                }
                System.out.println("Exiting normally...");
                Platform.exit();
                System.exit(0);
            }
            catch (InterruptedException ex) {
                Logger.getLogger(gui_javafx.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(Exception exp){
                pst.println("Error in gui_javafx.shutdown_data_collection()");
                exp.printStackTrace(pst);
            }
    }
    
    Button btn;
    TextField txt_db_name,txt_database_loc,txt_db_usr,txt_db_pwd,txt_db_del_freq;
    static String dbname,dbloc,dbusr,dbpwd;
    static int db_del_freq_days;
    
    public void createloginscene(Stage stg){
        System.out.println("starting");
        
        stg.setTitle("Network Bandwidth Monitor");
        
        
        stg.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override public void handle(WindowEvent t) {
                System.out.println("CLOSING");
                
                close_app(stg);
                
            }
            });
        
        
        int rowindex=0,colindex=0;
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 5, 25, 5));

        Scene s = new Scene(grid, 340, 480);
        
        s.getStylesheets().add(gui_javafx.class.getResource("initial_setup.css").toExternalForm());
        
        
        Text scenetitle = new Text("        Initial Setup");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 28));
        grid.add(scenetitle, colindex,rowindex, 2, 1);
        
        
        Image icon = new Image(Gui_fxmlController.class.getResourceAsStream("images/icon.png"));
        stg.getIcons().add(icon);
        
        
        ImageView icon_img = new ImageView(icon);
        icon_img.setFitHeight(160);
        icon_img.setFitWidth(160);
        
        VBox vb_img = new VBox(icon_img);
        vb_img.setPadding(new Insets(0, 0, 5, 75));
        
        grid.add(vb_img,colindex,++rowindex,2,1);
        
        
        String style="-fx-font-size: 14px;"
                            //+ "-fx-font-style: italic;"
                            + "-fx-font-weight: bold;"
                            //+ "-fx-font-family: fantasy;"
                            + "-fx-text-fill: black;"
                            //+ "-fx-background-color: aqua"
                            ;
        
        
        Label db_loc_lbl = new Label("Database IP and Port:");
        grid.add(db_loc_lbl, colindex,++rowindex);
        
        txt_database_loc = new TextField();
        txt_database_loc.setPromptText("localhost");
        grid.add(txt_database_loc, ++colindex,rowindex);
        txt_database_loc.setStyle(style);
        
        Label db_name_lbl = new Label("Database Name:");
        grid.add(db_name_lbl, --colindex,++rowindex);

        txt_db_name = new TextField();
        txt_db_name.setPromptText("bwmon");
        grid.add(txt_db_name, ++colindex,rowindex);
        txt_db_name.setStyle(style);
        
        Label db_usr_lbl = new Label("Database Username:");
        grid.add(db_usr_lbl, --colindex,++rowindex);

        txt_db_usr = new TextField();
        txt_db_usr.setPromptText("root");
        grid.add(txt_db_usr, ++colindex,rowindex);
        txt_db_usr.setStyle(style);
        
        Label db_pwd_lbl = new Label("Database Password:");
        grid.add(db_pwd_lbl, --colindex,++rowindex);

        txt_db_pwd = new TextField();
        txt_db_pwd.setPromptText("root");
        grid.add(txt_db_pwd, ++colindex,rowindex);
        txt_db_pwd.setStyle(style);
        
        Label db_del_freq_lbl = new Label("Store Records for:\n\t( Days )");
        grid.add(db_del_freq_lbl, --colindex,++rowindex,1,2);

        txt_db_del_freq = new TextField();
        txt_db_del_freq.setPromptText("60");
        grid.add(txt_db_del_freq, ++colindex,rowindex);
        txt_db_del_freq.setStyle(style);
        
        btn = new Button(" Start ");
        btn.setId("btn_login");
        
        btn.setMinSize(100, 30);
        HBox hbBtn = new HBox(10);
        
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, colindex,++rowindex+1);
        hbBtn.setStyle(style);
        hbBtn.requestFocus();
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        
        
        
        btn.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
                
                if(txt_db_usr.getText().equals("")){
                    dbusr="root";
                }
                else{
                    dbusr=txt_database_loc.getText();
                }
                
                if(txt_db_pwd.getText().equals("")){
                    dbpwd="root";
                }
                else{
                    dbpwd=txt_db_pwd.getText();
                }
                
                if(txt_database_loc.getText().equals("")){
                    dbloc="localhost";
                }
                else{
                    dbloc=txt_database_loc.getText();
                }
                
                if(txt_db_name.getText().equals("")){
                    dbname="bwmon";
                }
                else{
                    dbname=txt_db_name.getText();
                }
                if(txt_db_del_freq.getText().equals("")){
                    db_del_freq_days=60;
                }
                else{
                    try{
                        Integer num = Integer.parseInt(txt_db_del_freq.getText());
                        if(num>0&&num<365){
                            db_del_freq_days=60;
                        }
                        else{
                            Gui_fxmlController.create_dialog("Please enter a valid range");
                        }
                    }
                    catch(NumberFormatException exp){
                        db_del_freq_days=60;
                    }
                }
                
                try {
                    //create_loading_scene(stg);
                    open_main_app(stg);
                } catch (Exception ex) {
                    pst.println("Error in creating Initial Setup Screen");
                    ex.printStackTrace(pst);
                    Logger.getLogger(gui_javafx.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
        
        stg.setScene(s);
        stg.show();
        System.out.println("shown");
    }
    
    public void create_loading_scene(Stage stg){
        System.out.println("Loading...");
        
        stg.setTitle("Loading data from Database");
        
        
        stg.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override public void handle(WindowEvent t) {
                System.out.println("CLOSING");
                
                close_app(stg);
                
            }
            });
        
        
        int rowindex=0,colindex=0;
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 5, 25, 5));

        Scene s = new Scene(grid, 340, 280);
        
        s.getStylesheets().add(gui_javafx.class.getResource("initial_setup.css").toExternalForm());
        
        
        Text scenetitle = new Text("  Please wait");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 28));
        grid.add(scenetitle, colindex,++rowindex, 2, 1);
        
        
        Image icon = new Image(Gui_fxmlController.class.getResourceAsStream("images/icon.png"));
        stg.getIcons().add(icon);
        
        
        ImageView icon_img = new ImageView(icon);
        icon_img.setFitHeight(160);
        icon_img.setFitWidth(160);
        
        VBox vb_img = new VBox(icon_img);
        vb_img.setPadding(new Insets(0, 0, 5, 30));
        
        grid.add(vb_img,colindex,++rowindex,2,1);
        
        
        String style="-fx-font-size: 14px;"
                            //+ "-fx-font-style: italic;"
                            + "-fx-font-weight: bold;"
                            //+ "-fx-font-family: fantasy;"
                            + "-fx-text-fill: black;"
                            //+ "-fx-background-color: aqua"
                            ;
        
        
        Label lbl_status = new Label("Connecting to database...");
        lbl_status.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        grid.add(lbl_status, colindex,++rowindex);
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        
        
        
        
        stg.setScene(s);
        stg.show();
        System.out.println("shown");
    }
    
}
