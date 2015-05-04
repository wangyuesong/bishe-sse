package sse.enums;

/**
 * @author yuesongwang
 *         该Enum用来纪录老师和学生之间的结对关系是通过何种方式达成的
 */
public enum MatchTypeEnum {

    教师接受("教师接受"), 手工分配("手工分配"), 系统分配("系统分配"), UNKNOWN("Unknown");

    private final String value;

    private MatchTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static MatchTypeEnum getType(String value) {
        for (MatchTypeEnum type : MatchTypeEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

}
