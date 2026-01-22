package pl.moje.go.serwer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.moje.go.common.Kolor;

import static org.junit.jupiter.api.Assertions.*;

class TeritoryCounterTest {

    private TeritoryCounter counter;
    private Kolor[][] board;
    private final int SIZE = 19;

    @BeforeEach
    void setUp() {
        counter = new TeritoryCounter();
        board = new Kolor[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = Kolor.NONE;
    }

    @Test
    void testSinglePointTerritory() {

        board[0][1] = Kolor.BLACK;
        board[1][0] = Kolor.BLACK;

        board[18][18] = Kolor.WHITE;

        int[] result = counter.count(board);

        assertEquals(1, result[0], "Czarne powinny mieć 1 punkt terytorium");
        assertEquals(0, result[1], "Białe powinny mieć 0 punktów");
    }
}
