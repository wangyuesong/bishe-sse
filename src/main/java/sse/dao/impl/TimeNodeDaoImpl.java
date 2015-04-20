package sse.dao.impl;

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
            event.setTitle(t.getName());
            event.setDate("" + t.getTime().getTime());
            event.setDescription(t.getDescription());
            eventList.add(event);
        }
        return eventList;
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
