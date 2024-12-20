
package jud.gestorfacturas.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Interviniente {
    
    private String nombre;
    private String direccion;
    private String codigoPostal;
    @Id
    private String nif;
    
    public Interviniente() {
        
    }
    
    public Interviniente(String _nombre, String _direccion, String _codigoPostal, String _nif) {
        this.nombre = _nombre;
        this.direccion = _direccion;
        this.codigoPostal = _codigoPostal;
        this.nif = _nif;
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
}
