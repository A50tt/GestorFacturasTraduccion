
package jud.gestorfacturas.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Emisor")
public class Emisor extends Interviniente {
    
    private String nombreCompleto;
    private String iban;
    
    public Emisor() {
        super();
    }
    
    public Emisor (String _nombre, String _nombreCompleto, String _direccion, String _codigoPostal, String _nif, String _iban) {
        super(_nombre, _direccion, _codigoPostal, _nif);
        this.nombreCompleto = _nombreCompleto;
        this.iban = _iban;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }    
}
