package pl.moje.go.common;


/**
 * Stan pola na planszy Go:
 * WHITE/BLACK - kamień gracza,
 * NONE - puste pole,
 * DEAD_WHITE/DEAD_BLACK - kamień oznaczony jako martwy do liczenia punktów.
 */

public enum Kolor {
    WHITE,
    BLACK,
    NONE,
    DEAD_WHITE,
    DEAD_BLACK
}
