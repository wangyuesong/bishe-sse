package sse.enums;

public enum TopicTypeEnum {

    教师选题("教师选题"), 企业项目("企业选题"), 个人选题("个人选题"), UNKNOWN("Unknown");

    private final String value;

    private TopicTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static TopicTypeEnum getType(String value) {
        for (TopicTypeEnum type : TopicTypeEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

}
