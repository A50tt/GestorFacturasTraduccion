package jud.gestorfacturas.manager;

import com.formdev.flatlaf.FlatLightLaf;
import utils.FrameUtils;
import javax.persistence.PersistenceException;
import jud.gestorfacturas.gui.MainContainerController;

public class Main {
    
    //TODO Testear entrada en vacío
    //TODO Configuración VIEW
    //TODO Puntos de miles en factura
    //TODO Puntos de miles en factura
    
    public static void main(String[] args) {
        // Optionally set default theme here
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                new MainContainerController();
            } catch (PersistenceException ex) {
                FrameUtils.showErrorMessage("Error iniciando la aplicación", ex.toString());
            }
        });
    }
}
