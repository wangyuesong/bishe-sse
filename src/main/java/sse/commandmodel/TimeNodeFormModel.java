package sse.commandmodel;

/**
 * @Project: sse
 * @Title: TimeNodeFormModel.java
 * @Package sse.commandmodel
 * @Description: 用于管理员更新TimeNode
 * @author YuesongWang
 * @date 2015年5月12日 下午3:25:07
 * @version V1.0
 */
public class TimeNodeFormModel {
    String tianbaozhiyuan_date;
    String ketishenbao_date;
    String bishejinxing_date;
    String dabianshenqing_date;
    String tianbaozhiyuan_desc;
    String ketishenbao_desc;
    String bishejinxing_desc;
    String dabianshenqing_desc;

    public String getTianbaozhiyuan_date() {
        return tianbaozhiyuan_date;
    }

    public void setTianbaozhiyuan_date(String tianbaozhiyuan_date) {
        this.tianbaozhiyuan_date = tianbaozhiyuan_date;
    }

    public String getKetishenbao_date() {
        return ketishenbao_date;
    }

    public void setKetishenbao_date(String ketishenbao_date) {
        this.ketishenbao_date = ketishenbao_date;
    }

    public String getBishejinxing_date() {
        return bishejinxing_date;
    }

    public void setBishejinxing_date(String bishejinxing_date) {
        this.bishejinxing_date = bishejinxing_date;
    }

    public String getDabianshenqing_date() {
        return dabianshenqing_date;
    }

    public void setDabianshenqing_date(String dabianshenqing_date) {
        this.dabianshenqing_date = dabianshenqing_date;
    }

    public String getTianbaozhiyuan_desc() {
        return tianbaozhiyuan_desc;
    }

    public void setTianbaozhiyuan_desc(String tianbaozhiyuan_desc) {
        this.tianbaozhiyuan_desc = tianbaozhiyuan_desc;
    }

    public String getKetishenbao_desc() {
        return ketishenbao_desc;
    }

    public void setKetishenbao_desc(String ketishenbao_desc) {
        this.ketishenbao_desc = ketishenbao_desc;
    }

    public String getBishejinxing_desc() {
        return bishejinxing_desc;
    }

    public void setBishejinxing_desc(String bishejinxing_desc) {
        this.bishejinxing_desc = bishejinxing_desc;
    }

    public String getDabianshenqing_desc() {
        return dabianshenqing_desc;
    }

    public void setDabianshenqing_desc(String dabianshenqing_desc) {
        this.dabianshenqing_desc = dabianshenqing_desc;
    }

    public TimeNodeFormModel() {
        super();
    }

    public TimeNodeFormModel(String tianbaozhiyuan_date, String ketishenbao_date, String bishejinxing_date,
            String dabianshenqing_date, String tianbaozhiyuan_desc, String ketishenbao_desc, String bishejinxing_desc,
            String dabianshenqing_desc) {
        super();
        this.tianbaozhiyuan_date = tianbaozhiyuan_date;
        this.ketishenbao_date = ketishenbao_date;
        this.bishejinxing_date = bishejinxing_date;
        this.dabianshenqing_date = dabianshenqing_date;
        this.tianbaozhiyuan_desc = tianbaozhiyuan_desc;
        this.ketishenbao_desc = ketishenbao_desc;
        this.bishejinxing_desc = bishejinxing_desc;
        this.dabianshenqing_desc = dabianshenqing_desc;
    }

}
