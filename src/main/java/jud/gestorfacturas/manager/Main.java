package jud.gestorfacturas.manager;

import utils.FrameUtils;
import utils.DBUtils;
import javax.persistence.PersistenceException;
import jud.gestorfacturas.gui.MainContainerController;

public class Main {

    public static void main(String[] args) {
        new Main().inicio();
    }

    public void inicio() {        
        boolean correctStart = true;
        try {
            EntityManagerLoader.getEntityManagerConfiguredInstance().isOpen();
        } catch (PersistenceException ex) {
            correctStart = false;
        }

        if (correctStart) {
            try {
                DBUtils dbUtils = new DBUtils();
                dbUtils.checkIfTablesAreCreatedOnDB();
                new MainContainerController();
            } catch (PersistenceException ex) {
                FrameUtils.showErrorMessage("FATAL ERROR", ex.toString());
            }
        } else {
            try {
                FrameUtils.showErrorMessage("Error", "La base de datos de PostgreSQL o las credenciales que se encuentran en el archivo de propiedades están vacías o son incorrectas.\nRecomendamos configurar la base de datos antes de empezar a usar la aplicación.");
                MainContainerController containerWindow = new MainContainerController();
                containerWindow.openConfiguracion();
            } catch (PersistenceException ex) {
                FrameUtils.showErrorMessage("FATAL ERROR", ex.toString());
            }
        }
    }
}