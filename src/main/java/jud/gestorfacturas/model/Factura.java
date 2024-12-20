package jud.gestorfacturas.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import javax.persistence.Entity;
import javax.persistence.Id;
import jud.gestorfacturas.manager.Utils;

@Entity
public class Factura {

    @Id
    private String numFactura;
    private Date fechaEmision;
    private int diasPago;
    private Date fechaVencimiento;
    private String formaPago;
    private Cliente cliente;
    private Emisor emisor;
    private Servicio[] listaServicios;
    private double baseImponible = 0;
    private final double TASA_IVA = 21.0d;
    private double iva;
    private final double TASA_IRPF = 7.0d;
    private double irpf;
    private double importeTotal;
    private Timestamp fechaUltActualizacion;
    
    @Override
    public String toString() {
        final String CONST_COMMA = ", ";
        return "[" + numFactura + CONST_COMMA + fechaEmision + CONST_COMMA + diasPago
                + CONST_COMMA + fechaVencimiento + CONST_COMMA + formaPago + CONST_COMMA
                + cliente + CONST_COMMA + emisor + CONST_COMMA + listaServicios + CONST_COMMA
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
        for (Servicio servicio: this.listaServicios) {
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

    public Factura(String _numFactura, Date _fechaEmision, int _diasPago, String _formaPago, Cliente _cliente, Emisor _emisor, Servicio[] _listaServicios) {
        this.numFactura = _numFactura;
        this.fechaEmision = _fechaEmision;
        this.diasPago = _diasPago;
        this.fechaVencimiento = Date.valueOf(this.fechaEmision.toLocalDate().plusDays(this.diasPago));
        this.formaPago = _formaPago;
        this.cliente = _cliente;
        this.emisor = _emisor;
        this.listaServicios = _listaServicios;
        calculaServicios(); //Calcula baseImponible a partir de listaServicios
        this.iva = Utils.formatDecimalNumberToDoubleIfNecessary(baseImponible * (TASA_IVA / 100d), 2);
        this.irpf = Utils.formatDecimalNumberToDoubleIfNecessary(-baseImponible * (TASA_IRPF / 100d), 2);
        this.importeTotal = baseImponible + iva + irpf;
        this.fechaUltActualizacion = new Timestamp(System.currentTimeMillis());
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

    public Servicio[] getListaServicios() {
        return listaServicios;
    }

    public double getBaseImponible() {
        return baseImponible;
    }

    public double getTASA_IVA() {
        return TASA_IVA;
    }

    public double getIva() {
        return iva;
    }

    public double getTASA_IRPF() {
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
        for (Servicio servicio : this.listaServicios) {
            baseImponible += servicio.getPrecioFinal();
        }
    }

}
