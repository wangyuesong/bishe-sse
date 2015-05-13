package sse.dao.impl;

import org.springframework.stereotype.Repository;

import sse.dao.base.GenericDao;
import sse.entity.ActionEvent;
import sse.entity.User;

@Repository
public class ActionEventDaoImpl extends GenericDao<Integer, ActionEvent>
{

    public void createActionEvent(User actor, User listener, String description)
    {
        ActionEvent event = new ActionEvent(actor, listener, description);
        this.persistWithTransaction(event);
    }
}
