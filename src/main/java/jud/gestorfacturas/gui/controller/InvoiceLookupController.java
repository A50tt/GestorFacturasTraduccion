
package jud.gestorfacturas.gui.controller;

import interfaces.Controller;
import interfaces.DataListenerController;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import jud.gestorfacturas.gui.view.InvoiceLookupView;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.manager.FrameUtils;
import jud.gestorfacturas.manager.PDFGenerator;
import jud.gestorfacturas.manager.Utils;
import jud.gestorfacturas.model.Factura;

public class InvoiceLookupController implements Controller, DataListenerController {
    
    DBUtils dbUtils = new DBUtils();
    
    private Controller sourceController;
    private InvoiceLookupView invoiceLookupView;
    
    final String[] tiposCampo = {"Num. Factura", "Fecha emisión", "Fecha venc.", "N.º Cliente", "Nombre", "Importe total"};
    
    JComboBox campoBusquedaComboBox;
    JTextField inputTextField;
    JTable resultadosTable;
    
    public InvoiceLookupController(Controller _sourceController) {
        invoiceLookupView = new InvoiceLookupView(this);
        FrameUtils.centerViewOnScreen(invoiceLookupView);
        sourceController = _sourceController;
        initialize();
        invoiceLookupView.setVisible(true);
    }
    
    protected void initialize() {
        campoBusquedaComboBox = invoiceLookupView.campoBusquedaComboBox;
        for (String tipo : tiposCampo) {
             campoBusquedaComboBox.addItem(tipo);
        }
        campoBusquedaComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (inputTextField.getText().equals("")) {
                    lookupAllInvoices();
                } else {
                    lookupInvoice();
                }
            }
        });
        inputTextField = invoiceLookupView.inputTextField;
        resultadosTable = invoiceLookupView.resultadosTable;
        lookupAllInvoices();
    }
    
    public void lookupAllInvoices() {
        Factura[] listaClientesFiltrados = dbUtils.getTodasFacturas();
        deleteAllClientesRows();
        addFacturasRows(listaClientesFiltrados);
    }
    
    public void lookupInvoice() {
        String campoFiltro = null;
        String campoActual = campoBusquedaComboBox.getSelectedItem().toString();
        if (campoActual.equals(tiposCampo[0])) {
            campoFiltro = "f.numFactura";
//            campoFiltro = "f.numFactura";
        } else if (campoActual.equals(tiposCampo[1])) {
            campoFiltro = "to_char(f.fechaemision, 'YYYY-MM-DD')";
//            campoFiltro = "f.fechaEmision";
        } else if (campoActual.equals(tiposCampo[2])) {
            campoFiltro = "to_char(f.fechavencimiento, 'YYYY-MM-DD')";
//            campoFiltro = "f.fechaVencimiento";
        } else if (campoActual.equals(tiposCampo[3])) {
            campoFiltro = "c.id";
//            campoFiltro = "c.id";
        } else if (campoActual.equals(tiposCampo[4])) {
            campoFiltro = "c.nombre";
//            campoFiltro = "c.nombre";
        } else if (campoActual.equals(tiposCampo[5])) {
            campoFiltro = "f.importeTotal";
//            campoFiltro = "f.importeTotal";
        }
        String valor = inputTextField.getText().replace("*", "%");
        Factura[] listaFacturasFiltradas = dbUtils.getFacturasFiltradas(campoFiltro, valor);
//        Factura[] facturasFiltradasArray = new Factura[listaFacturasFiltradas.size()];
//        for (int i = 0; i < facturasFiltradasArray.length; i++) {
//            facturasFiltradasArray[i] = listaFacturasFiltradas.get(i);
//        }
        deleteAllClientesRows();
        addFacturasRows(listaFacturasFiltradas);
    }
    
    public void deleteAllClientesRows() {
        if (resultadosTable != null) {
            int rowsAlready = ((DefaultTableModel) resultadosTable.getModel()).getRowCount();
            for (int i = 0; i < rowsAlready; i++) {
                ((DefaultTableModel) resultadosTable.getModel()).removeRow(0);
            }
        }
    }
    
    public void addFacturasRows(Factura[] listaFacturasFiltradas) {
        for (int i = 0; i < listaFacturasFiltradas.length; i++) {
            Factura factura = listaFacturasFiltradas[i];
            String numFactura = factura.getNumFactura();
            String fechaEmision = Utils.convertDateToString(factura.getFechaEmision(), "yyyy-MM-dd");
            String fechaVencimiento = Utils.convertDateToString(factura.getFechaVencimiento(), "yyyy-MM-dd");
            int numCliente = Integer.valueOf(factura.getCliente().getId());
            String nombreCliente = factura.getCliente().getNombre();
            String importeTotal = String.format("%.2f", factura.getImporteTotal()).replace(".", ",");
            ((DefaultTableModel) resultadosTable.getModel()).addRow(new Object[]{numFactura, fechaEmision, fechaVencimiento, numCliente, nombreCliente, importeTotal});
        }
    }
    
    public void lookupAndOpenInvoice(int row) {
        Factura factura = dbUtils.getFacturaByNif(invoiceLookupView.resultadosTable.getModel().getValueAt(row, 0).toString());
        PDFGenerator.openPDF(factura.getPdfFactura());
    }

    @Override
    public void recibeClienteLookup(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setVisible(boolean visible) {
        invoiceLookupView.setVisible(false);
    }
    
    @Override
    public void closeView() {
        returnControlToSource(this);
        invoiceLookupView.dispose();
    }

    @Override
    public void returnControlToSource(Controller controller) {
        sourceController.returnControlToSource(this);
    }
}
