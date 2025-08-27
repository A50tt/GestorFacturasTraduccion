
package jud.gestorfacturas.gui.configuracion;

import java.awt.event.ItemEvent;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import utils.ConfigUtils;


public class ConfigAparienciaController {
    
    ConfiguracionController globalConfigController;
    
    ConfigAparienciaView configAparienciaView;
    
    JComboBox firstRowAparienciaComboBox;

    public ConfigAparienciaController(ConfiguracionController _globalConfigController) {
        this.configAparienciaView = new ConfigAparienciaView(this);
        this.globalConfigController = _globalConfigController;
        initialize();
    }
    
    private void initialize() {
        firstRowAparienciaComboBox = configAparienciaView.firstRowAparienciaComboBox;
        firstRowAparienciaComboBox.setSelectedItem(ConfigUtils.loadProperty("ui.theme"));
        
    }
    
    public JPanel getView() {
        return this.configAparienciaView;
    }
    
    public void aparienciaUIChanged(ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String item = firstRowAparienciaComboBox.getSelectedItem().toString();
            globalConfigController.addConfiguration(firstRowAparienciaComboBox.getName(), item);
        }
    }
}
