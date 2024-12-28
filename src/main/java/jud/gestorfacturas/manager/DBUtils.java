
package jud.gestorfacturas.manager;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
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
        if (em.isOpen()) {
            em.close();
        }
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
    
    public Cliente getCliente(Cliente cliente) {
        return em.find(Cliente.class, cliente.getNif());
    }

    public Cliente[] getAllClientes(Cliente[] clientes) {
        Cliente[] clientesArray = new Cliente[clientes.length];
        for (int i = 0; i < clientes.length; i++) {
            clientesArray[i] = em.find(Cliente.class, clientes[i].getNif());
        }
        return clientesArray;
    }
    
    public boolean clienteExists(Cliente cliente) {
        return (em.find(Cliente.class, cliente.getNif()) != null);
    }
    
    public Cliente getClienteByNif(String nif) {
        return em.find(Cliente.class, nif);
    }
    
    public Cliente getClienteById(String id) {
        Query query = em.createNativeQuery("SELECT nif FROM cliente WHERE id = " + id);
        try {
            String nif = query.getSingleResult().toString();
            //String _nombre, String _direccion, String _codigoPostal, String _nif
            return em.find(Cliente.class, nif);
        } catch (NoResultException ex1) {
            return null;
        } catch (PersistenceException ex2) {
            return new Cliente (null, null, null, null);
        }
    }
    
    public List getTodosClientes() {
        Cliente[] clientesArray;
        Query query = em.createNativeQuery("SELECT id, nif, nombre, direccion, codigopostal, activado FROM cliente ORDER BY id");
        try {
            return query.getResultList();
        } catch (NoResultException ex1) {
            return null;
        }
    } 
    
    public List getTodosClientesPorCampo(String nombreCampo, String valorBuscado) {
        Cliente[] clientesArray;
        Query query = em.createNativeQuery("SELECT id, nif, nombre, direccion, codigopostal, activado FROM cliente WHERE " + nombreCampo + " LIKE '" + valorBuscado + "' ORDER BY id");
        try {
            return query.getResultList();
        } catch (NoResultException ex1) {
            return null;
        }
    } 
    
    public int getSiguienteIdCliente() {
        int max = 0;
        Query query = em.createNativeQuery("SELECT max(id) FROM cliente");
        if (query.getSingleResult() == null) {
            max = 1;
        } else {
            max = (int)query.getSingleResult() + 1;
        }
        return max;
    }
    
    public void insertIntoDB(Object obj) {
        em.persist(obj);
    }
    
    public Timestamp getTimestampOfInvoice(Factura factura) {
        Factura facturaBD = em.find(Factura.class, factura.getNumFactura());
        return facturaBD.getFechaUltActualizacion();
    }
    
    public Timestamp getTimestampCliente(Cliente cliente) {
        Cliente clienteDB = em.find(Cliente.class, cliente.getNif());
        return clienteDB.getFechaUltActualizacion();
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
