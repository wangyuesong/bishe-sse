package sse.excelmodel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project: sse
 * @Title: MatchPairExcelModel.java
 * @Package sse.excelmodel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月15日 下午3:24:05
 * @version V1.0
 */
public class MatchPairExcelModel {
    public String studentAccount;
    public String studentName;
    public String teacherAccount;
    public String teacherName;
    public String matchLevel;
    public String matchType;

    public String getStudentAccount() {
        return studentAccount;
    }

    public void setStudentAccount(String studentAccount) {
        this.studentAccount = studentAccount;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTeacherAccount() {
        return teacherAccount;
    }

    public void setTeacherAccount(String teacherAccount) {
        this.teacherAccount = teacherAccount;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getMatchLevel() {
        return matchLevel;
    }

    public void setMatchLevel(String matchLevel) {
        this.matchLevel = matchLevel;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public MatchPairExcelModel(String studentAccount, String studentName, String teacherAccount, String teacherName,
            String matchLevel, String matchType) {
        super();
        this.studentAccount = studentAccount;
        this.studentName = studentName;
        this.teacherAccount = teacherAccount;
        this.teacherName = teacherName;
        this.matchLevel = matchLevel;
        this.matchType = matchType;
    }

    public static List<String> getColNames()
    {
        List<String> returnList = new ArrayList<String>();
        for (Field f : MatchPairExcelModel.class.getFields())
        {
            returnList.add(f.getName());
        }
        return returnList;
    }

}
