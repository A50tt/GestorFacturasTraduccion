package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DebugLogger {
    
    private static final String ERROR_MESSAGE_TITLE = "Error interno";
    private static final String ERROR_MESSAGE_BODY = "Error intentando registrar el LOG: ";
    private static final File logFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "application.log");

    public static void writeLog(String message) {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write("*** [ERROR] " + LocalDateTime.now() + " - " + message + "\n");
        } catch (IOException ex) {
            FrameUtils.showErrorMessage(ERROR_MESSAGE_TITLE, ERROR_MESSAGE_BODY + ex.getMessage());
        }
    }
   
    public static void writeLog(Exception e, StackTraceElement[] stackTrace) {
        String result = Arrays.stream(stackTrace)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n\t"));

//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(e.getMessage() + ":\n");
//        for (StackTraceElement stack: stackTrace) {
//            stringBuilder.append(stack.toString());
//        }
        try (FileWriter fw = new FileWriter(logFile, true)) {
//            fw.write("*** " + LocalDateTime.now() + " - " + stringBuilder + "\n");
            fw.write("*** [ERROR] " + LocalDateTime.now() + " - " + e.getMessage() + "\n\t" + result + "\n");
        } catch (IOException ex) {
            FrameUtils.showErrorMessage(ERROR_MESSAGE_TITLE, ERROR_MESSAGE_BODY + ex.getMessage());
        }
    }
}
