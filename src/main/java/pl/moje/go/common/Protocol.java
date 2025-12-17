package pl.moje.go.common;

public class Protocol {
    private Protocol() {}

    public static final String CMD_MOVE = "MOVE";
    public static final String CMD_EXIT = "EXIT";

    public static final String MSG_FULL = "FULL";
    public static final String MSG_BYE = "BYE";
    public static final String MSG_MOVE_OK = "Wykonano ruch";
    public static final String MSG_MOVE_INVALID = "Niepoprawny ruch";

    public static final String BOARD_BEGIN = "BOARD";
    public static final String BOARD_END = "END_BOARD";
}
