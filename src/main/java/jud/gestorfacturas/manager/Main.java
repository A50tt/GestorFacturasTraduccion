package jud.gestorfacturas.manager;

import jud.gestorfacturas.gui.controller.MainMenuController;

public class Main {

    public static void main(String[] args) {
        DBUtils dbUtils = new DBUtils();
        dbUtils.checkIfTablesAreCreatedOnDB();
        Main gf = new Main();
        gf.inicio();        
    }
    
    public void inicio() {
        MainMenuController mainMenu = new MainMenuController();
    }
}
