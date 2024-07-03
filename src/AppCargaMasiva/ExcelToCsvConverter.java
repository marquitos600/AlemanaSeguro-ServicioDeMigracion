package AppCargaMasiva;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelToCsvConverter {

    public static void convertExcelToCsv(String excelFilePath, String csvFilePath) {
        FileInputStream fis = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(new File(excelFilePath));
            workbook = new XSSFWorkbook(fis);

            Sheet sheet = workbook.getSheetAt(0);

            try (FileWriter csvWriter = new FileWriter(csvFilePath)) {
                for (Row row : sheet) {
                    for (org.apache.poi.ss.usermodel.Cell cell : row) {
                        csvWriter.append(cell.toString());
                        csvWriter.append(",");
                    }
                    csvWriter.append("\n");
                }
            }
            String userDir = System.getProperty("user.dir");
      		 
            System.out.println("La conversión se ha completado con éxito.");
           

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) {
                    //workbook.;
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
    	String userDir = System.getProperty("user.dir");
    	
        String excelFilePath = userDir+"//AppCargaMasiva//ArchivosCarga//index.xlsx"; // Ruta del archivo Excel de entrada
        String csvFilePath = userDir+"//AppCargaMasiva//ArchivosCarga//index.csv";   // Ruta del archivo CSV de salida

        convertExcelToCsv(excelFilePath, csvFilePath);
    }
}
