package com.alemanaseguros.services;

import com.alemanaseguros.utils.PropertiesLoader;
import com.alemanaseguros.utils.Logger;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class TokenService {
    public static String getToken() throws Exception {
        System.out.println("Entrando al token \n");
        var properties = PropertiesLoader.loadProperties();
        String url = properties.getProperty("token.url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        String requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

        System.out.println("antes de obtener la peticion \n");
        System.out.println("requestBody:  \n");
        System.out.println(requestBody);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .method("GET", ofString(requestBody));

        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Failed to obtain token");
        }
        System.out.println("Token obtenido! \n");

        // Extraer el valor de 'accessToken' manualmente usando regex
        Pattern pattern = Pattern.compile("\"accessToken\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(response.body());
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new Exception("accessToken not found in response");
        }
    }

    private static BodyPublisher ofString(String body) {
        try {
            return (BodyPublisher) BodyPublishers.class.getDeclaredMethod("ofString", String.class).invoke(null, body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
