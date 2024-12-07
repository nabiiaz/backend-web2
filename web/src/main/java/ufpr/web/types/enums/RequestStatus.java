package ufpr.web.types.enums;

public enum RequestStatus {
    ABERTA (0),
    ORÇADA (1),
    APROVADA (2),
    REJEITADA (3),
    ARRUMADA (4),
    PAGA (5),
    CONCLUIDA (6);

    private final int value;

    RequestStatus(int value) {
            this.value = value;
        }

    public int getValue() {
        return value;
    }

    public static RequestStatus fromInt(int i) {
        for (RequestStatus level : RequestStatus.values()) {
            if (level.getValue() == i) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + i);
    }
}
