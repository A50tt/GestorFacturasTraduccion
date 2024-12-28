
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jud.gestorfacturas.manager.PDFGenerator;
import jud.gestorfacturas.manager.DBUtils;
import jud.gestorfacturas.model.Cliente;
import jud.gestorfacturas.model.Emisor;
import jud.gestorfacturas.model.Factura;
import jud.gestorfacturas.model.Servicio;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GestorFacturasUnit {
    
    DBUtils dbMgr;
    PDFGenerator pdfu;
    Factura[] listaFacturas;
    Servicio[] listaServicios;
    Cliente[] listaClientes;
    
    public GestorFacturasUnit() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
        
    }
    
    @BeforeEach
    public void setUp() {
        pdfu = new PDFGenerator("hello.pdf");
        dbMgr = new DBUtils();
        //listaFacturas = new Factura[] { dbMgr.getFacturas().get(0) };
//        TODO
        dbMgr = new DBUtils();
        dbMgr.getEntityManager().getTransaction().begin();
        dbMgr.getEntityManager().createQuery("DELETE FROM Factura").executeUpdate();
        dbMgr.getEntityManager().createQuery("DELETE FROM Cliente").executeUpdate();
        dbMgr.getEntityManager().getTransaction().commit();
        insert50Facturas();
    }
    
    @AfterEach
    public void tearDown() {
//        TODO
//        if (dbMgr.getEntityManager().isOpen()) {
//            dbMgr.getEntityManager().close();
//        }
    }
    
    public void insert50Facturas() {
        int count = 0;
        int max_count = 50;
        listaFacturas = new Factura[max_count];
        listaServicios = new Servicio[max_count];
        listaClientes = new Cliente[max_count];
        while (count < max_count) {
            dbMgr.getEntityManager().getTransaction().begin();
            //INSERT CLIENTE
            Random ran = new Random();
            Cliente cliente = new Cliente("B" + String.valueOf(ran.nextInt(99999999)), "Empresa Test " + String.valueOf(ran.nextInt(1000)), "Calle Falsa " + String.valueOf(ran.nextInt(1000)), String.valueOf(ran.nextInt(99999)));
            if (dbMgr.clienteExists(cliente)) {
                dbMgr.mergeIntoDB(cliente);
            } else {
                dbMgr.insertIntoDB(cliente);
            }
            dbMgr.getEntityManager().getTransaction().commit();
            listaClientes[count] = cliente;

            //GET UNICO EMISOR
            Emisor emisor = dbMgr.getUnicoEmisor();
            
            //INSERT FACTURA
            String[] tiposTipo = {"Clip", "Palabra", "Error", "Página"};
            String[] idiomas = {"ES", "EN", "PT", "IT", "FR", "DE", "RU","RU", "NL", "NO", "FI"};
            Servicio[] servicios = new Servicio[ran.nextInt(1,5)];
            //he puesto un RAND en el size
            for (int s = 0; s < servicios.length; s++) {
                servicios[s] = new Servicio(String.valueOf(idiomas[ran.nextInt(idiomas.length)]), String.valueOf(idiomas[ran.nextInt(idiomas.length)]),"Servicio " + String.valueOf(ran.nextInt(999)), tiposTipo[ran.nextInt(tiposTipo.length)], ran.nextDouble(0.30d), ran.nextInt(500));
            }
            listaServicios = servicios;
            
            String formaPago = (ran.nextInt(2) == 1) ? "Transferencia bancaria" : "Cheque";
            Date dataEmision = new Date(ran.nextLong(61694780400000l));
            Factura factura = new Factura(dataEmision.toString(), dataEmision, ran.nextInt(91), formaPago, cliente, emisor, servicios);
            dbMgr.getEntityManager().getTransaction().begin();
            if (dbMgr.facturaExists(factura)) {
                dbMgr.mergeIntoDB(factura);
            } else {
                dbMgr.insertIntoDB(factura);
            }
            dbMgr.getEntityManager().getTransaction().commit();
            listaFacturas[count] = factura;

            //List<Factura> facturas = dbMgr.getFacturas();
            //System.out.println(facturas);
            count++;
        }
    }

    @Test
    @Order(1)
    public void readEverything() {
        System.out.println("-------------- TEST 1 --------------");
        Factura[] facturas = new Factura[dbMgr.getFacturas().size()];
        List<Factura> listFacturas = dbMgr.getFacturas();
        for (int i = 0; i < listFacturas.size(); i++) {
            facturas[i] = listFacturas.get(i);
        }
        
        for (int y = 0; y < facturas.length; y++) {
            //System.out.println(listaFacturas[y].getFechaUltActualizacion() + " <--> " + facturas[y].getFechaUltActualizacion());
            assertEquals(listaFacturas[y], facturas[y]);
        }
    }
    
//    @Test
//    @Order(2)
//    public void createInvoice() {
//        System.out.println("-------------- TEST 1 --------------");
//        File pdf = pdfu.generaPDDocumentFactura(listaFacturas[0]);
//    }
}
