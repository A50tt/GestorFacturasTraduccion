
package jud.gestorfacturas.model;

import java.io.Serializable;

public class Servicio implements Serializable {
    
    private String descripcion;
    private double precioUnitario;
    private double cantidad;
    private double precioFinal;
    
    public Servicio(String _descripcion, double _precioUnitario, double _cantidad) {
        this.descripcion = _descripcion;
        this.precioUnitario = _precioUnitario;
        this.cantidad = _cantidad;
        this.precioFinal = this.precioUnitario * this.cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        return new Servicio(newDescripcion, newPrecioUnitario, servicio.getCantidad());
    }
    
    public static String[] getInstanceStringArray(Servicio[] servicios) {
        String[] str = new String[servicios.length];
        for (int i = 0; i < str.length; i++) {
            str[i] = servicios[i].getDescripcion();
        }
        return str;
    }
    
}
