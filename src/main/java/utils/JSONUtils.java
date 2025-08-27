package utils;

import java.io.File;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import jud.gestorfacturas.model.Cliente;
import jud.gestorfacturas.model.Emisor;
import jud.gestorfacturas.model.Factura;
import org.json.JSONArray;
import org.json.JSONException;

public class JSONUtils {

    public static final String WORKING_DIR = System.getProperty("user.dir");
    public static final String JSON_FILEPATH = Paths.get(WORKING_DIR, "src", "main", "resources", "META-INF").toString();
    public static final String[] jsonObjNames = {"emisor", "clientes", "facturas"};
    public static final String tagClienteEnJsonFacturas = "cliente";
    public static final String[] dbFileNames = {jsonObjNames[0] + ".json", jsonObjNames[1] + ".json", jsonObjNames[2] + ".json"};

    public static int checkJsonFilesIntegrityOrResetThem(List<String> jsonsToCheck) {
        if (jsonsToCheck.contains(dbFileNames[0]) && findEmisorGuardado() == null) {
            int respuesta = FrameUtils.showErrorQuestionBoxSiNo("Base de datos dañada", "La base de datos está dañada y no se puede leer. ¿Repararla? (Esto eliminará todos los datos de 'Emisor').");
            if (respuesta == JOptionPane.OK_OPTION) {
                try (FileWriter file = new FileWriter(Paths.get(JSON_FILEPATH, dbFileNames[0]).toFile())) {
                    String jsonText = "{}";
                    file.write(jsonText);
                } catch (IOException ex) {
                    ex.getStackTrace();
                }
            } else {
                return -1;
            }
            
        }
        if (jsonsToCheck.contains(dbFileNames[1]) && readJsonClientes() == null) {
            int respuesta = FrameUtils.showErrorQuestionBoxSiNo("Base de datos dañada", "La base de datos está dañada y no se puede leer. ¿Repararla? (Esto eliminará todos los datos de 'Clientes').");
            if (respuesta == JOptionPane.OK_OPTION) {
                try (FileWriter file = new FileWriter(Paths.get(JSON_FILEPATH, dbFileNames[1]).toFile())) {
                    String jsonText = "{\"" + jsonObjNames[1] + "\":[]}";
                    file.write(jsonText);
                } catch (IOException ex) {
                    ex.getStackTrace();
                }
            } else {
                return -1;
            }
        }
        if (jsonsToCheck.contains(dbFileNames[2]) && readJsonFacturas() == null) {
            int respuesta = FrameUtils.showErrorQuestionBoxSiNo("Base de datos dañada", "La base de datos está dañada y no se puede leer. ¿Repararla? (Esto eliminará todos los datos de 'Facturas')");
            if (respuesta == JOptionPane.OK_OPTION) {
                try (FileWriter file = new FileWriter(Paths.get(JSON_FILEPATH, dbFileNames[2]).toFile())) {
                    String jsonText = "{\"" + jsonObjNames[2] + "\":[]}";
                    file.write(jsonText);
                } catch (IOException ex) {
                    ex.getStackTrace();
                }
            } else {
                return -1;
            }
        }
        return 1;
    }
    
    public static JSONObject readJsonFile(File file) {
        Path jsonFile = file.toPath();
        try {
            // 1. Leer el archivo existente
            String content = new String(Files.readAllBytes(jsonFile));
            return new JSONObject(content);
        } catch (IOException ex) {
            FrameUtils.showErrorMessage("Error", ex.getMessage());
            return null;
        }
    }

    // EMISOR
    public static void saveEmisor(Emisor emisor, boolean showResultado) {
        // 1. Leer el root
        Path jsonFile = Path.of(JSON_FILEPATH, dbFileNames[0]);
        try {
            // 2. Crear el nuevo emisor
            JSONObject emisorObj = Emisor.buildJson(emisor);

            // 3. Guardar el archivo (sobreescribe, pero con el array ampliado)
            try (FileWriter file = new FileWriter(jsonFile.toFile())) {
                file.write(emisorObj.toString(4));
                if (showResultado) {
                    FrameUtils.showPlainMessage("Éxito", "El emisor se ha guardado correctamente.");
                }
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }

    public static Emisor findEmisorGuardado() {
        try {
            String content = new String(Files.readAllBytes(Path.of(JSON_FILEPATH, dbFileNames[0])));
            JSONObject emisorObj = new JSONObject(content);
            return Emisor.getInstanceFromJson(emisorObj);
        } catch (IOException | JSONException ex) {
            return null;
        }
    }

    // CLIENTES
    public static JSONObject readJsonClientes() {
        Path jsonFile = Path.of(JSON_FILEPATH, dbFileNames[1]);
        try {
            // 1. Leer el archivo existente
            String content = new String(Files.readAllBytes(jsonFile));
            if (content.contains(jsonObjNames[1])) {
                return new JSONObject(content);
            } else {
                return null;
            }
        } catch (IOException ex) {
            FrameUtils.showErrorMessage("Error", ex.getMessage());
            return null;
        } catch (JSONException ex) {
            ex.getStackTrace();
            return null;
        }
    }

    public static void saveAllClientes(List<Cliente> clientes, boolean showResultado) {
        try {
            // 1. Leer el root
            Path jsonFile = Path.of(JSON_FILEPATH, dbFileNames[1]);

            JSONObject root = readJsonClientes();
            // 2. Obtener el array de facturas
            JSONArray clientesObj = root.getJSONArray(jsonObjNames[1]);

            for (Cliente cliente : clientes) {
                // 3. Crear el nuevo cliente y sus objetos
                JSONObject clienteObj = Cliente.buildJson(cliente);

                // 4. Agregar el nuevo cliente al array
                clientesObj.put(clienteObj);
            }
            // 5. Guardar el archivo (sobreescribe, pero con el array ampliado)
            try (FileWriter file = new FileWriter(jsonFile.toFile())) {
                file.write(root.toString(4));
                if (showResultado) {
                    FrameUtils.showPlainMessage("Éxito", "Clientes guardadas correctamente.");
                }
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void replaceAllClientes(List<Cliente> clientes, boolean showResultado) {
        try {
            // 1. Leer el root
            Path jsonFile = Path.of(JSON_FILEPATH, dbFileNames[1]);

            JSONObject root = new JSONObject();
            // 2. Obtener el array de facturas
            JSONArray clientesArray = new JSONArray();
            for (Cliente cliente : clientes) {
                // 3. Crear cada cliente y sus objetos
                JSONObject clienteObj = Cliente.buildJson(cliente);

                // 4. Agregar el nuevo cliente al array
                clientesArray.put(clienteObj);
            }
            root.put(jsonObjNames[1], clientesArray);
            // 5. Guardar el archivo (sobreescribe, pero con el array ampliado)
            try (FileWriter file = new FileWriter(jsonFile.toFile())) {
                file.write(root.toString(4));
                if (showResultado) {
                    FrameUtils.showPlainMessage("Éxito", "Clientes guardadas correctamente.");
                }
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }

    public static void replaceCliente(int index, Cliente cliente) {
        try {
            // 1. Leer el root
            Path jsonFile = Path.of(JSON_FILEPATH, dbFileNames[1]);

            JSONObject root = readJsonClientes();
            // 2. Obtener el array de facturas
            JSONArray clientesObj = root.getJSONArray(jsonObjNames[1]);
            // 3. Crear el nuevo cliente y sus objetos
            JSONObject clienteObj = Cliente.buildJson(cliente);

            // 4. Agregar el nuevo cliente al array
            clientesObj.put(index, clienteObj);

            // 5. Guardar el archivo (sobreescribe, pero con el array ampliado)
            try (FileWriter file = new FileWriter(jsonFile.toFile())) {
                file.write(root.toString(4));
                FrameUtils.showPlainMessage("Éxito", "El cliente se ha actualizado correctamente.");
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveCliente(Cliente cliente) {
        // 1. Leer el root
        Path jsonFile = Path.of(JSON_FILEPATH, dbFileNames[1]);
        try {
            JSONObject root = readJsonClientes();
            // 2. Obtener el array de facturas
            JSONArray clientes = root.getJSONArray(jsonObjNames[1]);

            // 3. Crear el nuevo cliente y sus objetos
            JSONObject clienteObj = Cliente.buildJson(cliente);

            // 4. Agregar el nuevo cliente al array
            clientes.put(clienteObj);

            // 5. Guardar el archivo (sobreescribe, pero con el array ampliado)
            try (FileWriter file = new FileWriter(jsonFile.toFile())) {
                file.write(root.toString(4));
                FrameUtils.showPlainMessage("Éxito", "El cliente se ha guardado correctamente.");
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }

    public static void actualizaCliente(Cliente cliente) {
        List<Cliente> listClientes = getAllClientes();
        int oldClientIndex = 0;
        for (Cliente listClient : listClientes) {
            if (cliente.getId() == listClient.getId()) {
                listClientes.set(oldClientIndex, cliente);
                break;
            } else {
                oldClientIndex++;
            }
        }
        replaceCliente(oldClientIndex, cliente);
    }

    public static List<Cliente> getAllClientes() {
        JSONObject root = readJsonClientes();
        JSONArray clientesObj = root.getJSONArray(jsonObjNames[1]);
        List<Cliente> listaClientes = new ArrayList<>();
        for (int i = 0; i < clientesObj.length(); i++) {
            JSONObject loadedItem = clientesObj.getJSONObject(i);
            listaClientes.add(Cliente.getInstanceFromJson(loadedItem));
        }
        return listaClientes;
    }

    public static Cliente getClienteById(int id) {
        List<Cliente> allClientesList = getAllClientes();
        for (Cliente cliente : allClientesList) {
            if (id == cliente.getId()) {
                return cliente;
            }
        }
        return null;
    }

    public static List<Cliente> getClientesByFilter(String value, String jsonProperty) {
        List<Cliente> listaClientesFiltrados = new ArrayList();
        JSONObject root = readJsonClientes();
        JSONArray clientesObj = root.getJSONArray(jsonObjNames[1]);
        for (int i = 0; i < clientesObj.length(); i++) {
            JSONObject loadedItem = clientesObj.getJSONObject(i);
            String jsonValue = loadedItem.getString(jsonProperty);
            if (value.contains("*")) {
                String[] splitStr = value.split("[*]");
                int coincidences = 0;
                for (String str : splitStr) {
                    if (value.contains(str)) {
                        coincidences++;
                    };
                }
                if (coincidences == splitStr.length) {
                    listaClientesFiltrados.add(Cliente.getInstanceFromJson(loadedItem));
                }
            } else {
                if (value == jsonValue) {
                    listaClientesFiltrados.add(Cliente.getInstanceFromJson(loadedItem));
                }
            }
        }
        return listaClientesFiltrados.isEmpty() ? null : listaClientesFiltrados;
    }

    public static boolean nifClienteYaExiste(String nif) {
        JSONObject root = readJsonClientes();
        JSONArray JSONArray = root.getJSONArray(jsonObjNames[1]);
        for (int i = 0; i < JSONArray.length(); i++) {
            JSONObject loadedItem = JSONArray.getJSONObject(i);
            String objNif = loadedItem.getString("nif");
            if (nif.equals(objNif)) {
                return true;
            }
        }
        return false;
    }

    public static boolean idClienteYaExiste(int id) {
        JSONObject root = readJsonClientes();
        JSONArray JSONArray = root.getJSONArray(jsonObjNames[1]);
        for (int i = 0; i < JSONArray.length(); i++) {
            JSONObject loadedItem = JSONArray.getJSONObject(i);
            int objId = loadedItem.getInt("id");
            if (id == objId) {
                return true;
            }
        }
        return false;
    }

    public static int getSiguienteIdClienteDisponible() {
        int propId = 1;
        while (true) {
            if (!idClienteYaExiste(propId)) {
                return propId;
            }
            propId++;
        }
    }
    
    public static List<Factura> getFacturasByClienteFilter(String value, String jsonProperty) {
        List<Factura> listaFacturasFiltradas = new ArrayList();
        JSONObject root = readJsonFacturas();
        JSONArray facturasObj = root.getJSONArray(jsonObjNames[2]);

        for (int i = 0; i < facturasObj.length(); i++) {
            JSONObject factura = facturasObj.getJSONObject(i);
            JSONObject clientesObj = factura.getJSONObject(tagClienteEnJsonFacturas);
            String jsonValue;
            switch (jsonProperty) {
                case "id":
                    jsonValue = Integer.toString(clientesObj.getInt(jsonProperty));
                    break;
                case "nombre":
                    jsonValue = clientesObj.getString(jsonProperty);
                    break;
                default:
                    jsonValue = null;
                    break;
            }
            if (value.contains("*")) {
                String regex = value.replace(".", "\\.").replace("*", ".*");
                if (Pattern.matches(regex, jsonValue)) {
                    listaFacturasFiltradas.add(Factura.getInstanceFromJson(factura));
                }
            } else {
                if (value.equals(jsonValue)) {
                    listaFacturasFiltradas.add(Factura.getInstanceFromJson(factura));
                }
            }
        }
        return listaFacturasFiltradas.isEmpty() ? null : listaFacturasFiltradas;
    }

    // FACTURAS
    public static JSONObject readJsonFacturas() {
        try {
            Path jsonFile = Path.of(JSON_FILEPATH, dbFileNames[2]);
            // 1. Leer el archivo existente
            String content = new String(Files.readAllBytes(jsonFile));
            if (content.contains(jsonObjNames[2])) {
                return new JSONObject(content);
            } else {
                return null;
            }
        } catch (IOException ex) {
            ex.getStackTrace();
            return null;
        }
    }

    public static void saveFactura(Factura factura) {
        if (isNumFacturaExistente(factura.getNumFactura())) {
            FrameUtils.showErrorMessage("Error", "Ya existe una factura con este n.º de factura. No se permiten duplicados.");
            return;
        }
        // 1. Leer el root
        Path jsonFile = Path.of(JSON_FILEPATH, dbFileNames[2]);
        try {
            JSONObject root = readJsonFacturas();
            // 2. Obtener el array de facturas
            JSONArray facturas = root.getJSONArray(jsonObjNames[2]);

            // 3. Crear la nueva factura y sus objetos
            JSONObject facturaObj = Factura.buildJson(factura);

            // 4. Agregar la nueva factura al array
            facturas.put(facturaObj);

            // 5. Guardar el archivo (sobreescribe, pero con el array ampliado)
            try (FileWriter file = new FileWriter(jsonFile.toFile())) {
                file.write(root.toString(4));
                FrameUtils.showPlainMessage("Éxito", "La factura se ha guardado correctamente.");
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveAllFacturas(List<Factura> facturas, boolean showResultado) {
        // 1. Leer el root
        Path jsonFile = Path.of(JSON_FILEPATH, dbFileNames[2]);
        try {
            JSONObject root = readJsonFacturas();
            // 2. Obtener el array de facturas
            JSONArray facturasObj = root.getJSONArray(jsonObjNames[2]);

            for (Factura factura : facturas) {
                // 3. Crear la nueva factura y sus objetos
                JSONObject facturaObj = Factura.buildJson(factura);

                // 4. Agregar la nueva factura al array
                facturasObj.put(facturaObj);
            }

            // 5. Guardar el archivo (sobreescribe, pero con el array ampliado)
            try (FileWriter file = new FileWriter(jsonFile.toFile())) {
                file.write(root.toString(4));
                if (showResultado) {
                    FrameUtils.showPlainMessage("Éxito", "Facturas guardadas correctamente.");
                }
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void replaceAllFacturas(List<Factura> facturas, boolean showResultado) {
        // 1. Leer el root
        Path jsonFile = Path.of(JSON_FILEPATH, dbFileNames[2]);
        try {
            JSONObject root = new JSONObject();
            // 2. Obtener el array de facturas
            JSONArray facturasArray = new JSONArray();

            for (Factura factura : facturas) {
                // 3. Crear la nueva factura y sus objetos
                JSONObject facturaObj = Factura.buildJson(factura);

                // 4. Agregar la nueva factura al array
                facturasArray.put(facturaObj);
            }

            root.put(jsonObjNames[2], facturasArray);
            // 5. Guardar el archivo (sobreescribe, pero con el array ampliado)
            try (FileWriter file = new FileWriter(jsonFile.toFile())) {
                file.write(root.toString(4));
                if (showResultado) {
                    FrameUtils.showPlainMessage("Éxito", "Facturas guardadas correctamente.");
                }
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }

    public static Factura findFactura(String numFactura) {
        JSONObject root = readJsonFacturas();
        JSONArray JSONArray = root.getJSONArray(jsonObjNames[2]);
        for (int i = 0; i < JSONArray.length(); i++) {
            JSONObject readFactura = JSONArray.getJSONObject(i);
            String objNumFra = readFactura.getString("num_factura");
            if (numFactura.equals(objNumFra)) {
                return Factura.getInstanceFromJson(readFactura);
            }
        }
        return null;
    }

    public static List<Factura> getFacturasByFilter(String searchValue, String jsonProperty) {
        List<Factura> listaFacturasFiltradas = new ArrayList();
        try {
            JSONObject root = readJsonFacturas();
            JSONArray facturasObj = root.getJSONArray(jsonObjNames[2]);
            for (int i = 0; i < facturasObj.length(); i++) {
                JSONObject loadedItem = facturasObj.getJSONObject(i);
                String jsonValue;
                switch (jsonProperty) {
                    case "num_factura":
                    case "fecha_emision":
                    case "fecha_vencimiento":
                        jsonValue = loadedItem.getString(jsonProperty);
                        break;
                    case "importe_total":
                        jsonValue = String.format("%.2f", loadedItem.getDouble(jsonProperty)); // Quita miles en ENG y luego sustituye decimales en ENG a ES.
                        break;
                    default:
                        jsonValue = null;
                        break;
                }
                if (searchValue.contains("*")) {
                    String regex = searchValue.replace(".", "\\.").replace("*", ".*");
                    if (Pattern.matches(regex, jsonValue)) {
                        listaFacturasFiltradas.add(Factura.getInstanceFromJson(loadedItem));
                    }
                } else if ( (searchValue.startsWith("<") || searchValue.startsWith(">")) && jsonProperty.equals("importe_total") && searchValue.length() > 1) {
                    jsonValue = jsonValue.replace(",", ".");
                    switch (searchValue.charAt(0)) {
                        case '<':
                            if (searchValue.charAt(1) == '=') {
                                if (searchValue.length() > 2 && Double.valueOf(jsonValue) <= Double.valueOf(searchValue.substring(2).replace(",", "."))) {
                                    listaFacturasFiltradas.add(Factura.getInstanceFromJson(loadedItem));
                                }
                            } else {
                                if (Double.valueOf(jsonValue) < Double.valueOf(searchValue.replace(",", ".").substring(1))) {
                                    listaFacturasFiltradas.add(Factura.getInstanceFromJson(loadedItem));
                                }
                            }
                            break;
                        case '>':
                            if (searchValue.charAt(1) == '=') {
                                if (searchValue.length() > 2 && Double.valueOf(jsonValue) >= Double.valueOf(searchValue.substring(2).replace(",", "."))) {
                                    listaFacturasFiltradas.add(Factura.getInstanceFromJson(loadedItem));
                                }
                            } else {
                                if (Double.valueOf(jsonValue) > Double.valueOf(searchValue.replace(",", ".").substring(1))) {
                                    listaFacturasFiltradas.add(Factura.getInstanceFromJson(loadedItem));
                                }
                            }
                            break;
                    }
                } else {
                    if (searchValue.equals(jsonValue)) {
                        listaFacturasFiltradas.add(Factura.getInstanceFromJson(loadedItem));
                    }
                }
            }
            return listaFacturasFiltradas.isEmpty() ? null : listaFacturasFiltradas;
        } catch (Exception ex) {
            ex.getStackTrace();
            return null;
        }
    }
    
    public static List<Factura> getFacturasByFilter(String value, Double jsonProperty) {
        return getFacturasByFilter(value, jsonProperty.toString());
    }

    public static List<Factura> findAllFacturas() {
        JSONObject root = readJsonFacturas();
        JSONArray JSONArray = root.getJSONArray(jsonObjNames[2]);
        List<Factura> listaFacturas = new ArrayList<Factura>();
        for (int i = 0; i < JSONArray.length(); i++) {
            JSONObject readFactura = JSONArray.getJSONObject(i);
            listaFacturas.add(Factura.getInstanceFromJson(readFactura));
        }
        return listaFacturas;
    }

    public static Factura findFacturaByNumFactura(String numFactura) {
        return findFactura(numFactura);
    }

    public static boolean isNumFacturaExistente(String numFra) {
        JSONObject root = readJsonFacturas();
        JSONArray JSONArray = root.getJSONArray(jsonObjNames[2]);
        for (int i = 0; i < JSONArray.length(); i++) {
            JSONObject loadedItem = JSONArray.getJSONObject(i);
            String objNumFra = loadedItem.getString("num_factura");
            if (numFra.equals(objNumFra)) {
                return true;
            }
        }
        return false;
    }

    public static String findNextNumFactura() {
        int propNumero = 1;
        while (true) {
            String propNumFra = String.valueOf(Year.now().getValue()) + String.format("-%03d", propNumero);
            if (!isNumFacturaExistente(propNumFra)) {
                return propNumFra;
            } else if (propNumero == 999) {
                FrameUtils.showErrorMessage("Error", "Se han agotado las combinaciones para números de factura de este año (+999 factura en " + String.valueOf(Year.now().getValue()) + ")");
            }
        }
    }
}
