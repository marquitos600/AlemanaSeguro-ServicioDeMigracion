package com.alemanaseguros.services;

import com.alemanaseguros.utils.PropertiesLoader;
import com.alemanaseguros.utils.Logger;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Base64;

public class AlfrescoService {
    public static boolean uploadDocument(String base64Content, String nameFile, String rut, String nPoliza, String tipoSolicitud, String tipoArchivo) throws Exception {
        var properties = PropertiesLoader.loadProperties();
        String url = properties.getProperty("alfresco.upload.url").replace("/share/", "/alfresco/service/cargaALE/masiva");
        String username = properties.getProperty("alfresco.username");
        String password = properties.getProperty("alfresco.password");

        String credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(Charset.forName("UTF-8")));

        // Decodificar el contenido base64 a bytes
        byte[] fileBytes = Base64.getDecoder().decode(base64Content);

        // Definir el boundary para separar las partes del cuerpo del formulario
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        String lineSeparator = "\r\n";

        // Construir el cuerpo de la solicitud multipart
        StringBuilder bodyBuilder = new StringBuilder();

        // nombre_archivo
        bodyBuilder.append("--").append(boundary).append(lineSeparator);
        bodyBuilder.append("Content-Disposition: form-data; name=\"nombre_archivo\"").append(lineSeparator);
        bodyBuilder.append(lineSeparator);
        bodyBuilder.append(nameFile).append(lineSeparator);

        // file (contenido binario)
        bodyBuilder.append("--").append(boundary).append(lineSeparator);
        bodyBuilder.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(nameFile).append("\"").append(lineSeparator);
        bodyBuilder.append("Content-Type: application/octet-stream").append(lineSeparator);
        bodyBuilder.append(lineSeparator);

        // Construir la parte binaria (archivo)
        byte[] bodyPrefix = bodyBuilder.toString().getBytes(Charset.forName("UTF-8"));
        byte[] bodySuffix = (lineSeparator + "--" + boundary + "--" + lineSeparator).getBytes(Charset.forName("UTF-8"));

        byte[] requestBody = new byte[bodyPrefix.length + fileBytes.length + bodySuffix.length];
        System.arraycopy(bodyPrefix, 0, requestBody, 0, bodyPrefix.length);
        System.arraycopy(fileBytes, 0, requestBody, bodyPrefix.length, fileBytes.length);
        System.arraycopy(bodySuffix, 0, requestBody, bodyPrefix.length + fileBytes.length, bodySuffix.length);

        // rut_titular
        bodyBuilder.setLength(0);
        bodyBuilder.append("--").append(boundary).append(lineSeparator);
        bodyBuilder.append("Content-Disposition: form-data; name=\"rut_titular\"").append(lineSeparator);
        bodyBuilder.append(lineSeparator);
        bodyBuilder.append("12345678-9").append(lineSeparator);

        // numero_poliza
        bodyBuilder.append("--").append(boundary).append(lineSeparator);
        bodyBuilder.append("Content-Disposition: form-data; name=\"numero_poliza\"").append(lineSeparator);
        bodyBuilder.append(lineSeparator);
        bodyBuilder.append("123").append(lineSeparator);

        // tipo_solicitud
        bodyBuilder.append("--").append(boundary).append(lineSeparator);
        bodyBuilder.append("Content-Disposition: form-data; name=\"tipo_solicitud\"").append(lineSeparator);
        bodyBuilder.append(lineSeparator);
        bodyBuilder.append("Siniestro").append(lineSeparator);

        // tipo_archivo
        bodyBuilder.append("--").append(boundary).append(lineSeparator);
        bodyBuilder.append("Content-Disposition: form-data; name=\"tipo_archivo\"").append(lineSeparator);
        bodyBuilder.append(lineSeparator);
        bodyBuilder.append("prueba").append(lineSeparator);

        // Completar el cuerpo de la solicitud
        byte[] textPartBytes = bodyBuilder.toString().getBytes(Charset.forName("UTF-8"));
        byte[] finalRequestBody = new byte[requestBody.length + textPartBytes.length];
        System.arraycopy(requestBody, 0, finalRequestBody, 0, requestBody.length);
        System.arraycopy(textPartBytes, 0, finalRequestBody, requestBody.length, textPartBytes.length);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(finalRequestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Failed to upload document to Alfresco: " + response.body());
        }

        return true;
    }
}
