package snmp;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import javafx.collections.ObservableList;

public class report{
public static String filename;
    public static void main(String args[]) throws DocumentException, FileNotFoundException{
    }
static ArrayList<gui_model_record> data=new ArrayList<>();    

public static void get_data(ObservableList<gui_model_record> record_data){
    data.clear();
    data.addAll(record_data);
}

static String start_date,end_date;
public static void get_Date(String start,String end){
    start=start.substring(0,19);
    start_date=start;
    end=end.substring(0, 19);
    end_date=end;
}
static void change_date_format(){
    String tokens[]=start_date.split(" ");
    String time = tokens[1];
    String mysql_date = tokens[0];
    String date[] = mysql_date.split("-");
    String year = date[0];
    String month = date[1];
    String day = date[2];
    start_date=day+"-"+month+"-"+year+" "+time;

    tokens=end_date.split(" ");
    time = tokens[1];
    mysql_date = tokens[0];
    date = mysql_date.split("-");
    year = date[0];
    month = date[1];
    day = date[2];
    end_date=day+"-"+month+"-"+year+" "+time;

}
static PdfPTable create_columns(PdfPTable table){
                Font f = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD);
    
                PdfPCell cell_col_serialno = new PdfPCell(new Paragraph("S.\nNo.",f));
    
                Paragraph para_ip = new Paragraph("IP\nAddress",f);
                para_ip.setAlignment(Element.ALIGN_CENTER);
                PdfPCell cell_col_ip = new PdfPCell(para_ip);
                Font f_big = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
                Paragraph para_dscr = new Paragraph("Interface\nDescription",f_big);
                para_dscr.setAlignment(Element.ALIGN_CENTER);
                
                PdfPCell cell_col_dscr = new PdfPCell(para_dscr);
                PdfPCell cell_col_inbw = new PdfPCell(new Paragraph("Average \nInput \nBandwidth\n( % )",f));
                PdfPCell cell_col_outbw = new PdfPCell(new Paragraph("Average \nOutput \nBandwidth\n( % )",f));
                PdfPCell cell_col_datain = new PdfPCell(new Paragraph("Total \nData-IN\n( MB )",f));
                PdfPCell cell_col_dataout = new PdfPCell(new Paragraph("Total \nData-OUT\n( MB )",f));
                PdfPCell cell_col_maxspeed = new PdfPCell(new Paragraph("Max \n Speed\n( Mbps )",f));
                PdfPCell cell_col_dataratein = new PdfPCell(new Paragraph("Average \nIN Speed\n( Mbps )",f));
                PdfPCell cell_col_datarateout = new PdfPCell(new Paragraph("Average \nOUT Speed\n( Mbps )",f));
                PdfPCell cell_col_totalbw = new PdfPCell(new Paragraph("Total \nBandwidth\n( % )",f));
                PdfPCell cell_col_totaldata = new PdfPCell(new Paragraph("Total Data\nTransferred\n( MB )",f));
                PdfPCell cell_col_totalrate = new PdfPCell(new Paragraph("Total \nSpeed\n( Mbps )",f));
                
                cell_col_serialno.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_ip.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_dscr.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_inbw.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_outbw.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_datain.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_dataout.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_maxspeed.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_dataratein.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_datarateout.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_totalbw.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_totaldata.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell_col_totalrate.setBackgroundColor(BaseColor.LIGHT_GRAY);
                
                cell_col_ip.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_col_dscr.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_col_inbw.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_col_outbw.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_col_datain.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_col_dataout.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_col_maxspeed.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_col_dataratein.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_col_datarateout.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_col_totalbw.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_col_totaldata.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_col_totalrate.setHorizontalAlignment(Element.ALIGN_CENTER);
                        
                table.addCell(cell_col_serialno);
                table.addCell(cell_col_ip);
                table.addCell(cell_col_dscr);
                table.addCell(cell_col_inbw);
                table.addCell(cell_col_outbw);
                table.addCell(cell_col_datain);
                table.addCell(cell_col_dataout);
                table.addCell(cell_col_maxspeed);
                table.addCell(cell_col_dataratein);
                table.addCell(cell_col_datarateout);
                table.addCell(cell_col_totalbw);
                table.addCell(cell_col_totaldata);
                table.addCell(cell_col_totalrate);
                
                return table;       
        }



public static void generate_report(File filelocation){
         Document document = new Document(PageSize.A4.rotate());

        try {
            
            PdfWriter.getInstance(document,new FileOutputStream(filelocation));
            
            document.open();
            
            
            
            Font title_font = new Font(Font.FontFamily.TIMES_ROMAN, 28,Font.BOLD);
            Paragraph title = new Paragraph(20f,"DESIDOC NSD", title_font);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10f);
           
            
            
            Paragraph title_sub = new Paragraph(20f,"Network Bandwidth Monitor", title_font);
            title_sub.setAlignment(Element.ALIGN_CENTER);
            
            
            
            title_sub.setSpacingBefore(15f);
            title_sub.setSpacingAfter(10f);
            //document.add(title);
            
            LineSeparator ls = new LineSeparator();
            //document.add(new Chunk(ls));
            
            document.add(title_sub);
            
            change_date_format();
            
            Font date_font = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
            Paragraph from_date = new Paragraph(20f,"From : "+start_date, date_font);
            title_sub.setAlignment(Element.ALIGN_LEFT);
            Paragraph till_date = new Paragraph(20f,"Till : "+end_date, date_font);
            title_sub.setAlignment(Element.ALIGN_LEFT);
            from_date.setSpacingBefore(10f);
            till_date.setSpacingAfter(10f);
            document.add(from_date);
            document.add(till_date);
            
            PdfPTable table = new PdfPTable(13);
            table.setSpacingBefore(15f);
            table.setSpacingAfter(10f);
            table.setWidthPercentage(100);
            
            table.setWidths(new float[]{0.03f,0.08f,0.16f,0.075f,0.075f,0.077f,0.075f,0.07f,0.075f,0.06f,0.075f,0.09f,0.052f});
                                      //s.no   ip   dscr   inbw   outbw  din    dout  speed   insp outsp  tbw   tdata  tsp
            
            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 8);
            
            table=create_columns(table);
            int serialno=1;
            for(gui_model_record r:data){
                PdfPCell cell_serialno = new PdfPCell(new Paragraph(""+serialno+".",f));
                serialno++;
                cell_serialno.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                PdfPCell cell_ip = new PdfPCell(new Paragraph(r.getIp(),f));
                PdfPCell cell_dscr = new PdfPCell(new Paragraph(r.getDscr(),f));
                PdfPCell cell_inbw = new PdfPCell(new Paragraph(""+r.getInbw(),f));
                PdfPCell cell_outbw = new PdfPCell(new Paragraph(""+(r.getOutbw()),f));
                PdfPCell cell_datain = new PdfPCell(new Paragraph(""+(r.getDatain()),f));
                PdfPCell cell_dataout = new PdfPCell(new Paragraph(""+(r.getDataout()),f));
                PdfPCell cell_max_speed = new PdfPCell(new Paragraph(""+(r.getSpeed()),f));
                PdfPCell cell_dataratein = new PdfPCell(new Paragraph(""+(r.getDataratein()),f));
                PdfPCell cell_datarateout = new PdfPCell(new Paragraph(""+(r.getDatarateout()),f));
                PdfPCell cell_totalbw = new PdfPCell(new Paragraph(""+(r.getTotalbw()),f));
                PdfPCell cell_totaldata = new PdfPCell(new Paragraph(""+(r.getTotaldata()),f));
                PdfPCell cell_totalrate = new PdfPCell(new Paragraph(""+(r.getTotalrate()),f));
                
                cell_ip.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_dscr.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_inbw.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_outbw.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_datain.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_dataout.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_max_speed.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_dataratein.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_datarateout.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_totalbw.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_totaldata.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell_totalrate.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                
                table.addCell(cell_serialno);
                table.addCell(cell_ip);
                table.addCell(cell_dscr);
                table.addCell(cell_inbw);
                table.addCell(cell_outbw);
                table.addCell(cell_datain);
                table.addCell(cell_dataout);
                table.addCell(cell_max_speed);
                table.addCell(cell_dataratein);
                table.addCell(cell_datarateout); 
                table.addCell(cell_totalbw);
                table.addCell(cell_totaldata);
                table.addCell(cell_totalrate);
                
            }
            
            document.add(table);
            System.out.println("created");
            document.close();
        
        } 
        catch(Exception e){

        }
}
}