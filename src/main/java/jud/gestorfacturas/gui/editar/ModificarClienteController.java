
package jud.gestorfacturas.gui.editar;

import jud.gestorfacturas.gui.buscar.BuscarClienteController;
import jud.gestorfacturas.interfaces.Controller;
import jud.gestorfacturas.interfaces.DataListenerController;
import jud.gestorfacturas.gui.editar.ModificarClienteView;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import utils.FrameUtils;
import jud.gestorfacturas.model.Cliente;
import utils.JSONUtils;

public class ModificarClienteController implements Controller, DataListenerController {
    
    ModificarClienteView modificarClienteView;
    
    private final String ERROR_CLIENT_OBLIGATORY_FIELDS = "Por favor, rellene todos los campos obligatorios.";
    private final Color DEFAULT_BG_COLOR = Color.white;
    private final Color ERROR_BG_COLOR = Color.red;
    private String viewName = "Modificar cliente";
    
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
        modificarClienteView = new ModificarClienteView(this);
        initialize();
    }
    
    private void initialize() {
    actualizarBtn = modificarClienteView.switchEstadoClienteBtn;
    anadirBtn = modificarClienteView.actualizarBtn;
    numeroClienteTxtField = modificarClienteView.numeroClienteTxtField;
    codigoPostalTxtField = modificarClienteView.codigoPostalTxtField;
    direccionTxtField = modificarClienteView.direccionTxtField;
    nifTxtField = modificarClienteView.nifTxtField;
    nombreClienteSearchBtn = modificarClienteView.nombreClienteSearchBtn;
    nombreClienteSearchBtn.setIcon(FrameUtils.SEARCH_FLATSVGICON);
    nombreTxtField = modificarClienteView.nombreTxtField;
    stateTxtField = modificarClienteView.stateTxtField;
    resetClienteBtn = modificarClienteView.resetClienteBtn;
    resetClienteBtn.setIcon(FrameUtils.REDO_FLATSVGICON);
    }
    
    public void cargaDatosDeNumeroCliente() {
        Cliente cliente = JSONUtils.getClienteById(Integer.parseInt(numeroClienteTxtField.getText()));
//        Cliente cliente = dbUtils.getClienteById(numeroClienteTxtField.getText());
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

    public void actualizaCliente() {
        String nombreCliente = (nombreTxtField.getText().isBlank()) ? null : nifTxtField.getText();
        
        if (nombreCliente != null) {
            Cliente clienteDB = JSONUtils.getClienteById(Integer.parseInt(numeroClienteTxtField.getText()));
//            Cliente clienteDB = dbUtils.getClienteByNif(nifTxtField.getText());
            clienteDB.setNombre(nombreTxtField.getText());
            clienteDB.setDireccion(direccionTxtField.getText());
            clienteDB.setCodigoPostal(codigoPostalTxtField.getText());
            
            nombreTxtField.setBackground(DEFAULT_BG_COLOR);
            
            JSONUtils.actualizaCliente(clienteDB);
            
//            dbUtils.getEntityManager().getTransaction().begin();
//            dbUtils.mergeIntoDB(clienteDB);
//            dbUtils.getEntityManager().getTransaction().commit();

        } else if (nifTxtField.getText().isBlank()) {
            FrameUtils.showErrorMessage("Cliente no seleccionado", "No se ha seleccionado ningún cliente al que aplicar la modificación.\n"
                    + "Introduce el número de cliente y pulsa 'Enter' o utiliza la búsqueda manual.");
        } else if (nombreCliente == null) {
            nombreTxtField.setBackground(ERROR_BG_COLOR);
            FrameUtils.showErrorMessage("Error", ERROR_CLIENT_OBLIGATORY_FIELDS);
        } else if (nifTxtField.getText().isBlank()) {
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
        String nifOldCliente = (nifTxtField.getText() == null) ? null : nifTxtField.getText();
        
        if (!nifOldCliente.equals("")) {
            Cliente clienteDB = JSONUtils.getClienteById(Integer.parseInt(numeroClienteTxtField.getText()));
//            Cliente clienteDB = dbUtils.getClienteByNif(nifOldCliente);
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
                JSONUtils.saveCliente(clienteDB);
//                dbUtils.getEntityManager().getTransaction().begin();
//                dbUtils.mergeIntoDB(clienteDB);
//                dbUtils.getEntityManager().getTransaction().commit();
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
    
    @Override
    public void recibeClienteLookup(String id) {
        numeroClienteTxtField.setText(String.valueOf(id));
        cargaDatosDeNumeroCliente();
    }
 
    public void abrirClienteLookupFrame() {
        BuscarClienteController clc = new BuscarClienteController(this, false);
    }

    @Override
    public JPanel getView() {
        return this.modificarClienteView;
    }

    @Override
    public String getViewName() {
        return this.viewName;
    }

    @Override
    public void setViewName(String newName) {
        this.viewName = newName;
    }
}
