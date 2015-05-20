package sse.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sse.commandmodel.BasicJson;
import sse.pagemodel.GenericDataGrid;
import sse.service.impl.PersonalInfoServiceImpl;
import sse.service.impl.PersonalInfoServiceImpl.PersonalInfoEntryModel;

/**
 * @Project: sse
 * @Title: DocumentController.java
 * @Package sse.controller
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月13日 上午10:41:39
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/personal")
public class PersonalInfoController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PersonalInfoController.class);

    @Autowired
    public PersonalInfoServiceImpl personalInfoServiceImpl;

    /**
     * Description: 根据userId获取用户的信息
     * 
     * @param userId
     * @return
     *         GenericDataGrid<PersonalInfoModel>
     */
    @ResponseBody
    @RequestMapping(value = "/getPersonalInfo")
    public GenericDataGrid<PersonalInfoEntryModel> getPersonalInfo(int userId)
    {
        return personalInfoServiceImpl.getPersonalInfo(userId);
    }

    @ResponseBody
    @RequestMapping(value = "/saveTeacherPersonalInfo")
    public BasicJson saveTeacherPersonalInfo(
            @RequestBody PersonalInfoEntries entries)
    {
        return personalInfoServiceImpl.saveTeacherPersonalInfo(entries.getStudentId(), entries.getModels());
    }

    @ResponseBody
    @RequestMapping(value = "/saveStudentPersonalInfo")
    public BasicJson saveStudentPersonalInfo(
            @RequestBody PersonalInfoEntries entries)
    {
        return personalInfoServiceImpl.saveStudentPersonalInfo(entries.getStudentId(), entries.getModels());
    }

    public static class PersonalInfoEntries
    {
        int studentId;
        ArrayList<PersonalInfoEntryModel> models;

        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public ArrayList<PersonalInfoEntryModel> getModels() {
            return models;
        }

        public void setModels(ArrayList<PersonalInfoEntryModel> models) {
            this.models = models;
        }

        public PersonalInfoEntries(int studentId, ArrayList<PersonalInfoEntryModel> models) {
            super();
            this.studentId = studentId;
            this.models = models;
        }

    }
}
