package jud.gestorfacturas.gui.crear;

import jud.gestorfacturas.interfaces.Controller;
import jud.gestorfacturas.gui.crear.NuevoClienteView;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import utils.FrameUtils;
import jud.gestorfacturas.model.Cliente;
import utils.JSONUtils;

public class NuevoClienteController implements Controller {

    Color DEFAULT_BG_COLOR = Color.white;
    Color ERROR_BG_COLOR = Color.red;
    private String viewName = "Crear cliente";

    NuevoClienteView nuevoClienteView;

    protected JButton anadirBtn;
    protected JTextField codigoPostalTxtField;
    protected JTextField direccionTxtField;
    protected JTextField nifTxtField;
    protected JTextField nombreTxtField;

    public NuevoClienteController() {
        nuevoClienteView = new NuevoClienteView(this);
        initialize();
        nuevoClienteView.jGlobalPanel.setSize(600, 300);
    }

    private void initialize() {
        anadirBtn = nuevoClienteView.anadirBtn;
        codigoPostalTxtField = nuevoClienteView.codigoPostalTxtField;
        direccionTxtField = nuevoClienteView.direccionTxtField;
        nifTxtField = nuevoClienteView.nifTxtField;
        nombreTxtField = nuevoClienteView.nombreTxtField;
    }

    public void guardarCliente() {
        if (verificarCamposCorrectos()) {
            registraCliente(generaCliente());
        }
    }

    public boolean verificarCamposCorrectos() {
        int obligatorios = 0; // Cuenta los campos obligatorios rellenados
        int opcionales = 0; // Cuenta los campos obligatorios rellenados
        boolean faltaInfoOpcional = false;
        if (nifTxtField.getText().isEmpty()) {
            obligatorios--;
            nifTxtField.setBackground(ERROR_BG_COLOR);
        } else {
            obligatorios++;
            nifTxtField.setBackground(DEFAULT_BG_COLOR);
        }
        if (nombreTxtField.getText().isEmpty()) {
            obligatorios--;
            nombreTxtField.setBackground(ERROR_BG_COLOR);
        } else {
            obligatorios++;
            nombreTxtField.setBackground(DEFAULT_BG_COLOR);
        }
        if (!direccionTxtField.getText().isEmpty()) {
            opcionales++;
        }
        if (!codigoPostalTxtField.getText().isEmpty()) {
            opcionales++;
        }

        if (obligatorios == 2 && opcionales < 2) {
            setDefaultBackground(direccionTxtField);
            setDefaultBackground(codigoPostalTxtField);

            int input = FrameUtils.showQuestionBox("Campos incompletos", "Faltan campos opcionales por completar. ¿Continuar?");
            if (input == 0) { //0 si ok, 1 si cancel
                opcionales = 2;
            }
        }
        if (obligatorios < 2) {
            FrameUtils.showPlainMessage("Error", "Por favor, rellene todos los campos obligatorios.");
        } else if (opcionales == 2) {
            return true;
        }
        return false;
    }

    public Cliente generaCliente() {
        String nombreCliente = nombreTxtField.getText();
        String direccionCliente = direccionTxtField.getText();
        String codigoPostalCliente = codigoPostalTxtField.getText();
        String nifCliente = nifTxtField.getText();
        Cliente cliente = new Cliente(nifCliente, nombreCliente, direccionCliente, codigoPostalCliente);
        cliente.setId(JSONUtils.getSiguienteIdClienteDisponible());
//        cliente.setId(dbUtils.getSiguienteIdCliente());
        return cliente;
    }

    public void registraCliente(Cliente cliente) {
//        DBUtils dbUtils = new DBUtils();
//
//        if (!dbUtils.clienteExists(cliente)) {
//            dbUtils.getEntityManager().getTransaction().begin();
//            dbUtils.mergeIntoDB(cliente);
//            dbUtils.getEntityManager().getTransaction().commit();
//            FrameUtils.showInfoMessage("Éxito", "El cliente ha sido registrado correctamente con el ID n.º " + cliente.getId() + ".");
//        } else {
//            LocalDateTime ts = dbUtils.getTimestampCliente(cliente).toLocalDateTime();
//            FrameUtils.showErrorMessage("ERROR", "El NIF '" + cliente.getNif() + "' ya fue registrado el " + ts.getDayOfMonth() + "-" + ts.getMonthValue() + "-" + ts.getYear() + " a las " + ts.getHour() + ":" + String.format("%02d", ts.getMinute()) + "h. Pertenece al cliente con ID n.º '" + cliente.getId() + "'.");
//        }
        JSONUtils.saveCliente(cliente);
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
    public JPanel getView() {
        return nuevoClienteView;
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    @Override
    public void setViewName(String newName) {
        this.viewName = newName;
    }
}
