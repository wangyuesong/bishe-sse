package sse.commandmodel;

public class WillModel {

    private String studentId;
    private String studentName;
    private String studentAccount;
    // Following string store teacher's id
    private String firstWill;
    private String firstWillTeacherName;
    private String secondWill;
    private String secondWillTeacherName;
    private String thirdWill;
    private String thirdWillTeacherName;

    public WillModel() {
        super();
    }

    public WillModel(String studentId, String studentName, String studentAccount) {
        super();
        this.studentId = studentId;
        this.studentName = studentName;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

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

    public String getFirstWill() {
        return firstWill;
    }

    public void setFirstWill(String firstWill) {
        this.firstWill = firstWill;
    }

    public String getSecondWill() {
        return secondWill;
    }

    public void setSecondWill(String secondWill) {
        this.secondWill = secondWill;
    }

    public String getThirdWill() {
        return thirdWill;
    }

    public void setThirdWill(String thirdWill) {
        this.thirdWill = thirdWill;
    }

    public void setWillTeacherNameLevel(int level, String willContent)
    {
        if (level == 1)
            firstWillTeacherName = willContent;
        if (level == 2)
            secondWillTeacherName = willContent;
        if (level == 3)
            thirdWillTeacherName = willContent;
    }

    public void setWillByLevel(int level, String willContent)
    {
        if (level == 1)
            firstWill = willContent;
        if (level == 2)
            secondWill = willContent;
        if (level == 3)
            thirdWill = willContent;
    }

    public String getWillByLevel(int level)
    {
        if (level == 1)
            return firstWill;
        if (level == 2)
            return secondWill;
        if (level == 3)
            return thirdWill;
        else
            return null;
    }
}
