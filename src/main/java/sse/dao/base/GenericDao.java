package sse.dao.base;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import sse.entity.Teacher;

public abstract class GenericDao<K, E> implements Dao<K, E> {
    protected Class<E> entityClass;

    @PersistenceUnit
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public GenericDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
    }

    public void beginTransaction()
    {
        if (!this.getEntityManager().getTransaction().isActive())
            this.getEntityManager().getTransaction().begin();
    }

    public void commitTransaction() {
        this.getEntityManager().getTransaction().commit();
    }

    public EntityManager getEntityManager()
    {
        if (this.entityManager == null)
        {
            this.entityManager = JPASession.getEntityManagerFactory("SSEPU").createEntityManager();
            return this.entityManager;
        }
        else
            return this.entityManager;
    }

    public void persistWithTransaction(E entity)
    {
        beginTransaction();
        this.getEntityManager().persist(entity);
        commitTransaction();
    }

    public void mergeWithTransaction(E entity)
    {
        beginTransaction();
        this.getEntityManager().merge(entity);
        commitTransaction();
    }

    public void removeWithTransaction(E entity)
    {
        beginTransaction();
        this.getEntityManager().remove(entity);
        commitTransaction();
    }

    public void persist(E entity) {
        this.getEntityManager().persist(entity);
    }

    public void remove(E entity) {
        this.getEntityManager().remove(entity);
    }

    public void merge(E entity)
    {
        this.getEntityManager().merge(entity);
    }

    public E findById(K id) {
        return this.getEntityManager().find(entityClass, id);
    }

    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        return this.getEntityManager().createQuery("select w from " + entityClass.getName() + " w")
                .getResultList();
    }

    // FIND FOR PAGING
    @SuppressWarnings("unchecked")
    public List<E> findForPaging(String jql, HashMap<String, Object> params, int page, int pageSize,
            String sortCriteria, String order) {
        jql += (sortCriteria == null ? "" : " order by " + sortCriteria + " " + order);
        Query namedQuery = this.getEntityManager().createQuery(jql, entityClass);
        if (params != null)
            for (String oneKey : params.keySet())
                namedQuery.setParameter(oneKey, params.get(oneKey));
        namedQuery.setFirstResult((page - 1) * pageSize);
        namedQuery.setMaxResults(pageSize);
        return (List<E>) namedQuery.getResultList();
    }

    // FIND FOR COUNT
    public long findForCount(String jql, HashMap<String, Object> params) {
        Query namedQuery = this.getEntityManager().createQuery(jql);
        if (params != null)
            for (String oneKey : params.keySet())
                namedQuery.setParameter(oneKey, params.get(oneKey));
        return namedQuery.getResultList().size();
    }

    @SuppressWarnings("unchecked")
    public List<E> findForList(String jql, HashMap<String, Object> params)
    {

        Query namedQuery = this.getEntityManager().createQuery(jql, entityClass);
        if (params != null)
            for (String oneKey : params.keySet())
                namedQuery.setParameter(oneKey, params.get(oneKey));
        return (List<E>) namedQuery.getResultList();
    }

    public List<E> findForList(String jql)
    {
        return this.findForList(jql, null);
    }
}