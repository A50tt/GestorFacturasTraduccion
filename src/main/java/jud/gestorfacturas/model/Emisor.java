
package jud.gestorfacturas.model;

import java.sql.Timestamp;
import org.json.JSONObject;
import jud.gestorfacturas.interfaces.JsonDataType;

/* JSON:
{
    "nombre_completo": "Mi nombre completo",
    "codigo_postal": "00000",
    "ult_actualizacion": "2025-08-22 23:39:02.619",
    "iban": "ES42",
    "direccion": "Mi dirección",
    "nif": "A123",
    "nombre": "Mi nombre"
}
*/

public class Emisor extends Interviniente implements JsonDataType {
    
    private String nombreCompleto;
    private String iban;
    
    public Emisor() {
        super();
    }
    
    public Emisor (String _nif, String _nombre, String _nombreCompleto, String _direccion, String _codigoPostal, String _iban) {
        super(_nif, _nombre, _direccion, _codigoPostal);
        this.nombreCompleto = _nombreCompleto;
        this.iban = _iban;
    }
    
        public Emisor (String _nif, String _nombre, String _nombreCompleto, String _direccion, String _codigoPostal, String _iban, Timestamp _fechaUltActualizacion) {
        super(_nif, _nombre, _direccion, _codigoPostal, _fechaUltActualizacion);
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
    
    public static JSONObject buildJson(Emisor emisor) {
        JSONObject emisorObj = buildJsonWithoutTimestamp(emisor);
        emisorObj.put("ult_actualizacion", emisor.getFechaUltActualizacion());
        return emisorObj;
    }
    
    public static JSONObject buildJsonWithoutTimestamp(Emisor emisor) {
        JSONObject emisorObj = new JSONObject();
        emisorObj.put("nif", emisor.getNif());
        emisorObj.put("nombre", emisor.getNombre());
        emisorObj.put("nombre_completo", emisor.getNombreCompleto());
        emisorObj.put("direccion", emisor.getDireccion());
        emisorObj.put("codigo_postal", emisor.getCodigoPostal());
        emisorObj.put("iban", emisor.getIban());
        return emisorObj;
    }
    
    public static Emisor getInstanceFromJson(JSONObject emisorObj) {
        return new Emisor(
                emisorObj.getString("nif"),
                emisorObj.getString("nombre"),
                emisorObj.getString("nombre_completo"),
                emisorObj.optString("direccion"),
                emisorObj.optString("codigo_postal"),
                emisorObj.optString("iban"),
                Timestamp.valueOf(emisorObj.getString("ult_actualizacion"))
        );
    }
}
