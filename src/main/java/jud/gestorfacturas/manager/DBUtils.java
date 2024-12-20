
package jud.gestorfacturas.manager;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jud.gestorfacturas.model.Cliente;
import jud.gestorfacturas.model.Emisor;
import jud.gestorfacturas.model.Factura;

public class DBUtils {
    
    public EntityManager em;
    
    public DBUtils() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("facturasPA");
        em = emf.createEntityManager();
    }
    
    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        // do some crazy stuff here
    }
    public void onCreate() {
        em.getTransaction().begin();
    }

    public EntityManager getEntityManager() {
        return em;
    }
    
    public void switchEntityManager() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        } else {
            em.close();
        }
    }
    
    public boolean facturaExists(Factura factura) {
        return (em.find(Factura.class, factura.getNumFactura()) != null);
    }
    
    public File getPDFFactura(String numFactura) {
        Factura factura  = em.find(Factura.class, numFactura);
        return factura.getPdfFactura();
    }
    
    public boolean clienteExists(Cliente cliente) {
        return (em.find(Cliente.class, cliente.getNif()) != null);
    }
    
    public void insertIntoDB(Object obj) {
        em.persist(obj);
    }
    
    public Timestamp getTimestampOfInvoice(Factura factura) {
        Factura facturaBD = em.find(Factura.class, factura.getNumFactura());
        return facturaBD.getFechaUltActualizacion();
    }

    public void mergeIntoDB(Object obj) {
        em.merge(obj);
    }
    
    public List<Factura> getFacturas() {
        Query q = em.createQuery("SELECT f FROM Factura f ORDER BY f.fechaUltActualizacion ASC");
        return q.getResultList();
    }
    
    public Emisor getUnicoEmisor() {
        Query qu = em.createQuery("SELECT e FROM Emisor e");
        return (Emisor)qu.getResultList().get(0);
    }
}
