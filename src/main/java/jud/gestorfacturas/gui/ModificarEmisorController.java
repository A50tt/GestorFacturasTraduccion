
package jud.gestorfacturas.gui;

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
        direccionTxtField = view.direccionTxtField;
        codigoPostalTxtField = view.codigoPostalTxtField;
        ibanTxtField = view.ibanTxtField;
        cargaDatosEmisor();
    }
    
    protected void cargaDatosEmisor() {
        DBUtils dbUtils = new DBUtils();
        Emisor emisor = dbUtils.getUnicoEmisor();
        if (emisor != null && emisor.getNif() != null) {
            nifTxtField.setText(emisor.getNif());
            nombreTxtField.setText(emisor.getNombre());
            direccionTxtField.setText(emisor.getDireccion());
            codigoPostalTxtField.setText(emisor.getCodigoPostal());
            ibanTxtField.setText(emisor.getIban());
        } else {
            FrameUtils.showErrorMessage("Error", "No se han podido recuperar los datos.");
            nifTxtField.setText("ERROR");
            nombreTxtField.setText("ERROR");
            direccionTxtField.setText("ERROR");
            codigoPostalTxtField.setText("ERROR");
            ibanTxtField.setText("ERROR");
        }
    }

    protected void actualizaEmisor() {
        DBUtils dbUtils = new DBUtils();

        Emisor emisorDB = dbUtils.getUnicoEmisor();
        emisorDB.setNombre(nombreTxtField.getText());
        emisorDB.setDireccion(direccionTxtField.getText());
        emisorDB.setCodigoPostal(codigoPostalTxtField.getText());
        emisorDB.setIban(ibanTxtField.getText());

        dbUtils.getEntityManager().getTransaction().begin();
        dbUtils.mergeIntoDB(emisorDB);
        dbUtils.getEntityManager().getTransaction().commit();

        FrameUtils.showPlainMessage("Éxito", "Los datos han sido actualizados correctamente.");
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
