package utils;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileUtils {

    // Solo sirve para extensiones sencillas (no con .tar.gz y cosas así)
    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // extensión vacía
        }
        return name.substring(lastIndexOf + 1);
    }
    
    public static boolean isOneOfExtensions(File file, String[] validExtensions) {
        String fileExtension = getFileExtension(file);
        for (String validExtension : validExtensions) {
            if (fileExtension.toLowerCase().equals(validExtension.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static File[] openFileChooser(Component view, boolean multi) {
        JFileChooser fileChooser = new JFileChooser();
        if (multi) {
            fileChooser.setMultiSelectionEnabled(true);
        }
        int seleccion = fileChooser.showOpenDialog(view);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            if (multi) {
                return fileChooser.getSelectedFiles();
            }
            return new File[]{fileChooser.getSelectedFile()};
        }
        return null;
    }
    
    public static File openFileChooserWithDefaultPath(String defaultPath, Component view) {
        String currentLogoDirectoryPath = new File(defaultPath).getParent();
        JFileChooser fileChooser = new JFileChooser(currentLogoDirectoryPath);
        int seleccion = fileChooser.showOpenDialog(view);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public static void copy(Path sourceFile, Path targetFile) {
        try {
            if (targetFile.toFile().exists()) {
                int respuesta = FrameUtils.showQuestionBoxContinuarCancelar("El archivo ya existe", "Ya existe un archivo PDF de la factura con número " + targetFile.getFileName() + ". ¿Sobreescribir?");
                if (respuesta == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            FrameUtils.showErrorMessage("Error", ex.getMessage(), ex);
        }
    }
}
