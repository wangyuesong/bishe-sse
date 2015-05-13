package sse.enums;

public enum TimeNodeTypeEnum {

    关键("关键"), 普通("普通"), UNKNOWN("UNKNOWN");

    private final String value;

    private TimeNodeTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static TimeNodeTypeEnum getType(String value) {
        for (TimeNodeTypeEnum type : TimeNodeTypeEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

}
