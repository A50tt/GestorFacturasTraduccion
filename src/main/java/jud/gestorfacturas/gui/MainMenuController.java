
package jud.gestorfacturas.gui;

import javax.swing.JButton;

public class MainMenuController implements Controller {
    
    MainMenuView view;
    Controller sourceController;
    
    JButton crearFacturaBtn;
    JButton crearClienteBtn;
    JButton modificarClienteBtn;
    JButton datosPropiosBtn;
    
    public MainMenuController() {
        view = new MainMenuView(this);
        initialize();
        view.setVisible(true);
    }
    
    public MainMenuController(Controller _sourceController) {
        view = new MainMenuView(this);
        this.sourceController = _sourceController;
        initialize();
        view.setVisible(true);
    }
    
    protected void initialize() {
        crearFacturaBtn = view.crearFacturaBtn;
        crearClienteBtn = view.crearClienteBtn;
        modificarClienteBtn = view.modificarClienteBtn;
        datosPropiosBtn = view.datosPropiosBtn;
    }

    @Override
    public void recibeClienteLookup(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void returnControlToSource() {
        this.sourceController.setVisible(true);
    }

    @Override
    public void setVisible(boolean visible) {
        view.setVisible(visible);
    }
}
