package com.alemanaseguros;

import com.alemanaseguros.services.TokenService;
import com.alemanaseguros.services.DocumentService;
import com.alemanaseguros.services.AlfrescoService;
import com.alemanaseguros.utils.Logger;
import com.alemanaseguros.utils.PropertiesLoader;

public class Main {
    public static void main(String[] args) {
        System.out.println("inicio \n");
        try {
            String token = TokenService.getToken();
            Logger.log("Obtained token: " + token);
            System.out.println("Obtained token: \n");
            System.out.println(token);
            
            var documents = DocumentService.getDocuments(token);
            Logger.log("Obtained document list");
            System.out.println("Obtained document list \n");
            
            for (var document : documents) {
                System.out.println("Entrando al bucle \n");
                String base64Document = DocumentService.getDocumentContent(token, document.getId());
                Logger.log("Obtained document content for ID: " + document.getId());
                System.out.println("Obtained document content for ID: \n");
                
                boolean isUploaded = AlfrescoService.uploadDocument(base64Document, document.getFilePath());
                if (isUploaded) {
                    Logger.log("Uploaded document ID: " + document.getId());
                    System.out.println("Uploaded document ID \n");
                    DocumentService.setDocumentProcessed(document.getId(), document.getFilePath());
                    Logger.log("Set document as processed for ID: " + document.getId());
                    System.out.println("Set document as processed for ID \n");
                }
            }
        } catch (Exception e) {
            Logger.logError("An error occurred: " + e.getMessage());
            System.out.println("An error occurred:  \n");
        }
    }
}
