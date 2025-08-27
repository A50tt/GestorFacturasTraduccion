
package jud.gestorfacturas.gui.buscar;

import java.util.List;
import jud.gestorfacturas.interfaces.Controller;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import jud.gestorfacturas.gui.buscar.BuscarFacturaView;
import jud.gestorfacturas.manager.PDFGenerator;
import utils.FormatUtils;
import jud.gestorfacturas.model.Factura;
import utils.JSONUtils;

public class BuscarFacturaController implements Controller {
    
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
        List<Factura> listaClientesFiltrados = JSONUtils.findAllFacturas();
        deleteAllFacturasRows();
        addFacturasRows(listaClientesFiltrados);
    }
    
    public void lookupInvoice() {
        String campoFiltro = null;
        String campoActual = campoBusquedaComboBox.getSelectedItem().toString();
        List<Factura> listaFacturasFiltradas = null;
        String valor = inputTextField.getText();
        if (campoActual.equals(tiposCampo[0])) {
            campoFiltro = "num_factura";
            listaFacturasFiltradas = JSONUtils.getFacturasByFilter(valor, campoFiltro);
        } else if (campoActual.equals(tiposCampo[1])) {
            campoFiltro = "fecha_emision";
            listaFacturasFiltradas = JSONUtils.getFacturasByFilter(valor, campoFiltro);
        } else if (campoActual.equals(tiposCampo[2])) {
            campoFiltro = "fecha_vencimiento";
            listaFacturasFiltradas = JSONUtils.getFacturasByFilter(valor, campoFiltro);
        } else if (campoActual.equals(tiposCampo[3])) {
            campoFiltro = "id";
            listaFacturasFiltradas = JSONUtils.getFacturasByClienteFilter(valor, campoFiltro);
        } else if (campoActual.equals(tiposCampo[4])) {
            campoFiltro = "nombre";
            listaFacturasFiltradas = JSONUtils.getFacturasByClienteFilter(valor, campoFiltro);
        } else if (campoActual.equals(tiposCampo[5])) {
            campoFiltro = "importe_total";
            listaFacturasFiltradas = JSONUtils.getFacturasByFilter(valor, campoFiltro);
        }
        deleteAllFacturasRows();
        if (listaFacturasFiltradas != null) {
            addFacturasRows(listaFacturasFiltradas);
        }
    }
    
    public void deleteAllFacturasRows() {
        if (resultadosTable != null) {
            int rowsAlready = ((DefaultTableModel) resultadosTable.getModel()).getRowCount();
            for (int i = 0; i < rowsAlready; i++) {
                ((DefaultTableModel) resultadosTable.getModel()).removeRow(0);
            }
        }
    }
    
    public void addFacturasRows(List<Factura> listaFacturasFiltradas) {
        for (int i = 0; i < listaFacturasFiltradas.size(); i++) {
            Factura factura = listaFacturasFiltradas.get(i);
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
        Factura factura = JSONUtils.findFacturaByNumFactura(invoiceLookupView.resultadosTable.getModel().getValueAt(row, 0).toString());
//        Factura factura = dbUtils.getFacturaByNif(invoiceLookupView.resultadosTable.getModel().getValueAt(row, 0).toString());
        PDFGenerator.openPDF(factura.getRelativePathFactura());
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
