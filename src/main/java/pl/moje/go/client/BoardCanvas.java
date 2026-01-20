package pl.moje.go.client;

import java.awt.*;
import java.awt.event.*;

public class BoardCanvas extends Canvas {

    public static final int SIZE = 19;
    public static final int CELL = 30;

    private int[][] board = new int[SIZE][SIZE];
    private final GameClient client;

    public BoardCanvas(GameClient client) {
        this.client = client;
        setSize(SIZE * CELL, SIZE * CELL);
        setBackground(new Color(222, 184, 135)); // kolor drewna

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / CELL;
                int y = e.getY() / CELL;

                if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
                    client.sendMove(x, y);
                }
            }
        });
    }

    public void updateBoard(int[][] newBoard) {
        this.board = newBoard;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        // 1️⃣ wyczyść tło
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        drawGrid(g);
        drawStones(g);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);

        for (int i = 0; i < SIZE; i++) {
            g.drawLine(
                    CELL / 2,
                    CELL / 2 + i * CELL,
                    CELL / 2 + (SIZE - 1) * CELL,
                    CELL / 2 + i * CELL
            );

            g.drawLine(
                    CELL / 2 + i * CELL,
                    CELL / 2,
                    CELL / 2 + i * CELL,
                    CELL / 2 + (SIZE - 1) * CELL
            );
        }
    }

    private void drawStones(Graphics g) {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (board[y][x] == 0) continue;

                int px = x * CELL + 4;
                int py = y * CELL + 4;
                int size = CELL - 8;

                if (board[y][x] == 1) {
                    // czarny kamień
                    g.setColor(Color.BLACK);
                    g.fillOval(px, py, size, size);
                } else {
                    // biały kamień (Z OBRYSEM)
                    g.setColor(Color.WHITE);
                    g.fillOval(px, py, size, size);
                    g.setColor(Color.BLACK);
                    g.drawOval(px, py, size, size);
                }
            }
        }
    }
}
