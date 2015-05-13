package sse.dao.base;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import sse.entity.Teacher;

/**
 * @Project: sse
 * @Title: GenericDao.java
 * @Package sse.dao.base
 * @Description: 基础Dao
 * @author YuesongWang
 * @date 2015年5月8日 上午10:46:13
 * @version V1.0
 */
public abstract class GenericDao<K, E> implements Dao<K, E> {
    protected Class<E> entityClass;

    @PersistenceUnit
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public GenericDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
    }

    /**
     * Description: 开始事务
     * 
     * @param
     * @return void
     */
    public void beginTransaction()
    {
        if (!this.getEntityManager().getTransaction().isActive())
            this.getEntityManager().getTransaction().begin();

    }

    /**
     * Description: 提交事务
     * 
     * @param
     * @return void
     */
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

    /**
     * Description: 带事务的persist
     * 
     * @param @param entity
     * @return void
     */
    public void persistWithTransaction(E entity)
    {
        beginTransaction();
        this.getEntityManager().persist(entity);
        commitTransaction();
    }

    /**
     * Description: 带事务merge
     * 
     * @param entity
     *            void
     */
    public void mergeWithTransaction(E entity)
    {
        beginTransaction();
        this.getEntityManager().merge(entity);
        commitTransaction();
    }

    /**
     * Description: 带事务remove
     * 
     * @param entity
     *            void
     */
    public void removeWithTransaction(E entity)
    {
        beginTransaction();
        this.getEntityManager().remove(entity);
        commitTransaction();
    }

    /**
     * Description: 更新实体使之与数据库同步
     * 
     * @param entity
     *            void
     */
    public void refresh(E entity)
    {
        this.getEntityManager().refresh(entity);
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

    /**
     * Description: 查找所有
     * 
     * @return
     *         List<E>
     */
    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        return this.getEntityManager().createQuery("select w from " + entityClass.getName() + " w")
                .getResultList();
    }

    /**
     * Description: 带分页和排序,带参数表，返回(page-1)*pageSize到page*pageSize的纪录
     * 
     * @param jql
     * @param params
     * @param page
     * @param pageSize
     * @param sortCriteria
     * @param order
     * @return
     *         List<E>
     */
    @SuppressWarnings("unchecked")
    public List<E> findForPaging(String jql, HashMap<String, Object> params, int page, int pageSize,
            String sortCriteria, String order) {
        jql += ((sortCriteria == null || order == null) ? "" : " order by " + sortCriteria + " " + order);
        Query namedQuery = this.getEntityManager().createQuery(jql, entityClass);
        if (params != null)
            for (String oneKey : params.keySet())
                namedQuery.setParameter(oneKey, params.get(oneKey));
        if (page != 0) {
            namedQuery.setFirstResult((page - 1) * pageSize);
            if (pageSize != 0)
                namedQuery.setMaxResults(pageSize);
        }
        return (List<E>) namedQuery.getResultList();
    }

    /**
     * Description: 不带分页和排序的，带参数表，返回全部纪录
     * 
     * @param @param jql
     * @param @param params
     * @param @return
     * @return List<E>
     */
    @SuppressWarnings("unchecked")
    public List<E> findForPaging(String jql, HashMap<String, Object> params)
    {

        Query namedQuery = this.getEntityManager().createQuery(jql, entityClass);
        if (params != null)
            for (String oneKey : params.keySet())
                namedQuery.setParameter(oneKey, params.get(oneKey));
        return (List<E>) namedQuery.getResultList();
    }

    /**
     * Description: 不带分页和排序，也不带参数的，返回全部纪录
     * 
     * @param @param jql
     * @param @return
     * @return List<E>
     */
    public List<E> findForPaging(String jql)
    {
        return this.findForPaging(jql, null);
    }

    /**
     * Description: 只带排序的，返回全部纪录
     * 
     * @param @param jql
     * @param @param params
     * @param @param sortCriteria
     * @param @param order
     * @param @return
     * @return List<E>
     */
    @SuppressWarnings("unchecked")
    public List<E> findForPaging(String jql, HashMap<String, Object> params, String sortCriteria, String order)
    {
        jql += ((sortCriteria == null || order == null) ? "" : " order by " + sortCriteria + " " + order);
        Query namedQuery = this.getEntityManager().createQuery(jql, entityClass);
        if (params != null)
            for (String oneKey : params.keySet())
                namedQuery.setParameter(oneKey, params.get(oneKey));
        return (List<E>) namedQuery.getResultList();
    }

    /**
     * Description: 返回某表的所有纪录的计数，用于EasyUi分页时提供记录总数，便于分页
     * 
     * @return
     *         int
     */
    public int findAllForCount() {
        String sql = "select a from " + entityClass.getName() + " a";
        return findForCount(sql, null);
    }

    /**
     * Description: 返回符合该查询条件的所有纪录的计数，用于EasyUi分页时提供记录总数，便于分页
     * 
     * @param jql
     * @param params
     * 
     * @return long
     */
    public int findForCount(String jql, HashMap<String, Object> params) {
        Query namedQuery = this.getEntityManager().createQuery(jql);
        if (params != null)
            for (String oneKey : params.keySet())
                namedQuery.setParameter(oneKey, params.get(oneKey));
        return namedQuery.getResultList().size();
    }

}