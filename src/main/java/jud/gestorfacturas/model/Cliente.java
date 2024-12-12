
package jud.gestorfacturas.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Clientes")
public class Cliente extends Interviniente {
    
    public Cliente() {
        super();
    }
    
    public Cliente (String _nombre, String _direccion, String _codigoPostal, String _nif) {
        super(_nombre, _direccion, _codigoPostal, _nif);
    }
    
}
