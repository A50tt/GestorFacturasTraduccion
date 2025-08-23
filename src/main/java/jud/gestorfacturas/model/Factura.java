package jud.gestorfacturas.model;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import jud.gestorfacturas.manager.PDFGenerator;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.FormatUtils;


public class Factura implements Serializable {

    private String numFactura;
    private Date fechaEmision;
    private int diasPago;
    private Date fechaVencimiento;
    private String formaPago;
    private Cliente cliente;
    private Emisor emisor;
    private Servicio[] servicios;
    private double baseImponible = 0;
    private final double TASA_IVA = 21.0d;
    private double iva;
    private final double TASA_IRPF = 7.0d;
    private double irpf;
    private double importeTotal;
    private Timestamp fechaUltActualizacion;
    private File pathFactura;
    
    @Override
    public String toString() {
        final String CONST_COMMA = ", ";
        return "[" + numFactura + CONST_COMMA + fechaEmision + CONST_COMMA + diasPago
                + CONST_COMMA + fechaVencimiento + CONST_COMMA + formaPago + CONST_COMMA
                + cliente + CONST_COMMA + emisor + CONST_COMMA + servicios + CONST_COMMA
                + baseImponible + CONST_COMMA + iva + CONST_COMMA + irpf + CONST_COMMA
                + importeTotal + CONST_COMMA + fechaUltActualizacion + "]";
    }
    
    public String toBeautifulString() {
        DecimalFormat dc = new DecimalFormat("0.00");
        StringBuilder str = new StringBuilder();
        str.append("======== " + this.getNumFactura() + " ========");
        str.append("\nFecha Emisión: " + this.getFechaEmision());
        str.append("\nFecha Vencimiento: " + this.getFechaVencimiento());
        str.append("\nDías Pago: " + this.getFormaPago());
        str.append("\n\nCLIENTE: " + this.cliente.getNombre());
        str.append("\nNif: " + this.cliente.getNif());
        str.append("\nDirección: " + this.cliente.getDireccion());
        str.append("\nCódigo Postal: " + this.cliente.getCodigoPostal());
        str.append("\n\nEMISOR: " + this.emisor.getNombre());
        str.append("\nNIF: " + this.emisor.getNif());
        str.append("\nDirección: " + this.emisor.getDireccion());
        str.append("\nCódigo Postal: " + this.emisor.getCodigoPostal());
        str.append("\n\nSERVICIOS:");
        for (Servicio servicio: this.servicios) {
            str.append("\n" + new DecimalFormat("0").format(servicio.getCantidad()) + "x " + servicio.getDescripcion() + " a " + dc.format(servicio.getPrecioUnitario()) + " € = " + dc.format(servicio.getPrecioFinal()) + " €");
        }
        
        str.append("\n\n\tBASE IMPONIBLE = " + dc.format(this.getBaseImponible()) + " €");
        str.append("\n\tIVA = " + dc.format(this.getIva()) + " €");
        str.append("\n\tIMPORTE FINAL = " + dc.format(this.getImporteTotal()) + " €");
        str.append("\n");
        return str.toString();
    }

    public Factura() {
        
    }

    public Factura(String _numFactura, Date _fechaEmision, int _diasPago, String _formaPago, Cliente _cliente, Emisor _emisor, Servicio[] _servicios) {
        this.numFactura = _numFactura;
        this.fechaEmision = _fechaEmision;
        this.diasPago = _diasPago;
        this.fechaVencimiento = Date.valueOf(this.fechaEmision.toLocalDate().plusDays(this.diasPago));
        this.formaPago = _formaPago;
        this.cliente = _cliente;
        this.emisor = _emisor;
        this.servicios = _servicios;
        calculaServicios(); //Calcula baseImponible a partir de servicios
        this.iva = FormatUtils.formatDecimalNumberToDoubleIfNecessary(baseImponible * (TASA_IVA / 100d), 2);
        this.irpf = FormatUtils.formatDecimalNumberToDoubleIfNecessary(-baseImponible * (TASA_IRPF / 100d), 2);
        this.importeTotal = baseImponible + iva + irpf;
        this.fechaUltActualizacion = new Timestamp(System.currentTimeMillis());
    }
    
      public Factura(String _numFactura, Date _fechaEmision, int _diasPago, String _formaPago, Cliente _cliente, Emisor _emisor, Servicio[] _servicios, Timestamp _fechaUltActualizacion) {
        this.numFactura = _numFactura;
        this.fechaEmision = _fechaEmision;
        this.diasPago = _diasPago;
        this.fechaVencimiento = Date.valueOf(this.fechaEmision.toLocalDate().plusDays(this.diasPago));
        this.formaPago = _formaPago;
        this.cliente = _cliente;
        this.emisor = _emisor;
        this.servicios = _servicios;
        calculaServicios(); //Calcula baseImponible a partir de servicios
        this.iva = FormatUtils.formatDecimalNumberToDoubleIfNecessary(baseImponible * (TASA_IVA / 100d), 2);
        this.irpf = FormatUtils.formatDecimalNumberToDoubleIfNecessary(-baseImponible * (TASA_IRPF / 100d), 2);
        this.importeTotal = baseImponible + iva + irpf;
        this.fechaUltActualizacion = _fechaUltActualizacion;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public int getDiasPago() {
        return diasPago;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Emisor getEmisor() {
        return emisor;
    }

    public Servicio[] getServicios() {
        return servicios;
    }

    public double getBaseImponible() {
        return baseImponible;
    }

    public double getTasaIva() {
        return TASA_IVA;
    }

    public double getIva() {
        return iva;
    }

    public double getTasaIrpf() {
        return TASA_IRPF;
    }

    public double getIrpf() {
        return irpf;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public Timestamp getFechaUltActualizacion() {
        return fechaUltActualizacion;
    }

    public String getNumFactura() {
        return numFactura;
    }
    
    private void calculaServicios() {
        if (this.servicios != null) {
            for (Servicio servicio : this.servicios) {
                baseImponible += servicio.getPrecioFinal();
            }
        }
    }

    public File getPathFactura() {
        return pathFactura;
    }

    public void setPathFactura(File pdfFactura) {
        this.pathFactura = pdfFactura;
    }
 
    public File getRelativePathFactura() {
        File facturasDir = new File(PDFGenerator.INVOICES_DIRECTORY);
        return Path.of(facturasDir.toString(), this.pathFactura.toString()).toFile();
    }
    
    public void setRelativePathFactura(File pdfFactura) {
        Path facturasDir = Path.of(PDFGenerator.INVOICES_DIRECTORY);
        Path relativePath = facturasDir.relativize(pdfFactura.toPath());
        this.pathFactura = relativePath.toFile();
    }
    
    public static JSONObject buildFacturaJsonObject(Factura factura) {
        // Factura
        JSONObject facturaObj = new JSONObject();
        facturaObj.put("num_factura", factura.getNumFactura());
        facturaObj.put("fecha_emision", factura.getFechaEmision());
        facturaObj.put("dias_pago", factura.getDiasPago());
        facturaObj.put("fecha_vencimiento", factura.getFechaVencimiento());
        facturaObj.put("forma_pago", factura.getFormaPago());
        facturaObj.put("base_imponible", factura.getBaseImponible());
        facturaObj.put("tasa_irpf", factura.getTasaIrpf());
        facturaObj.put("irpf", factura.getIrpf());
        facturaObj.put("tasa_iva", factura.getTasaIva());
        facturaObj.put("iva", factura.getIva());
        facturaObj.put("importe_total", factura.getImporteTotal());
        facturaObj.put("ruta_pdf", factura.getPathFactura());
        facturaObj.put("ult_actualizacion", factura.getFechaUltActualizacion());
        
        JSONArray serviciosObj = Servicio.buildServiciosJsonArrayFromServiciosArray(factura.getServicios());
        facturaObj.put("servicios", serviciosObj);
        // Cliente
        JSONObject clienteObj = Cliente.buildClienteJsonObject(factura.getCliente());
        facturaObj.put("cliente", clienteObj);
        // Emisor
        JSONObject emisorObj = Emisor.buildEmisorJsonObject(factura.getEmisor());
        facturaObj.put("emisor", emisorObj);
        
        return facturaObj;
    }
    
        public static Factura buildFacturaFromJson(JSONObject facturaObj) {
        Factura factura = new Factura(
                facturaObj.getString("num_factura"),
                Date.valueOf(facturaObj.getString("fecha_emision")),
                facturaObj.getInt("dias_pago"),
                facturaObj.getString("forma_pago"),
                Cliente.buildClienteFromJson(facturaObj.getJSONObject("cliente")),
                Emisor.buildEmisorFromJson(facturaObj.getJSONObject("emisor")),
                Servicio.buildServiciosFromJson(facturaObj.getJSONArray("servicios")),
                Timestamp.valueOf(facturaObj.getString("ult_actualizacion"))
        );
        factura.setPathFactura(new File(facturaObj.getString("ruta_pdf")));
        return factura;
    }
}
