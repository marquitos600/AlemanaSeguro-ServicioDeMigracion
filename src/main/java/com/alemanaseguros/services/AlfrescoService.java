package com.alemanaseguros.services;

import com.alemanaseguros.models.Path;
import com.alemanaseguros.utils.PropertiesLoader;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AlfrescoService {
    public static String uploadDocument(String base64Content, String nameFile, String rut, String nPoliza, String tipoSolicitud, String tipoArchivo) throws Exception {
        var properties = PropertiesLoader.loadProperties();
        String url = properties.getProperty("alfresco.upload.url").replace("/share/", "/alfresco/service/cargaALE/masiva");
        String username = properties.getProperty("alfresco.username");
        String password = properties.getProperty("alfresco.password");

        String credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

        // Decodificar el contenido base64 a bytes
        byte[] fileBytes = Base64.getDecoder().decode(base64Content);

        // Definir el boundary para separar las partes del cuerpo del formulario
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        String lineSeparator = "\r\n";

        // Construir el cuerpo de la solicitud multipart
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // nombre_archivo
        outputStream.write(("--" + boundary + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(("Content-Disposition: form-data; name=\"nombre_archivo\"" + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(lineSeparator.getBytes(StandardCharsets.UTF_8));
        outputStream.write((nameFile + lineSeparator).getBytes(StandardCharsets.UTF_8));

        // file (contenido binario)
        outputStream.write(("--" + boundary + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + nameFile + "\"" + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(("Content-Type: application/octet-stream" + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(lineSeparator.getBytes(StandardCharsets.UTF_8));
        outputStream.write(fileBytes);
        outputStream.write(lineSeparator.getBytes(StandardCharsets.UTF_8));

        // rut_titular
        outputStream.write(("--" + boundary + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(("Content-Disposition: form-data; name=\"rut_titular\"" + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(lineSeparator.getBytes(StandardCharsets.UTF_8));
        outputStream.write((rut + lineSeparator).getBytes(StandardCharsets.UTF_8));

        // numero_poliza
        outputStream.write(("--" + boundary + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(("Content-Disposition: form-data; name=\"numero_poliza\"" + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(lineSeparator.getBytes(StandardCharsets.UTF_8));
        outputStream.write((nPoliza + lineSeparator).getBytes(StandardCharsets.UTF_8));

        // tipo_solicitud
        outputStream.write(("--" + boundary + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(("Content-Disposition: form-data; name=\"tipo_solicitud\"" + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(lineSeparator.getBytes(StandardCharsets.UTF_8));
        outputStream.write((tipoSolicitud + lineSeparator).getBytes(StandardCharsets.UTF_8));

        // tipo_archivo
        outputStream.write(("--" + boundary + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(("Content-Disposition: form-data; name=\"tipo_archivo\"" + lineSeparator).getBytes(StandardCharsets.UTF_8));
        outputStream.write(lineSeparator.getBytes(StandardCharsets.UTF_8));
        outputStream.write((tipoArchivo + lineSeparator).getBytes(StandardCharsets.UTF_8));

        // Terminar el cuerpo
        outputStream.write(("--" + boundary + "--" + lineSeparator).getBytes(StandardCharsets.UTF_8));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(outputStream.toByteArray()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());



        // Supongamos que ya tienes la respuesta en 'response.body()'
        String responseBody = response.body();


        // Crear un ObjectMapper de Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        // Convertir la respuesta JSON a un objeto Java
        Path myPath = objectMapper.readValue(responseBody, Path.class);


        if (response.statusCode() != 200) {
            throw new Exception("Failed to upload document to Alfresco: " + response.body());
        }

        return myPath.getPath();
    }
}
