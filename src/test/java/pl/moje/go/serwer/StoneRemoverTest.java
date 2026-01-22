package pl.moje.go.serwer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.moje.go.common.Kolor;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class StoneRemoverTest {

    private StoneRemover remover;
    private Kolor[][] board;
    private final int SIZE = 19;

    @BeforeEach
    void setUp() {
        remover = new StoneRemover();
        board = new Kolor[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = Kolor.NONE;
    }

    @Test
    void testCaptureSingleStone() {
        board[1][1] = Kolor.WHITE;

        board[0][1] = Kolor.BLACK;
        board[2][1] = Kolor.BLACK;
        board[1][0] = Kolor.BLACK;

        board[1][2] = Kolor.BLACK;

        ArrayList<Integer> result = remover.removeAround(1, 2, board, Kolor.BLACK);

        assertNotNull(result);
        assertTrue(result.contains(1));
        assertTrue(result.contains(1));
        assertEquals(1, result.get(result.size() - 1));
    }
}
