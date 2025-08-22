
package jud.gestorfacturas.interfaces;

import javax.swing.JPanel;

public interface Controller {

    JPanel getView();
    String getViewName();
    void setViewName(String newName);
}
