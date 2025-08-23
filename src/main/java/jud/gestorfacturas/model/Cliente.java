
package jud.gestorfacturas.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.json.JSONObject;

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
    
    public Cliente(int _id, String _nif, String _nombre, String _direccion, String _codigoPostal) {
        super(_nif, _nombre, _direccion, _codigoPostal);
        this.id = _id;
    }

    public Cliente (String _nif, String _nombre, String _direccion, String _codigoPostal) {
        super(_nif, _nombre, _direccion, _codigoPostal);
    }
    
     public Cliente(String _nif, String _nombre, String _direccion, String _codigoPostal, Timestamp _fechaUltActualizacion) {
        super(_nif, _nombre, _direccion, _codigoPostal, _fechaUltActualizacion);
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

    public static JSONObject buildClienteJsonObject(Cliente cliente) {
        JSONObject clienteObj = buildClienteJsonObjectWithoutTimestamp(cliente);
        clienteObj.put("ult_actualizacion", cliente.getFechaUltActualizacion());
        return clienteObj;
    }

    public static JSONObject buildClienteJsonObjectWithoutTimestamp(Cliente cliente) {
        JSONObject clienteObj = new JSONObject();
        clienteObj.put("id", cliente.getId());
        clienteObj.put("nif", cliente.getNif());
        clienteObj.put("nombre", cliente.getNombre());
        clienteObj.put("direccion", cliente.getDireccion());
        clienteObj.put("codigo_postal", cliente.getCodigoPostal());
        clienteObj.put("activado", cliente.isActivado());
        return clienteObj;
    }

    public static Cliente buildClienteFromJson(JSONObject clienteObj) {
        Cliente cliente = new Cliente(
                clienteObj.getString("nif"),
                clienteObj.getString("nombre"),
                clienteObj.getString("direccion"),
                clienteObj.getString("codigo_postal"),
                Timestamp.valueOf(clienteObj.getString("ult_actualizacion")));
        cliente.setId(clienteObj.getInt("id"));
        cliente.setActivado(clienteObj.getBoolean("activado"));
        return cliente;
    }
}
