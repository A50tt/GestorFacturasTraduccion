package utils;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jud.gestorfacturas.manager.PDFGenerator;
import static javax.swing.JOptionPane.showMessageDialog;

public class FrameUtils {
    
    private static final String[] RESPUESTAS_MSGBOX_CONTINUAR_CANCELAR = {"Continuar", "Cancelar"};
    private static final String[] RESPUESTAS_MSGBOX_SI_NO = {"Sí", "No"};
    public static final FlatSVGIcon REDO_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "redo_black_icon.svg"));
    public static final FlatSVGIcon STANDBY_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "standby_status_icon.svg"));
    public static final FlatSVGIcon SEARCH_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "busqueda_black_icon.svg"));
    public static final FlatSVGIcon OK_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "ok_status_icon.svg"));
    public static final FlatSVGIcon KO_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "ko_status_icon.svg"));
    public static final FlatSVGIcon CONFIG_FLATSVGICON = new FlatSVGIcon(new File(PDFGenerator.IMAGES_DIRECTORY + "config_icon.svg"));

    public static void centerViewOnScreen(JFrame view) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - view.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - view.getHeight()) / 2);
        view.setLocation(x, y);
    }

    public static void showPlainMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void showErrorMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfoMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    // 0 -> SÍ, 1 -> NO
    public static int showQuestionBoxContinuarCancelar(String title, String msg) {
        return JOptionPane.showOptionDialog(null, msg, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, RESPUESTAS_MSGBOX_CONTINUAR_CANCELAR, null);
    }
    
    public static int showQuestionBoxSiNo(String title, String msg) {
        return JOptionPane.showOptionDialog(null, msg, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, RESPUESTAS_MSGBOX_SI_NO, null);
    }
    
    public static int showErrorQuestionBoxSiNo(String title, String msg) {
        return JOptionPane.showOptionDialog(null, msg, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, RESPUESTAS_MSGBOX_SI_NO, null);
    }
    
    public static String showTextInputBox(String title, String msg) {
        return JOptionPane.showInputDialog(null, msg, title, 0);
    }
}
