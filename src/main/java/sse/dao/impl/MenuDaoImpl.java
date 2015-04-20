package sse.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import sse.dao.base.GenericDao;
import sse.entity.Menu;

@Repository
public class MenuDaoImpl extends GenericDao<Integer, Menu> 
         {

    public List<Menu> findMenusByRole(String role) {
        String queryStr = "select m from Menu m where m.role = :role";
        List<Menu> menus = this.getEntityManager()
                .createQuery(queryStr, Menu.class)
                .setParameter("role", role).getResultList();
        return menus;
    }

    public List<Menu> findTopMenusByRole(String role) {
        String queryStr = "select m from Menu m where m.role = :role and m.parentId is NULL";
        List<Menu> menus = this.getEntityManager()
                .createQuery(queryStr, Menu.class)
                .setParameter("role", role).
                getResultList();
        return menus;
    }
    
    public List<Menu> findChildMenusByRoleAndMenuId(int id, String role) {
        String queryStr = "select m from Menu m where m.role = :role and m.parentId= :parentId" ;
        List<Menu> menus = this.getEntityManager()
                .createQuery(queryStr, Menu.class)
                .setParameter("role", role)
                .setParameter("parentId", id).
                getResultList();
        return menus;
    }
    

}
