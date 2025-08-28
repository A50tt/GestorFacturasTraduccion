
package jud.gestorfacturas.interfaces;

import java.util.List;
import org.json.JSONObject;

public interface JsonDataType {
    
    public boolean importado = false;
    
    public static JSONObject buildJsonWithoutTimestamp(JsonDataType rawObject) {
        return null;
    };
    
    public static JSONObject buildJson(JsonDataType obj) {
        return null;
    };
    public static JsonDataType getInstanceFromJson(JSONObject jsonObj) {
        return null;
    };
    
    public static List<JsonDataType> cleanDuplicates(List<JsonDataType> list) {
        return null;
    }
}
