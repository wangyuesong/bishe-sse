package sse.pageModel;

public class WillModel {

    private String firstWill;
    private String secondWill;
    private String thirdWill;

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
