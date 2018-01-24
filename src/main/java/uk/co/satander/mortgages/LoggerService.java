package uk.co.satander.mortgages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class LoggerService {

    private static final Logger alertLogger = LoggerFactory.getLogger("moon.alert");
    private static final Logger moonMonitor = LoggerFactory.getLogger("moon.monitor");

    private static Properties MESSAGES = new Properties();

    static {
        try {
            MESSAGES.load(LoggerService.class.getClassLoader().getResourceAsStream("alert.properties"));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void raiseAlert(String errorCode, Object... args) {
        String message = MESSAGES.getProperty(errorCode);
        if (message == null) {
            throw new IllegalArgumentException("Unknown Alert Code: " + errorCode);
        }
        alertLogger.error(String.format(message, args));
    }

    public static void monitor(String errorCode, Object... args) {
        String message = MESSAGES.getProperty(errorCode);
        if (message == null) {
            throw new IllegalArgumentException("Unknown Event Code: " + errorCode);
        }
        moonMonitor.info(String.format(message, args));
    }
}
