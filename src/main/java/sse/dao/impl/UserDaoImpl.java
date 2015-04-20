package sse.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import sse.dao.base.GenericDao;
import sse.entity.User;

@Repository
public class UserDaoImpl extends GenericDao<Integer, User> {

    public User findUserByAccount(String account) {
        String queryStr = "select u from User u where u.account = :account";
        List<User> users = this.getEntityManager()
                .createQuery(queryStr, User.class)
                .setParameter("account", account).getResultList();
        if (users.size() != 0)
            return users.get(0);
        else
            return null;
    }

}
