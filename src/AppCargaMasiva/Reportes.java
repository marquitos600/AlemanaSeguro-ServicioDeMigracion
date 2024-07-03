package AppCargaMasiva;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Reportes 
{

	public void generarReportes(Properties properties, ArrayList ArrayPartes) throws IOException
	{
  
        File excel = new File(System.getProperty("user.dir")+"//src//excel//template.xls");
        //File excel = new File("/opt/AppCargaMasiva/template.xls");
        FileInputStream fis = new FileInputStream(excel);
        HSSFWorkbook libro = new HSSFWorkbook(fis,true);
        HSSFSheet sheet = libro.getSheetAt(0);
        for(int f=12;f<ArrayPartes.size()+12;f++)
        {
        	Row fila = sheet.createRow(f-1);
        	
        	ArrayList filecontent = (ArrayList) ArrayPartes.get(f-12); 
        	
        	for(int c=1;c<filecontent.size()-1;c++)
        	{
        		
        		   Cell celda = fila.createCell(c);
               	   celda.setCellValue(filecontent.get(c).toString());
                   //System.out.print(filecontent.get(c).toString()+ "\t\t");
        	}
        }
        File archivoxls = new File(properties.get("ubicacionReportes")+"//Reporte_"+this.getFechaActual()+".xls");
        archivoxls.createNewFile();
        FileOutputStream out = new FileOutputStream(archivoxls);
        libro.write(out);
        out.close();

	}
	
	public String getFechaActual() 
	{
	    Date ahora = new Date();
	    SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy-HHmmss");
	    return formateador.format(ahora);
	}
}


