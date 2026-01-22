package pl.moje.go.common;


/** Stałe protokołu komunikacji (komendy, odpowiedzi i znaczniki planszy). **/

public class Protocol {
    private Protocol() {}

    public static final String CMD_MOVE = "MOVE";
    public static final String CMD_EXIT = "EXIT";
    public static final String CMD_PASS = "PASS";
    public static final String CMD_FF = "FF";
    public static final String CMD_CONFIRM = "CONFIRM";
    public static final String CMD_REJECT = "REJECT";

    public static final String MSG_FULL = "FULL";
    public static final String MSG_BYE = "BYE";
    public static final String MSG_MOVE_OK = "Wykonano ruch";
    public static final String MSG_MOVE_INVALID = "Niepoprawny ruch";
    public static final String MSG_GAME_OVER = "GAME_OVER";
    public static final String MSG_TURN = "TURN";

    public static final String BOARD_BEGIN = "BOARD";
    public static final String BOARD_END = "END_BOARD";
}
