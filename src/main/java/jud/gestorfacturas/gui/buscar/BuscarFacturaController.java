
package jud.gestorfacturas.gui.buscar;

import jud.gestorfacturas.interfaces.Controller;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import jud.gestorfacturas.gui.buscar.BuscarFacturaView;
import utils.DBUtils;
import jud.gestorfacturas.manager.PDFGenerator;
import utils.FormatUtils;
import jud.gestorfacturas.model.Factura;

public class BuscarFacturaController implements Controller {
    
    DBUtils dbUtils = new DBUtils();
    
    private BuscarFacturaView invoiceLookupView;
    
    final String[] tiposCampo = {"Num. Factura", "Fecha emisión", "Fecha venc.", "N.º Cliente", "Nombre", "Importe total"};
    
    JComboBox campoBusquedaComboBox;
    JTextField inputTextField;
    JTable resultadosTable;
    
    private String viewName = "Buscar factura";
    
    public BuscarFacturaController() {
        invoiceLookupView = new BuscarFacturaView(this);
        initialize();
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
            String fechaEmision = FormatUtils.convertDateToString(factura.getFechaEmision(), "yyyy-MM-dd");
            String fechaVencimiento = FormatUtils.convertDateToString(factura.getFechaVencimiento(), "yyyy-MM-dd");
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
    public JPanel getView() {
        return this.invoiceLookupView;
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
