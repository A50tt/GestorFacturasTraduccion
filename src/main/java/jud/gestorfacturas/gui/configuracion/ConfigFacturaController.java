
package jud.gestorfacturas.gui.configuracion;

import java.io.File;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import jud.gestorfacturas.manager.PDFGenerator;
import utils.ConfigUtils;
import utils.FileUtils;
import utils.FrameUtils;

public class ConfigFacturaController {
    
    ConfiguracionController globalConfigController;
    
    ConfigFacturaView configFacturaView;
    
    JButton firstRowExaminarBtn;
    JSpinner logoPosXSpinner;
    JSpinner logoPosYSpinner;
    JSpinner logoAnchoSpinner;
    JSpinner logoAlturaSpinner;
    

    public ConfigFacturaController(ConfiguracionController _globalConfigController) {
        configFacturaView = new ConfigFacturaView(this);
        this.globalConfigController = _globalConfigController;
        initialize();
    }
    
    private void initialize() {
        firstRowExaminarBtn = configFacturaView.firstRowExaminarBtn;
        logoPosXSpinner = configFacturaView.logoPosXSpinner;
        logoPosYSpinner = configFacturaView.logoPosYSpinner;
        logoAnchoSpinner = configFacturaView.logoAnchoSpinner;
        logoAlturaSpinner = configFacturaView.logoAlturaSpinner;
        
        File iconFile = new File(ConfigUtils.loadProperty(firstRowExaminarBtn.getName()));
        configFacturaView.firstRowNombreArchivoLogoLbl.setText(iconFile.getName());
        logoPosXSpinner.setValue(Integer.valueOf(ConfigUtils.loadProperty("factura.logo.x")));
        logoPosYSpinner.setValue(Integer.valueOf(ConfigUtils.loadProperty("factura.logo.y")));
        logoAnchoSpinner.setValue(Integer.valueOf(ConfigUtils.loadProperty("factura.logo.width")));
        logoAlturaSpinner.setValue(Integer.valueOf(ConfigUtils.loadProperty("factura.logo.height")));
    }
    
    public JPanel getView() {
        return this.configFacturaView;
    }

    public void openFileChooser(JButton sourceButton) {
        String currentLogoFilePath = ConfigUtils.loadProperty(sourceButton.getName());
        File iconFile = FileUtils.openFileChooserWithDefaultPath(currentLogoFilePath, configFacturaView);
        // Solo soporta jpg, jpeg, tif, tiff, gif, bmp y png.
        if (iconFile != null) {
            if (FileUtils.isOneOfExtensions(iconFile, PDFGenerator.VALID_LOGO_EXTENSIONS)) {
                globalConfigController.addConfiguration(sourceButton.getName(), iconFile.toString());
                configFacturaView.firstRowNombreArchivoLogoLbl.setText(iconFile.getName());
            } else {
                FrameUtils.showErrorMessage("Error", "Solo son compatibles los archivos con las siguientes extensiones:\nJPG, JPEG, TIF, TIFF, GIF, BMP y PNG.");
            }
        }
    }
    
    public void setNewLogoProperty(JSpinner sourceSpinner) {
        globalConfigController.addConfiguration(sourceSpinner.getName(), sourceSpinner.getValue().toString());
    }
}
