
package jud.gestorfacturas.gui.controller;

import jud.gestorfacturas.gui.view.NuevoClienteView;
import java.awt.Color;
import java.time.LocalDateTime;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTextField;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.manager.FrameUtils;
import jud.gestorfacturas.manager.Utils;
import jud.gestorfacturas.model.Cliente;

public class NuevoClienteController implements Controller {
    
    Utils utils = new Utils();
    Color DEFAULT_BG_COLOR = Color.white;
    Color ERROR_BG_COLOR = Color.red;
    String[] RESPUESTAS_MSGBOX_FALTAN_OPCIONALES = {"Continuar", "Cancelar"};

    NuevoClienteView view;
    Controller sourceController;
    
    protected JButton anadirBtn;
    protected JTextField codigoPostalTxtField;
    protected JTextField direccionTxtField;
    protected JTextField nifTxtField;
    protected JTextField nombreTxtField;
    
    public NuevoClienteController(Controller _sourceController) {
        view = new NuevoClienteView(this);
        this.sourceController = _sourceController;
        initialize();
        view.setVisible(true);
    }
    
    private void initialize() {
    anadirBtn = view.anadirBtn;
    codigoPostalTxtField = view.codigoPostalTxtField;
    direccionTxtField = view.direccionTxtField;
    nifTxtField = view.nifTxtField;
    nombreTxtField = view.nombreTxtField;
    }
    
    public boolean verificarCamposCorrectos() {
        boolean isCorrect = true;
        boolean faltaInfoOpcional = false;
        if (nifTxtField.getText().isEmpty()) {
            isCorrect = false;
            nifTxtField.setBackground(ERROR_BG_COLOR);
        } else {
            nifTxtField.setBackground(DEFAULT_BG_COLOR);
        }
        if (nombreTxtField.getText().isEmpty()) {
            isCorrect = false;
            nombreTxtField.setBackground(ERROR_BG_COLOR);
        } else {
            nombreTxtField.setBackground(DEFAULT_BG_COLOR);
        }
        if (direccionTxtField.getText().isEmpty()) {
            faltaInfoOpcional = false;
        }
        if (codigoPostalTxtField.getText().isEmpty()) {
            faltaInfoOpcional = true;
        }
        
        if (isCorrect && faltaInfoOpcional) {
            int input = JOptionPane.showOptionDialog(null, "Faltan campos opcionales por completar. ¿Continuar?", "Campos incompletos", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, RESPUESTAS_MSGBOX_FALTAN_OPCIONALES, null);
            //showInfoMessage("Faltan campos opcionales" ,"No todos los campos han sido completados. ¿Proceder?");
            if (input == 1) { //0 si ok, 1 si cancel
                if (direccionTxtField.getText().isEmpty()) {
                    isCorrect = false;
                    setErrorBackground(direccionTxtField);
                }
                if (codigoPostalTxtField.getText().isEmpty()) {
                    isCorrect = false;
                    setErrorBackground(codigoPostalTxtField);
                }
            } else {
                setDefaultBackground(direccionTxtField);
                setDefaultBackground(codigoPostalTxtField);
            }
        }
        return isCorrect;
    }
    
    public Cliente generaCliente() {
        DBUtils dbUtils = new DBUtils();
        String nombreCliente = nombreTxtField.getText();
        String direccionCliente = direccionTxtField.getText();
        String codigoPostalCliente = codigoPostalTxtField.getText();
        String nifCliente = nifTxtField.getText();
        Cliente cliente = new Cliente(nifCliente, nombreCliente, direccionCliente, codigoPostalCliente);
        cliente.setId(dbUtils.getSiguienteIdCliente());
        return cliente;
    }
    
    public void registraCliente(Cliente cliente) {
        DBUtils dbUtils = new DBUtils();
        
        if (!dbUtils.clienteExists(cliente)) {
            dbUtils.getEntityManager().getTransaction().begin();
            dbUtils.mergeIntoDB(cliente);
            dbUtils.getEntityManager().getTransaction().commit();
            FrameUtils.showInfoMessage("Éxito", "El cliente ha sido registrado correctamente.");
        } else {
            LocalDateTime ts = dbUtils.getTimestampCliente(cliente).toLocalDateTime();
            FrameUtils.showErrorMessage("ERROR", "El número de cliente " + cliente.getId() + " con NIF " + cliente.getNif() + " ya fue registrado el " + ts.getDayOfMonth() + "-" + ts.getMonthValue() + "-" + ts.getYear() + " a las " + ts.getHour() + ":" + ts.getMinute());
        }
    }

    public void setDisabledBackground(JComponent comp) {
        ((JComponent) comp).setBackground(new Color(238, 238, 238));
    }

    public void setErrorBackground(JComponent comp) {
        ((JComponent) comp).setBackground(Color.red);
    }

    public void setDefaultBackground(JComponent comp) {
        ((JComponent) comp).setBackground(Color.white);
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
