
package jud.gestorfacturas.model;

import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.FormatUtils;

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
        this.precioUnitario = FormatUtils.formatDecimalNumberToDoubleIfNecessary(_precioUnitario, 3);
        this.cantidad = FormatUtils.formatDecimalNumberToDoubleIfNecessary(_cantidad, 2);
        this.precioFinal = FormatUtils.formatDecimalNumberToDoubleIfNecessary(this.precioUnitario * this.cantidad, 2);
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
    
    public static Servicio[] buildServiciosFromJson(JSONArray serviciosObj) {
        Servicio[] servicios = new Servicio[serviciosObj.length()];
        for (int i = 0; i < serviciosObj.length(); i++) {
            JSONObject servicioObj = serviciosObj.getJSONObject(i);
            String idiomaOrigen = servicioObj.getString("idioma_origen");
            String idiomaDestino = servicioObj.getString("idioma_destino");
            String descripcion = servicioObj.getString("descripcion");
            String tipo = servicioObj.getString("tipo");
            Double precioUnitario = servicioObj.getDouble("precio_unitario");
            Double cantidad = servicioObj.getDouble("cantidad");
            Servicio servicio = new Servicio(idiomaOrigen, idiomaDestino, descripcion, tipo, precioUnitario, cantidad);
            servicios[i] = servicio;
        }
        return servicios;
    }

    public static JSONArray buildServiciosJsonArrayFromServiciosArray(Servicio[] servicios) {
        JSONArray serviciosArr = new JSONArray();
        for (Servicio servicio : servicios) {
            JSONObject servicioObj = new JSONObject();
            servicioObj.put("idioma_origen", servicio.getIdiomaOrigen());
            servicioObj.put("idioma_destino", servicio.getIdiomaDestino());
            servicioObj.put("descripcion", servicio.getDescripcion());
            servicioObj.put("tipo", servicio.getTipo());
            servicioObj.put("precio_unitario", servicio.getPrecioUnitario());
            servicioObj.put("cantidad", servicio.getCantidad());
            servicioObj.put("precio_final", servicio.getPrecioFinal());
            serviciosArr.put(servicioObj);
        }
        return serviciosArr;
    }

}
