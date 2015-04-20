package sse.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sse.dao.impl.MenuDaoImpl;
import sse.entity.Menu;
import sse.entity.User;
import sse.pageModel.TreeModel;

@Service
public class MenuServiceImpl {

    @Autowired
    MenuDaoImpl menuDao;

    // Hardcode now
    /*
     * (non-Javadoc)
     * 
     * @see sse.service.impl.IMenuService#getMenu(sse.pageModel.TreeModel, sse.entitymodel.User)
     */
    public List<TreeModel> getMenu(TreeModel tree, User user) {
        List<Menu> menus = null;
        List<TreeModel> treelist = new ArrayList<TreeModel>();
        if (tree == null || tree.getId() == null)
            menus = menuDao.findTopMenusByRole(user.getRole());
        else
            menus = menuDao.findChildMenusByRoleAndMenuId(tree.getId(), user.getRole());
        for (Menu m : menus)
        {
            TreeModel model = new TreeModel();
            model.setText(m.getName());
            model.setId(m.getId());
            Map<String, String> attributes = new HashMap<String, String>();
            attributes.put("url", m.getUrl());
            model.setChildren(getMenu(model, user));
            model.setAttributes(attributes);
            treelist.add(model);
        }
        return treelist;
    }
}
