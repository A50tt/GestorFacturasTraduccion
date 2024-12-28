
package jud.gestorfacturas.manager;

import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;

public class FrameUtils {
    public static void showPlainMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void showErrorMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfoMessage(String title, String msg) {
        showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
