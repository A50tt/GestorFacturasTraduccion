
package interfaces;

public interface Controller {

    void setVisible(boolean visible);
    void closeView();
    void returnControlToSource(Controller controller);
}
