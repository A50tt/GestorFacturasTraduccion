
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
    
    public static Cliente getDummyInstance() {
        return new Cliente("dummy", null, null, null);
    }
    
    public Cliente (String _nif, String _nombre, String _direccion, String _codigoPostal) {
        super(_nif, _nombre, _direccion, _codigoPostal);
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }
}
