package sse.enums;

public enum StatusEnum {

    PENDING("Pending"), REJECTED("Rejected"), ACCEPTED("Accepted"), UNKNOWN("Unknown");

    private final String value;

    private StatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static StatusEnum getType(String value) {
        for (StatusEnum type : StatusEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

}
