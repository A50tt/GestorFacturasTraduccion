package jud.gestorfacturas.gui.controller;

import interfaces.DataListenerController;
import jud.gestorfacturas.gui.view.ClienteLookupView;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.manager.FrameUtils;
import jud.gestorfacturas.manager.Utils;
import jud.gestorfacturas.model.Cliente;

public class ClienteLookupController {
    
    DBUtils dbUtils = new DBUtils();
    ClienteLookupView clienteLookupView;
    DataListenerController sourceController;
    boolean checkIfClienteIsActivado;
    
    JComboBox campoClienteComboBox;
    JTextField inputTextField;
    JTable resultadosTable;
    
    final String[] tiposCampo = {"Nombre", "NIF", "Dirección", "Código postal"};
    final String[] columnasTabla = {"ID", "Nombre", "NIF", "Dirección", "Código postal"};

    public ClienteLookupController(DataListenerController _sourceController, boolean _checkIfClienteIsActivado) {
        clienteLookupView = new ClienteLookupView(this);
        FrameUtils.centerViewOnScreen(clienteLookupView);
        sourceController = _sourceController;
        checkIfClienteIsActivado = _checkIfClienteIsActivado;
        initialize();
        clienteLookupView.setVisible(true);
    }

    private void initialize() {
        campoClienteComboBox = clienteLookupView.campoClienteComboBox;
        for (String tipo : tiposCampo) {
             campoClienteComboBox.addItem(tipo);
        }
        inputTextField = clienteLookupView.inputTextField;
        resultadosTable = clienteLookupView.resultadosTable;
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
            String nombre = cliente[1].toString();
            String nif = cliente[2].toString();
            String direccion = cliente[3].toString();
            String codigoPostal = cliente[4].toString();
            String estado = ((boolean)cliente[5] == true) ? "Activado" : "Desactivado";
            ((DefaultTableModel) resultadosTable.getModel()).addRow(new Object[]{id, nif, nombre, direccion, codigoPostal, estado});
        }
    }

    public void lookupClientes() {
        String valor = clienteLookupView.inputTextField.getText();
        if (valor.contains("*")) {
            valor = valor.replace("*", "%");
        }
        String campo = clienteLookupView.campoClienteComboBox.getSelectedItem().toString();
        String propiedad = null;
        if (campo == tiposCampo[0]) {
            propiedad = "nombre";
        } else if (campo == tiposCampo[1]) {
            propiedad = "nif";
        } else if (campo == tiposCampo[2]) {
            propiedad = "direccion";
        } else if (campo == tiposCampo[3]) {
            propiedad = "codigopostal";
        }
        List listaClientesFiltrados = dbUtils.getTodosClientesPorCampo(propiedad, valor);
        deleteAllClientesRows();
        addClientesRows(listaClientesFiltrados);
    }
  
    public void returnClienteToSource(int row) {
        if (checkIfClienteIsActivado) {
            if (clienteLookupView.resultadosTable.getModel().getValueAt(row, 5).toString().equals("Activado")) {
                sourceController.recibeClienteLookup(clienteLookupView.resultadosTable.getModel().getValueAt(row, 0).toString());
                clienteLookupView.dispose();
            } else {
                FrameUtils.showErrorMessage("Cliente desactivado", "El cliente no se puede utilizar porque está desactivado. Para activarlo, se ha de modificar desde 'Modificar clientes'.");
            }
        } else {
            sourceController.recibeClienteLookup(clienteLookupView.resultadosTable.getModel().getValueAt(row, 0).toString());
            clienteLookupView.dispose();
        }
    }
}
