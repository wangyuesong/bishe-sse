package sse.service.impl.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.BeforeClass;

public class BaseJPATest {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("SSEPU");
    EntityManager em = factory.createEntityManager();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    public void beginTransaction()
    {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    public void commitTransaction()
    {
        em.getTransaction().commit();
    }
}
