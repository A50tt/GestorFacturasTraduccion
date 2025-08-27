package jud.gestorfacturas.gui.configuracion;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import jud.gestorfacturas.gui.MainContainerController;
import jud.gestorfacturas.interfaces.Controller;
import jud.gestorfacturas.interfaces.GlobalController;
import jud.gestorfacturas.manager.PDFGenerator;
import jud.gestorfacturas.model.Cliente;
import jud.gestorfacturas.model.Emisor;
import jud.gestorfacturas.model.Factura;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.FileUtils;
import utils.FrameUtils;
import utils.JSONUtils;

public class ImportarController implements Controller {

    ImportarView importarView;
    JLabel importarBDEmisorResultLbl;
    JLabel importarBDClientesResultLbl;
    JLabel importarBDFacturasResultLbl;
    JList listaImportaciones;

    final String MARCA_ESPECIAL_PATH = " | ";
    final String SEPARATOR_ESPECIAL_PATH = " - ";
    final String ERROR_PATH = " [ERROR] ";

    List<File> bdEmisor = new ArrayList<File>();
    String savedEmisor; // Lleva la cuenta de que solo haya un emisor cargado (límite de importación de emisor es 1)
    final String MARCA_TIPO_BD_EMISOR = "BDEmisor";
    final String MARCA_PATH_BD_EMISOR = MARCA_ESPECIAL_PATH + MARCA_TIPO_BD_EMISOR + MARCA_ESPECIAL_PATH + SEPARATOR_ESPECIAL_PATH;
    final String MARCA_PATH_BD_EMISOR_ERROR = MARCA_ESPECIAL_PATH + MARCA_TIPO_BD_EMISOR + MARCA_ESPECIAL_PATH + ERROR_PATH + SEPARATOR_ESPECIAL_PATH;

    List<File> bdClientes = new ArrayList<File>();
    final String MARCA_TIPO_BD_CLIENTE = "BDClientes";
    final String MARCA_PATH_BD_CLIENTE = MARCA_ESPECIAL_PATH + MARCA_TIPO_BD_CLIENTE + MARCA_ESPECIAL_PATH + SEPARATOR_ESPECIAL_PATH;
    final String MARCA_PATH_BD_CLIENTE_ERROR = MARCA_ESPECIAL_PATH + MARCA_TIPO_BD_CLIENTE + MARCA_ESPECIAL_PATH + ERROR_PATH + SEPARATOR_ESPECIAL_PATH;

    List<File> bdFacturas = new ArrayList<File>();
    final String MARCA_TIPO_BD_FACTURA = "BDFacturas";
    final String MARCA_PATH_BD_FACTURA = MARCA_ESPECIAL_PATH + MARCA_TIPO_BD_FACTURA + MARCA_ESPECIAL_PATH + SEPARATOR_ESPECIAL_PATH;
    final String MARCA_PATH_BD_FACTURA_ERROR = MARCA_ESPECIAL_PATH + MARCA_TIPO_BD_FACTURA + MARCA_ESPECIAL_PATH + ERROR_PATH + SEPARATOR_ESPECIAL_PATH;

    List<File> pdfFacturas = new ArrayList<File>();
    final String MARCA_TIPO_PDF_FACTURA = "PDFFacturas";
    final String MARCA_PATH_PDF_FACTURA = MARCA_ESPECIAL_PATH + MARCA_TIPO_PDF_FACTURA + MARCA_ESPECIAL_PATH + SEPARATOR_ESPECIAL_PATH;
    final String MARCA_PATH_PDF_FACTURA_ERROR = MARCA_ESPECIAL_PATH + MARCA_TIPO_PDF_FACTURA + MARCA_ESPECIAL_PATH + ERROR_PATH + SEPARATOR_ESPECIAL_PATH;

    // Objetos finales a importar
    Emisor emisorToImport = null;
    List<Cliente> clientesToImport = new ArrayList<Cliente>();
    List<Factura> facturasToImport = new ArrayList<Factura>();

    private String viewName = "Importar";

    public ImportarController(GlobalController globalController) {
        this.importarView = new ImportarView(this);
        initialize();
    }

    private void initialize() {
        this.importarBDEmisorResultLbl = importarView.firstRowResultLbl;
        this.importarBDClientesResultLbl = importarView.secondRowResultLbl;
        this.importarBDFacturasResultLbl = importarView.thirdRowResultLbl;
        this.listaImportaciones = importarView.importacionesList;
    }

    public JPanel getView() {
        return this.importarView;
    }

    @Override
    public String getViewName() {
        return this.viewName;
    }

    @Override
    public void setViewName(String newName) {
        this.viewName = newName;
    }

    public void importFile(JButton sourceButton, JLabel resultLabel, boolean multi) {
        File[] selectedFiles = FileUtils.openFileChooser(importarView, multi);
        if (selectedFiles == null) {
            return;
        }
        
        // Comprobamos formato aceptado
        String formatoAceptado;
        switch (sourceButton.getName()) {
            case MARCA_TIPO_BD_EMISOR:
            case MARCA_TIPO_BD_CLIENTE:
            case MARCA_TIPO_BD_FACTURA:
                formatoAceptado = "JSON";
                break;
            case MARCA_TIPO_PDF_FACTURA:
                formatoAceptado = "PDF";
                break;
            default:
                formatoAceptado = null;
        }
        
        for (File file : selectedFiles) {
            if (FileUtils.isOneOfExtensions(file, new String[]{formatoAceptado})) {
                // Guardamos en el File[] del controller
                String listString = null;
                switch (sourceButton.getName()) {
                    case MARCA_TIPO_BD_EMISOR:
                        if (isCategoryInList(MARCA_TIPO_BD_EMISOR)) {
                            FrameUtils.showInfoMessage("Reemplazado", "Solo se puede importar un único archivo de emisor. El fichero seleccionado reemplazará al anterior que había en cola.");
                            ImportarController.this.removeJListItem(MARCA_PATH_BD_EMISOR + savedEmisor);
                        }
                        savedEmisor = file.getAbsolutePath();
                        listString = MARCA_PATH_BD_EMISOR + file.getAbsolutePath();
                        break;
                    case MARCA_TIPO_BD_CLIENTE:
                        listString = MARCA_PATH_BD_CLIENTE + file.getAbsolutePath();
                        break;
                    case MARCA_TIPO_BD_FACTURA:
                        listString = MARCA_PATH_BD_FACTURA + file.getAbsolutePath();
                        break;
                    case MARCA_TIPO_PDF_FACTURA:
                        listString = MARCA_PATH_PDF_FACTURA + file.getAbsolutePath();
                        break;
                }
                // Añadimos a la JList
                importarView.importacionesPendientesModel.addElement(listString);
            } else {
                FrameUtils.showErrorMessage("Error", "No se ha podido añadir " + file.getName() + ". Solo son compatibles los archivos con las siguientes extensiones:\n" + formatoAceptado);
            }
        }
    }

    public void removeImportFile(java.awt.event.MouseEvent evt) {
        int jListIndex = listaImportaciones.locationToIndex(evt.getPoint());
        if (jListIndex != -1) {
            String jListItem = importarView.importacionesPendientesModel.get(jListIndex);
            // Eliminar de la JList
            importarView.importacionesPendientesModel.remove(jListIndex);
        }
    }

    public boolean isCategoryInList(String category) {
        DefaultListModel<String> model = (DefaultListModel<String>) listaImportaciones.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).contains(category)) {
                return true;
            }
        }
        return false;
    }

    public void addItemToList(String itemStr) {
        importarView.importacionesPendientesModel.addElement(itemStr);
    }

    public void removeAllJListItems() {
        importarView.importacionesPendientesModel.removeAllElements();
    }

    public void removeJListItem(String itemText) {
        DefaultListModel<String> model = (DefaultListModel<String>) listaImportaciones.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).equals(itemText)) {
                importarView.importacionesPendientesModel.remove(i);
            }
        }
    }

    public void defaultAllDataContainerVariables() {
        bdEmisor = new ArrayList();
        bdClientes = new ArrayList();
        bdFacturas = new ArrayList();
        pdfFacturas = new ArrayList();
        
        emisorToImport = null;
        clientesToImport = new ArrayList<>();
        facturasToImport = new ArrayList<>();
    }

    public void loadFiles() {
        DefaultListModel<String> model = (DefaultListModel<String>) listaImportaciones.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            String itemStr = model.elementAt(i);
            int startIndex = itemStr.indexOf(MARCA_ESPECIAL_PATH) + MARCA_ESPECIAL_PATH.length();
            int endIndex = itemStr.indexOf(MARCA_ESPECIAL_PATH, startIndex);
            String type = itemStr.substring(startIndex, endIndex);
            File file = new File(itemStr.substring(itemStr.indexOf(SEPARATOR_ESPECIAL_PATH) + SEPARATOR_ESPECIAL_PATH.length()));
            switch (type) {
                case MARCA_TIPO_BD_EMISOR:
                    bdEmisor.add(file);
                    break;
                case MARCA_TIPO_BD_CLIENTE:
                    bdClientes.add(file);
                    break;
                case MARCA_TIPO_BD_FACTURA:
                    bdFacturas.add(file);
                    break;
                case MARCA_TIPO_PDF_FACTURA:
                    pdfFacturas.add(file);
                    break;
            }
        }
    }

    public void buildObjects() {
        List<String> informeErrores = new ArrayList<>();
        if (!bdEmisor.isEmpty()) {
            try {
                JSONObject root = JSONUtils.readJsonFile(bdEmisor.getFirst());
                emisorToImport = Emisor.getInstanceFromJson(root);
                bdEmisor.remove(bdEmisor.getFirst());
            } catch (JSONException ex) {
                ex.getStackTrace();
                informeErrores.add("Error importando el fichero " + bdEmisor.getFirst().getAbsolutePath() + ". -> " + ex.getMessage());
            }
        }
        if (!bdClientes.isEmpty()) {
            for (File clientesBdFile : bdClientes) {
                boolean correctImport = true;
                List<Cliente> jsonTempClients = new ArrayList<>(); // Hasta que todos los archivos de un JSON no estén correctamente extraidos, no se añade a "clientesToImport".
                try {
                    JSONObject root = JSONUtils.readJsonFile(clientesBdFile);
                    JSONArray clientesArr = root.getJSONArray(JSONUtils.jsonObjNames[1]);
                    for (int i = 0; i < clientesArr.length(); i++) {
                        try {
                            JSONObject clienteJson = clientesArr.getJSONObject(i);
                            jsonTempClients.add(Cliente.getInstanceFromJson(clienteJson));
                        } catch (JSONException ex) {
                            ex.getStackTrace();
                            correctImport = false;
                            informeErrores.add("Error importando el fichero " + clientesBdFile.getAbsolutePath() + ". -> " + ex.getMessage());
                        }
                    }
                } catch (JSONException ex) {
                    ex.getStackTrace();
                    correctImport = false;
                    informeErrores.add("Error importando el fichero " + clientesBdFile.getAbsolutePath() + ". -> " + ex.getMessage());
                }
                if (correctImport) {
                    for (Cliente clienteTemp : jsonTempClients) {
                        clientesToImport.add(clienteTemp);
                    }
                }
            }
        }
        if (!bdFacturas.isEmpty()) {
            for (File facturasBdFile : bdFacturas) {
                boolean correctImport = true;
                List<Factura> jsonTempFacturas = new ArrayList<>(); // Hasta que todos los archivos de un JSON no estén correctamente extraidos, no se añade a "facturasToImport".
                try {
                    JSONObject root = JSONUtils.readJsonFile(facturasBdFile);
                    JSONArray facturasArr = root.getJSONArray(JSONUtils.jsonObjNames[2]);
                    for (int i = 0; i < facturasArr.length(); i++) {
                        try {
                            JSONObject facturaJson = facturasArr.getJSONObject(i);
                            jsonTempFacturas.add(Factura.getInstanceFromJson(facturaJson));
                        } catch (JSONException ex) {
                            ex.getStackTrace();
                            correctImport = false;
                            informeErrores.add("Error importando el fichero " + facturasBdFile.getAbsolutePath() + ". -> " + ex.getMessage());
                        }
                    }
                } catch (JSONException ex) {
                    ex.getStackTrace();
                    correctImport = false;
                    informeErrores.add("Error importando el fichero " + facturasBdFile.getAbsolutePath() + ". -> " + ex.getMessage());
                }
                if (correctImport) {
                    for (Factura facturaTemp : jsonTempFacturas) {
                        facturasToImport.add(facturaTemp);
                    }
                }
            }
        }
        if (!informeErrores.isEmpty()) {
            int respuesta = FrameUtils.showErrorQuestionBoxSiNo("Error en la importación", "Ha fallado la importación de algunos ficheros. ¿Ver el informe de errores?");
            if (respuesta == JOptionPane.OK_OPTION) {
                // Datos del modelo de la JList
                String[] items = informeErrores.toArray(new String[0]);
                JList<String> list = new JList<>(items);

                // Creamos un JFrame aparte.
                JFrame frame = new JFrame("Informe de errores");
                frame.add(new JScrollPane(list));
                frame.setSize(800, 500);
                frame.setLocationRelativeTo(MainContainerController.frameContainerView);
                frame.setVisible(true);
            }
        }
    }
    
    public int checkJsonFilesIntegrityOrResetThem() {
        List<String> jsonsToCheck = new ArrayList<>();
        if (isCategoryInList(MARCA_TIPO_BD_EMISOR)) {
            jsonsToCheck.add(JSONUtils.dbFileNames[0]);
        }
        if (isCategoryInList(MARCA_TIPO_BD_CLIENTE)) {
            jsonsToCheck.add(JSONUtils.dbFileNames[1]);
        }
        if (isCategoryInList(MARCA_TIPO_BD_FACTURA)) {
            jsonsToCheck.add(JSONUtils.dbFileNames[2]);
        }
        return JSONUtils.checkJsonFilesIntegrityOrResetThem(jsonsToCheck);
    }

    public void saveObjectsToMainFiles() {
        if (emisorToImport != null) {
            JSONUtils.saveEmisor(emisorToImport, false);
        }

        if (!clientesToImport.isEmpty()) {
            clientesToImport = Cliente.cleanDuplicates(clientesToImport);
            JSONUtils.replaceAllClientes(clientesToImport, false);
        }

        if (!facturasToImport.isEmpty()) {
            facturasToImport = Factura.cleanDuplicates(facturasToImport);
            JSONUtils.replaceAllFacturas(facturasToImport, false);
        }

        if (!pdfFacturas.isEmpty()) {
            for (File facturaPdfFile : pdfFacturas) {
                FileUtils.copy(facturaPdfFile.toPath(), Paths.get(PDFGenerator.INVOICES_DIRECTORY, facturaPdfFile.getName()));
            }
        }
    }

    public void ejecutarImportacionGlobal() {
        if (!importarView.importacionesPendientesModel.isEmpty()) {
            if (checkJsonFilesIntegrityOrResetThem() == 1) { // El usuario no ha querido reparar un JSON dañado que estaba incluido, se aborta la importación.
                int respuesta = FrameUtils.showQuestionBoxContinuarCancelar("Aviso importante", "Está seguro que quiere importar estos archivos? Esto sobreescribirá las base de datos en las que se aplique,\npor lo que se recomienda que se haga solo sobre una base de datos vacía.");
                if (respuesta == JOptionPane.OK_OPTION) {
                    loadFiles();
                    buildObjects();
                    saveObjectsToMainFiles(); // Está sobreescribiendo los archivos de BD principales, no añadiendo. Excepto PDFs de facturas, que pregunta antes.
                    removeAllJListItems();
                    defaultAllDataContainerVariables();
                    FrameUtils.showInfoMessage("Finalizado", "El proceso de importación ha finalizado.");
                }
            } else {
                FrameUtils.showInfoMessage("Finalizado", "Se ha abortado la importación debido a un problema con la base de datos.");
            }
        } else {
            FrameUtils.showInfoMessage("Aviso", "No se ha seleccionado ningún fichero para importar.");
        }
    }
}
