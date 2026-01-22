package pl.moje.go.serwer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.moje.go.common.Kolor;

import static org.junit.jupiter.api.Assertions.*;

class MoveValidatorTest {

    private MoveValidator validator;
    private Kolor[][] board;
    private final int SIZE = 19;

    @BeforeEach
    void setUp() {
        validator = new MoveValidator();
        board = new Kolor[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = Kolor.NONE;
            }
        }
    }

    @Test
    void testSuicideMove() {
        board[1][0] = Kolor.WHITE;
        board[0][1] = Kolor.WHITE;
        board[2][1] = Kolor.WHITE;
        board[1][2] = Kolor.WHITE;

        boolean result = validator.isValidMove(board, 1, 1, Kolor.BLACK);

        assertFalse(result, "Ruch samobójczy nie powinien być dozwolony");
    }
}
