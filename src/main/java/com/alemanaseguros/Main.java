package com.alemanaseguros;

import com.alemanaseguros.services.TokenService;
import com.alemanaseguros.services.DocumentService;
import com.alemanaseguros.services.AlfrescoService;
import com.alemanaseguros.utils.Logger;
import com.alemanaseguros.utils.PropertiesLoader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        System.out.println("inicio \n");

        // Crear una instancia de LocalDateTime
        LocalDateTime now = LocalDateTime.now();

        // Definir el formato deseado para la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Convertir la fecha a cadena
        String dateString = now.format(formatter);

        // Reemplazar caracteres no válidos en el nombre del archivo
        String safeDateString = dateString.replace(":", "_");

        // Concatenar el timestamp con el nombre del archivo
        String fileName = safeDateString + ".txt";

        Logger logger = new Logger(fileName);
        try {
            String token = TokenService.getToken();
            logger.log("Obtained token.");
            
            var documents = DocumentService.getDocuments(token);
            logger.log("Obtained document list");

            for (var document : documents) {
                String base64Document = DocumentService.getDocumentContent(token, document.getId());
                logger.log("Obtained document content for ID: " + document.getId());

                String isUploaded = AlfrescoService.uploadDocument(base64Document, document.getDocumentoNombre(), document.getSpcSolRutTitular(), document.getSpcSolSinNpoliza(), document.getDocumentoTipoSolicitud(), document.getDocumentoTipoArchivo());
                if (!Objects.equals(isUploaded, "")) {
                    logger.log("Uploaded document ID: " + document.getId());
                    DocumentService.setDocumentProcessed(document.getId(), isUploaded, token);
                    logger.log("Set document as processed for ID: " + document.getId());
                }
            }
        } catch (Exception e) {
            logger.log("An error occurred: " + e.getMessage());
            System.out.println("An error occurred:  \n");
        }
    }
}
