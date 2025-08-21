package jud.gestorfacturas.manager;

import javax.persistence.PersistenceException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import jud.gestorfacturas.gui.controller.ConfigurationController;
import jud.gestorfacturas.gui.controller.MainMenuController;

public class Main {

    public static void main(String[] args) {
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
                new Main().inicio();
            } catch (PersistenceException ex) {
                FrameUtils.showErrorMessage("FATAL ERROR", ex.toString());
            }
        } else {
            try {
                JFrame jAviso = new JFrame();
                jAviso.setVisible(true);
                showMessageDialog(jAviso, "La base de datos de PostgreSQL y las credenciales que se encuentran en el archivo de propiedades están vacías o son incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
                jAviso.dispose();
                MainMenuController mmc = new MainMenuController();
                mmc.setVisible(false);
                ConfigurationController cc = new ConfigurationController(mmc);
            } catch (PersistenceException ex) {
                FrameUtils.showErrorMessage("FATAL ERROR", ex.toString());
            }
        }
    }

    public void inicio() {
        MainMenuController mainMenu = new MainMenuController();
    }
}
