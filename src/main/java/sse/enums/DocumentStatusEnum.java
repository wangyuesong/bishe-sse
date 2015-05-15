package sse.enums;

import java.util.ArrayList;
import java.util.List;


public enum DocumentStatusEnum {

    未开始("未开始"), 进行中("进行中"), 完成("完成"), UNKNOWN("Unknown");

    private final String value;

    private DocumentStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static DocumentStatusEnum getType(String value) {
        for (DocumentStatusEnum type : DocumentStatusEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

    public static List<String> getAllTypeValues()
    {
        ArrayList<String> returnList = new ArrayList<String>();
        for (DocumentStatusEnum type : DocumentStatusEnum.values())
        {
            if (type.getValue().equals("Unknown"))
                continue;
            else
                returnList.add(type.getValue());
        }
        return returnList;
    }
}
