package jud.gestorfacturas.gui.crear;

import jud.gestorfacturas.gui.buscar.BuscarClienteController;
import jud.gestorfacturas.interfaces.Controller;
import jud.gestorfacturas.interfaces.DataListenerController;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jud.gestorfacturas.gui.editar.ModificarClienteController;
import jud.gestorfacturas.manager.PDFGenerator;
import utils.FormatUtils;
import jud.gestorfacturas.model.Cliente;
import jud.gestorfacturas.model.Emisor;
import jud.gestorfacturas.model.Factura;
import jud.gestorfacturas.model.Servicio;
import utils.FrameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import utils.JSONUtils;

public class CrearFacturaController implements Controller, DataListenerController {

    FormatUtils utils = new FormatUtils();
    protected int fichaEsCorrecta = 0; // -1 si es INCORRECTA, 0 si es NEUTRAL, 1 si es CORRECTA
    public static final String VIEW_NAME = "Crear factura";
    public String finalViewName;

    public Color defaultBackgroundTxtBoxColor = Color.white;
    public Color clienteBackgroundTxtBoxColor = Color.white;
    public final Color ERROR_BG_COLOR = Color.red;
    public final Color BLOCKED_STATE_BTN_COLOR = Color.orange;
    public Color freeStateBtnColor = Color.white;
    public String[] TIPOS_UNIDAD = {"", "Clip", "Hora", "Minuto", "Palabra"};
    public String[] FORMAS_PAGO = {"", "Transferencia bancaria", "Cheque"};
    private final String ERROR_FUT_GENERANDO_FACTURA_NO_EMISOR = "No se podrá guardar una factura porque no se ha informado de los datos fiscales del emisor. Por favor, hágalo en 'Editar' > 'Editar datos personales...'.";
    private final String ERROR_AHO_GENERANDO_FACTURA_NO_EMISOR = "No se puede guardar una factura porque no se ha informado de los datos fiscales del emisor. Por favor, hágalo en 'Editar' > 'Editar datos personales...'.";
    private final String ERROR_EMISOR_NO_IBAN = "El EMISOR no tiene un IBAN informado y se ha elegido la forma de pago '" + FORMAS_PAGO[1] + "'. No se puede hacer una factura correcta.";
    
    private Controller sourceController;
    public CrearFacturaView facturaView;
    private JPanel jPanel;
    private JTextField numeroFraTxtField;
    private JTextField fechaEmisionTxtField;
    private JTextField diasParaPagoTxtField;
    private JTextField fechaVencimientoTxtField;
    private JComboBox formaPagoComboBox;

    private JPanel clienteInfoTxtFieldPanel;
    private JButton nombreClienteSearchBtn;
    private JTextField numeroClienteTxtField;
    private JTextField nombreClienteTxtField;
    private JTextField nifClienteTxtField;
    private JTextField direccionClienteTxtField;
    private JTextField codigoPostalClienteTxtField;

    private JLabel msgLbl;

    private JTextField concepto1TxtField;
    private JTextField idiomaOrigen1TxtField;
    private JTextField idiomaDestino1TxtField;
    private JTextField cantidad1TxtField;
    private JTextField precio1TxtField;
    private JComboBox item1ComboBox;
    private JTextField totalImporte1TxtField;

    private JTextField concepto2TxtField;
    private JTextField idiomaOrigen2TxtField;
    private JTextField idiomaDestino2TxtField;
    private JTextField cantidad2TxtField;
    private JTextField precio2TxtField;
    private JComboBox item2ComboBox;
    private JTextField totalImporte2TxtField;

    private JTextField concepto3TxtField;
    private JTextField idiomaOrigen3TxtField;
    private JTextField idiomaDestino3TxtField;
    private JTextField cantidad3TxtField;
    private JTextField precio3TxtField;
    private JComboBox item3ComboBox;
    private JTextField totalImporte3TxtField;

    private JTextField concepto4TxtField;
    private JTextField idiomaOrigen4TxtField;
    private JTextField idiomaDestino4TxtField;
    private JTextField cantidad4TxtField;
    private JTextField precio4TxtField;
    private JComboBox item4ComboBox;
    private JTextField totalImporte4TxtField;

    private JButton verificarFichaBtn;
    private JButton previewFacturaBtn;
    private JButton registrarFacturaBtn;
    
    public CrearFacturaController() {
        facturaView = new CrearFacturaView(this);
        initialize();
    }
    
    public JPanel getView() {
        return this.facturaView;
    }

    private void initialize() {
        jPanel = facturaView.jPanel1;
        numeroFraTxtField = facturaView.numeroFraTxtField;
        defaultBackgroundTxtBoxColor = numeroFraTxtField.getBackground();
        numeroFraTxtField.setText(getNextDefaultNumFactura());
        fechaEmisionTxtField = facturaView.fechaEmisionTxtField;
        String year = String.format("%04d", LocalDateTime.now().getYear());
        String month = String.format("%02d", LocalDateTime.now().getMonthValue());
        String day = String.format("%02d", LocalDateTime.now().getDayOfMonth());
        fechaEmisionTxtField.setText(day + "-" + month + "-" + year);
        diasParaPagoTxtField = facturaView.diasParaPagoTxtField;
        fechaVencimientoTxtField = facturaView.fechaVencimientoTxtField;
        formaPagoComboBox = facturaView.formaPagoComboBox;

        clienteInfoTxtFieldPanel = facturaView.clienteInfoTxtFieldPanel;
        nombreClienteSearchBtn = facturaView.nombreClienteSearchBtn;
        nombreClienteSearchBtn.setIcon(FrameUtils.SEARCH_FLATSVGICON);
        numeroClienteTxtField = facturaView.numeroClienteTxtField;
        nombreClienteTxtField = facturaView.nombreClienteTxtField;
        clienteBackgroundTxtBoxColor = nombreClienteTxtField.getBackground();
        nifClienteTxtField = facturaView.nifClienteTxtField;
        direccionClienteTxtField = facturaView.direccionClienteTxtField;
        codigoPostalClienteTxtField = facturaView.codigoPostalClienteTxtField;

        msgLbl = facturaView.msgLbl;
        setStandbyStatus();
        msgLbl.getIcon();

        concepto1TxtField = facturaView.concepto1TxtField;
        idiomaOrigen1TxtField = facturaView.idiomaOrigen1TxtField;
        idiomaDestino1TxtField = facturaView.idiomaDestino1TxtField;
        cantidad1TxtField = facturaView.cantidad1TxtField;
        precio1TxtField = facturaView.precio1TxtField;
        item1ComboBox = facturaView.item1ComboBox;
        totalImporte1TxtField = facturaView.totalImporte1TxtField;

        concepto2TxtField = facturaView.concepto2TxtField;
        idiomaOrigen2TxtField = facturaView.idiomaOrigen2TxtField;
        idiomaDestino2TxtField = facturaView.idiomaDestino2TxtField;
        cantidad2TxtField = facturaView.cantidad2TxtField;
        precio2TxtField = facturaView.precio2TxtField;
        item2ComboBox = facturaView.item2ComboBox;
        totalImporte2TxtField = facturaView.totalImporte2TxtField;

        concepto3TxtField = facturaView.concepto3TxtField;
        idiomaOrigen3TxtField = facturaView.idiomaOrigen3TxtField;
        idiomaDestino3TxtField = facturaView.idiomaDestino3TxtField;
        cantidad3TxtField = facturaView.cantidad3TxtField;
        precio3TxtField = facturaView.precio3TxtField;
        item3ComboBox = facturaView.item3ComboBox;
        totalImporte3TxtField = facturaView.totalImporte3TxtField;

        concepto4TxtField = facturaView.concepto4TxtField;
        idiomaOrigen4TxtField = facturaView.idiomaOrigen4TxtField;
        idiomaDestino4TxtField = facturaView.idiomaDestino4TxtField;
        cantidad4TxtField = facturaView.cantidad4TxtField;
        precio4TxtField = facturaView.precio4TxtField;
        item4ComboBox = facturaView.item4ComboBox;
        totalImporte4TxtField = facturaView.totalImporte4TxtField;

        verificarFichaBtn = facturaView.verificarFichaBtn;
        freeStateBtnColor = verificarFichaBtn.getBackground();
        previewFacturaBtn = facturaView.previewFacturaBtn;
        registrarFacturaBtn = facturaView.registrarFacturaBtn;

        setDefaultBackgroundToAllComponents(jPanel);
    }

    public void setDefaultBackgroundToAllComponents(Component comp) {
        if (comp instanceof JPanel) {
            for (Component newComp : ((JPanel) comp).getComponents()) {
                switch (newComp) {
//                    case JTextField txtField:
//                        txtField.setBackground(defaultBackgroundTxtBoxColor);
//                        break;
                    case JPanel jPanel:
                        if (jPanel != clienteInfoTxtFieldPanel) {
                            setDefaultBackgroundToAllComponents(jPanel);
                        } else {
                            setDefaultBackground(numeroClienteTxtField);
                        }
                        break;
                    default:
                        newComp.setBackground(defaultBackgroundTxtBoxColor);
                        break;
                }
            }
        } else {
            comp.setBackground(defaultBackgroundTxtBoxColor);
        }
    }

    public void calculaFechaVencimiento(javax.swing.JTextField fechaEmision, javax.swing.JTextField diasSumar, javax.swing.JTextField fechaVencimiento) {
        if (!fechaEmision.getText().isBlank()) {
            try {
                Date fechaEmisionDate = utils.convertStringToDate(fechaEmision.getText());
                int dias = Integer.valueOf(diasParaPagoTxtField.getText());
                Date date = Date.valueOf(fechaEmisionDate.toLocalDate().plusDays(dias));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                fechaVencimiento.setText(String.format("%02d", day) + "-" + String.format("%02d", month) + "-" + String.format("%04d", year));
            } catch (NumberFormatException e) {
                fechaVencimiento.setText("");
            }
        }
    }

    public void actualizaStatusFicha(int status) {
        this.fichaEsCorrecta = status;
        switch (this.fichaEsCorrecta) {
            case -1:
                setKOStatus();
                break;
            case 0:
                setStandbyStatus();
                break;
            case 1:
                setOKStatus();
                break;
        }
    }
    
    public void reiniciarView() {
        numeroFraTxtField.setText(getNextDefaultNumFactura());
        String year = String.format("%04d", LocalDateTime.now().getYear());
        String month = String.format("%02d", LocalDateTime.now().getMonthValue());
        String day = String.format("%02d", LocalDateTime.now().getDayOfMonth());
        fechaEmisionTxtField.setText(day + "-" + month + "-" + year);
        diasParaPagoTxtField.setText("");
        fechaVencimientoTxtField.setText("");
        formaPagoComboBox.setSelectedIndex(0);

        nombreClienteSearchBtn.setEnabled(false);
        numeroClienteTxtField.setText("");
        nombreClienteTxtField.setText("");
        nifClienteTxtField.setText("");
        direccionClienteTxtField.setText("");
        codigoPostalClienteTxtField.setText("");

        concepto1TxtField.setText("");
        idiomaOrigen1TxtField.setText("");
        idiomaDestino1TxtField.setText("");
        cantidad1TxtField.setText("");
        precio1TxtField.setText("");
        item1ComboBox.setSelectedIndex(0);
        totalImporte1TxtField.setText("");

        concepto2TxtField.setText("");
        idiomaOrigen2TxtField.setText("");
        idiomaDestino2TxtField.setText("");
        cantidad2TxtField.setText("");
        precio2TxtField.setText("");
        item2ComboBox.setSelectedIndex(0);
        totalImporte2TxtField.setText("");

        concepto3TxtField.setText("");
        idiomaOrigen3TxtField.setText("");
        idiomaDestino3TxtField.setText("");
        cantidad3TxtField.setText("");
        precio3TxtField.setText("");
        item3ComboBox.setSelectedIndex(0);
        totalImporte3TxtField.setText("");

        concepto4TxtField.setText("");
        idiomaOrigen4TxtField.setText("");
        idiomaDestino4TxtField.setText("");
        cantidad4TxtField.setText("");
        precio4TxtField.setText("");
        item4ComboBox.setSelectedIndex(0);
        totalImporte4TxtField.setText("");
        
        actualizaStatusFicha(0);
        enableAllEditables();
        setDefaultBackgroundToAllComponents(jPanel);        
    }

    public void disableAllEditables() {
        numeroFraTxtField.setEditable(false);
        fechaEmisionTxtField.setEditable(false);
        diasParaPagoTxtField.setEditable(false);
        formaPagoComboBox.setEnabled(false);

        nombreClienteSearchBtn.setEnabled(false);
        numeroClienteTxtField.setEditable(false);
        nombreClienteTxtField.setEditable(false);
        nifClienteTxtField.setEditable(false);
        direccionClienteTxtField.setEditable(false);
        codigoPostalClienteTxtField.setEditable(false);

        concepto1TxtField.setEditable(false);
        idiomaOrigen1TxtField.setEditable(false);
        idiomaDestino1TxtField.setEditable(false);
        cantidad1TxtField.setEditable(false);
        precio1TxtField.setEditable(false);
        item1ComboBox.setEnabled(false);

        concepto2TxtField.setEditable(false);
        idiomaOrigen2TxtField.setEditable(false);
        idiomaDestino2TxtField.setEditable(false);
        cantidad2TxtField.setEditable(false);
        precio2TxtField.setEditable(false);
        item2ComboBox.setEnabled(false);

        concepto3TxtField.setEditable(false);
        idiomaOrigen3TxtField.setEditable(false);
        idiomaDestino3TxtField.setEditable(false);
        cantidad3TxtField.setEditable(false);
        precio3TxtField.setEditable(false);
        item3ComboBox.setEnabled(false);

        concepto4TxtField.setEditable(false);
        idiomaOrigen4TxtField.setEditable(false);
        idiomaDestino4TxtField.setEditable(false);
        cantidad4TxtField.setEditable(false);
        precio4TxtField.setEditable(false);
        item4ComboBox.setEnabled(false);

        previewFacturaBtn.setEnabled(true);
        registrarFacturaBtn.setEnabled(true);
    }

    public void enableAllEditables() {
        numeroFraTxtField.setEditable(true);
        fechaEmisionTxtField.setEditable(true);
        diasParaPagoTxtField.setEditable(true);
        formaPagoComboBox.setEnabled(true);

        nombreClienteSearchBtn.setEnabled(true);
        numeroClienteTxtField.setEditable(true);

        concepto1TxtField.setEditable(true);
        idiomaOrigen1TxtField.setEditable(true);
        idiomaDestino1TxtField.setEditable(true);
        cantidad1TxtField.setEditable(true);
        precio1TxtField.setEditable(true);
        item1ComboBox.setEnabled(true);

        concepto2TxtField.setEditable(true);
        idiomaOrigen2TxtField.setEditable(true);
        idiomaDestino2TxtField.setEditable(true);
        cantidad2TxtField.setEditable(true);
        precio2TxtField.setEditable(true);
        item2ComboBox.setEnabled(true);

        concepto3TxtField.setEditable(true);
        idiomaOrigen3TxtField.setEditable(true);
        idiomaDestino3TxtField.setEditable(true);
        cantidad3TxtField.setEditable(true);
        precio3TxtField.setEditable(true);
        item3ComboBox.setEnabled(true);

        concepto4TxtField.setEditable(true);
        idiomaOrigen4TxtField.setEditable(true);
        idiomaDestino4TxtField.setEditable(true);
        cantidad4TxtField.setEditable(true);
        precio4TxtField.setEditable(true);
        item4ComboBox.setEnabled(true);

        previewFacturaBtn.setEnabled(false);
        registrarFacturaBtn.setEnabled(false);
    }

    public Factura extraeFactura() {
        String numFra = numeroFraTxtField.getText();
        Date fechaEmision = utils.convertStringToDate(fechaEmisionTxtField.getText());
        int diasPago = Integer.valueOf(diasParaPagoTxtField.getText());
        String formaPago = formaPagoComboBox.getSelectedItem().toString();
        Cliente cliente = JSONUtils.getClienteById(Integer.parseInt(numeroClienteTxtField.getText()));
        Emisor emisor = JSONUtils.findEmisorGuardado();
        Servicio servicio1 = null, servicio2 = null, servicio3 = null, servicio4 = null;
        int servicioCount = 0;
        if (!concepto1TxtField.getText().isBlank()) {
            servicio1 = new Servicio(idiomaOrigen1TxtField.getText(), idiomaDestino1TxtField.getText(), concepto1TxtField.getText(), item1ComboBox.getSelectedItem().toString(), utils.checkIfDecimalAndReturnDotDouble(precio1TxtField.getText()), utils.checkIfDecimalAndReturnDotDouble(cantidad1TxtField.getText()));
            servicioCount++;
        }
        if (!concepto2TxtField.getText().isBlank()) {
            servicio2 = new Servicio(idiomaOrigen2TxtField.getText(), idiomaDestino2TxtField.getText(), concepto2TxtField.getText(), item2ComboBox.getSelectedItem().toString(), utils.checkIfDecimalAndReturnDotDouble(precio2TxtField.getText()), utils.checkIfDecimalAndReturnDotDouble(cantidad2TxtField.getText()));
            servicioCount++;
        }
        if (!concepto3TxtField.getText().isBlank()) {
            servicio3 = new Servicio(idiomaOrigen3TxtField.getText(), idiomaDestino3TxtField.getText(), concepto3TxtField.getText(), item3ComboBox.getSelectedItem().toString(), utils.checkIfDecimalAndReturnDotDouble(precio3TxtField.getText()), utils.checkIfDecimalAndReturnDotDouble(cantidad3TxtField.getText()));
            servicioCount++;
        }
        if (!concepto4TxtField.getText().isBlank()) {
            servicio4 = new Servicio(idiomaOrigen4TxtField.getText(), idiomaDestino4TxtField.getText(), concepto4TxtField.getText(), item4ComboBox.getSelectedItem().toString(), utils.checkIfDecimalAndReturnDotDouble(precio4TxtField.getText()), utils.checkIfDecimalAndReturnDotDouble(cantidad4TxtField.getText()));
            servicioCount++;
        }
        Servicio[] serviciosAux = {servicio1, servicio2, servicio3, servicio4};
        Servicio[] servicios = new Servicio[servicioCount];
        int indexServ = 0;
        for (int i = 0; i < servicios.length; i++) {
            if (serviciosAux[i] != null) {
                servicios[indexServ] = serviciosAux[i];
                indexServ++;
            }
        }
        return new Factura(numFra, fechaEmision, diasPago, formaPago, cliente, emisor, servicios, false);
    }

    public File createTempPdf(Factura factura) {
        try {
            File file = File.createTempFile("invoice_" + factura.getNumFactura(), ".pdf");
            PDFGenerator pdfGen = new PDFGenerator(file.getName());
            PDDocument pdDoc = pdfGen.generaPDDocumentFactura(factura, file);
            pdDoc.save(file);
            return file;
        } catch (IOException ex) {
            Logger.getLogger(CrearFacturaController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public File guardaFacturaPdf(Factura factura) {
        File file = new File(PDFGenerator.INVOICES_DIRECTORY + factura.getNumFactura() + ".pdf");
        PDFGenerator pdfGen = new PDFGenerator(file);
        PDDocument pdDoc = pdfGen.generaPDDocumentFactura(factura, file);
        try {
            pdDoc.save(file);
            return file;
        } catch (IOException ex) {
            ex.getStackTrace();
            return null;
        }
    }

    public void openFile(File file) {
        try {
            if (file == null) {
                throw new NullPointerException();
            }
            Desktop.getDesktop().open(file);
        } catch (IOException ex1) {
            Logger.getLogger(CrearFacturaController.class.getName()).log(Level.SEVERE, null, ex1);
            FrameUtils.showErrorMessage("ERROR", ex1 + ": La factura " + file.getName() + " no se ha podido encontrar.");
        } catch (NullPointerException ex2) {
            Logger.getLogger(CrearFacturaController.class.getName()).log(Level.SEVERE, null, ex2);
            FrameUtils.showErrorMessage("ERROR", ex2 + ": La factura no se ha podido encontrar.");
        }
    }
    
    public void previewFacturaBtnPulsado() {
        if (JSONUtils.findEmisorGuardado() != null) {
            Factura factura = extraeFactura();
            File pdfFile = createTempPdf(factura);
            openFile(pdfFile);
        } else {
            FrameUtils.showErrorMessage("Error al generar factura", ERROR_AHO_GENERANDO_FACTURA_NO_EMISOR);
        }
    }

    public boolean verificaEntradaDatos() {
        boolean isCorrect = true;

        //NUM FACTURA
        if (numeroFraTxtField.getText().isBlank()) {
            isCorrect = false;
            setErrorBackground(numeroFraTxtField);
        } else {
            setDefaultBackground(numeroFraTxtField);
        }

        //FECHA EMISIÓN
        if (fechaEmisionTxtField.getText().isBlank()) {
            isCorrect = false;
            setErrorBackground(fechaEmisionTxtField);
        } else {
            if (utils.convertStringToDate(fechaEmisionTxtField.getText()) != null) {
                setDefaultBackground(fechaEmisionTxtField);
            } else {
                isCorrect = false;
                setErrorBackground(fechaEmisionTxtField);
            }
        }

        //DÍAS DE PAGO
        if (diasParaPagoTxtField.getText().isBlank()) {
            isCorrect = false;
            setErrorBackground(diasParaPagoTxtField);
        } else {
            boolean foundNotDigit = false;
            for (char c : diasParaPagoTxtField.getText().toCharArray()) {
                if (!Character.isDigit(c)) {
                    foundNotDigit = true;
                    setErrorBackground(diasParaPagoTxtField);
                    break;
                }
            }
            if (!foundNotDigit) {
                setDefaultBackground(diasParaPagoTxtField);
            } else {
                isCorrect = false;
            }
        }

        //NO ES NECESARIO VERIFICAR LA FECHA VENCIMIENTO, PUES DEPENDE DE "DÍAS DE PAGO", QUE SÍ SE VERIFICA

        //FORMA DE PAGO
        if (formaPagoComboBox.getSelectedItem().equals("")) {
            isCorrect = false;
            setErrorBackground(formaPagoComboBox);
        } else {
            setDisabledBackground(formaPagoComboBox);
        } 
        
        //ID CLIENTE
        if (numeroClienteTxtField.getText().isBlank()) {
            isCorrect = false;
            setErrorBackground(numeroClienteTxtField);
        } else {
            setDefaultBackground(numeroClienteTxtField);
        }

        //NOMBRE CLIENTE
        if (nombreClienteTxtField.getText().isBlank()) {
            isCorrect = false;
            setErrorBackground(numeroClienteTxtField);
        } else {
            setDefaultBackground(numeroClienteTxtField);
        }

        //NIF
        if (nifClienteTxtField.getText().isBlank()) {
            isCorrect = false;
            setErrorBackground(numeroClienteTxtField);
        } else {
            setDefaultBackground(numeroClienteTxtField);
            nifClienteTxtField.setText(nifClienteTxtField.getText().toUpperCase());
        }

        //Dirección es OPCIONAL y viene dado por la base de datos.

        //Código postal es OPCIONAL y viene dado por la base de datos.

        //Servicios
        javax.swing.JComponent[][] serviciosTextField = {
            {concepto1TxtField, idiomaOrigen1TxtField, idiomaDestino1TxtField, cantidad1TxtField, precio1TxtField, item1ComboBox, totalImporte1TxtField},
            {concepto2TxtField, idiomaOrigen2TxtField, idiomaDestino2TxtField, cantidad2TxtField, precio2TxtField, item2ComboBox, totalImporte2TxtField},
            {concepto3TxtField, idiomaOrigen3TxtField, idiomaDestino3TxtField, cantidad3TxtField, precio3TxtField, item3ComboBox, totalImporte3TxtField},
            {concepto4TxtField, idiomaOrigen4TxtField, idiomaDestino4TxtField, cantidad4TxtField, precio4TxtField, item4ComboBox, totalImporte4TxtField}
        };

        for (javax.swing.JComponent[] fila : serviciosTextField) {
            if (!((javax.swing.JTextField) fila[0]).getText().isBlank() || !((javax.swing.JTextField) fila[1]).getText().isBlank() || !((javax.swing.JTextField) fila[2]).getText().isBlank() || !((javax.swing.JTextField) fila[3]).getText().isBlank() || !((javax.swing.JTextField) fila[4]).getText().isBlank() || !((javax.swing.JComboBox) fila[5]).getSelectedItem().equals("") || !((javax.swing.JTextField) fila[6]).getText().isBlank()) {
                if (((javax.swing.JTextField) fila[0]).getText().isBlank()) {
                    isCorrect = false;
                    setErrorBackground(fila[0]);
                } else {
                    setDefaultBackground(fila[0]);
                }
                if (((javax.swing.JTextField) fila[1]).getText().isBlank()) {
                    isCorrect = false;
                    setErrorBackground(fila[1]);
                } else {
                    setDefaultBackground(fila[1]);
                }
                if (((javax.swing.JTextField) fila[2]).getText().isBlank()) {
                    isCorrect = false;
                    setErrorBackground(fila[2]);
                } else {
                    setDefaultBackground(fila[2]);
                }
                if (((javax.swing.JTextField) fila[3]).getText().isBlank() || !utils.isParseableToDouble(((javax.swing.JTextField) fila[3]).getText().replace(",", "."))) {
                    isCorrect = false;
                    setErrorBackground(fila[3]);
                } else {
                    setDefaultBackground(fila[3]);
                }
                if (((javax.swing.JTextField) fila[4]).getText().isBlank() || !utils.isParseableToDouble(((javax.swing.JTextField) fila[4]).getText().replace(",", "."))) {
                    isCorrect = false;
                    setErrorBackground(fila[4]);
                } else {
                    setDefaultBackground(fila[4]);
                }
                if (((javax.swing.JComboBox) fila[5]).getSelectedItem().equals("")) {
                    isCorrect = false;
                    setErrorBackground(fila[5]);
                } else {
                    setDefaultBackground(fila[5]);
                }
            } else {
                setDefaultBackground(fila[0]);
                setDefaultBackground(fila[1]);
                setDefaultBackground(fila[2]);
                setDefaultBackground(fila[3]);
                setDefaultBackground(fila[4]);
            }
        }
        // Si va a devolver que es válido, informamos en caso de que se haya seleccionado 'Transf. bancaria' y no esté informado en el emisor.
        if (isCorrect) {
            if (JSONUtils.findEmisorGuardado() == null) {
                FrameUtils.showInfoMessage("Aviso", ERROR_FUT_GENERANDO_FACTURA_NO_EMISOR);
            } else {
                if (formaPagoComboBox.getSelectedItem() == FORMAS_PAGO[1] && (JSONUtils.findEmisorGuardado().getIban() == null || JSONUtils.findEmisorGuardado().getIban().isBlank())) {
                    FrameUtils.showInfoMessage("Aviso", ERROR_EMISOR_NO_IBAN);
                }
            }

        }
        return isCorrect;
    }

    public void setOKStatus() {
        setSVGIcon(msgLbl, FrameUtils.OK_FLATSVGICON);
    }

    public void setKOStatus() {
        setSVGIcon(msgLbl, FrameUtils.KO_FLATSVGICON);
    }

    public void setStandbyStatus() {
        setSVGIcon(msgLbl, FrameUtils.STANDBY_FLATSVGICON);
    }

    public void setDisabledBackground(JComponent comp) {
        ((JComponent) comp).setBackground(new Color(238, 238, 238));
    }

    public void setErrorBackground(JComponent comp) {
        ((JComponent) comp).setBackground(ERROR_BG_COLOR);
    }

    public void setDefaultBackground(JComponent comp) {
        ((JComponent) comp).setBackground(defaultBackgroundTxtBoxColor);
    }

    public void setSVGIcon(javax.swing.JLabel label, FlatSVGIcon icon) {
        label.setIcon(icon);
    }

    public void setSVGIcon(javax.swing.JButton button, File icon) {
        button.setIcon(new FlatSVGIcon(icon));
    }

    public void calculaPrecioServicio(javax.swing.JTextField txt1, javax.swing.JTextField txt2, javax.swing.JTextField txtTotal) {
        try {
            if (!txt1.getText().isBlank()) {
                Double d1 = Double.valueOf(txt1.getText().replace(",", "."));
                if (!txt2.getText().isBlank()) {
                    double d2 = 0f;
                    try {
                        d2 = Double.valueOf(txt2.getText().replace(",", "."));
                        txtTotal.setText(utils.formatDecimalNumberToStringAlways(d1 * d2, 2));
                    } catch (NumberFormatException e) {
                        txtTotal.setText("");
                    }
                    setDefaultBackground(txt1);
                    setDefaultBackground(txt2);
                }
            } else { //Is empty
                setDefaultBackground(txt1);
                txtTotal.setText("");
            }
        } catch (NumberFormatException e) {
            setErrorBackground(txt1);
            txtTotal.setText("");
        }
    }

    public void registraFactura() {
        // Comprobamos que el emisor está informado para poder hacer la factura.
        Emisor emisor = JSONUtils.findEmisorGuardado();
        if (emisor != null) {
            Factura factura = extraeFactura();
            // Ahora comprobamos que tenga informado IBAN si la forma de pago es 'Transferencia bancaria'.
            if (factura.getFormaPago().equals(FORMAS_PAGO[1]) && (emisor.getIban() == null || emisor.getIban().isBlank())) {
                FrameUtils.showErrorMessage("Error al generar factura", ERROR_EMISOR_NO_IBAN);
            } else {
                // Si la factura existe en la BD, sigue con el registro. De lo contrario muestra el error.
                if (!JSONUtils.isNumFacturaExistente(factura.getNumFactura())) {
                    File pdfFile = guardaFacturaPdf(factura);
                    factura.setRelativePathFactura(pdfFile);
                    JSONUtils.saveFactura(factura);
                } else {
                    LocalDateTime ts = JSONUtils.findFactura(factura.getNumFactura()).getFechaUltActualizacion().toLocalDateTime();
                    String day = String.format("%02d", ts.getDayOfMonth());
                    String month = String.format("%02d", ts.getMonthValue());
                    String year = String.format("%04d", ts.getYear());
                    String hour = String.format("%02d", ts.getHour());
                    String minute = String.format("%02d", ts.getMinute());
                    FrameUtils.showErrorMessage("Error", "La factura " + factura.getNumFactura() + " ya fue registrada el " + day + "-" + month + "-" + year + " a las " + hour + ":" + minute + "h.");
                }
            }
        } else {
            FrameUtils.showErrorMessage("Error al generar factura", ERROR_AHO_GENERANDO_FACTURA_NO_EMISOR);
        }
    }

    public void descargaPDF(Factura factura) {
        File file = null;
        if (JSONUtils.isNumFacturaExistente(factura.getNumFactura())) {
            file = JSONUtils.findFacturaByNumFactura(factura.getNumFactura()).getPathFactura();
        }
        openFile(file);

    }

    public void gestionaToggleButtonVerificarDatos() {
        // Si está verificando los datos introducidos...
        if (fichaEsCorrecta == 0) {
            boolean verificacion = verificaEntradaDatos();
            verificarFichaBtn.setBackground(BLOCKED_STATE_BTN_COLOR);
            fichaEsCorrecta = verificacion == true ? 1 : -1;
            if (fichaEsCorrecta == 1) {
                actualizaStatusFicha(1);
                disableAllEditables();
            } else if (fichaEsCorrecta == -1) {
                actualizaStatusFicha(-1);
            }
        } else { // Está retrocediendo la verificación para modificar datos.
            actualizaStatusFicha(0);
            enableAllEditables();
            setDefaultBackgroundToAllComponents(jPanel);
        }
    }
    
    public void setCamposCliente(String nif, String nombre, String direccion, String codigoPostal) {
        nifClienteTxtField.setText(nif);
        nombreClienteTxtField.setText(nombre);
        direccionClienteTxtField.setText(direccion);
        codigoPostalClienteTxtField.setText(codigoPostal);
    }

    public void cargaDatosDeNumeroCliente() {
        try {
        Cliente cliente = JSONUtils.getClienteById(Integer.parseInt(numeroClienteTxtField.getText()));
            if (cliente != null) {
                if (cliente.isActivado()) {
                    if (cliente != null && cliente.getNif() != null) {
                        setCamposCliente(cliente.getNif(), cliente.getNombre(), cliente.getDireccion(), cliente.getCodigoPostal());
                    } else {
                        FrameUtils.showErrorMessage("Error", "El numero de cliente '" + numeroClienteTxtField.getText() + "' no existe.");

                    }
                } else {
                    FrameUtils.showErrorMessage("Cliente desactivado", "El cliente no se puede utilizar porque está desactivado. Para activarlo, se ha de modificar desde '" + ModificarClienteController.VIEW_NAME + "'.");
                    clearDatosCliente();
                }
            } else {
                FrameUtils.showErrorMessage("Cliente no encontrado", "El cliente '" + numeroClienteTxtField.getText() + "' no se ha encontrado en la base de datos.");
                clearDatosCliente();
            }
        } catch (NumberFormatException ex) {
            ex.getStackTrace();
            return;
        }
    }

    private String getNextDefaultNumFactura() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-");
        LocalDateTime now = LocalDateTime.now();
        String numFactura = dtf.format(now);
        int n = 1;
        while (true) {
            String numFacturaAux = numFactura + String.format("%03d", n);
            if (!JSONUtils.isNumFacturaExistente(numFacturaAux)) {
                return numFacturaAux;
            }
            n++;
        }
    }

    public void clearDatosCliente() {
        numeroClienteTxtField.setText("");
        nifClienteTxtField.setText("");
        nombreClienteTxtField.setText("");
        direccionClienteTxtField.setText("");
        codigoPostalClienteTxtField.setText("");
    }

    @Override
    public void recibeClienteLookup(String id) {
        clearDatosCliente();
        numeroClienteTxtField.setText(String.valueOf(id));
        cargaDatosDeNumeroCliente();
    }

    public void abrirClienteLookupFrame() {
        BuscarClienteController lookupController = new BuscarClienteController(this, true);
    }
    
    @Override
    public String getViewName() {
        if (this.finalViewName != null) {
            return finalViewName;
        } else {
            return this.VIEW_NAME;
        }
    }

    @Override
    public void setViewName(String str) {
        finalViewName = str;
    }
}
