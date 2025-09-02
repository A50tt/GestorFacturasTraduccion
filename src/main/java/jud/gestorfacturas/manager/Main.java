package jud.gestorfacturas.manager;

import com.formdev.flatlaf.FlatLightLaf;
import utils.FrameUtils;
import javax.persistence.PersistenceException;
import jud.gestorfacturas.gui.MainContainerController;

public class Main {
    
    // TODO Testea theme dark en nueva factura
    // TODO Testear entrada en vacío
    
    public static void main(String[] args) {
        // Optionally set default theme here
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                new MainContainerController();
            } catch (PersistenceException ex) {
                FrameUtils.showErrorMessage("Error iniciando la aplicación", ex.toString(), ex);
            }
        });
    }
}
