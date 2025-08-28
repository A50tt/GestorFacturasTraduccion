
package jud.gestorfacturas.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import jud.gestorfacturas.interfaces.JsonDataType;

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
            "nombre": "Nombre 2",
            "importado": false
        }
    ]
}
*/

public class Cliente extends Interviniente implements JsonDataType {
    
    private int id;
    private boolean activado = true;
    
    public Cliente() {
        super();
    }
    
    public Cliente(int _id, String _nif, String _nombre, String _direccion, String _codigoPostal, boolean _importado) {
        super(_nif, _nombre, _direccion, _codigoPostal, _importado);
        this.id = _id;
    }

    public Cliente (String _nif, String _nombre, String _direccion, String _codigoPostal, boolean _importado) {
        super(_nif, _nombre, _direccion, _codigoPostal, _importado);
    }
    
     public Cliente(String _nif, String _nombre, String _direccion, String _codigoPostal, boolean _importado, Timestamp _fechaUltActualizacion) {
        super(_nif, _nombre, _direccion, _codigoPostal, _importado, _fechaUltActualizacion);
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

    public static JSONObject buildJson(Cliente cliente) {
        JSONObject clienteObj = buildJsonWithoutTimestamp(cliente);
        clienteObj.put("ult_actualizacion", cliente.getFechaUltActualizacion());
        return clienteObj;
    }

    public static JSONObject buildJsonWithoutTimestamp(Cliente cliente) {
        JSONObject clienteObj = new JSONObject();
        clienteObj.put("id", cliente.getId());
        clienteObj.put("nif", cliente.getNif());
        clienteObj.put("nombre", cliente.getNombre());
        clienteObj.put("direccion", cliente.getDireccion());
        clienteObj.put("codigo_postal", cliente.getCodigoPostal());
        clienteObj.put("activado", cliente.isActivado());
        clienteObj.put("importado", cliente.isImportado());
        return clienteObj;
    }

    public static Cliente getInstanceFromJson(JSONObject clienteObj) {
        Cliente cliente = new Cliente(
                clienteObj.getString("nif"),
                clienteObj.getString("nombre"),
                clienteObj.getString("direccion"),
                clienteObj.getString("codigo_postal"),
                clienteObj.getBoolean("importado"),
                Timestamp.valueOf(clienteObj.getString("ult_actualizacion")));
        cliente.setId(clienteObj.getInt("id"));
        cliente.setActivado(clienteObj.getBoolean("activado"));
        return cliente;
    }
    
    public static List<Cliente> cleanDuplicates(List<Cliente> clientes) {
        List<Integer> idsFound = new ArrayList<>();
        List<Cliente> clientesToRemove = new ArrayList<>();
        for (Cliente cliente : clientes) {
            if (idsFound.contains(cliente.getId())) {
                clientesToRemove.add(cliente);
            } else {
                idsFound.add(cliente.getId());
            }
        }
        
        for (Cliente clienteToRemove : clientesToRemove) {
            clientes.remove(clienteToRemove);
        }
        return clientes;
    }
}
