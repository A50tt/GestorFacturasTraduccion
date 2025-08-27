package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import jud.gestorfacturas.gui.MainContainerController;
import jud.gestorfacturas.gui.configuracion.ConfigAparienciaController;

public class ConfigUtils {
    
    public static final String CONFIG_FILEPATH = "src/main/resources/META-INF/config.properties";
    
    public static final Map<String, String> UI_THEMES = new LinkedHashMap<>() {{
            put("Metal", "javax.swing.plaf.metal.MetalLookAndFeel");
            put("Nimbus", "javax.swing.plaf.nimbus.NimbusLookAndFeel");
            put("Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            put("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            put("FlatLaF Light", "com.formdev.flatlaf.FlatLightLaf");
            put("FlatLaf Dark", "com.formdev.flatlaf.FlatDarkLaf");
            put("FlatLaf IntelliJ", "com.formdev.flatlaf.FlatIntelliJLaf");
            put("FlatLaf Darcula", "com.formdev.flatlaf.FlatDarculaLaf");
            put("FlatLaf macOS Light", "com.formdev.flatlaf.themes.FlatMacLightLaf");
            put("FlatLaf macOS Dark", "com.formdev.flatlaf.themes.FlatMacDarkLaf");
    }};
    
    public static Properties loadAllProperties() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(CONFIG_FILEPATH));
            return props;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String loadProperty(String key) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(CONFIG_FILEPATH));
            return props.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void storeProperties(Map<String, String> properties) {
        Properties props = loadAllProperties();
        // Save properties back to file
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILEPATH)) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                props.put(entry.getKey(), entry.getValue());
                if (entry.getKey().equals("ui.theme")) {
                    applyUIThemeChange(entry.getValue());
                }
            }
            props.store(out, null);
        } catch (FileNotFoundException ex) {
            ex.getStackTrace();
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }
    
    public static void applyUIThemeChange(String themeKey) {
        try {
                UIManager.setLookAndFeel(ConfigUtils.UI_THEMES.get(themeKey));
                SwingUtilities.updateComponentTreeUI(MainContainerController.frameContainerView);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConfigAparienciaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(ConfigAparienciaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ConfigAparienciaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(ConfigAparienciaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    }
}
