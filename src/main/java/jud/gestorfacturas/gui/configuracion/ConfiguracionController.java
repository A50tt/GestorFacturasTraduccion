
package jud.gestorfacturas.gui.configuracion;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;
import jud.gestorfacturas.interfaces.Controller;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;
import utils.ConfigUtils;
import utils.FrameUtils;

public class ConfiguracionController implements Controller {
    
    ConfiguracionView configurationView;
    
    JPanel rightPanel;
    public static Map<String, String> configsPendingToApply;
    
    public static final String VIEW_NAME = "Configuración";
    public String finalViewName;
    
    public ConfiguracionController() {
        configurationView = new ConfiguracionView(this);
        configsPendingToApply = new HashMap<>();
        initialize();
    }
    
    public void initialize() {
        this.rightPanel = configurationView.rightPanel;
        this.rightPanel.add(new ConfigFacturaController(this).getView(), "Factura");
        this.rightPanel.add(new ConfigAparienciaController(this).getView(), "Apariencia");
    }

    public void openConfigurationView(java.beans.PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        if (newValue instanceof TreePath) {
            Object selectedNode = ((TreePath) newValue).getLastPathComponent();
            ((CardLayout) rightPanel.getLayout()).show(rightPanel, selectedNode.toString());
        }
    }
    
    public void addConfiguration(String key, String value) {
        configsPendingToApply.put(key, value);
    }
    
    public void saveConfigurations() {
        ConfigUtils.storeProperties(configsPendingToApply);
        configsPendingToApply = new HashMap<>(); // Vacía de nuevo
        FrameUtils.showPlainMessage("Éxito", "La configuración se ha guardado correctamente.\nPara evitar problemas visuales, se recomienda reiniciar todas las pestañas abiertas.");
    }

    @Override
    public JPanel getView() {
        return this.configurationView;
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
