package sse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.dao.impl.ActionEventDaoImpl;
import sse.dao.impl.UserDaoImpl;

/**
 * @Project: sse
 * @Title: AdminWillServiceImpl.java
 * @Package sse.service.impl
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月8日 上午11:16:22
 * @version V1.0
 */
@Service
public class ActionEventServiceImpl {
    @Autowired
    private ActionEventDaoImpl actionEventDaoImpl;

    @Autowired
    private UserDaoImpl userDaoImpl;

    public void createActionEvent(Integer actorId, Integer listenerId, String description)
    {
        actionEventDaoImpl.createActionEvent(userDaoImpl.findById(actorId), userDaoImpl.findById(listenerId),
                description);

    }
    public void createActionEvent(Integer actorId, String description)
    {
        actionEventDaoImpl.createActionEvent(userDaoImpl.findById(actorId), null,
                description);
    }
}
