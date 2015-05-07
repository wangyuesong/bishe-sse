package sse.enums;

public enum TimeNodeEnum {

    填报志愿("填报志愿"), 课题申报("申报课题"), 毕设进行("毕设进行"), 答辩申请("答辩申请"), UNKNOWN("UNKNOWN");

    private final String value;

    private TimeNodeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static TimeNodeEnum getType(String value) {
        for (TimeNodeEnum type : TimeNodeEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

}
