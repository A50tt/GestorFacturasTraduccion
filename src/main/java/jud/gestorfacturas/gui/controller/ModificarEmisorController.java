
package jud.gestorfacturas.gui.controller;

import jud.gestorfacturas.gui.view.ModificarEmisorView;
import javax.swing.JButton;
import javax.swing.JTextField;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.manager.FrameUtils;
import jud.gestorfacturas.model.Emisor;

public class ModificarEmisorController implements Controller {
    
    ModificarEmisorView view;
    Controller sourceController;
    
    protected JButton actualizarBtn;
    protected JTextField codigoPostalTxtField;
    protected JTextField direccionTxtField;
    protected JTextField nifTxtField;
    protected JTextField nombreTxtField;
    protected JTextField nombreCompletoTxtField;
    protected JTextField ibanTxtField;
    
    public ModificarEmisorController(Controller _sourceController) {
        view = new ModificarEmisorView(this);
        this.sourceController = _sourceController;
        initialize();
        view.setVisible(true);
    }
    
    private void initialize() {
        nifTxtField = view.nifTxtField;
        nombreTxtField = view.nombreTxtField;
        nombreCompletoTxtField = view.nombreCompletoTxtField;
        direccionTxtField = view.direccionTxtField;
        codigoPostalTxtField = view.codigoPostalTxtField;
        ibanTxtField = view.ibanTxtField;
        cargaDatosEmisor();
    }
    
    public void cargaDatosEmisor() {
        DBUtils dbUtils = new DBUtils();
        Emisor emisor = dbUtils.getUnicoEmisor();
        if (emisor.getNif() != null && emisor.getNif() != null) {
            nifTxtField.setText(emisor.getNif());
            nifTxtField.setEditable(false);
            nifTxtField.setFocusable(false);
            nombreTxtField.setText(emisor.getNombre());
            nombreCompletoTxtField.setText(emisor.getNombreCompleto());
            direccionTxtField.setText(emisor.getDireccion());
            codigoPostalTxtField.setText(emisor.getCodigoPostal());
            ibanTxtField.setText(emisor.getIban());
        } else {
            FrameUtils.showInfoMessage("Datos no encontrados", "No se han encontrado datos anteriores.");
            nifTxtField.setText("");
            nifTxtField.setEditable(true);
            nifTxtField.setFocusable(true);
            nombreTxtField.setText("");
            nombreCompletoTxtField.setText("");
            direccionTxtField.setText("");
            codigoPostalTxtField.setText("");
            ibanTxtField.setText("");
        }
    }

    public void actualizaEmisor() {
        DBUtils dbUtils = new DBUtils();

        Emisor emisorDB = dbUtils.getUnicoEmisor();
        emisorDB.setNif(nifTxtField.getText());
        emisorDB.setNombre(nombreTxtField.getText());
        emisorDB.setNombreCompleto(nombreCompletoTxtField.getText());
        emisorDB.setDireccion(direccionTxtField.getText());
        emisorDB.setCodigoPostal(codigoPostalTxtField.getText());
        emisorDB.setIban(ibanTxtField.getText());

        dbUtils.getEntityManager().getTransaction().begin();
        dbUtils.mergeIntoDB(emisorDB);
        dbUtils.getEntityManager().getTransaction().commit();

        FrameUtils.showPlainMessage("Éxito", "Los datos han sido actualizados correctamente.");
        nifTxtField.setEditable(false);
        nifTxtField.setFocusable(false);
    }
 
    @Override
    public void setVisible(boolean visible) {
        view.setVisible(visible);
    }

    @Override
    public void returnControlToSource() {
        this.sourceController.setVisible(true);
    }

    @Override
    public void recibeClienteLookup(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
