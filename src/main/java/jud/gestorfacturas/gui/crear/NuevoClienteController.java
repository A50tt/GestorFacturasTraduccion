package jud.gestorfacturas.gui.crear;

import jud.gestorfacturas.interfaces.Controller;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import utils.FrameUtils;
import jud.gestorfacturas.model.Cliente;
import utils.JSONUtils;

public class NuevoClienteController implements Controller {

    Color defaultTxtFieldGbColor = Color.white;
    Color ERROR_BG_COLOR = Color.red;
    public static final String VIEW_NAME = "Crear cliente";
    public String finalViewName;

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
        defaultTxtFieldGbColor = codigoPostalTxtField.getBackground();
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
            nifTxtField.setBackground(defaultTxtFieldGbColor);
        }
        if (nombreTxtField.getText().isEmpty()) {
            obligatorios--;
            nombreTxtField.setBackground(ERROR_BG_COLOR);
        } else {
            obligatorios++;
            nombreTxtField.setBackground(defaultTxtFieldGbColor);
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

            int input = FrameUtils.showQuestionBoxContinuarCancelar("Campos incompletos", "Faltan campos opcionales por completar. ¿Continuar?");
            if (input == JOptionPane.YES_OPTION) { //0 si ok, 1 si cancel
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
        Cliente cliente = new Cliente(nifCliente, nombreCliente, direccionCliente, codigoPostalCliente, false);
        cliente.setId(JSONUtils.getSiguienteIdClienteDisponible());
        return cliente;
    }

    public void registraCliente(Cliente cliente) {
        if (JSONUtils.saveCliente(cliente) == -2) {
            nifTxtField.setBackground(ERROR_BG_COLOR);
        }
    }

    public void setDisabledBackground(JComponent comp) {
        ((JComponent) comp).setBackground(new Color(238, 238, 238));
    }

    public void setErrorBackground(JComponent comp) {
        ((JComponent) comp).setBackground(ERROR_BG_COLOR);
    }

    public void setDefaultBackground(JComponent comp) {
        ((JComponent) comp).setBackground(defaultTxtFieldGbColor);
    }

    @Override
    public JPanel getView() {
        return nuevoClienteView;
    }

    @Override
    public String getViewName() {
        if (this.finalViewName != null) {
            return finalViewName;
        } else {
            return this.VIEW_NAME;
        }
    }

    @Override
    public void setViewName(String str) {
        finalViewName = str;
    }
}
