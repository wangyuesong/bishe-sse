package sse.enums;

public enum WillStatusEnum {

    待定("待定"), 拒绝("拒绝"), 接受("接受"), UNKNOWN("Unknown");

    private final String value;

    private WillStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static WillStatusEnum getType(String value) {
        for (WillStatusEnum type : WillStatusEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

}
