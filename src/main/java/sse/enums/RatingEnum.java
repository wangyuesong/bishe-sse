package sse.enums;

public enum RatingEnum {

    GOOD("Good"), NEEDREVISE("NeedRevise"), BAD("Bad"), UNKNOWN("Unknown");

    private final String value;

    private RatingEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static RatingEnum getType(String value) {
        for (RatingEnum type : RatingEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

}
