
package jud.gestorfacturas.manager;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import jud.gestorfacturas.model.Cliente;
import jud.gestorfacturas.model.Emisor;
import jud.gestorfacturas.model.Factura;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.postgresql.util.PSQLException;

public class DBUtils {
    
    public EntityManager em;
 
    private String createFacturasTableQuery = "CREATE TABLE public.facturas "
            + "("
            + "numfactura character varying(255) NOT NULL, "
            + "tasa_irpf double precision, "
            + "tasa_iva double precision, "
            + "baseimponible double precision, "
            + "diaspago integer, "
            + "fechaemision date, "
            + "fechaultactualizacion timestamp without time zone, "
            + "fechavencimiento date, "
            + "formapago character varying(255), "
            + "importetotal double precision, "
            + "irpf double precision, "
            + "iva double precision, "
            + "listaservicios bytea, "
            + "pdffactura bytea, "
            + "cliente_nif character varying(255), "
            + "emisor_nif character varying(255), "
            + "CONSTRAINT factura_pkey PRIMARY KEY (numfactura), "
            + "CONSTRAINT fk_factura_cliente_nif FOREIGN KEY (cliente_nif) "
            + "REFERENCES public.cliente (nif) MATCH SIMPLE "
            + "ON UPDATE NO ACTION "
            + "ON DELETE NO ACTION, "
            + "CONSTRAINT fk_factura_emisor_nif FOREIGN KEY (emisor_nif) "
            + "REFERENCES public.emisor (nif) MATCH SIMPLE "
            + "ON UPDATE NO ACTION "
            + "ON DELETE NO ACTION "
            + ");";
    
    private String createClientesTableQuery  = "CREATE TABLE IF NOT EXISTS public.clientes\n"
            + "("
            + "nif character varying(255) COLLATE pg_catalog.\"default\" NOT NULL,"
            + "activado boolean,"
            + "codigopostal character varying(255) COLLATE pg_catalog.\"default\","
            + "direccion character varying(255) COLLATE pg_catalog.\"default\","
            + "fechaultactualizacion timestamp without time zone,\n"
            + "id integer,"
            + "nombre character varying(255) COLLATE pg_catalog.\"default\","
            + "CONSTRAINT cliente_pkey PRIMARY KEY (nif)"
            + ")"
            + "TABLESPACE pg_default;"
            + "ALTER TABLE IF EXISTS public.cliente"
            + "OWNER to postgres;";

    private String createEmisorTableQuery = "-- Table: public.emisor"
            + ""
            + "-- DROP TABLE IF EXISTS public.emisor;"
            + "CREATE TABLE IF NOT EXISTS public.emisor"
            + "("
            + "nif character varying(255) COLLATE pg_catalog.\"default\" NOT NULL,"
            + "dtype character varying(31) COLLATE pg_catalog.\"default\","
            + "codigopostal character varying(255) COLLATE pg_catalog.\"default\","
            + "direccion character varying(255) COLLATE pg_catalog.\"default\","
            + "iban character varying(255) COLLATE pg_catalog.\"default\","
            + "nombre character varying(255) COLLATE pg_catalog.\"default\","
            + "nombrecompleto character varying(255) COLLATE pg_catalog.\"default\","
            + "fechaultactualizacion timestamp without time zone,"
            + "id integer,"
            + "CONSTRAINT emisor_pkey PRIMARY KEY (nif)"
            + ")"
            + "TABLESPACE pg_default;"
            + "ALTER TABLE IF EXISTS public.emisor"
            + "OWNER to postgres;";
    
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

    public void checkIfTablesAreCreatedOnDB() {
        try {
            em.getTransaction().begin();
            Query queryFacturas = em.createNativeQuery("SELECT * FROM facturas");
            Factura facturaSacrificada = null;
            Cliente clienteSacrificado = null;
            Emisor emisorSacrificado = null;
            if (queryFacturas.getFirstResult() == 0) {
                facturaSacrificada = Factura.getDummyInstance();
                em.persist(facturaSacrificada);
            }
            Query queryClientes = em.createNativeQuery("SELECT * FROM clientes");
            if (queryClientes.getFirstResult() == 0) {
                clienteSacrificado = Cliente.getDummyInstance();
                em.persist(clienteSacrificado);
            }
            Query queryEmisor = em.createNativeQuery("SELECT * FROM emisors");
            if (queryEmisor.getFirstResult() == 0) {
                emisorSacrificado = Emisor.getDummyInstance();
                em.persist(emisorSacrificado);
            }
            em.getTransaction().commit();
            
            em.getTransaction().begin();
            if (facturaSacrificada != null) {
                em.remove(facturaSacrificada);
            }
            if (clienteSacrificado != null) {
                em.remove(clienteSacrificado);
            }
            if (emisorSacrificado != null) {
                em.remove(emisorSacrificado);
            }
            em.getTransaction().commit();
            
        } catch (IllegalStateException ex1) {
            System.out.println(ex1.toString());
        } catch (PersistenceException ex2) {
            System.out.println(ex2.toString());
        } catch (DatabaseException ex3) {
            System.out.println(ex3.toString());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
    
    public boolean facturaExists(Factura factura) {
        return (em.find(Factura.class, factura.getNumFactura()) != null);
    }

    public boolean facturaExists(String numFactura) {
        return (em.find(Factura.class, numFactura) != null);
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
        int next;
        Query query = em.createNativeQuery("SELECT max(id) FROM cliente");
        if (query.getSingleResult() == null) {
            next = 1;
        } else {
            next = (int)query.getSingleResult() + 1;
        }
        return next;
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
        Query query = em.createQuery("SELECT e FROM Emisor e");
        if (query.getResultList().size() != 0) {
            return (Emisor)query.getResultList().get(0);
        }
        return new Emisor();
    }
}
