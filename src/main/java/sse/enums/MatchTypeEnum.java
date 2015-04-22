package sse.enums;

/**
 * @author yuesongwang
 *         该Enum用来纪录老师和学生之间的结对关系是通过第几志愿达成的
 *         1:第一志愿 。。。 0:管理员分配
 */
public enum MatchTypeEnum {

    First("第一志愿"), Second("第二志愿"), Third("第三志愿"), Manual("管理员分配"), UNKNOWN("Unknown");

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

    public static MatchTypeEnum getTypeByIntLevel(int level) {
        if (level == 1)
            return First;
        if (level == 2)
            return Second;
        if (level == 3)
            return Third;
        if (level == 0)
            return Manual;
        else
            return UNKNOWN;
    }
}
