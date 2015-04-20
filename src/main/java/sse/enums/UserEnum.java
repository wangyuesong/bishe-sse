package sse.enums;

public enum UserEnum {

    TEACHER("Teacher"), STUDENT("Student"), ADMIN("Admin"), UNKNOWN("Unknown");

    private final String value;

    private UserEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static UserEnum getType(String value) {
        for (UserEnum type : UserEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

}
