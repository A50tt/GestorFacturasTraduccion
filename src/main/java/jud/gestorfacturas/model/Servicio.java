
package jud.gestorfacturas.model;

import java.io.Serializable;

public class Servicio implements Serializable {
    
    private String combinacionIdiomas;
    private String descripcion;
    private String tipo;
    private double precioUnitario;
    private double cantidad;
    private double precioFinal;
    
    public Servicio(String _combinacionIdiomas, String _descripcion, String _tipo, double _precioUnitario, double _cantidad) {
        this.combinacionIdiomas = _combinacionIdiomas;
        this.descripcion = _descripcion;
        this.tipo = _tipo;
        this.precioUnitario = _precioUnitario;
        this.cantidad = _cantidad;
        this.precioFinal = this.precioUnitario * this.cantidad;
    }
    
    public String getCombinacionIdiomas() {
        return combinacionIdiomas;
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
        return new Servicio(servicio.getCombinacionIdiomas(), newDescripcion, servicio.getTipo(), newPrecioUnitario, servicio.getCantidad());
    }
    
    public static String[] getInstanceStringArray(Servicio[] servicios) {
        String[] str = new String[servicios.length];
        for (int i = 0; i < str.length; i++) {
            str[i] = servicios[i].getDescripcion();
        }
        return str;
    }
    
}
