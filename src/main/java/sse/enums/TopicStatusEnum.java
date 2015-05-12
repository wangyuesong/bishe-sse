package sse.enums;

public enum TopicStatusEnum {

    通过("通过"), 待审核("待审核"), 待修正("待修正"), UNKNOWN("Unknown");

    private final String value;

    private TopicStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static TopicStatusEnum getType(String value) {
        for (TopicStatusEnum type : TopicStatusEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

}
