package sse.commandmodel;

import sse.entity.Student;
import sse.entity.Teacher;
import sse.enums.MatchLevelEnum;

public class MatchPair
{

    private String studentId;
    private String studentAccount;
    private String studentName;
    private String teacherId;
    private String teacherAccount;
    private String teacherName;
    private MatchLevelEnum matchLevel;

    public MatchPair(Student student, Teacher t, MatchLevelEnum macthLevels)
    {
        this.studentId = student.getId() + "";
        this.studentAccount = student.getAccount();
        this.studentName = student.getName();
        if (t != null)
        {
            this.teacherAccount = t.getAccount();
            this.teacherId = t.getId() + "";
            this.teacherName = t.getName();
        }
        this.matchLevel = macthLevels;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

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

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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

    public MatchLevelEnum getMatchLevel() {
        return matchLevel;
    }

    public void setMatchLevel(MatchLevelEnum matchLevel) {
        this.matchLevel = matchLevel;
    }

}