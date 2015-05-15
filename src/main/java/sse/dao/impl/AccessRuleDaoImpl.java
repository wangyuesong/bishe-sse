package sse.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import sse.dao.base.GenericDao;
import sse.entity.AccessRule;
import sse.entity.Teacher;

@Repository
public class AccessRuleDaoImpl extends GenericDao<Integer, AccessRule>
{
    public AccessRule findAccessRuleByUrlAndRole(String url, String role)
    {
        String queryStr = "select a from AccessRule a where a.role = :role and a.url = :url";
        List<AccessRule> rules = this.getEntityManager()
                .createQuery(queryStr, AccessRule.class)
                .setParameter("role", role).setParameter("url", url).getResultList();
        if (rules.size() == 0)
            return null;
        else
            return rules.get(0);
    }
}
