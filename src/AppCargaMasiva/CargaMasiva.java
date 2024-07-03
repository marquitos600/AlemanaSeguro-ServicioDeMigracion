package AppCargaMasiva;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;


public class CargaMasiva {
	 public static void main(String[] parametro) throws IOException 
	 {
		
		 // Declaración de objetos
		 Properties properties = new Properties();
		 CargaMasiva cargaMasiva = new CargaMasiva();
		 LeerTxtAspectos leerTxtArchivo = new LeerTxtAspectos();
		 
		 ExcelToCsvConverter excelToCsvConverter = new ExcelToCsvConverter();
		 //ExcelToCsvConverter.convertExcelToCsv("C://AppCargaMasiva//ArchivosCarga//index.xlsx", "C://AppCargaMasiva//ArchivosCarga//index.csv");
		 
		 //ExcelToCsvConverter.convertExcelToCsv(System.getProperty("user.dir")+"//AppCargaMasiva//ArchivosCarga//index.xlsx" , System.getProperty("user.dir")+"//AppCargaMasiva//ArchivosCarga//index.csv");
		 
		 
		 // asignar variable properties
		 
		 cargaMasiva.setProperties(properties);
		 
		
		 
		 
		 
		 // se recorre el archivo de indexes y se toman los datos
		 
		 ArrayList allFileAddresses = new ArrayList();
		 allFileAddresses =  cargaMasiva.TomarDatosIndex(properties, leerTxtArchivo);
		 
		 //vemos que tiene el index adentro
		 /*for(int i = 0; i < allFileAddresses.size(); i++)
	        {
			 	System.out.println("Datos de archivo: \n");
			 	System.out.println(allFileAddresses.get(i));
	        }*/
		
		 // se recupera el ticket
		 
		 String ticket = cargaMasiva.getTicket(properties);
		 
		 cargaMasiva.cargaDocumentosAlfresco(ticket, allFileAddresses, properties);
		 

         System.out.println("\n Fin");
		
		 
	  }
	 
	 public void setProperties(Properties properties) throws IOException
	 {
		 FileInputStream fileProp = null;
		 try 
		 {
			fileProp = new FileInputStream(System.getProperty("user.dir")+"//archivo.properties");
			//fileProp = new FileInputStream("C://AppCargaMasiva//archivo.properties");
			//fileProp = new FileInputStream("/opt/AppCargaMasiva/archivo.properties");
		 } 
		 catch (Exception e) 
		 {
			 e.printStackTrace();
			 System.out.println("El archivo no existe, por favor revise si existe el archivo");
		 }
		 
		 properties.load(fileProp);
	 }
	 
	 public ArrayList TomarDatosIndex(Properties properties, LeerTxtAspectos leerTxtArchivo) 
	 {
		 ArrayList allFileAddresses = new ArrayList();
		 String ubicacionArchivoIndex = (String) properties.get("ubicacionArchivoIndex");
		 char delimitador =  ((String) properties.get("delimitador")).charAt(0);
		 allFileAddresses =  leerTxtArchivo.leerArchivo(ubicacionArchivoIndex, delimitador);
		 return allFileAddresses;
	}
	 
	 public String getTicket(Properties properties)
    {
		 
		String pass = properties.getProperty("passAlfresco");
		String encodedPassword = "";
		try {
			encodedPassword = URLEncoder.encode(pass, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuilder url = new StringBuilder().append(properties.getProperty("ipAlfresco")).append(":").append(properties.getProperty("puertoAlfresco")).append("/alfresco/service/api/login?u=").append(properties.getProperty("userAlfresco")).append("&pw=").append(encodedPassword);
		GetMethod var1 = new GetMethod(url.toString());
	    HttpClient client = new HttpClient();
        String ticket;
        try
        {
            client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
            client.executeMethod(var1);
            ticket = var1.getResponseBodyAsString();
            ticket = getStringContent(ticket);
        }
        catch(Exception ex)
        {
        	System.out.println("Error al obtener token , url: \n");
        	System.out.println(url);
        	System.out.println("\n");
            ticket = "";
            System.out.println((new StringBuilder()).append("No se pudo obtener el Ticket. Error: ").append(ex).toString());
        }
        return ticket;
		 
    }
	
	 private String getStringContent(String ticket)
    {
        String ticket1[] = ticket.split("<");
        String ticket2[] = ticket1[2].split(">");
        ticket = ticket2[1];
        return ticket;
    }
	
	private String cargaDocumentosAlfresco(String ticket, ArrayList allFileAddresses, Properties properties) throws HttpException, IOException
	{
		String ticketCarga = ticket ;
		HttpClient client;
	    PostMethod filePost;
	    File file = null;
	    ArrayList ArrayPartes= new ArrayList();
	    Reportes rep = new Reportes();
	    int status = 0;
	    GenerarIndexRechazados generarIndex = new GenerarIndexRechazados();
	   
	    StringBuilder url = new StringBuilder().append(properties.getProperty("ipAlfresco")).append(":").append(properties.getProperty("puertoAlfresco")).append("/alfresco/service/cargaBCI/masiva").append("?alf_ticket=").append(ticket);
	    
	    
		ArrayList fileContent = new ArrayList();
		
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
        filePost = null;
        File folder = new File(properties.getProperty("carpRechazo")+this.getFechaActual());
     	folder.mkdirs();
     	
     	System.out.print("url: ");
     	System.out.print(url);
     	
     	
     	//aca lee los archivos de audio para subir
        
        for(int i = 0; i < allFileAddresses.size(); i++)
        {
            filePost = new PostMethod(url.toString());
            filePost.getParams().setBooleanParameter("http.protocol.expect-continue", true);
            fileContent = (ArrayList)allFileAddresses.get(i);
            
            String rutaArchivo =properties.getProperty("ubicacionArchivos");
            int SO = System.getProperty("os.name").toUpperCase().indexOf("WINDOWS");
            if(SO == 0)
                file = new File((new StringBuilder()).append(rutaArchivo).append(fileContent.get(0)).toString());
            else
                file = new File((new StringBuilder()).append(rutaArchivo).append(fileContent.get(0)).toString());
            String nombre_archivo = (String)fileContent.get(0);
            
            //vemos el contenido de fileContent
            /*System.out.print("\n NombreArchivo : ");
            System.out.print(fileContent.get(0));
            System.out.print("\n");
            
            System.out.print("\n Producto : ");
            System.out.print(fileContent.get(1));
            System.out.print("\n");
            
            System.out.print("\n Nombre : ");
            System.out.print(fileContent.get(2));
            System.out.print("\n");
            
            System.out.print("\n Apellido : ");
            System.out.print(fileContent.get(3));
            System.out.print("\n");
            
            System.out.print("\n FechaCarga : ");
            System.out.print(fileContent.get(4));
            System.out.print("\n");
            
            System.out.print("\n Rut : ");
            System.out.print(fileContent.get(5));
            System.out.print("\n");
            
            System.out.print("\n NombreCall : ");
            System.out.print(fileContent.get(6));
            System.out.print("\n");
            
            System.out.print("\n FechaVenta : ");
            System.out.print(fileContent.get(7));
            System.out.print("\n");
            
            System.out.print("\n Calidad : ");
            System.out.print(fileContent.get(8));
            System.out.print("\n");*/
            
            
            
    
            if(file.exists())
            {
	            Part parts[] = {
	                //new StringPart("contenttype", properties.getProperty("contenttype")), new StringPart("author",acentos(properties.getProperty("author"))), new StringPart("filename", acentos(nombre_archivo)), 
	                //new StringPart("mimetype", properties.getProperty("mimetype")), new FilePart("file", rutaArchivo+nombre_archivo, file), 
	                //new StringPart("nroFactura",acentos(nroFactura.toString())),
	                //new StringPart("monto",acentos(monto.toString()))
	            		
	            	new StringPart("file", properties.getProperty("mimetype")), new FilePart("file", rutaArchivo+nombre_archivo, file),
	            	new StringPart("mimeType",properties.getProperty("mimetype")),
	            	new StringPart("nombre_archivo",acentos(nombre_archivo)),
	            	
	            	new StringPart("nombreAudio",acentos(nombre_archivo)),
	            	new StringPart("fechaCarga",acentos(fileContent.get(4).toString())),
	            	new StringPart("fechaVenta",acentos(fileContent.get(7).toString())),
	            	new StringPart("producto",acentos(fileContent.get(1).toString())),
	            	new StringPart("nombre",acentos(fileContent.get(2).toString())),
	            	new StringPart("apellido",acentos(fileContent.get(3).toString())),
	            	new StringPart("rut",acentos(fileContent.get(5).toString())),
	            	new StringPart("nombreCall",acentos(fileContent.get(6).toString())),
	            	new StringPart("calidad",acentos(fileContent.get(8).toString())),

	            };
	            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
	            status = client.executeMethod(filePost);
	            
	        	System.out.print("\n Archivo con nombre: ");
	        	System.out.print(nombre_archivo);
	        	System.out.print(" subido con exito!");
	        	
	        	System.out.print("\n Status: ");
	        	System.out.print(status);
            }
            else
            {
            	status = 1;
            }
            if(status == 1)
            {
            	fileContent.add(0, status);
            	fileContent.add(9, "Error : La ubicación del archivo descripto en el index no existe");
            	System.out.println("Error : La ubicación del archivo descripto en el index no existe");
            	//generarIndex.generarIndexRechazados(fileContent,folder, properties); 
            }
            
            if(status == 401)
            {
            	fileContent.add(0, status);
            	fileContent.add(9, "Error : Problemas con el contenido del documento");
            	System.out.println("Error : Problemas con el contenido del documento");
            	File fdest = new File(folder.getAbsolutePath()+"/"+nombre_archivo);
            	file.renameTo(fdest);
            	//generarIndex.generarIndexRechazados(fileContent,folder, properties); 
            }
            if(status == 500)
            {
            	fileContent.add(0, status);
             	fileContent.add(9, "Error : El documento ya se encuentra cargado en esa ubicacion o tiene problemas con el contenido");
             	File fdest = new File(folder.getAbsolutePath()+"/"+nombre_archivo);           	
             	file.renameTo(fdest);
             	//generarIndex.generarIndexRechazados(fileContent,folder, properties); 
             
             	System.out.print("\n Error : El documento ya se encuentra cargado en esa ubicacion o tiene problemas con el contenido");
             	
             	String responseBody = filePost.getResponseBodyAsString();
                System.out.println("Cuerpo de la respuesta para HTTP 500: " + responseBody);
             	
             	
            }
            if(status == 200)
            {
            	fileContent.add(0, status);
            	fileContent.add(9, " Exito: El documento fue cargado exitosamente");
            	System.out.println(" Exito: El documento fue cargado exitosamente");
            	file.delete();
            }
            if(status == 300)
            {
            	fileContent.add(0, status);
            	fileContent.add(9, " Error : Problemas con la metadata del index");
            	System.out.println(" Error : Problemas con la metadata del index");
            	File fdest = new File(folder.getAbsolutePath()+"/"+nombre_archivo);
            	file.renameTo(fdest);
            	//generarIndex.generarIndexRechazados(fileContent,folder, properties); 
            }
            
            if(status == 444)
            {
            	fileContent.add(0, status);
            	fileContent.add(9, " Error : El path es incorrecto");
            	System.out.println(" Error : El path es incorrecto");
            	File fdest = new File(folder.getAbsolutePath()+"/"+nombre_archivo);
            	file.renameTo(fdest);
            	//generarIndex.generarIndexRechazados(fileContent,folder, properties); 
            }
            
            if(status == 404)
            {
            	fileContent.add(0, status);
            	fileContent.add(9, " Error : no encuentra webscript");
            	System.out.println(" Error : no encuentra webscript");
            	File fdest = new File(folder.getAbsolutePath()+"/"+nombre_archivo);
            	file.renameTo(fdest);
            	//generarIndex.generarIndexRechazados(fileContent,folder, properties); 
            }
            
            ArrayPartes.add(fileContent.clone()); 
           
        }
    
       
        
        //generarIndex.generarIndexRechazados(ArrayPartes,folder, properties);
        fileContent.clear();
        rep.generarReportes(properties, ArrayPartes);
		 return "";
	}
	public String getFechaActual() 
	{
	    Date ahora = new Date();
	    SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy-HHmm");
	    return formateador.format(ahora);
	}
    public String acentos (String dato) throws UnsupportedEncodingException
    {
    	
		ArrayList<String> acentos = new ArrayList<String>();
		acentos.add("á");
		acentos.add("é");
		acentos.add("í");
		acentos.add("ó");
		acentos.add("ú");
		acentos.add("Á");
		acentos.add("É");
		acentos.add("Í");
		acentos.add("Ó");
		acentos.add("Ú");
	    acentos.add("ñ");
	    acentos.add("Ñ");
	    //System.out.println(dato);
		for (Object a: acentos)
		{
			 
			if(dato.indexOf(a.toString()) != -1){
				dato = dato.replace(a.toString(), "acent" + cambiarLetra(a.toString()));
			}
		}
		//System.out.println(dato);
		return dato;
    }

	public String cambiarLetra(String a)
	{
		if(a.toString().indexOf("á") != -1)
			return "a";
		if(a.toString().indexOf("é") != -1)
			return "e";
		if(a.toString().indexOf("í") != -1)
			return "i";
		if(a.toString().indexOf("ó") != -1)
			return "o";
		if(a.toString().indexOf("ú") != -1)
			return "u";
		if(a.toString().indexOf("Á") != -1)
			return "A";
		if(a.toString().indexOf("É") != -1)
			return "E";
		if(a.toString().indexOf("Í") != -1)
			return "I";
		if(a.toString().indexOf("Ó") != -1)
			return "O";
		if(a.toString().indexOf("Ú") != -1)
			return "U";
	            if(a.toString().indexOf("Ñ") != -1)
			return "N";
		if(a.toString().indexOf("ñ") != -1)
			return "n";
		return null;
	}
}
