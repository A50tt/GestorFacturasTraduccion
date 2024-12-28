package jud.gestorfacturas.gui.controller;

import jud.gestorfacturas.gui.view.ClienteLookupView;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.manager.FrameUtils;
import jud.gestorfacturas.manager.Utils;
import jud.gestorfacturas.model.Cliente;

public class ClienteLookupController {
    
    Utils utils = new Utils();
    DBUtils dbUtils = new DBUtils();
    ClienteLookupView view;
    Controller sourceController;
    boolean checkIfClienteIsActivado;
    
    JFrame jframe;
    JComboBox campoClienteComboBox;
    JTextField inputTextField;
    JButton buscarBtn;
    JTable resultadosTable;
    
    final String[] tiposCampo = {"Nombre", "NIF", "Dirección", "Código postal"};
    final String[] columnasTabla = {"ID", "Nombre", "NIF", "Dirección", "Código postal"};

    public ClienteLookupController(Controller _sourceController, boolean _checkIfClienteIsActivado) {
        view = new ClienteLookupView(this);
        sourceController = _sourceController;
        checkIfClienteIsActivado = _checkIfClienteIsActivado;
        initialize();
        view.setVisible(true);
    }

    private void initialize() {
        campoClienteComboBox = view.campoClienteComboBox;
        for (String tipo : tiposCampo) {
             campoClienteComboBox.addItem(tipo);
        }
        inputTextField = view.inputTextField;
        resultadosTable = view.resultadosTable;
        campoClienteComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (inputTextField.getText().equals("")) {
                    lookupAllClientes();
                } else {
                    lookupClientes();
                }
            }
        });
        lookupAllClientes();
        resultadosTable.getColumn("ID").setCellRenderer(
                new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setText(value.toString());
                setBackground(Color.LIGHT_GRAY);
                return this;
            }
        });

        resultadosTable.getColumn("Estado").setCellRenderer(
                new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setText(value.toString());
                setBackground(Color.LIGHT_GRAY);
                return this;
            }
        });
//        ((DefaultTableModel) resultadosTable.getModel()).addRow(new Object[]{"Hola 1", "Nombre 1", "Dirección 1", "CP 1", "Activado"});
//        ((DefaultTableModel) resultadosTable.getModel()).addRow(new Object[]{"Hola 2", "Nombre 2", "Dirección 2", "CP 2", "Desactivado"});
//        ((DefaultTableModel) resultadosTable.getModel()).addRow(new Object[]{"Hola 3", "Nombre 3", "Dirección 3", "CP 3", "Activado"});
//        ((DefaultTableModel) resultadosTable.getModel()).addRow(new Object[]{"Hola 4", "Nombre 4", "Dirección 4", "CP 4", "Activado"});
//        ((DefaultTableModel) resultadosTable.getModel()).addRow(new Object[]{"Hola 5", "Nombre 5", "Dirección 5", "CP 5", "Desactivado"});
    }
    
    public Cliente getCliente() {
        return null;
    }
    
    public void lookupAllClientes() {
        List listaClientesFiltrados = dbUtils.getTodosClientes();
        deleteAllClientesRows();
        addClientesRows(listaClientesFiltrados);
    }

    public void deleteAllClientesRows() {
        if (resultadosTable != null) {
            int rowsAlready = ((DefaultTableModel) resultadosTable.getModel()).getRowCount();
            for (int i = 0; i < rowsAlready; i++) {
                ((DefaultTableModel) resultadosTable.getModel()).removeRow(0);
            }
        }
    }

    public void addClientesRows(List listaClientesFiltrados) {
        for (int i = 0; i < listaClientesFiltrados.size(); i++) {
            Object[] cliente = ((Object[])listaClientesFiltrados.get(i));
            int id = (Integer) cliente[0];
            String nif = cliente[1].toString();
            String nombre = cliente[2].toString();
            String direccion = cliente[3].toString();
            String codigoPostal = cliente[4].toString();
            String estado = ((boolean)cliente[5] == true) ? "Activado" : "Desactivado";
            ((DefaultTableModel) resultadosTable.getModel()).addRow(new Object[]{id, nif, nombre, direccion, codigoPostal, estado});
        }
    }

    public void lookupClientes() {
        String valor = view.inputTextField.getText();
        if (valor.contains("*")) {
            valor = valor.replace("*", "%");
        }
        String campo = view.campoClienteComboBox.getSelectedItem().toString();
        String propiedad = null;
        //{"NIF", "Nombre", "Dirección", "Código postal"};
        if (campo == tiposCampo[0]) {
            propiedad = "nif";
        } else if (campo == tiposCampo[1]) {
            propiedad = "nombre";
        } else if (campo == tiposCampo[2]) {
            propiedad = "direccion";
        } else if (campo == tiposCampo[3]) {
            propiedad = "codigopostal";
        }
        List listaClientesFiltrados = dbUtils.getTodosClientesPorCampo(propiedad, valor);
        deleteAllClientesRows();
        addClientesRows(listaClientesFiltrados);
    }
    
    public void showDebugMsg(String msg) {
        showMessageDialog(null, msg, "DEBUG", JOptionPane.PLAIN_MESSAGE);
    }
  
    public void returnClienteToSource(int row) {
        
        if (checkIfClienteIsActivado) {
            if (view.resultadosTable.getModel().getValueAt(row, 5).toString().equals("Activado")) {
                sourceController.recibeClienteLookup(view.resultadosTable.getModel().getValueAt(row, 0).toString());
                view.dispose();
            } else {
                FrameUtils.showErrorMessage("Cliente desactivado", "El cliente no se puede utilizar porque está desactivado. Para activarlo, se ha de modificar desde 'Modificar clientes'.");
            }
        } else {
            sourceController.recibeClienteLookup(view.resultadosTable.getModel().getValueAt(row, 0).toString());
            view.dispose();
        }
    }
}
