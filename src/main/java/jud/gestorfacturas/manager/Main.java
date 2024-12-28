package jud.gestorfacturas.manager;

import java.sql.Date;
import java.util.List;
import java.util.Random;
import jud.gestorfacturas.gui.controller.NuevoClienteController;
import jud.gestorfacturas.gui.controller.FacturaController;
import jud.gestorfacturas.gui.controller.MainMenuController;
import jud.gestorfacturas.gui.controller.ModificarClienteController;
import jud.gestorfacturas.model.Cliente;
import jud.gestorfacturas.model.Emisor;
import jud.gestorfacturas.model.Factura;
import jud.gestorfacturas.model.Servicio;

public class Main {
    
    DBUtils xmlmgr;

    public static void main(String[] args) {
        Main gf = new Main();
        gf.inicio();        
    }
    
    public void inicio() {
        MainMenuController mainMenu = new MainMenuController();
        //FacturaController controller = new FacturaController();
        //NuevoClienteController controller = new NuevoClienteController();
        //ModificarClienteController controller = new ModificarClienteController();
        //PDFGenerator pdfu = new PDFGenerator("hello.pdf");
        //pdfu.createPDF();
        //xmlmgr = new DBUtils();
        //xmlmgr.getEntityManager().getTransaction().begin();
        //readEverything();
        //insert50();
    }
    
    public void readEverything() {

    }
    
    
}
