package sse.excelmodel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project: sse
 * @Title: WillExcelModel.java
 * @Package sse.excelmodel
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月15日 下午3:38:34
 * @version V1.0
 */
public class WillExcelModel {
    public String studentName;
    public String studentAccount;
    public String firstWillTeacherName;
    public String secondWillTeacherName;
    public String thirdWillTeacherName;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAccount() {
        return studentAccount;
    }

    public void setStudentAccount(String studentAccount) {
        this.studentAccount = studentAccount;
    }

    public String getFirstWillTeacherName() {
        return firstWillTeacherName;
    }

    public void setFirstWillTeacherName(String firstWillTeacherName) {
        this.firstWillTeacherName = firstWillTeacherName;
    }

    public String getSecondWillTeacherName() {
        return secondWillTeacherName;
    }

    public void setSecondWillTeacherName(String secondWillTeacherName) {
        this.secondWillTeacherName = secondWillTeacherName;
    }

    public String getThirdWillTeacherName() {
        return thirdWillTeacherName;
    }

    public void setThirdWillTeacherName(String thirdWillTeacherName) {
        this.thirdWillTeacherName = thirdWillTeacherName;
    }

    public WillExcelModel(String studentName, String studentAccount, String firstWillTeacherName,
            String secondWillTeacherName, String thirdWillTeacherName) {
        super();
        this.studentName = studentName;
        this.studentAccount = studentAccount;
        this.firstWillTeacherName = firstWillTeacherName;
        this.secondWillTeacherName = secondWillTeacherName;
        this.thirdWillTeacherName = thirdWillTeacherName;
    }

    public static List<String> getColNames()
    {
        List<String> returnList = new ArrayList<String>();
        for (Field f : WillExcelModel.class.getFields())
        {
            returnList.add(f.getName());
        }
        return returnList;
    }

}
