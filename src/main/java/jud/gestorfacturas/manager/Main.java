package jud.gestorfacturas.manager;

import utils.FrameUtils;
import javax.persistence.PersistenceException;
import jud.gestorfacturas.gui.MainContainerController;

public class Main {
    
    //TODO Testear entrada en vacío
    //TODO Configuración VIEW
    //TODO Puntos de miles en factura
    //TODO Puntos de miles en factura
    
    public static void main(String[] args) {
        new Main().inicio();
    }

    public void inicio() {
        try {
            new MainContainerController();
        } catch (PersistenceException ex) {
            FrameUtils.showErrorMessage("FATAL ERROR", ex.toString());
        }
    }
}
