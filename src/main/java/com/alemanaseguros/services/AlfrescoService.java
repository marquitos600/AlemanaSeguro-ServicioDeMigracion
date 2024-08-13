package com.alemanaseguros.services;

import com.alemanaseguros.utils.PropertiesLoader;
import com.alemanaseguros.utils.Logger;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Base64;

public class AlfrescoService {
    public static boolean uploadDocument(String base64Content, String filePath) throws Exception {
        var properties = PropertiesLoader.loadProperties();
        String url = properties.getProperty("alfresco.upload.url");
        String username = properties.getProperty("alfresco.username");
        String password = properties.getProperty("alfresco.password");

        String credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        String requestBody = String.format("{\"content\":\"%s\", \"path\":\"%s\"}", base64Content, filePath);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Failed to upload document to Alfresco");
        }

        return true;
    }
}
