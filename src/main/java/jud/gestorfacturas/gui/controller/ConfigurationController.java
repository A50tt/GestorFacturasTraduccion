
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
    
    ConfigurationView configView;
    Controller sourceController;
    
    JTextField serverNameTxtField;
    JTextField userTxtField;
    JPasswordField passwordTxtField;
    JTextField consoleMsgTxtField;
    JButton probarConexionBtn;
    JButton confirmarConfigBtn;
    
    public ConfigurationController(Controller _sourceController) {
        configView = new ConfigurationView(this);
        FrameUtils.centerViewOnScreen(configView);
        this.sourceController = _sourceController;
        initialize();
        configView.setVisible(true);
    }
    
    public void initialize() {
        serverNameTxtField = configView.serverNameTxtField;
        serverNameTxtField.setText(properties.getServerName());
        userTxtField = configView.userTxtField;
        userTxtField.setText(properties.getUser());
        passwordTxtField = configView.passwordTxtField;
        passwordTxtField.setText(properties.getPassword());
        consoleMsgTxtField = configView.consoleMsgTxtField;
        probarConexionBtn = configView.probarConexionBtn;
        confirmarConfigBtn = configView.confirmarConfigBtn;
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
        configView.setVisible(visible);
    }

    @Override
    public void returnControlToSource() {
        this.sourceController.setVisible(true);
        configView.dispose();
    }
    
}
