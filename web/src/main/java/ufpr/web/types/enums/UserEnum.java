package ufpr.web.types.enums;


public enum UserEnum {
    
    CUSTOMER (0),
    EMPLOYEE (1);

    private final int value;

    UserEnum(int value) {
            this.value = value;
        }

    public int getValue() {
        return value;
    }

    public static UserEnum fromInt(int i) {
        for (UserEnum level : UserEnum.values()) {
            if (level.getValue() == i) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + i);
    }

}
