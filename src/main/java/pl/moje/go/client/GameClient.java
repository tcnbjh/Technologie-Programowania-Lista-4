package pl.moje.go.client;

import pl.moje.go.common.Kolor;
import pl.moje.go.common.Protocol;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class GameClient extends Thread {

    private final GoFrame frame;
    private final BufferedReader in;
    private final PrintWriter out;
    private final int size;

    public GameClient(GoFrame frame, BufferedReader in, PrintWriter out, int size) {
        this.frame = frame;
        this.in = in;
        this.out = out;
        this.size = size;
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                handleMessage(msg);
            }
        } catch (IOException e) {
            System.out.println("Połączenie przerwane: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleMessage(String msg) throws IOException {
        if (msg.equals(Protocol.BOARD_BEGIN)) { //

            Kolor[][] board = new Kolor[size][size];

            // 1. Pomiń linię nagłówka (numery kolumn: "   0 1 2 ...")
            in.readLine();

            // 2. Czytaj wiersze planszy
            for (int y = 0; y < size; y++) {
                String line = in.readLine();
                if (line == null) break;

                // Dzielimy linię po białych znakach (spacjach)
                // Linia wygląda np. tak: " 0 □ □ B W ..."
                // parts[0] to numer wiersza, parts[1] to pierwszy kamień
                String[] parts = line.trim().split("\\s+");

                for (int x = 0; x < size; x++) {
                    // +1 bo pierwszy element (indeks 0) to numer wiersza
                    if (x + 1 < parts.length) {
                        String symbol = parts[x + 1];
                        if (symbol.equals("B")) {
                            board[y][x] = Kolor.BLACK;
                        } else if (symbol.equals("W")) {
                            board[y][x] = Kolor.WHITE;
                        } else {
                            board[y][x] = Kolor.NONE;
                        }
                    }
                }
            }

            // 3. Konsumuj znacznik końca planszy (END_BOARD), aby nie zakłócił pętli głównej
            in.readLine();

            // Odśwież GUI w wątku Swinga/AWT
            EventQueue.invokeLater(() -> frame.updateBoard(board));

        } else if (msg.startsWith("ERROR")) {
            System.out.println("Błąd serwera: " + msg);
        } else if (msg.equals(Protocol.MSG_MOVE_INVALID)) {
            System.out.println("Ruch niepoprawny!");
        }
    }

    public void sendMove(int x, int y) {
        out.println(Protocol.CMD_MOVE + " " + x + " " + y); //
    }

    public void sendExit() {
        out.println(Protocol.CMD_EXIT);
    }
}
