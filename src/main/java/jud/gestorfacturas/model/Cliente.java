
package jud.gestorfacturas.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Table;

/* JSON
{
    "clientes": [
        {
            "codigo_postal": "00002",
            "ult_actualizacion": "2025-08-22 23:22:21.668",
            "activado": true,
            "direccion": "Dirección 2",
            "nif": "X87654321",
            "id": 1,
            "nombre": "Nombre 2"
        }
    ]
}
*/

public class Cliente extends Interviniente {
    
    private int id;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
