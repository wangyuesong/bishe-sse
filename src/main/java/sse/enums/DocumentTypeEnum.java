package sse.enums;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public enum DocumentTypeEnum {

    任务书("任务书"), 开题报告("开题报告"),  最终论文("最终论文"), UNKNOWN("Unknown");

    private final String value;

    private DocumentTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static DocumentTypeEnum getType(String value) {
        for (DocumentTypeEnum type : DocumentTypeEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

    public static DocumentTypeEnum getTypeByPinYin(String value) {
        if (StringUtils.equals(value, "kaitibaogao"))
            return DocumentTypeEnum.开题报告;
        if (StringUtils.equals(value, "renwushu"))
            return DocumentTypeEnum.任务书;
        if (StringUtils.equals(value, "zuizhonglunwen"))
            return DocumentTypeEnum.最终论文;
        return UNKNOWN;
    }

    public static List<String> getAllTypeValues()
    {
        ArrayList<String> returnList = new ArrayList<String>();
        for (DocumentTypeEnum type : DocumentTypeEnum.values())
        {
            if (type.getValue().equals("Unknown"))
                continue;
            else
                returnList.add(type.getValue());
        }
        return returnList;
    }
}
