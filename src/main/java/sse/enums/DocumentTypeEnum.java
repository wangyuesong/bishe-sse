package sse.enums;

import java.util.ArrayList;
import java.util.List;

public enum DocumentTypeEnum {

    RenWuShu("任务书"), OpenDefense("开题报告"), MiddleDefense("中期自查"), FinalDefense("最终答辩"), UNKNOWN("Unknown");

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
