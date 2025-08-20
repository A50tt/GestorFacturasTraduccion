
package jud.gestorfacturas.gui.controller;

import jud.gestorfacturas.gui.view.MainMenuView;
import javax.swing.JButton;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.manager.FrameUtils;

public class MainMenuController implements Controller {
    
    MainMenuView view;
    Controller sourceController;
    
    JButton crearFacturaBtn;
    JButton crearClienteBtn;
    JButton modificarClienteBtn;
    JButton datosPropiosBtn;
    JButton configBtn;
    
    private final String INFO_NO_EXISTE_EMISOR = "No se han iniciado los datos del EMISOR.";
    private final String INFO_NO_EXISTE_DEUDOR = "No se ha registrado ningún DEUDOR.";
    private final String INFO_NO_EXISTEN_EMISOR_NI_DEUDOR = "No se han registrado datos del EMISOR ni de ningún DEUDOR (o no hay ninguno con status 'activado').";
    
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
        configBtn = view.configBtn;
        configBtn.setIcon(FrameUtils.CONFIG_FLATSVGICON);
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
    
    public void openCrearFacturaView() {
        DBUtils dbutils = new DBUtils();
        byte caso = 0;
        if (dbutils.getUnicoEmisor() == null) {
            caso += 1;
            if (!dbutils.hayClientesActivos()) {
                caso += 2;
            }
        }
        
        switch (caso) {
            case 1:
                FrameUtils.showInfoMessage("Aviso", INFO_NO_EXISTE_EMISOR);
                break;
            case 2:
                FrameUtils.showInfoMessage("Aviso", INFO_NO_EXISTE_DEUDOR);
                break;
            case 3:
                FrameUtils.showInfoMessage("Aviso", INFO_NO_EXISTEN_EMISOR_NI_DEUDOR);
                break;
        }
        
        FacturaController fc = new FacturaController(this);
        view.setVisible(false);
    }
}