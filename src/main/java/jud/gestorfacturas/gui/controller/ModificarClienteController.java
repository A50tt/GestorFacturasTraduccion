
package jud.gestorfacturas.gui.controller;

import jud.gestorfacturas.gui.view.ModificarClienteView;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.manager.FrameUtils;
import jud.gestorfacturas.manager.Utils;
import jud.gestorfacturas.model.Cliente;

public class ModificarClienteController implements Controller {
    
    Utils utils = new Utils();
    
    ModificarClienteView view;
    Controller sourceController;
    
    protected JButton actualizarBtn;
    protected JButton anadirBtn;
    protected JTextField numeroClienteTxtField;
    protected JTextField codigoPostalTxtField;
    protected JTextField stateTxtField;
    protected JTextField direccionTxtField;
    protected JTextField nifTxtField;
    protected JButton nombreClienteSearchBtn;
    protected JButton resetClienteBtn;
    protected JTextField nombreTxtField;
    
    public ModificarClienteController(Controller _sourceController) {
        view = new ModificarClienteView(this);
        this.sourceController = _sourceController;
        initialize();
        view.setVisible(true);
    }
    
    private void initialize() {
    actualizarBtn = view.switchEstadoClienteBtn;
    anadirBtn = view.actualizarBtn;
    numeroClienteTxtField = view.numeroClienteTxtField;
    codigoPostalTxtField = view.codigoPostalTxtField;
    direccionTxtField = view.direccionTxtField;
    nifTxtField = view.nifTxtField;
    nombreClienteSearchBtn = view.nombreClienteSearchBtn;
    nombreClienteSearchBtn.setIcon(FrameUtils.SEARCH_FLATSVGICON);
    nombreTxtField = view.nombreTxtField;
    stateTxtField = view.stateTxtField;
    resetClienteBtn = view.resetClienteBtn;
    resetClienteBtn.setIcon(FrameUtils.REDO_FLATSVGICON);
    }
    
    public void cargaDatosDeNumeroCliente() {
        DBUtils dbUtils = new DBUtils();
        Cliente cliente = dbUtils.getClienteById(numeroClienteTxtField.getText());
        if (cliente != null && cliente.getNif() != null) {
            nifTxtField.setText(cliente.getNif());
            nombreTxtField.setText(cliente.getNombre());
            nombreTxtField.setEditable(true);
            direccionTxtField.setText(cliente.getDireccion());
            direccionTxtField.setEditable(true);
            codigoPostalTxtField.setText(cliente.getCodigoPostal());
            codigoPostalTxtField.setEditable(true);
            stateTxtField.setText(cliente.isActivado() ? "Activado" : "Desactivado");
            if (!cliente.isActivado()) {
                stateTxtField.setForeground(Color.red);
            } else {
                stateTxtField.setForeground(Color.black);
            }
            numeroClienteTxtField.setEditable(false);
        } else {
            FrameUtils.showErrorMessage("Error", "El numero de cliente '" + numeroClienteTxtField.getText() + "' no existe.");
            nifTxtField.setText("");
            nombreTxtField.setText("");
            direccionTxtField.setText("");
            codigoPostalTxtField.setText("");
            stateTxtField.setText("");
        }
    }

    public void actualizaCliente () {
        DBUtils dbUtils = new DBUtils();
        
        String nifOldCliente = (nifTxtField.getText() == null) ? null : nifTxtField.getText();
        
        if (!nifOldCliente.equals("")) {
            Cliente clienteDB = dbUtils.getClienteByNif(nifOldCliente);
            clienteDB.setNombre(nombreTxtField.getText());
            clienteDB.setDireccion(direccionTxtField.getText());
            clienteDB.setCodigoPostal(codigoPostalTxtField.getText());
            
            dbUtils.getEntityManager().getTransaction().begin();
            dbUtils.mergeIntoDB(clienteDB);
            dbUtils.getEntityManager().getTransaction().commit();
            
            FrameUtils.showPlainMessage("Éxito", "El cliente ha sido actualizado correctamente.");
        } else {
            FrameUtils.showErrorMessage("Cliente no seleccionado", "No se ha seleccionado ningún cliente al que aplicar la modificación.\n"
                    + "Introduce el número de cliente y pulsa 'Enter' o utiliza la búsqueda manual.");
        }
    }
    
    public void reiniciarCamposEditables() {
        numeroClienteTxtField.setText(null);
        numeroClienteTxtField.setEditable(true);
        
        nifTxtField.setText(null);
        
        nombreTxtField.setText(null);
        nombreTxtField.setEditable(false);
        
        direccionTxtField.setText(null);
        direccionTxtField.setEditable(false);
        
        codigoPostalTxtField.setText(null);
        codigoPostalTxtField.setEditable(false);
        
        stateTxtField.setText(null);
    }
    
    public void switchEstadoCliente() {
        boolean orden;
        DBUtils dbUtils = new DBUtils();
        
        String nifOldCliente = (nifTxtField.getText() == null) ? null : nifTxtField.getText();
        
        if (!nifOldCliente.equals("")) {
            Cliente clienteDB = dbUtils.getClienteByNif(nifOldCliente);
            if (clienteDB.isActivado()) {
                orden = false; //Cliente está activado, hay que desactivarlo.
            } else {
                orden = true; //Cliente está desactivado, hay que activarlo.
            }
            String estadoString = (orden == true) ? "Activado" : "Desactivado";
            if (clienteDB.isActivado() == orden) {
                FrameUtils.showInfoMessage("Acción no es necesaria", "El cliente ya se estaba " + estadoString.toLowerCase() + ".");
            } else {
                clienteDB.setActivado(orden);

                dbUtils.getEntityManager().getTransaction().begin();
                dbUtils.mergeIntoDB(clienteDB);
                dbUtils.getEntityManager().getTransaction().commit();
                stateTxtField.setText(estadoString);
                if (!orden) {
                    stateTxtField.setForeground(Color.red);
                } else {
                    stateTxtField.setForeground(Color.black);
                }
                FrameUtils.showPlainMessage("Éxito", "El cliente ha sido " + estadoString.toLowerCase() + " correctamente.");

            }
        } else {
            FrameUtils.showErrorMessage("Cliente no seleccionado", "No se ha seleccionado ningún cliente al que aplicar la modificación.\n"
                    + "Introduce el número de cliente y pulsa 'Enter' o utiliza la búsqueda manual.");
        }
    }
    
    public void recibeClienteLookup(String id) {
        numeroClienteTxtField.setText(String.valueOf(id));
        cargaDatosDeNumeroCliente();
    }
 
    public void abrirClienteLookupFrame() {
        ClienteLookupController clc = new ClienteLookupController(this, false);
    }

    @Override
    public void setVisible(boolean visible) {
        view.setVisible(visible);
    }

    @Override
    public void returnControlToSource() {
        this.sourceController.setVisible(true);
    }

}
