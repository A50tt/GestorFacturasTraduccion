
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
    
    public static Emisor getDummyInstance() {
        return new Emisor("dummy", null, null, null, null, null);
    }
    
    public Emisor (String _nif, String _nombre, String _nombreCompleto, String _direccion, String _codigoPostal, String _iban) {
        super(_nif, _nombre, _direccion, _codigoPostal);
        this.nombreCompleto = _nombreCompleto;
        this.iban = _iban;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
   
    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
