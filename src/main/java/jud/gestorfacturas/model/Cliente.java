
package jud.gestorfacturas.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Cliente")
public class Cliente extends Interviniente {
    
    private boolean activado = true;
    
    public Cliente() {
        super();
    }
    
    public Cliente (String _nombre, String _direccion, String _codigoPostal, String _nif) {
        super(_nombre, _direccion, _codigoPostal, _nif);
    }
    
    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }
}
