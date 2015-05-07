package sse.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import sse.dao.base.GenericDao;
import sse.entity.TimeNode;

@Repository
public class TimeNodeDaoImpl extends GenericDao<Integer, TimeNode>
{
    public List<CalendarEvent> getAllTimeNodes()
    {
        String queryStr = "select t from TimeNode t order by t.time asc";
        List<TimeNode> timeNodes = this.getEntityManager()
                .createQuery(queryStr, TimeNode.class).
                getResultList();
        List<CalendarEvent> eventList = new ArrayList<CalendarEvent>();
        for (TimeNode t : timeNodes)
        {
            CalendarEvent event = new CalendarEvent();
            event.setTitle(t.getName().toString());
            event.setDate("" + t.getTime().getTime());
            event.setDescription(t.getDescription());
            eventList.add(event);
        }
        return eventList;
    }

    public Date getTimeNodeByName(String name)
    {
        String queryStr = "select t from TimeNode t where t.name=:name";
        TimeNode tmNode = this.getEntityManager()
                .createQuery(queryStr, TimeNode.class).setParameter("name", name).
                getResultList().get(0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(tmNode.getTime().toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static class CalendarEvent
    {
        String date;
        String type;
        String title;
        String description;
        String url;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

}
