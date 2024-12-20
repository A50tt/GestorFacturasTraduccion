
package jud.gestorfacturas.gui;

import javax.swing.JButton;
import javax.swing.JTextField;
import jud.gestorfacturas.manager.Utils;

public class NuevoClienteController {
    
    Utils utils = new Utils();
    
    NuevoClienteView view;
    
    protected JButton actualizarBtn;
    protected JButton anadirBtn;
    protected JTextField codigoPostalTxtField;
    protected JTextField direccionTxtField;
    protected JTextField nifTxtField;
    protected JButton nombreClienteSearchBtn;
    protected JTextField nombreTxtField;
    
    public NuevoClienteController() {
        view = new NuevoClienteView(this);
        initialize();
        view.setVisible(true);
    }
    
    private void initialize() {
    actualizarBtn = view.actualizarBtn;
    anadirBtn = view.anadirBtn;
    codigoPostalTxtField = view.codigoPostalTxtField;
    direccionTxtField = view.direccionTxtField;
    nifTxtField = view.nifTxtField;
    nombreClienteSearchBtn = view.nombreClienteSearchBtn;
    nombreClienteSearchBtn.setIcon(utils.SEARCH_FLATSVGICON);
    nombreTxtField = view.nombreTxtField;
    }
    
}
