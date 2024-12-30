
package jud.gestorfacturas.gui.controller;

import java.awt.Color;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import jud.gestorfacturas.gui.view.ConfigurationView;
import jud.gestorfacturas.gui.view.ModificarClienteView;
import jud.gestorfacturas.manager.EntityManagerLoader;
import jud.gestorfacturas.manager.FrameUtils;
import jud.gestorfacturas.manager.PropertiesLoader;
import org.postgresql.util.PSQLException;

public class ConfigurationController implements Controller {

    PropertiesLoader properties = new PropertiesLoader();
    
    ConfigurationView view;
    Controller sourceController;
    
    JTextField serverNameTxtField;
    JTextField userTxtField;
    JPasswordField passwordTxtField;
    JTextField consoleMsgTxtField;
    JButton probarConexionBtn;
    JButton confirmarConfigBtn;
    
    public ConfigurationController(Controller _sourceController) {
        view = new ConfigurationView(this);
        this.sourceController = _sourceController;
        initialize();
        view.setVisible(true);
    }
    
    public void initialize() {
        serverNameTxtField = view.serverNameTxtField;
        serverNameTxtField.setText(properties.getServerName());
        userTxtField = view.userTxtField;
        userTxtField.setText(properties.getUser());
        passwordTxtField = view.passwordTxtField;
        passwordTxtField.setText(properties.getPassword());
        consoleMsgTxtField = view.consoleMsgTxtField;
        probarConexionBtn = view.probarConexionBtn;
        confirmarConfigBtn = view.confirmarConfigBtn;
    }
    
    public boolean checkConnection() {
        String server = serverNameTxtField.getText();
        String user = userTxtField.getText();
        String pwd = "";
        for (char ch : passwordTxtField.getPassword()) {
            pwd += ch;
        }
        try {
            EntityManager eml = EntityManagerLoader.getEntityManagerInstance(server, user, pwd);
            consoleMsgTxtField.setText("Conexión correcta.");
            consoleMsgTxtField.setBackground(Color.green);
            return true;
        } catch (PersistenceException ex) {
            consoleMsgTxtField.setText("Conexión incorrecta.");
            consoleMsgTxtField.setBackground(Color.red);
            return false;
        }
    }
    
    public void saveParamsToProperties() {
        if (checkConnection()) {
        properties.setServerName(serverNameTxtField.getText());
        properties.setUser(userTxtField.getText());
        String pwd = "";
        for (char ch : passwordTxtField.getPassword()) {
            pwd += ch;
        }
        properties.setPassword(pwd);
        FrameUtils.showInfoMessage("Éxito", "Configuración guardada correctamente.");
        returnControlToSource();
        } else {
            FrameUtils.showErrorMessage("Conexión incorrecta", "Las credenciales son incorrectas o el servidor no existe.");
        }
    }
    
    @Override
    public void recibeClienteLookup(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setVisible(boolean visible) {
        view.setVisible(visible);
    }

    @Override
    public void returnControlToSource() {
        this.sourceController.setVisible(true);
        view.dispose();
    }
    
}
