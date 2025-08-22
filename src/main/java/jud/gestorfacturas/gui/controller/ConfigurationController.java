
package jud.gestorfacturas.gui.controller;

import interfaces.Controller;
import java.awt.Color;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import jud.gestorfacturas.gui.view.ConfigurationView;
import jud.gestorfacturas.manager.EntityManagerLoader;
import jud.gestorfacturas.manager.FrameUtils;
import jud.gestorfacturas.manager.PropertiesLoader;

public class ConfigurationController implements Controller {

    PropertiesLoader properties = new PropertiesLoader();
    
    ConfigurationView configurationView;
    Controller sourceController;
    
    JTextField serverNameTxtField;
    JTextField userTxtField;
    JPasswordField passwordTxtField;
    JTextField consoleMsgTxtField;
    
    private String viewName = "Origen de datos";
    
    public ConfigurationController() {
        configurationView = new ConfigurationView(this);
        initialize();
    }
    
    public void initialize() {
        serverNameTxtField = configurationView.serverNameTxtField;
        serverNameTxtField.setText(properties.getServerName());
        userTxtField = configurationView.userTxtField;
        userTxtField.setText(properties.getUser());
        passwordTxtField = configurationView.passwordTxtField;
        passwordTxtField.setText(properties.getPassword());
        consoleMsgTxtField = configurationView.consoleMsgTxtField;
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
        } else {
            FrameUtils.showErrorMessage("Conexión incorrecta", "Las credenciales son incorrectas o el servidor no existe.");
        }
    }

    @Override
    public JPanel getView() {
        return this.configurationView;
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
