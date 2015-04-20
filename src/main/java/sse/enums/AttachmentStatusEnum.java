package sse.enums;

public enum AttachmentStatusEnum {

    TEMP("Temp"), FOREVER("Forever"), UNKNOWN("Unknown");

    private final String value;

    private AttachmentStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static AttachmentStatusEnum getType(String value) {
        for (AttachmentStatusEnum type : AttachmentStatusEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

}
