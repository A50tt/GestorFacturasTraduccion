
package jud.gestorfacturas.gui.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import jud.gestorfacturas.gui.view.MainMenuView;
import javax.swing.JButton;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.manager.FrameUtils;

public class MainMenuController implements Controller {
    
    MainMenuView mainMenuView;
    Controller sourceController;
    
    JButton configBtn;
    
    private final String INFO_NO_EXISTE_EMISOR = "No se han iniciado los datos del EMISOR.";
    private final String INFO_NO_EXISTE_DEUDOR = "No se ha registrado ningún DEUDOR.";
    private final String INFO_NO_EXISTEN_EMISOR_NI_DEUDOR = "No se han registrado datos del EMISOR ni de ningún DEUDOR (o no hay ninguno con status 'activado').";
    
    public MainMenuController() {
        mainMenuView = new MainMenuView(this);
        FrameUtils.centerViewOnScreen(mainMenuView);
        initialize();
        mainMenuView.setVisible(true);
    }

    public MainMenuController(Controller _sourceController) {
        mainMenuView = new MainMenuView(this);
        FrameUtils.centerViewOnScreen(mainMenuView);
        this.sourceController = _sourceController;
        initialize();
        mainMenuView.setVisible(true);
    }
    
    protected void initialize() {
        configBtn = mainMenuView.configBtn;
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
        mainMenuView.setVisible(visible);
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
        
        new FacturaController(this);
    }
    
    public void openCrearClienteView() {
        new NuevoClienteController(this);
    }
    
    public void openModificarClienteView() {
        new ModificarClienteController(this);
    }
    
    public void openModificarEmisorView() {
        new ModificarEmisorController(this);
    }
    
    public void openListaFacturasView() {
        new InvoiceLookupController(this);
    }
    
    public void openConfigurationView() {
        new ConfigurationController(this);
    }
}