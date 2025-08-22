package jud.gestorfacturas.gui.controller;

import interfaces.Controller;
import interfaces.DataListenerController;
import java.awt.Color;
import jud.gestorfacturas.gui.view.ModificarEmisorView;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.manager.FrameUtils;
import jud.gestorfacturas.model.Emisor;

public class ModificarEmisorController implements Controller, DataListenerController {

    ModificarEmisorView modificarEmisorView;
    Controller sourceController;
    DBUtils dbUtils = new DBUtils();

    Color DEFAULT_BG_COLOR = Color.white;
    Color ERROR_BG_COLOR = Color.red;

    private String viewName = "Datos personales";

    protected JButton actualizarBtn;
    protected JTextField codigoPostalTxtField;
    protected JTextField direccionTxtField;
    protected JTextField nifTxtField;
    protected JTextField nombreTxtField;
    protected JTextField nombreCompletoTxtField;
    protected JTextField ibanTxtField;

    public ModificarEmisorController() {
        modificarEmisorView = new ModificarEmisorView(this);
        initialize();
    }

    private void initialize() {
        nifTxtField = modificarEmisorView.nifTxtField;
        nombreTxtField = modificarEmisorView.nombreTxtField;
        nombreCompletoTxtField = modificarEmisorView.nombreCompletoTxtField;
        direccionTxtField = modificarEmisorView.direccionTxtField;
        codigoPostalTxtField = modificarEmisorView.codigoPostalTxtField;
        ibanTxtField = modificarEmisorView.ibanTxtField;
        cargaDatosEmisor();
    }

    public void cargaDatosEmisor() {
        DBUtils dbUtils = new DBUtils();
        Emisor emisor = dbUtils.getUnicoEmisor();
        if (emisor != null) {
            nifTxtField.setText(emisor.getNif());
            nombreTxtField.setText(emisor.getNombre());
            nombreCompletoTxtField.setText(emisor.getNombreCompleto());
            direccionTxtField.setText(emisor.getDireccion());
            codigoPostalTxtField.setText(emisor.getCodigoPostal());
            ibanTxtField.setText(emisor.getIban());
        } else {
            FrameUtils.showInfoMessage("Información", "No se han encontrado datos guardados.");
            nifTxtField.setText("");
            nombreTxtField.setText("");
            nombreCompletoTxtField.setText("");
            direccionTxtField.setText("");
            codigoPostalTxtField.setText("");
            ibanTxtField.setText("");
        }
    }

    public void actualizaEmisor() {
        Emisor emisorDB = dbUtils.getUnicoEmisor();
        if (emisorDB == null) {
            emisorDB = new Emisor();
        }

        if (!nifTxtField.getText().isBlank()
                && !nombreTxtField.getText().isBlank()
                && !nombreCompletoTxtField.getText().isBlank()) {

            emisorDB.setNif(nifTxtField.getText());
            emisorDB.setNombre(nombreTxtField.getText());
            emisorDB.setNombreCompleto(nombreCompletoTxtField.getText());

            nifTxtField.setBackground(DEFAULT_BG_COLOR);
            nombreTxtField.setBackground(DEFAULT_BG_COLOR);
            nombreCompletoTxtField.setBackground(DEFAULT_BG_COLOR);

            if (direccionTxtField.getText().isBlank() || codigoPostalTxtField.getText().isBlank() || codigoPostalTxtField.getText().isBlank() || ibanTxtField.getText().isBlank()) {
                int input = FrameUtils.showQuestionBox("Campos incompletos", "Faltan campos opcionales por completar. ¿Continuar?");
                if (input == 0) { //0 si ok, 1 si cancel
                    if (direccionTxtField.getText().isBlank()) {
                        emisorDB.setDireccion(null);
                    } else {
                        emisorDB.setDireccion(direccionTxtField.getText());
                    }

                    if (codigoPostalTxtField.getText().isBlank()) {
                        emisorDB.setCodigoPostal(null);
                    } else {
                        emisorDB.setCodigoPostal(codigoPostalTxtField.getText());
                    }

                    if (ibanTxtField.getText().isBlank()) {
                        emisorDB.setIban(null);
                    } else {
                        emisorDB.setIban(ibanTxtField.getText());
                    }
                } else {
                    return;
                }
            }
            saveEmisor(emisorDB);
        } else {
            if (nifTxtField.getText().isBlank()) {
                nifTxtField.setBackground(ERROR_BG_COLOR);
            } else {
                emisorDB.setNif(nifTxtField.getText());
                nifTxtField.setBackground(DEFAULT_BG_COLOR);
            }

            if (nombreTxtField.getText().isBlank()) {
                nombreTxtField.setBackground(ERROR_BG_COLOR);
            } else {
                emisorDB.setNombre(nombreTxtField.getText());
                nombreTxtField.setBackground(DEFAULT_BG_COLOR);
            }

            if (nombreCompletoTxtField.getText().isBlank()) {
                nombreCompletoTxtField.setBackground(ERROR_BG_COLOR);
            } else {
                emisorDB.setNombreCompleto(nombreCompletoTxtField.getText());
                nombreCompletoTxtField.setBackground(DEFAULT_BG_COLOR);
            }
            FrameUtils.showPlainMessage("Error", "Por favor, rellene todos los campos obligatorios.");
        }
    }
   
    private void saveEmisor(Emisor emisor) {
        try {
            dbUtils.getEntityManager().getTransaction().begin();
            dbUtils.mergeIntoDB(emisor);
            dbUtils.getEntityManager().getTransaction().commit();
            FrameUtils.showPlainMessage("Éxito", "Los datos han sido actualizados correctamente.");
        } catch (Exception e) {
            FrameUtils.showErrorMessage("Error", "Ha ocurrido un error en el guardado a la base de datos.");
        }
    }

    @Override
    public void recibeClienteLookup(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JPanel getView() {
        return this.modificarEmisorView;
    }

    @Override
    public String getViewName() {
        return this.viewName;
    }

    @Override
    public void setViewName(String newName) {
        this.viewName = newName;
    }

}
