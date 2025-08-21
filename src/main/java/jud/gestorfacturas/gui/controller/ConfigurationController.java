
package jud.gestorfacturas.gui.controller;

import interfaces.Controller;
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
    
    ConfigurationView configurationView;
    Controller sourceController;
    
    JTextField serverNameTxtField;
    JTextField userTxtField;
    JPasswordField passwordTxtField;
    JTextField consoleMsgTxtField;
    JButton probarConexionBtn;
    JButton confirmarConfigBtn;
    
    public ConfigurationController(Controller _sourceController) {
        configurationView = new ConfigurationView(this);
        FrameUtils.centerViewOnScreen(configurationView);
        this.sourceController = _sourceController;
        initialize();
        configurationView.setVisible(true);
    }
    
    public void initialize() {
        serverNameTxtField = configurationView.serverNameTxtField;
        serverNameTxtField.setText(properties.getServerName());
        userTxtField = configurationView.userTxtField;
        userTxtField.setText(properties.getUser());
        passwordTxtField = configurationView.passwordTxtField;
        passwordTxtField.setText(properties.getPassword());
        consoleMsgTxtField = configurationView.consoleMsgTxtField;
        probarConexionBtn = configurationView.probarConexionBtn;
        confirmarConfigBtn = configurationView.confirmarConfigBtn;
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
        sourceController.returnControlToSource(this);
        } else {
            FrameUtils.showErrorMessage("Conexión incorrecta", "Las credenciales son incorrectas o el servidor no existe.");
        }
    }

    @Override
    public void setVisible(boolean visible) {
        configurationView.setVisible(visible);
    }
    
    @Override
    public void closeView() {
        returnControlToSource(this);
        configurationView.dispose();
    }

    @Override
    public void returnControlToSource(Controller controller) {
        sourceController.returnControlToSource(this);
    }
}
