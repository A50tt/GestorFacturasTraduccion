package jud.gestorfacturas.gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.manager.PDFGenerator;
import jud.gestorfacturas.manager.Utils;
import jud.gestorfacturas.model.Cliente;
import jud.gestorfacturas.model.Emisor;
import jud.gestorfacturas.model.Factura;
import jud.gestorfacturas.model.Servicio;

public class CreateInvoiceController {

    Utils utils = new Utils();
    private String RESOURCE_DIRECTORY = System.getProperty("user.dir") + "\\src\\main\\resources\\";
    private String IMAGES_DIRECTORY = RESOURCE_DIRECTORY + "img\\";
    Color DEFAULT_BG_COLOR = Color.white;
    Color ERROR_BG_COLOR = Color.red;
    String[] TIPOS_UNIDAD = {"", "Clip", "Hora", "Minuto", "Palabra"};
    String[] FORMAS_PAGO = {"", "Transferencia Bancaria", "Cheque"};

    private CreateInvoiceView view;
    private JPanel jPanel;
    private JTextField numeroFraTxtField;
    private JTextField fechaEmisionTxtField;
    private JTextField diasParaPagoTxtField;
    private JTextField fechaVencimientoTxtField;
    private JComboBox formaPagoComboBox;

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

    private JToggleButton verificarFichaBtn;
    private JButton previewFacturaBtn;
    private JButton registrarFacturaBtn;

    public CreateInvoiceController() {
        view = new CreateInvoiceView(this);
        initialize();
        view.setVisible(true);
    }

    private void initialize() {
        jPanel = view.jPanel;

        numeroFraTxtField = view.numeroFraTxtField;
        fechaEmisionTxtField = view.fechaEmisionTxtField;
        diasParaPagoTxtField = view.diasParaPagoTxtField;
        fechaVencimientoTxtField = view.fechaVencimientoTxtField;
        formaPagoComboBox = view.formaPagoComboBox;

        nombreClienteSearchBtn = view.nombreClienteSearchBtn;
        setSVGIcon(nombreClienteSearchBtn, new File("src\\main\\resources\\img\\busqueda_black_icon.svg"));
        numeroClienteTxtField = view.numeroClienteTxtField;
        nombreClienteTxtField = view.nombreClienteTxtField;
        nifClienteTxtField = view.nifClienteTxtField;
        direccionClienteTxtField = view.direccionClienteTxtField;
        codigoPostalClienteTxtField = view.codigoPostalClienteTxtField;

        msgLbl = view.msgLbl;
        setSVGIcon(msgLbl, new File("src\\main\\resources\\img\\standby_icon.svg"));
        msgLbl.setOpaque(false);

        concepto1TxtField = view.concepto1TxtField;
        idiomaOrigen1TxtField = view.idiomaOrigen1TxtField;
        idiomaDestino1TxtField = view.idiomaDestino1TxtField;
        cantidad1TxtField = view.cantidad1TxtField;
        precio1TxtField = view.precio1TxtField;
        item1ComboBox = view.item1ComboBox;
        totalImporte1TxtField = view.totalImporte1TxtField;

        concepto2TxtField = view.concepto2TxtField;
        idiomaOrigen2TxtField = view.idiomaOrigen2TxtField;
        idiomaDestino2TxtField = view.idiomaDestino2TxtField;
        cantidad2TxtField = view.cantidad2TxtField;
        precio2TxtField = view.precio2TxtField;
        item2ComboBox = view.item2ComboBox;
        totalImporte2TxtField = view.totalImporte2TxtField;

        concepto3TxtField = view.concepto3TxtField;
        idiomaOrigen3TxtField = view.idiomaOrigen3TxtField;
        idiomaDestino3TxtField = view.idiomaDestino3TxtField;
        cantidad3TxtField = view.cantidad3TxtField;
        precio3TxtField = view.precio3TxtField;
        item3ComboBox = view.item3ComboBox;
        totalImporte3TxtField = view.totalImporte3TxtField;

        concepto4TxtField = view.concepto4TxtField;
        idiomaOrigen4TxtField = view.idiomaOrigen4TxtField;
        idiomaDestino4TxtField = view.idiomaDestino4TxtField;
        cantidad4TxtField = view.cantidad4TxtField;
        precio4TxtField = view.precio4TxtField;
        item4ComboBox = view.item4ComboBox;
        totalImporte4TxtField = view.totalImporte4TxtField;

        verificarFichaBtn = view.verificarFichaBtn;
        previewFacturaBtn = view.previewFacturaBtn;
        registrarFacturaBtn = view.registrarFacturaBtn;

        setBackgroundColorToAllComponents(jPanel, DEFAULT_BG_COLOR);
    }

    protected void setBackgroundColorToAllComponents(Component comp, Color color) {
        if (comp instanceof JPanel) {
            for (Component newComp : ((JPanel) comp).getComponents()) {
                setBackgroundColorToAllComponents(newComp, color);
            }
        } else {
            comp.setBackground(color);
        }
    }

    protected void calculaFechaVencimiento(javax.swing.JTextField fechaEmision, javax.swing.JTextField diasSumar, javax.swing.JTextField fechaVencimiento) {
        if (!fechaEmision.getText().isEmpty()) {
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

    //Este método no está actualmente en uso.
    //Sirve para actualizar el campo de texto Concepto en caso de que se quiere verificar el formate dado el evento KeyRelease
    protected void verificaConceptosServicio(javax.swing.JTextField concepto, javax.swing.JTextField cantidad, javax.swing.JTextField precio) {
        if (concepto.getText().isEmpty() && cantidad.getText().isEmpty() && precio.getText().isEmpty()) {
            setDefaultBackground(concepto);
        } else if (concepto.getText().isEmpty() && (cantidad.getText().isEmpty() || precio.getText().isEmpty())) {
            setErrorBackground(concepto);
        } else {
            setDefaultBackground(concepto);
        }
    }

    protected void disableAllEditables() {
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

    protected void enableAllEditables() {
        numeroFraTxtField.setEditable(true);
        fechaEmisionTxtField.setEditable(true);
        diasParaPagoTxtField.setEditable(true);
        formaPagoComboBox.setEnabled(true);

        nombreClienteSearchBtn.setEnabled(true);
        numeroClienteTxtField.setEditable(true);
        nombreClienteTxtField.setEditable(true);
        nifClienteTxtField.setEditable(true);
        direccionClienteTxtField.setEditable(true);
        codigoPostalClienteTxtField.setEditable(true);

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

    protected Factura extraeDatosYGeneraFactura() {
        String numFra = numeroFraTxtField.getText();
        Date fechaEmision = utils.convertStringToDate(fechaEmisionTxtField.getText());
        int diasPago = Integer.valueOf(diasParaPagoTxtField.getText());
        String formaPago = formaPagoComboBox.getSelectedItem().toString();
        String nombreCliente = nombreClienteTxtField.getText();
        String direccionCliente = direccionClienteTxtField.getText();
        String codigoPostalCliente = codigoPostalClienteTxtField.getText();
        String nif = nifClienteTxtField.getText();
        Cliente cliente = new Cliente(nombreCliente, direccionCliente, codigoPostalCliente, nif);
        DBUtils dbUtils = new DBUtils();
        Emisor emisor = dbUtils.getUnicoEmisor();
        Servicio servicio1 = null, servicio2 = null, servicio3 = null, servicio4 = null;
        int servicioCount = 0;
        if (!concepto1TxtField.getText().isEmpty()) {
            servicio1 = new Servicio(idiomaOrigen1TxtField.getText(), idiomaDestino1TxtField.getText(), concepto1TxtField.getText(), item1ComboBox.getSelectedItem().toString(), utils.checkIfDecimalAndReturnDotDouble(precio1TxtField.getText()), utils.checkIfDecimalAndReturnDotDouble(cantidad1TxtField.getText()));
            servicioCount++;
        }
        if (!concepto2TxtField.getText().isEmpty()) {
            servicio2 = new Servicio(idiomaOrigen2TxtField.getText(), idiomaDestino2TxtField.getText(), concepto2TxtField.getText(), item2ComboBox.getSelectedItem().toString(), utils.checkIfDecimalAndReturnDotDouble(precio2TxtField.getText()), utils.checkIfDecimalAndReturnDotDouble(cantidad2TxtField.getText()));
            servicioCount++;
        }
        if (!concepto3TxtField.getText().isEmpty()) {
            servicio3 = new Servicio(idiomaOrigen3TxtField.getText(), idiomaDestino3TxtField.getText(), concepto3TxtField.getText(), item3ComboBox.getSelectedItem().toString(), utils.checkIfDecimalAndReturnDotDouble(precio3TxtField.getText()), utils.checkIfDecimalAndReturnDotDouble(cantidad3TxtField.getText()));
            servicioCount++;
        }
        if (!concepto4TxtField.getText().isEmpty()) {
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
        return new Factura(numFra, fechaEmision, diasPago, formaPago, cliente, emisor, servicios);
    }

    protected void previewPDF(Factura factura) {
        PDFGenerator pdfGen = new PDFGenerator("invoice_" + factura.getNumFactura() + ".pdf");
        try {
            File tempFile = File.createTempFile("invoice_", "_" + factura.getNumFactura() + ".pdf");
            tempFile.deleteOnExit();
            pdfGen.createPDF(factura, tempFile);
            Desktop.getDesktop().open(tempFile);
        } catch (IOException ex) {
            Logger.getLogger(CreateInvoiceView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected boolean verificaEntradaDatos() {
        boolean isCorrect = true;

        //NUM FACTURA
        if (numeroFraTxtField.getText().isEmpty()) {
            isCorrect = false;
            setErrorBackground(numeroFraTxtField);
        } else {
            setDefaultBackground(numeroFraTxtField);
        }

        //FECHA EMISIÓN
        if (fechaEmisionTxtField.getText().isEmpty()) {
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
        if (diasParaPagoTxtField.getText().isEmpty()) {
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

        //FECHA VENCIMIENTO
        if (fechaVencimientoTxtField.getText().isEmpty()) {
            isCorrect = false;
            setErrorBackground(fechaVencimientoTxtField);
        } else {
            setDisabledBackground(fechaVencimientoTxtField);
        }
        
        //FORMA DE PAGO
        if (formaPagoComboBox.getSelectedItem().equals("")) {
            isCorrect = false;
            setErrorBackground(formaPagoComboBox);
        } else {
            setDisabledBackground(formaPagoComboBox);
        }

        //NOMBRE CLIENTE
        if (nombreClienteTxtField.getText().isEmpty()) {
            isCorrect = false;
            setErrorBackground(nombreClienteTxtField);
        } else {
            setDefaultBackground(nombreClienteTxtField);
        }

        //NIF
        if (nifClienteTxtField.getText().isEmpty()) {
            isCorrect = false;
            setErrorBackground(nifClienteTxtField);
        } else {
            setDefaultBackground(nifClienteTxtField);
            nifClienteTxtField.setText(nifClienteTxtField.getText().toUpperCase());
        }

        //Dirección
        if (direccionClienteTxtField.getText().isEmpty()) {
            isCorrect = false;
            setErrorBackground(direccionClienteTxtField);
        } else {
            setDefaultBackground(direccionClienteTxtField);
        }

        //Código postal
        if (codigoPostalClienteTxtField.getText().isEmpty()) {
            isCorrect = false;
            setErrorBackground(codigoPostalClienteTxtField);
        } else {
            setDefaultBackground(codigoPostalClienteTxtField);
            codigoPostalClienteTxtField.setText(codigoPostalClienteTxtField.getText().toUpperCase());
        }

        //Servicios
        javax.swing.JComponent[][] serviciosTextField = {
            {concepto1TxtField, idiomaOrigen1TxtField, idiomaDestino1TxtField, cantidad1TxtField, precio1TxtField, item1ComboBox, totalImporte1TxtField},
            {concepto2TxtField, idiomaOrigen2TxtField, idiomaDestino2TxtField, cantidad2TxtField, precio2TxtField, item2ComboBox, totalImporte2TxtField},
            {concepto3TxtField, idiomaOrigen3TxtField, idiomaDestino3TxtField, cantidad3TxtField, precio3TxtField, item3ComboBox, totalImporte3TxtField},
            {concepto4TxtField, idiomaOrigen4TxtField, idiomaDestino4TxtField, cantidad4TxtField, precio4TxtField, item4ComboBox, totalImporte4TxtField}
        };

        for (javax.swing.JComponent[] fila : serviciosTextField) {
            if (!((javax.swing.JTextField) fila[0]).getText().isEmpty() || !((javax.swing.JTextField) fila[1]).getText().isEmpty() || !((javax.swing.JTextField) fila[2]).getText().isEmpty() || !((javax.swing.JTextField) fila[3]).getText().isEmpty() || !((javax.swing.JTextField) fila[4]).getText().isEmpty() || !((javax.swing.JComboBox) fila[5]).getSelectedItem().equals("") || !((javax.swing.JTextField) fila[6]).getText().isEmpty()) {
                if (((javax.swing.JTextField) fila[0]).getText().isEmpty()) {
                    isCorrect = false;
                    setErrorBackground(fila[0]);
                } else {
                    setDefaultBackground(fila[0]);
                }
                if (((javax.swing.JTextField) fila[1]).getText().isEmpty()) {
                    isCorrect = false;
                    setErrorBackground(fila[1]);
                } else {
                    setDefaultBackground(fila[1]);
                }
                if (((javax.swing.JTextField) fila[2]).getText().isEmpty()) {
                    isCorrect = false;
                    setErrorBackground(fila[2]);
                } else {
                    setDefaultBackground(fila[2]);
                }
                if (((javax.swing.JTextField) fila[3]).getText().isEmpty()) {
                    isCorrect = false;
                    setErrorBackground(fila[3]);
                } else {
                    setDefaultBackground(fila[3]);
                }
                if (((javax.swing.JTextField) fila[4]).getText().isEmpty()) {
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
                if (((javax.swing.JTextField) fila[6]).getText().isEmpty()) {
                    isCorrect = false;
                    setErrorBackground(fila[6]);
                } else {
                    setDefaultBackground(fila[6]);
                }
            } else {
                setDefaultBackground(fila[0]);
                setDefaultBackground(fila[1]);
                setDefaultBackground(fila[2]);
            }
        }
        return isCorrect;
    }

    protected void setOKStatus() {
        setSVGIcon(msgLbl, new File(IMAGES_DIRECTORY + "ok_status_icon.svg"));
    }

    protected void setKOStatus() {
        setSVGIcon(msgLbl, new File(IMAGES_DIRECTORY + "ko_status_icon.svg"));
    }

    protected void setStandbyStatus() {
        setSVGIcon(msgLbl, new File(IMAGES_DIRECTORY + "standby_icon.svg"));
    }

    protected void setDisabledBackground(JComponent comp) {
        ((JComponent) comp).setBackground(new Color(238, 238, 238));
    }

    protected void setErrorBackground(JComponent comp) {
        ((JComponent) comp).setBackground(Color.red);
    }

    protected void setDefaultBackground(JComponent comp) {
        ((JComponent) comp).setBackground(Color.white);
    }

    protected void setSVGIcon(javax.swing.JLabel label, File icon) {
        label.setIcon(new FlatSVGIcon(icon));
    }

    protected void setSVGIcon(javax.swing.JButton button, File icon) {
        button.setIcon(new FlatSVGIcon(icon));
    }

    protected void calculaPrecioServicio(javax.swing.JTextField txt1, javax.swing.JTextField txt2, javax.swing.JTextField txtTotal) {
        try {
            if (!txt1.getText().isEmpty()) {
                Double d1 = Double.valueOf(txt1.getText().replace(",", "."));
                if (!txt2.getText().isEmpty()) {
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
}
