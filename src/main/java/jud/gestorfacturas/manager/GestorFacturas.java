package jud.gestorfacturas.manager;

import java.sql.Date;
import java.util.List;
import java.util.Random;
import jud.gestorfacturas.model.Cliente;
import jud.gestorfacturas.model.Emisor;
import jud.gestorfacturas.model.Factura;
import jud.gestorfacturas.model.Servicio;

public class GestorFacturas {
    
    XMLManager xmlmgr;

    public static void main(String[] args) {
        GestorFacturas gf = new GestorFacturas();
        gf.inicio();        
    }
    
    public void inicio() {
        xmlmgr = new XMLManager();
        xmlmgr.getEntityManager().getTransaction().begin();
        readEverything();
        //insert50();
    }
    
    public void readEverything() {

    }
    
    
}
