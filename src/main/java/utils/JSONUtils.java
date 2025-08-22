package utils;

import java.io.File;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jud.gestorfacturas.manager.CustomException;
import jud.gestorfacturas.model.Cliente;
import jud.gestorfacturas.model.Emisor;
import jud.gestorfacturas.model.Factura;
import jud.gestorfacturas.model.Servicio;
import org.json.JSONArray;
import org.json.JSONException;
import utils.FrameUtils;

public class JSONUtils {
    
    public static final String WORKING_DIR = System.getProperty("user.dir");
    public static final String JSON_FILEPATH = Paths.get(WORKING_DIR, "src", "main", "resources", "META-INF").toString();

    public static void createJsonExample() {
        // --- 1. Create hierarchical JSON ---

        // Customer object
        JSONObject customer = new JSONObject();
        customer.put("name", "John Doe");
        customer.put("email", "john@example.com");

        // Items array
        JSONArray items = new JSONArray();

        JSONObject item1 = new JSONObject();
        item1.put("description", "Widget");
        item1.put("quantity", 2);
        item1.put("price", 10.0);
        items.put(item1);

        JSONObject item2 = new JSONObject();
        item2.put("description", "Gadget");
        item2.put("quantity", 1);
        item2.put("price", 20.0);
        items.put(item2);

        // Main invoice object
        JSONObject invoice = new JSONObject();
        invoice.put("id", 123);
        invoice.put("date", "2024-08-20");
        invoice.put("customer", customer);
        invoice.put("items", items);
        invoice.put("total", 40.0);

        // --- 2. Write to file ---
        try (FileWriter file = new FileWriter(JSON_FILEPATH)) {
            file.write(invoice.toString(4)); // pretty print with 4 spaces
            System.out.println("Hierarchical JSON file written.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readJsonExample() {
        // --- 3. Read back from file and access data ---
        try {
            String content = new String(Files.readAllBytes(Paths.get("invoice.json")));
            JSONObject loadedInvoice = new JSONObject(content);

            // Access top-level fields
            int id = loadedInvoice.getInt("id");
            String date = loadedInvoice.getString("date");

            // Access nested customer object
            JSONObject loadedCustomer = loadedInvoice.getJSONObject("customer");
            String customerName = loadedCustomer.getString("name");
            String customerEmail = loadedCustomer.getString("email");

            // Access items array
            JSONArray loadedItems = loadedInvoice.getJSONArray("items");
            for (int i = 0; i < loadedItems.length(); i++) {
                JSONObject loadedItem = loadedItems.getJSONObject(i);
                String description = loadedItem.getString("description");
                int quantity = loadedItem.getInt("quantity");
                double price = loadedItem.getDouble("price");
                System.out.println("Item: " + description + ", Quantity: " + quantity + ", Price: " + price);
            }

            // Access total
            double total = loadedInvoice.getDouble("total");

            System.out.println("Invoice ID: " + id);
            System.out.println("Date: " + date);
            System.out.println("Customer: " + customerName + " (" + customerEmail + ")");
            System.out.println("Total: " + total);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // EMISOR
    
    private static JSONObject buildEmisorJsonObject(Emisor emisor) {
        JSONObject emisorObj = new JSONObject();
        emisorObj.put("nif", emisor.getNif());
        emisorObj.put("nombre", emisor.getNombre());
        emisorObj.put("nombre_completo", emisor.getNombreCompleto());
        emisorObj.put("direccion", emisor.getDireccion());
        emisorObj.put("codigo_postal", emisor.getCodigoPostal());
        emisorObj.put("iban", emisor.getIban());
        emisorObj.put("ult_actualizacion", emisor.getFechaUltActualizacion());

        return emisorObj;
    }
    
    public static void saveEmisor(Emisor emisor) {
        // 1. Leer el root
        Path jsonFile = Path.of(JSON_FILEPATH, "emisor.json");
        try {            
            // 2. Crear el nuevo emisor
            JSONObject emisorObj = buildEmisorJsonObject(emisor);
            
            // 3. Guardar el archivo (sobreescribe, pero con el array ampliado)
            try (FileWriter file = new FileWriter(jsonFile.toFile())) {
                file.write(emisorObj.toString(4));
                FrameUtils.showPlainMessage("Éxito", "El emisor se ha guardado correctamente.");
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    public static Emisor getEmisorGuardado() {
        try {
            String content = new String(Files.readAllBytes(Path.of(JSON_FILEPATH, "emisor.json")));
            JSONObject emisorObj = new JSONObject(content);
            String nif = emisorObj.getString("nif");
            String nombre = emisorObj.getString("nombre");
            String nombreCompleto = emisorObj.getString("nombre_completo");
            String direccion = emisorObj.optString("direccion");
            String codigoPostal = emisorObj.optString("codigo_postal");
            String iban = emisorObj.optString("iban");
            return new Emisor(nif, nombre, nombreCompleto, direccion, codigoPostal, iban);
        } catch (IOException | JSONException ex) {
            return null;
        }
    }
    
    private static Emisor buildEmisorFromJson(JSONObject emisorObj) {
        return new Emisor(
                emisorObj.getString("nif"),
                emisorObj.getString("nombre"),
                emisorObj.getString("nombre_completo"),
                emisorObj.getString("direccion"),
                emisorObj.getString("codigo_postal"),
                emisorObj.getString("iban")
        );
    }

    // CLIENTES
    
    public static JSONObject readJsonClientes() throws IOException {
        Path jsonFile = Path.of(JSON_FILEPATH, "clientes.json");
        // 1. Leer el archivo existente
        String content = new String(Files.readAllBytes(jsonFile));
        return new JSONObject(content);
    }
    
    public static void saveAllClientes(List<Cliente> clientes) {
        try {
            // 1. Leer el root
            Path jsonFile = Path.of(JSON_FILEPATH, "clientes.json");

            JSONObject root = readJsonClientes();
            // 2. Obtener el array de facturas
            JSONArray clientesObj = new JSONArray();

            for (Cliente cliente : clientes) {
                // 3. Crear el nuevo cliente y sus objetos
                JSONObject clienteObj = buildClienteJsonObject(cliente);

                // 4. Agregar el nuevo cliente al array
                clientesObj.put(clienteObj);
            }
            // 5. Guardar el archivo (sobreescribe, pero con el array ampliado)
            try (FileWriter file = new FileWriter(jsonFile.toFile())) {
                file.write(root.toString(4));
                FrameUtils.showPlainMessage("Éxito", "El cliente se ha actualizado correctamente.");
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void replaceCliente(int index, Cliente cliente) {
        try {
            // 1. Leer el root
            Path jsonFile = Path.of(JSON_FILEPATH, "clientes.json");

            JSONObject root = readJsonClientes();
            // 2. Obtener el array de facturas
            JSONArray clientesObj = root.getJSONArray("clientes");
            // 3. Crear el nuevo cliente y sus objetos
            JSONObject clienteObj = buildClienteJsonObject(cliente);

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
        Path jsonFile = Path.of(JSON_FILEPATH, "clientes.json");
        try {
            JSONObject root = readJsonClientes();
            // 2. Obtener el array de facturas
            JSONArray clientes = root.getJSONArray("clientes");
            
            // 3. Crear el nuevo cliente y sus objetos
            JSONObject clienteObj = buildClienteJsonObject(cliente);
            
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

    private static JSONObject buildClienteJsonObject(Cliente cliente) {
        JSONObject clienteObj = new JSONObject();
        clienteObj.put("id", cliente.getId());
        clienteObj.put("nif", cliente.getNif());
        clienteObj.put("nombre", cliente.getNombre());
        clienteObj.put("direccion", cliente.getDireccion());
        clienteObj.put("codigo_postal", cliente.getCodigoPostal());
        clienteObj.put("activado", cliente.isActivado());
        clienteObj.put("ult_actualizacion", cliente.getFechaUltActualizacion());
        return clienteObj;
    }
    
    public static List<Cliente> getAllClientes() {
        try {
            JSONObject root = readJsonClientes();
            JSONArray clientesObj = root.getJSONArray("clientes");
            List<Cliente> listaClientes = new ArrayList<>();
            for (int i = 0; i < clientesObj.length(); i++) {
                JSONObject loadedItem = clientesObj.getJSONObject(i);
                listaClientes.add(buildClienteFromJson(loadedItem));
            }
            return listaClientes;
        } catch (IOException ex) {
            ex.getStackTrace();
            return null;
        }
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
        try {
            JSONObject root = readJsonClientes();
            JSONArray clientesObj = root.getJSONArray("clientes");
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
                        listaClientesFiltrados.add(buildClienteFromJson(loadedItem));
                    }
                } else {
                    if (value == jsonValue) {
                        listaClientesFiltrados.add(buildClienteFromJson(loadedItem));
                    }
                }
            }
            return listaClientesFiltrados.isEmpty() ? null : listaClientesFiltrados;
        } catch (IOException ex) {
            ex.getStackTrace();
            return null;
        }
    }
    
    private static Cliente buildClienteFromJson(JSONObject clienteObj) {
        Cliente cliente = new Cliente(
                clienteObj.getString("nif"),
                clienteObj.getString("nombre"),
                clienteObj.getString("direccion"),
                clienteObj.getString("codigo_postal"));
        cliente.setId(clienteObj.getInt("id"));
        cliente.setActivado(clienteObj.getBoolean("activado"));
        return cliente;
    }

    public static boolean nifClienteYaExiste(String nif) {
        try {
            JSONObject root = readJsonClientes();
            JSONArray JSONArray = root.getJSONArray("clientes");
            for (int i = 0; i < JSONArray.length(); i++) {
                JSONObject loadedItem = JSONArray.getJSONObject(i);
                String objNif = loadedItem.getString("nif");
                if (nif.equals(objNif)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
        public static boolean idClienteYaExiste(int id) {
        try {
            JSONObject root = readJsonClientes();
            JSONArray JSONArray = root.getJSONArray("clientes");
            for (int i = 0; i < JSONArray.length(); i++) {
                JSONObject loadedItem = JSONArray.getJSONObject(i);
                int objId = loadedItem.getInt("id");
                if (id == objId) {
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
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

    // FACTURAS
    
    private static JSONObject buildFacturaJsonObject(Factura factura) {
        // Factura
        JSONObject facturaObj = new JSONObject();
        facturaObj.put("num_factura", factura.getNumFactura());
        facturaObj.put("fecha_emision", factura.getFechaEmision());
        facturaObj.put("dias_pago", factura.getDiasPago());
        facturaObj.put("fecha_vencimiento", factura.getFechaVencimiento());
        facturaObj.put("forma_pago", factura.getFormaPago());
        facturaObj.put("base_imponible", factura.getBaseImponible());
        facturaObj.put("tasa_irpf", factura.getTasaIrpf());
        facturaObj.put("irpf", factura.getIrpf());
        facturaObj.put("tasa_iva", factura.getTasaIva());
        facturaObj.put("iva", factura.getIva());
        facturaObj.put("importe_total", factura.getImporteTotal());
        facturaObj.put("ruta_pdf", factura.getPdfFactura());
        facturaObj.put("ult_actualizacion", factura.getFechaUltActualizacion());
        
        JSONArray serviciosObj = buildServiciosJsonArrayFromServiciosArray(factura.getServicios());
        facturaObj.put("servicios", serviciosObj);
        // Cliente
        JSONObject clienteObj = buildClienteJsonObject(factura.getCliente());
        facturaObj.put("cliente", clienteObj);
        // Emisor
        JSONObject emisorObj = buildEmisorJsonObject(factura.getEmisor());
        facturaObj.put("emisor", emisorObj);
        
        return facturaObj;
    }
    
    public static JSONObject readJsonFacturas() throws IOException {
        Path jsonFile = Path.of(JSON_FILEPATH, "facturas.json");
        // 1. Leer el archivo existente
        String content = new String(Files.readAllBytes(jsonFile));
        return new JSONObject(content);
    }
    
    public static void saveFactura(Factura factura) {
        if (numFacturaYaExiste(factura.getNumFactura())) {
            FrameUtils.showErrorMessage("Error", "Ya existe una factura con este n.º de factura. No se permiten duplicados.");
            return;
        }
        // 1. Leer el root
        Path jsonFile = Path.of(JSON_FILEPATH, "facturas.json");
        try {
            JSONObject root = readJsonFacturas();
            // 2. Obtener el array de facturas
            JSONArray facturas = root.getJSONArray("facturas");
            
            // 3. Crear la nueva factura y sus objetos
            JSONObject facturaObj = buildFacturaJsonObject(factura);
            
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
 
    public static void saveAllFacturas(List<Factura> facturas) {
       // 1. Leer el root
        Path jsonFile = Path.of(JSON_FILEPATH, "facturas.json");
        try {
            JSONObject root = readJsonClientes();
            // 2. Obtener el array de facturas
            JSONArray facturasObj = root.getJSONArray("facturas");

            for (Factura factura : facturas) {
                // 3. Crear la nueva factura y sus objetos
                JSONObject facturaObj = buildFacturaJsonObject(factura);

                // 4. Agregar la nueva factura al array
                facturasObj.put(facturaObj);
            }
            
            // 5. Guardar el archivo (sobreescribe, pero con el array ampliado)
            try (FileWriter file = new FileWriter(jsonFile.toFile())) {
                file.write(root.toString(4));
                FrameUtils.showPlainMessage("Éxito", "La factura se ha guardado correctamente.");
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    public static Factura getFactura(String numFactura) {
        try {
            JSONObject root = readJsonFacturas();
            JSONArray JSONArray = root.getJSONArray("facturas");
            for (int i = 0; i < JSONArray.length(); i++) {
                JSONObject readFactura = JSONArray.getJSONObject(i);
                String objNumFra = readFactura.getString("num_factura");
                if (numFactura.equals(objNumFra)) {
                    return buildFacturaFromJson(readFactura);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }
    
    public static List<Factura> getFacturasByFilter(String value, String jsonProperty) {
        List<Factura> listaFacturasFiltradas = new ArrayList();
        try {
            JSONObject root = readJsonClientes();
            JSONArray facturasObj = root.getJSONArray("facturas");
            for (int i = 0; i < facturasObj.length(); i++) {
                JSONObject loadedItem = facturasObj.getJSONObject(i);
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
                        listaFacturasFiltradas.add(buildFacturaFromJson(loadedItem));
                    }
                } else {
                    if (value == jsonValue) {
                        listaFacturasFiltradas.add(buildFacturaFromJson(loadedItem));
                    }
                }
            }
            return listaFacturasFiltradas.isEmpty() ? null : listaFacturasFiltradas;
        } catch (IOException ex) {
            ex.getStackTrace();
            return null;
        }
    }
    
    public static List<Factura> getAllFacturas() {
        try {
            JSONObject root = readJsonFacturas();
            JSONArray JSONArray = root.getJSONArray("facturas");
            List<Factura> listaFacturas = new ArrayList<Factura>();
            for (int i = 0; i < JSONArray.length(); i++) {
                JSONObject readFactura = JSONArray.getJSONObject(i);
                listaFacturas.add(JSONUtils.buildFacturaFromJson(readFactura));
            }
            return listaFacturas;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static Factura getFacturaByNumFactura(String numFactura) {
        return getFactura(numFactura);
    }
    
    public static Factura buildFacturaFromJson(JSONObject facturaObj) {
        return new Factura(
                facturaObj.getString("num_factura"),
                Date.valueOf(facturaObj.getString("fecha_emision")),
                facturaObj.getInt("dias_pago"),
                facturaObj.getString("forma_pago"),
                buildClienteFromJson(facturaObj.getJSONObject("cliente")),
                buildEmisorFromJson(facturaObj.getJSONObject("emisor")),
                buildServiciosFromJson(facturaObj.getJSONArray("servicios")),
                Timestamp.valueOf(facturaObj.getString("ult_actualizacion"))
        );
    }
    
    public static boolean numFacturaYaExiste(String numFra) {
        try {
            JSONObject root = readJsonFacturas();
            JSONArray JSONArray = root.getJSONArray("facturas");
            for (int i = 0; i < JSONArray.length(); i++) {
                JSONObject loadedItem = JSONArray.getJSONObject(i);
                String objNumFra = loadedItem.getString("num_factura");
                if (numFra.equals(objNumFra)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public static String getSiguienteNumFacturaDisponible() {
        int propNumero = 1;
        try {
            JSONObject root = readJsonClientes();
            while (true) {
                String propNumFra = String.valueOf(Year.now().getValue()) + String.format("-%03d", propNumero);
                if (!numFacturaYaExiste(propNumFra)) {
                    return propNumFra;
                } else if (propNumero == 999) {
                    throw new CustomException("Se han agotado las combinaciones para números de factura de este año (+999 factura en " + String.valueOf(Year.now().getValue()) + ")");
                }
            }
        } catch (IOException | CustomException ex) {
            FrameUtils.showErrorMessage("Error", ex.getMessage());
            return null;
        }
    }
    
    // SERVICIOS
    private static Servicio[] buildServiciosFromJson(JSONArray serviciosObj) {
        Servicio[] servicios = new Servicio[serviciosObj.length()];
        for (int i = 0; i < serviciosObj.length(); i++) {
            JSONObject servicioObj = serviciosObj.getJSONObject(i);
            String idiomaOrigen = servicioObj.getString("idioma_origen");
            String idiomaDestino = servicioObj.getString("idioma_destino");
            String descripcion = servicioObj.getString("descripcion");
            String tipo = servicioObj.getString("tipo");
            Double precioUnitario = servicioObj.getDouble("precio_unitario");
            Double cantidad = servicioObj.getDouble("cantidad");
            Servicio servicio = new Servicio(idiomaOrigen, idiomaDestino, descripcion, tipo, precioUnitario, cantidad);
            servicios[i] = servicio;
        }
        return servicios;
    }
    
    private static JSONArray buildServiciosJsonArrayFromServiciosArray(Servicio[] servicios) {
        JSONArray serviciosArr = new JSONArray();
        for (Servicio servicio : servicios) {
            JSONObject servicioObj = new JSONObject();
            servicioObj.put("idioma_origen", servicio.getIdiomaOrigen());
            servicioObj.put("idioma_destino", servicio.getIdiomaDestino());
            servicioObj.put("descripcion", servicio.getDescripcion());
            servicioObj.put("tipo", servicio.getTipo());
            servicioObj.put("precio_unitario", servicio.getPrecioUnitario());
            servicioObj.put("cantidad", servicio.getCantidad());
            servicioObj.put("precio_final", servicio.getPrecioFinal());
            serviciosArr.put(servicioObj);
        }
        return serviciosArr;
    }
}
