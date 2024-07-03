package AppCargaMasiva;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;


public class GenerarIndexRechazados 
{
	
	public void generarIndexRechazados( ArrayList fileContent, File fildest, Properties properties)
	{
		String route = "";		
		String nombre_archivo = "";
		String id01 = "";
		String id02 = "";
		String unegocio = "";
		String tipodoc = "";
		String alfrescopath = "";
		String status = "";
		String linea = "";
		try 
		{
			
			
			File fileIndex = new File(fildest.getAbsolutePath()+"\\"+"IndexRechazados.txt");
			
			 if(!fileIndex.exists())
			 {
				 fileIndex.createNewFile();

            }
		
		 
			this.EscribirFichero(fileIndex, properties.getProperty("tituloIndex").toString(),false);
		 	for(int c=0;c<fileContent.size();c++)
        	{
		 		ArrayList lineaIndex = (ArrayList) fileContent.get(c);
		 		
		 		System.out.print("\n lineaIndex.size():  ");
	 			System.out.print(lineaIndex.size());
		 		
		 		for(int i=1;i<lineaIndex.size();i++)
		 		{
		 		
		 			System.out.print("\n indice:  ");
		 			System.out.print(lineaIndex.get(0));
		 			
	 				linea += lineaIndex.get(i).toString()+ properties.getProperty("delimitador").toString();
	 				System.out.print("\n linea:  ");
		 			System.out.print(linea);
		 			
		 			
		 		}
		 		this.EscribirFichero(fileIndex, linea,true);
		 		linea = "";
        	}
	
			
		
				
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	
	}
    public void EscribirFichero(File Ffichero,String SCadena,Boolean titulo)
    {
    	 
        try {
                
       
                BufferedWriter Fescribe=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Ffichero,true)));
                if(titulo==true)
                {
                	Fescribe.newLine();
                }
                
                Fescribe.write(SCadena);
             
                Fescribe.close();
            } catch (Exception ex) {
       
               System.out.println(ex.getMessage());
            }
 
    }


}
