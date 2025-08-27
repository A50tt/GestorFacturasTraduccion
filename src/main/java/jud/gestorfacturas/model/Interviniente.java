
package jud.gestorfacturas.model;

import java.sql.Timestamp;

public abstract class Interviniente {
    
    private String nombre;
    private String direccion;
    private String codigoPostal;
    private String nif;
    private Timestamp fechaUltActualizacion;
    
    public Interviniente() {
        
    }

    public Interviniente(String _nif, String _nombre, String _direccion, String _codigoPostal) {
        this.nombre = _nombre;
        this.direccion = _direccion;
        this.codigoPostal = _codigoPostal;
        this.nif = _nif;
        this.fechaUltActualizacion = new Timestamp(System.currentTimeMillis());
    }
   
    public Interviniente(String _nif, String _nombre, String _direccion, String _codigoPostal, Timestamp _fechaUltActualizacion) {
        this.nombre = _nombre;
        this.direccion = _direccion;
        this.codigoPostal = _codigoPostal;
        this.nif = _nif;
        this.fechaUltActualizacion = _fechaUltActualizacion;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Timestamp getFechaUltActualizacion() {
        return fechaUltActualizacion;
    }
}
