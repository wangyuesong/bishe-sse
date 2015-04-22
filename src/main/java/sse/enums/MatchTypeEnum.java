package sse.enums;

/**
 * @author yuesongwang
 *         该Enum用来纪录老师和学生之间的结对关系是通过第几志愿达成的
 *         1:第一志愿 。。。 0:管理员分配
 */
public enum MatchTypeEnum {

    第一志愿("第一志愿"), 第二志愿("第二志愿"), 第三志愿("第三志愿"), 管理员分配("管理员分配"), UNKNOWN("Unknown");

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
            return 第一志愿;
        if (level == 2)
            return 第二志愿;
        if (level == 3)
            return 第三志愿;
        if (level == 0)
            return 管理员分配;
        else
            return UNKNOWN;
    }
}
