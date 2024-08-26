package com.alemanaseguros.services;

import com.alemanaseguros.models.Document;
import com.alemanaseguros.utils.PropertiesLoader;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.http.HttpRequest.BodyPublishers.ofString;

public class DocumentService {
    public static List<Document> getDocuments(String token) throws Exception {
        System.out.println("Entrando al getDocuments: \n");
        var properties = PropertiesLoader.loadProperties();
        String url = properties.getProperty("files.url");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Failed to obtain documents");
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, Document.class));
    }

    public static String getDocumentContent(String token, String documentId) throws Exception {
        var properties = PropertiesLoader.loadProperties();
        String url = properties.getProperty("document.url");

        String requestBody = String.format("{\"documentID\":\"%s\"}", documentId);

        System.out.println("requestBody: \n");
        System.out.println(requestBody);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .method("GET", ofString(requestBody));
        HttpRequest request = requestBuilder.build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Failed to obtain document content");
        }

        // Extraer el valor de 'accessToken' manualmente usando regex
        Pattern pattern = Pattern.compile("\"archivoBase64\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(response.body());
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new Exception("accessToken not found in response");
        }
    }

    public static void setDocumentProcessed(String documentId, String filePath, String token) throws Exception {
        System.out.println("Entrando a setDocumentProcessed \n");
        var properties = PropertiesLoader.loadProperties();
        String url = properties.getProperty("setDocument.url");


        String requestBody = String.format("{\"documentID\":\"%s\",\"path\":\"%s\"}", documentId, filePath);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(ofString(requestBody))
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());



        if (response.statusCode() != 200) {
            throw new Exception("Failed to set document as processed");
        }
    }
}
