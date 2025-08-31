package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class DebugLogger {
    private static final File logFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "application.log");

    public static void writeLog(String message) {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(LocalDateTime.now() + " - " + message + "\n");
        } catch (IOException ex) {
            FrameUtils.showErrorMessage("Error interno", "Error intentando registrar el LOG: " + ex.getMessage());
        }
    }
}
