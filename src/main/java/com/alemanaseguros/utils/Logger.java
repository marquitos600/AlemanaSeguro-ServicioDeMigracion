package com.alemanaseguros.utils;

import java.time.LocalDateTime;

public class Logger {
    public static void log(String message) {
        System.out.println(LocalDateTime.now() + " INFO: " + message);
    }

    public static void logError(String message) {
        System.err.println(LocalDateTime.now() + " ERROR: " + message);
    }
}
