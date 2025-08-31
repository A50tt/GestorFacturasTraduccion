
package jud.gestorfacturas.gui;

import jud.gestorfacturas.gui.editar.ModificarEmisorController;
import jud.gestorfacturas.gui.editar.ModificarClienteController;
import jud.gestorfacturas.gui.buscar.BuscarFacturaController;
import jud.gestorfacturas.gui.crear.CrearFacturaController;
import jud.gestorfacturas.gui.configuracion.ConfiguracionController;
import jud.gestorfacturas.gui.crear.NuevoClienteController;
import jud.gestorfacturas.interfaces.Controller;
import jud.gestorfacturas.interfaces.GlobalController;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import jud.gestorfacturas.gui.configuracion.ImportarController;
import utils.ConfigUtils;
import utils.FrameUtils;
import utils.JSONUtils;

public class MainContainerController implements GlobalController {
    
    public static MainContainerView frameContainerView;
    protected JTabbedPane jTabbedPane;
    
    List<Controller> tabs = new ArrayList<Controller>();
    
    public MainContainerController() {
        setUITheme(ConfigUtils.UI_THEMES.get(ConfigUtils.loadProperty("ui.theme")));
        frameContainerView = new MainContainerView(this, "Gestión de facturas");
        FrameUtils.centerViewOnScreen(frameContainerView);
        initialize();
        setVisible(true);
        frameContainerView.setVisible(true);
        checkDataBaseIntegrity();
    }

    private void initialize() {
        jTabbedPane = frameContainerView.jTabbedPane1;
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE); // Aumenta el tiempo que los Tooltips se mantienen mostrándose al usuario.
    }
    
    public static void setUITheme(String theme) {
        try {
            UIManager.setLookAndFeel(theme);
        } catch (ClassNotFoundException ex) {
            FrameUtils.showErrorMessage("Error", ex.getMessage(), ex);
        } catch (InstantiationException ex) {
            FrameUtils.showErrorMessage("Error", ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            FrameUtils.showErrorMessage("Error", ex.getMessage(), ex);
        } catch (UnsupportedLookAndFeelException ex) {
            FrameUtils.showErrorMessage("Error", ex.getMessage(), ex);
        }
    }
    
    private void checkDataBaseIntegrity() {
        List<String> databasesNames = new ArrayList<String>();
        databasesNames.add(JSONUtils.dbFileNames[0]);
        databasesNames.add(JSONUtils.dbFileNames[1]);
        databasesNames.add(JSONUtils.dbFileNames[2]);
        JSONUtils.checkJsonFilesIntegrityOrResetThem(databasesNames);
    }

    @Override
    public void setVisible(boolean visible) {
        frameContainerView.setVisible(true);
    }

    public void openNuevaFactura() {
        CrearFacturaController facturaController = new CrearFacturaController();
        handleNewTab(facturaController);
    }
    
    public void openCrearCliente() {
        NuevoClienteController crearClienteController = new NuevoClienteController();
        handleNewTab(crearClienteController);
    }
    
    public void openModificarCliente() {
        ModificarClienteController modificarClienteController = new ModificarClienteController();
        handleNewTab(modificarClienteController);
    }
   
    public void openDatosPersonales() {
        ModificarEmisorController modificarEmisorController = new ModificarEmisorController();
        handleNewTab(modificarEmisorController);
    }

    public void openConfiguracion() {
        ConfiguracionController modificarEmisorController = new ConfiguracionController();
        handleNewTab(modificarEmisorController);
    }

    public void openBuscarFactura() {
        BuscarFacturaController buscarFacturaController = new BuscarFacturaController();
        handleNewTab(buscarFacturaController);
    }
    
     public void openImportar() {
        ImportarController importarController = new ImportarController(this);
        handleNewTab(importarController);
    }

    private void handleNewTab(Controller controller) {
        jTabbedPane.addTab(controller.getViewName(), controller.getView());
        addNewTab(controller);
        addCrossButtonToTab(jTabbedPane, controller, tabs.size() - 1);
        openTab(controller.getView());
        openLastTab();
    }
    
    private void addNewTab(Controller controller) {
        boolean sameTabExists = false;
        for (Controller tabs : tabs) {
            if (controller.getViewName().equals(tabs.getViewName())) {
                sameTabExists = true;
                break;
            }
        }

        if (sameTabExists) {
            boolean lookingSufix = true;
            int sufix = 2;
            while (lookingSufix) {
                int coincidences = 0;
                String proposedName = controller.getViewName() + " (" + sufix + ")";
                for (Controller tab : tabs) {
                    if (proposedName.equals(tab.getViewName())) {
                        coincidences++;
                    }
                }
                if (coincidences == 0) {
                    controller.setViewName(proposedName);
                    lookingSufix = false;
                } else {
                    sufix++;
                }
            }
        } else {
            controller.setViewName(controller.getViewName());
        }
        tabs.add(controller);
    }
  
    private void closeTab(JPanel pnlTab) {
        int updatedIndex = jTabbedPane.indexOfTabComponent(pnlTab);
        if (updatedIndex != -1) {
            tabs.remove(updatedIndex);
            jTabbedPane.removeTabAt(updatedIndex);
        }
    }
    
    private void openTab(JPanel pnlTab) {
        int i = jTabbedPane.indexOfTabComponent(pnlTab);
        jTabbedPane.setSelectedIndex(i);
    }
    
    private void openLastTab() {
        jTabbedPane.setSelectedIndex(tabs.size() - 1);
    }

    private void addCrossButtonToTab(JTabbedPane pane, Controller controller, int tabIndex) {
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        pnlTab.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel lblTitle = new JLabel(controller.getViewName());
        JButton btnClose = new JButton();
        btnClose.setBorder(null);
        
        btnClose.setContentAreaFilled(false);
        btnClose.setOpaque(false);

        ImageIcon icon = new ImageIcon("src/main/resources/img/close_x.png");
        Image image = icon.getImage(); // Get the original image
        Image scaledImage = image.getScaledInstance(12, 12, Image.SCALE_SMOOTH); // Width: 32, Height: 32
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        btnClose.setIcon(scaledIcon);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 6;
        gbc.insets = new Insets(0, 0, 0, 5);

        pnlTab.add(lblTitle, gbc);

        gbc.gridx++;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 5, 0, 0);
        pnlTab.add(btnClose, gbc);

        pane.setTabComponentAt(tabIndex, pnlTab);

        pnlTab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    openTab(pnlTab);
                }
            }
        });

        pnlTab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Prevent closing if clicking the close button
                if (e.getSource() == btnClose) {
                    return;
                }
                if (SwingUtilities.isLeftMouseButton(e)) {
                    openTab(pnlTab);
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    closeTab(pnlTab);
                }
            }
        });

        btnClose.addActionListener(evt -> {
            closeTab(pnlTab);
        });
    }
}
