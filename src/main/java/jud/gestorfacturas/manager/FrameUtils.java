package jud.gestorfacturas.manager;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.io.File;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;

public class FrameUtils {
    
    private static String[] RESPUESTAS_MSGBOX_FALTAN_OPCIONALES = {"Continuar", "Cancelar"};
    public static FlatSVGIcon REDO_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "redo_black_icon.svg"));
    public static FlatSVGIcon STANDBY_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "standby_status_icon.svg"));
    public static FlatSVGIcon SEARCH_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "busqueda_black_icon.svg"));
    public static FlatSVGIcon OK_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "ok_status_icon.svg"));
    public static FlatSVGIcon KO_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "ko_status_icon.svg"));
    public static FlatSVGIcon CONFIG_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "config_icon.svg"));
    
    public static void showPlainMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void showErrorMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfoMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static int showQuestionBox(String title, String msg) {
        return JOptionPane.showOptionDialog(null, msg, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, RESPUESTAS_MSGBOX_FALTAN_OPCIONALES, null);
    }
    
    public static String showTextInputBox(String title, String msg) {
        return JOptionPane.showInputDialog(null, msg, title, 0);
    }
}
