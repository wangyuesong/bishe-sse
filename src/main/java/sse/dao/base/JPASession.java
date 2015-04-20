package sse.dao.base;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPASession {
    private static EntityManagerFactory entityManagerFactory = null;

    public static EntityManagerFactory getEntityManagerFactory(String persistenceUnitName)
    {
        createEntityManagerFactory(persistenceUnitName);
        return entityManagerFactory;
    }

    public synchronized static void createEntityManagerFactory(String persistenceUnitName) {
        if (entityManagerFactory != null) {
            return;
        }
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

}
