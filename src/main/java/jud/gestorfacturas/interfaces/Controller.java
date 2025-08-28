
package jud.gestorfacturas.interfaces;

import javax.swing.JPanel;

public interface Controller {
    
    public static final String VIEW_NAME = null;

    public JPanel getView();
    public String getViewName();
    public void setViewName(String str);
    
}
