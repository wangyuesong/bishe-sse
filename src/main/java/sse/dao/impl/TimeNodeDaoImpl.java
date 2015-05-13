package sse.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import sse.commandmodel.TimeNodeFormModel;
import sse.dao.base.GenericDao;
import sse.entity.TimeNode;
import sse.enums.TimeNodeEnum;
import sse.exception.SSEException;

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

    /**
     * Description: 很二
     * 
     * @param model
     * @throws ParseException
     *             void
     */
    public void updateTimeNodes(TimeNodeFormModel model)
    {
        try {
            this.beginTransaction();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            TimeNode tianbaozhiuan = findTimeNodeByName("填报志愿");
            TimeNode ketishenbao = findTimeNodeByName("课题申报");
            TimeNode bishejinxing = findTimeNodeByName("毕设进行");
            TimeNode dabianshenqing = findTimeNodeByName("答辩申请");
            if (!StringUtils.isEmpty(model.getTianbaozhiyuan_date()))
                tianbaozhiuan.setTime(sdf.parse(model.getTianbaozhiyuan_date()));
            tianbaozhiuan.setDescription(model.getTianbaozhiyuan_desc());
            if (!StringUtils.isEmpty(model.getKetishenbao_date()))
                ketishenbao.setTime(sdf.parse(model.getKetishenbao_date()));
            ketishenbao.setDescription(model.getKetishenbao_desc());
            if (!StringUtils.isEmpty(model.getBishejinxing_date()))
                bishejinxing.setTime(sdf.parse(model.getBishejinxing_date()));
            bishejinxing.setDescription(model.getBishejinxing_desc());
            if (!StringUtils.isEmpty(model.getDabianshenqing_date()))
                dabianshenqing.setTime(sdf.parse(model.getDabianshenqing_date()));
            dabianshenqing.setDescription(model.getBishejinxing_desc());
            this.merge(tianbaozhiuan);
            this.merge(ketishenbao);
            this.merge(bishejinxing);
            this.merge(dabianshenqing);
            this.commitTransaction();

        } catch (Exception e)
        {
            throw new SSEException("时间日期转换失败", e);
        }

    }

    /**
     * Description:
     * 
     * @param name
     * @return
     *         TimeNode
     */
    public TimeNode findTimeNodeByName(String name)
    {
        String queryStr = "select t from TimeNode t where t.name=:name";
        List<TimeNode> tmNodes = this.getEntityManager()
                .createQuery(queryStr, TimeNode.class).setParameter("name", TimeNodeEnum.getType(name)).
                getResultList();
        if (tmNodes.size() != 0)
            return tmNodes.get(0);
        else
            return null;
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
