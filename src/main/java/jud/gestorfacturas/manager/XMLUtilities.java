
package jud.gestorfacturas.manager;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jud.gestorfacturas.model.Cliente;
import jud.gestorfacturas.model.Emisor;
import jud.gestorfacturas.model.Factura;
import jud.gestorfacturas.model.Interviniente;

public class XMLUtilities {
    
    public EntityManager em;
    
    public XMLUtilities() {
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
    
    
    /*public void insertPDF() {
        try {
            File pdfFile = new File("C:/Users/Gord/Desktop/zzTest.pdf");
            byte[] pdfData = new byte[(int) pdfFile.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(pdfFile));
            dis.readFully(pdfData);  // read from file into byte[] array
            dis.close();
            
            String myConnectionString
                    = "jdbc:mysql://localhost:3307/mydb";
            dbConnection = DriverManager.getConnection(myConnectionString, "root", "whatever");
            PreparedStatement ps = dbConnection.prepareStatement(
                    "INSERT INTO project ("
                            + "filename, "
                            + "pdf_file "
                            + ") VALUES (?,?)");
            ps.setString(1, "testpdf");
            ps.setBytes(2, pdfData);  // byte[] array
            ps.executeUpdate();
        } catch (IOException ex) {
            Logger.getLogger(XMLUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

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
        /*Query qu = em.createQuery("SELECT f FROM Factura f WHERE f.numFactura = ?1");
        qu.setParameter(1, factura.getNumFactura());
        List s = qu.getResultList();
        return (qu.getFirstResult() != 0);*/
    }
    
    public boolean clienteExists(Cliente cliente) {
        return (em.find(Cliente.class, cliente.getNif()) != null);
    }
    
    public void insertIntoDB(Object obj) {
        em.persist(obj);
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
