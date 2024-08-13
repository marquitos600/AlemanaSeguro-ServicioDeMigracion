# Aplicación de Procesamiento de Documentos

## Descripción

Esta aplicación Java se encarga de:

- Obtener un token de autenticación a través de un servicio GET.
- Obtener una lista de documentos a través de un servicio GET usando el token.
- Obtener el contenido de cada documento en formato base64.
- Subir el documento a un servidor Alfresco.
- Notificar a un servicio POST que el documento ha sido procesado.
- Generar logs para la trazabilidad del proceso.

## Requisitos

- Java 16
- Apache Maven
- Conexión a internet para acceder a los servicios web y Alfresco

## Configuración

El archivo `application.properties` contiene todas las configuraciones necesarias, incluidas las URL de los servicios, credenciales y otros parámetros.

## Ejecución

Compila y ejecuta la aplicación con Maven:

```sh
mvn clean install
java -jar target/document-processor.jar
