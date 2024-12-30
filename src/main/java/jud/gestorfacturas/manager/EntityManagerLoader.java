package jud.gestorfacturas.manager;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

public class EntityManagerLoader {
    
    public static EntityManager getEntityManagerInstance(String serverName, String user, String pwd) throws PersistenceException {
        if (serverName.isEmpty()) {
            throw new PersistenceException("Server name is blank.");
        }
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        EntityManagerFactory emf;
        Map HashMapProperties = new HashMap();
        HashMapProperties.put("javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/" + serverName);
        HashMapProperties.put("javax.persistence.jdbc.user", user);
        HashMapProperties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        HashMapProperties.put("javax.persistence.jdbc.password", pwd);
        HashMapProperties.put("javax.persistence.schema-generation.database.action", "create");
        emf = Persistence.createEntityManagerFactory(new PropertiesLoader().getEntityManagerName(), HashMapProperties);
        return (EntityManager) emf.createEntityManager();
    }

    public static EntityManager getEntityManagerConfiguredInstance() {
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        if (propertiesLoader.getServerName().isEmpty()) {
            throw new PersistenceException("Server name is blank.");
        }
        EntityManagerFactory emf;
        Map HashMapProperties = new HashMap();
        HashMapProperties.put("javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/" + propertiesLoader.getServerName());
        HashMapProperties.put("javax.persistence.jdbc.user", propertiesLoader.getUser());
        HashMapProperties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        HashMapProperties.put("javax.persistence.jdbc.password", propertiesLoader.getPassword());
        HashMapProperties.put("javax.persistence.schema-generation.database.action", "create");
        emf = Persistence.createEntityManagerFactory(new PropertiesLoader().getEntityManagerName(), HashMapProperties);
        return (EntityManager) emf.createEntityManager();
    }
}
