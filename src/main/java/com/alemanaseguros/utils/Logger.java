package com.alemanaseguros.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private String logFilePath;
    private BufferedWriter writer;

    public Logger(String logFilePath) {
        this.logFilePath = logFilePath;
        try {
            // Crea el archivo de log y el escritor
            writer = new BufferedWriter(new FileWriter(logFilePath, true));
        } catch (IOException e) {
            System.err.println("No se pudo abrir el archivo de log: " + e.getMessage());
        }
    }

    // Método para agregar una fila al archivo de log
    public synchronized void log(String message) {
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write(timestamp + " - " + message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
        }
    }

    // Método para cerrar el escritor cuando se termina
    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el archivo de log: " + e.getMessage());
        }
    }
}
