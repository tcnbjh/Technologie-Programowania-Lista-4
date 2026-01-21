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
            updateStatus("Rozłączono z serwerem.");
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
                        switch (symbol) {
                            case "B": board[y][x] = Kolor.BLACK; break;
                            case "W": board[y][x] = Kolor.WHITE; break;
                            case "b": board[y][x] = Kolor.DEAD_BLACK; break;
                            case "w": board[y][x] = Kolor.DEAD_WHITE; break;
                            default: board[y][x] = Kolor.NONE; break;
                        }
                    }
                }
            }

            // 3. Konsumuj znacznik końca planszy (END_BOARD), aby nie zakłócił pętli głównej
            in.readLine();

            // Odśwież GUI w AWT
            EventQueue.invokeLater(() -> frame.updateBoard(board));

        } else if (msg.startsWith(Protocol.MSG_GAME_OVER)) {
            String[] parts = msg.split(" ");

            String winner = parts[1];
            String blackPts = parts[2];
            String whitePts = parts[3];

            String text = "Koniec gry!\n" +
                    "Wygrał: " + winner + "\n\n" +
                    "Punkty czarnego: " + blackPts + "\n" +
                    "Punkty białego: " + whitePts;

            EventQueue.invokeLater(() -> {
                frame.setMessage("Koniec gry. Wygrał " + winner);
                javax.swing.JOptionPane.showMessageDialog(frame, text, "Wynik", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            });

        } else {
            updateStatus(msg);
        }
    }

    public void sendMove(int x, int y) {
        out.println(Protocol.CMD_MOVE + " " + x + " " + y);
    }

    public void sendExit() {
        out.println(Protocol.CMD_EXIT);
    }

    public void sendPass() {
        out.println(Protocol.CMD_PASS);
        updateStatus("Pasujesz.");
    }

    public void sendSurrender() {
        out.println(Protocol.CMD_FF);
        updateStatus("Poddajesz grę.");
    }

    public void sendConfirm() {
        out.println(Protocol.CMD_CONFIRM);
        updateStatus("Akceptujesz układ.");
    }

    public void sendReject() {
        out.println(Protocol.CMD_REJECT);
        updateStatus("Odrzucasz układ. Wznowienie gry.");
    }

    public void updateStatus(String text) {
        EventQueue.invokeLater(() -> frame.setMessage(text));
    }
}
