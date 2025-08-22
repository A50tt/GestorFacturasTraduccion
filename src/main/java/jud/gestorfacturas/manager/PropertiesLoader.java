package jud.gestorfacturas.manager;

import utils.FrameUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jud.gestorfacturas.manager.PDFGenerator.RESOURCES_DIRECTORY;

public class PropertiesLoader extends Properties {

    private String PROPERTIES_PATH = RESOURCES_DIRECTORY + File.separator + "META-INF" + File.separator + "config.properties";

    public PropertiesLoader() {
        try {
            this.load(new FileInputStream(PROPERTIES_PATH));
        } catch (IOException ex) {
            Logger.getLogger(PropertiesLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void store() {
        try {
            super.store(new FileOutputStream(PROPERTIES_PATH), null);
        } catch (IOException ex) {
            FrameUtils.showErrorMessage("FATAL ERROR", ex.toString());
        }
    }

    public String getServerName() {
        return this.getProperty("server.name");
    }

    public void setServerName(String name) {
        this.setProperty("server.name", name);
        this.store();
    }

    public String getUser() {
        return this.getProperty("server.user");
    }

    public void setUser(String user) {
        this.setProperty("server.user", user);
        this.store();
    }

    public String getPassword() {
        return this.getProperty("server.password");
    }

    public void setPassword(String pwd) {
        this.setProperty("server.password", pwd);
        this.store();
    }

    public String getEntityManagerName() {
        return this.getProperty("entitymanager.name");
    }
}
