
package jud.gestorfacturas.gui;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTextField;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.manager.Utils;
import jud.gestorfacturas.model.Cliente;

public class ModificarClienteController {
    
    Utils utils = new Utils();
    
    ModificarClienteView view;
    
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
    
    public ModificarClienteController() {
        view = new ModificarClienteView(this);
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
    nombreClienteSearchBtn.setIcon(utils.SEARCH_FLATSVGICON);
    nombreTxtField = view.nombreTxtField;
    stateTxtField = view.stateTxtField;
    resetClienteBtn = view.resetClienteBtn;
    resetClienteBtn.setIcon(utils.REDO_FLATSVGICON);
    }
    
    protected void cargaDatosDeNumeroCliente() {
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
            showErrorMessage("Error", "El numero de cliente '" + numeroClienteTxtField.getText() + "' no existe.");
            nifTxtField.setText("");
            nombreTxtField.setText("");
            direccionTxtField.setText("");
            codigoPostalTxtField.setText("");
            stateTxtField.setText("");
        }
    }

    public void showPlainMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.PLAIN_MESSAGE);
    }

    public void showErrorMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    public void showInfoMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    protected void actualizaCliente () {
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
            
            showPlainMessage("Éxito", "El cliente ha sido actualizado correctamente.");
        } else {
            showErrorMessage("Cliente no seleccionado", "No se ha seleccionado ningún cliente al que aplicar la modificación.\n"
                    + "Introduce el número de cliente y pulsa 'Enter' o utiliza la búsqueda manual.");
        }
    }
    
    protected void reiniciarCamposEditables() {
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
    
    protected void switchEstadoCliente() {
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
                showInfoMessage("Acción no es necesaria", "El cliente ya se estaba " + estadoString.toLowerCase() + ".");
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
                showPlainMessage("Éxito", "El cliente ha sido " + estadoString.toLowerCase() + " correctamente.");

            }
        } else {
            showErrorMessage("Cliente no seleccionado", "No se ha seleccionado ningún cliente al que aplicar la modificación.\n"
                    + "Introduce el número de cliente y pulsa 'Enter' o utiliza la búsqueda manual.");
        }
    }
    
}
