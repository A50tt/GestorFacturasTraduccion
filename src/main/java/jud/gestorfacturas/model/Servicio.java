
package jud.gestorfacturas.model;

import java.io.Serializable;
import jud.gestorfacturas.manager.Utils;

public class Servicio implements Serializable {
    
    private String idiomaOrigen;
    private String idiomaDestino;
    private String descripcion;
    private String tipo;
    private double precioUnitario;
    private double cantidad;
    private double precioFinal;
    
    public Servicio(String _idiomaOrigen, String _idiomaDestino, String _descripcion, String _tipo, double _precioUnitario, double _cantidad) {
        this.idiomaOrigen = _idiomaOrigen;
        this.idiomaDestino = _idiomaDestino;
        this.descripcion = _descripcion;
        this.tipo = _tipo;
        this.precioUnitario = Utils.formatDecimalNumberToDoubleIfNecessary(_precioUnitario, 3);
        this.cantidad = Utils.formatDecimalNumberToDoubleIfNecessary(_cantidad, 2);
        this.precioFinal = Utils.formatDecimalNumberToDoubleIfNecessary(this.precioUnitario * this.cantidad, 2);
    }

    public String getIdiomaOrigen() {
        return idiomaOrigen;
    }

    public String getIdiomaDestino() {
        return idiomaDestino;
    }

    public String getCombinacionIdiomas() {
        return idiomaOrigen + " - " + idiomaDestino;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getTipo() {
        return tipo;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public String getPrecioUnitarioString() {
        return String.valueOf(precioUnitario);
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }
    
    public static Servicio getInstanceServicioDescuento(Servicio servicio, double descuento) {
        String newDescripcion = "Descuento " + servicio.getDescripcion();
        double newPrecioUnitario = servicio.getPrecioUnitario() * (1d - (descuento / 100d));     
        return new Servicio(servicio.getIdiomaOrigen(), servicio.getIdiomaDestino(), newDescripcion, servicio.getTipo(), newPrecioUnitario, servicio.getCantidad());
    }
    
    public static String[] getInstanceStringArray(Servicio[] servicios) {
        String[] str = new String[servicios.length];
        for (int i = 0; i < str.length; i++) {
            str[i] = servicios[i].getDescripcion();
        }
        return str;
    }
    
}
