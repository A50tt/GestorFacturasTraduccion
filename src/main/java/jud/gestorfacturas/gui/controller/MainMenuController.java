
package jud.gestorfacturas.gui.controller;

import interfaces.Controller;
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
    
    private final String[] ERROR_LIMIT_TYPE_MODULE_EXCEEDED = {"Límite excedido", "Ya existe una ventana de este módulo abierta."};
    
    private int modulesOpened = 0;
    private boolean modificarEmisorOpened = false;
    private boolean configuracionOpened = false;
    
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
    
    public void onWindowClosing() {
        if (modulesOpened > 0) {
            int input = FrameUtils.showQuestionBox("Salir", "Aún existen ventanas abiertas. ¿Cerrar aplicación?");
            if (input == 0) { //0 si ok, 1 si cancel
                closeView();
            }
        } else {
            mainMenuView.dispose();
        }
    }

    public void returnControlToSource() {
        this.sourceController.setVisible(true);
        modulesOpened--;
    }

    public void returnControlToSource(Controller controller) {
        if (controller instanceof ModificarEmisorController) {
            modificarEmisorOpened = false;
        } else if (controller instanceof ConfigurationController) {
            configuracionOpened = false;
        }
        this.setVisible(true);
        modulesOpened--;
    }

    @Override
    public void setVisible(boolean visible) {
        mainMenuView.setVisible(visible);
    }
    
    @Override
    public void closeView() {
        System.exit(0);
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
        modulesOpened++;
    }
    
    public void openCrearClienteView() {
        new NuevoClienteController(this);
        modulesOpened++;
    }
    
    public void openModificarClienteView() {
        new ModificarClienteController(this);
        modulesOpened++;
    }
    
    public void openModificarEmisorView() {
        if (!modificarEmisorOpened) {
            modificarEmisorOpened = true;
            new ModificarEmisorController(this);
            modulesOpened++;
        } else {
            showLimitModuleExceededMessage();
        }
    }
    
    public void openListaFacturasView() {
        new InvoiceLookupController(this);
        modulesOpened++;
    }
    
    public void openConfigurationView() {
        if (!configuracionOpened) {
            configuracionOpened = true;
            new ConfigurationController(this);
            modulesOpened++;
        } else {
            showLimitModuleExceededMessage();
        }
    }
    
    public void showLimitModuleExceededMessage() {
        FrameUtils.showErrorMessage(ERROR_LIMIT_TYPE_MODULE_EXCEEDED[0], ERROR_LIMIT_TYPE_MODULE_EXCEEDED[1]);
    }
}