package pl.moje.go.client;

import pl.moje.go.common.Kolor;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardCanvas extends Canvas {

    private final int size;
    private Kolor[][] board;
    private GameClient client;

    private int cell;
    private int offset;

    public BoardCanvas(int size) {
        this.size = size;
        this.board = new Kolor[size][size];
        clearBoard();

        setBackground(new Color(220, 179, 92)); // Kolor drewna (Goban)

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    public void setClient(GameClient client) {
        this.client = client;
    }

    public void setBoard(Kolor[][] board) {
        this.board = board;
        repaint();
    }

    private void clearBoard() {
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                board[y][x] = Kolor.NONE;
    }

    private void updateGeometry() {
        // Obliczamy rozmiar komórki tak, aby plansza mieściła się w oknie z marginesem
        int minDym = Math.min(getWidth(), getHeight());
        cell = minDym / (size + 1);
        offset = cell; // Margines równy jednej kratce
    }

    private void handleClick(int mx, int my) {
        if (client == null) return;
        updateGeometry();

        // Prosta matematyka do znalezienia najbliższego przecięcia
        // Dodajemy 0.5 (czyli cell/2) przed dzieleniem dla zaokrąglenia (round logic)
        float fx = (float)(mx - offset) / cell;
        float fy = (float)(my - offset) / cell;

        int x = Math.round(fx);
        int y = Math.round(fy);

        if (x >= 0 && x < size && y >= 0 && y < size) {
            client.sendMove(y, x);
        }
    }

    @Override
    public void paint(Graphics g) {
        updateGeometry();

        // Rysowanie linii siatki
        g.setColor(Color.BLACK);
        for (int i = 0; i < size; i++) {
            int p = offset + i * cell;
            // Linie pionowe
            g.drawLine(offset + i * cell, offset, offset + i * cell, offset + (size - 1) * cell);
            // Linie poziome
            g.drawLine(offset, p, offset + (size - 1) * cell, p);
        }

        // Rysowanie kropek (hoshi) - opcjonalne, ale pomocne na planszy 19x19
        if (size == 19) {
            drawHoshi(g, 3, 3);
            drawHoshi(g, 15, 3);
            drawHoshi(g, 3, 15);
            drawHoshi(g, 15, 15);
            drawHoshi(g, 9, 9);
        }

        // Rysowanie kamieni
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Kolor k = board[y][x];
                if (k == Kolor.NONE) continue;

                int cx = offset + x * cell;
                int cy = offset + y * cell;
                int r = cell * 9 / 20; // Promień kamienia (trochę mniejszy niż pół kratki)

                if (k == Kolor.BLACK) {
                    g.setColor(Color.BLACK);
                    g.fillOval(cx - r, cy - r, 2 * r, 2 * r);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillOval(cx - r, cy - r, 2 * r, 2 * r);
                    g.setColor(Color.BLACK); // Obwódka dla białego kamienia
                    g.drawOval(cx - r, cy - r, 2 * r, 2 * r);
                }
            }
        }
    }

    private void drawHoshi(Graphics g, int x, int y) {
        int cx = offset + x * cell;
        int cy = offset + y * cell;
        int r = 3;
        g.fillOval(cx - r, cy - r, 2 * r, 2 * r);
    }
}
