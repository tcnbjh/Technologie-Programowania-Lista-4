package pl.moje.go.client;

import pl.moje.go.common.Kolor;
import pl.moje.go.common.Protocol;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Wątek odpowiedzialny za komunikację sieciową z serwerem gry.
 * <p>
 * Klasa ta nasłuchuje wiadomości przychodzących od serwera (np. stan planszy,
 * informacja o turze, wynik gry) i na ich podstawie aktualizuje stan
 * graficznego interfejsu użytkownika {@link GoFrame}.
 * Udostępnia również metody do wysyłania komend gracza (ruch, pas, poddanie).
 */
public class GameClient extends Thread {

    private final GoFrame frame;
    private final BufferedReader in;
    private final PrintWriter out;
    private final int size;
    private Kolor myColor;

    /**
     * Konstruktor klienta gry.
     *
     * @param frame referencja do głównego okna aplikacji (do aktualizacji GUI)
     * @param in    strumień wejściowy do czytania komunikatów z serwera
     * @param out   strumień wyjściowy do wysyłania komend do serwera
     * @param size  rozmiar planszy (np. 19)
     */
    public GameClient(GoFrame frame, BufferedReader in, PrintWriter out, int size) {
        this.frame = frame;
        this.in = in;
        this.out = out;
        this.size = size;
    }

    /**
     * Główna pętla wątku.
     * Czyta linie tekstu z serwera i przekazuje je do metody {@link #handleMessage(String)}.
     * Działa do momentu zerwania połączenia.
     */
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

    /**
     * Interpretuje surowy komunikat tekstowy otrzymany od serwera.
     * Obsługuje protokół gry (BOARD, MOVE_OK, TURN, GAME_OVER itd.).
     *
     * @param msg treść komunikatu
     * @throws IOException w przypadku błędu operacji wejścia/wyjścia
     */
    private void handleMessage(String msg) throws IOException {
        if (msg.equals(Protocol.BOARD_BEGIN)) { //

            Kolor[][] board = new Kolor[size][size];

            in.readLine();

            for (int y = 0; y < size; y++) {
                String line = in.readLine();
                if (line == null) break;

                String[] parts = line.trim().split("\\s+");

                for (int x = 0; x < size; x++) {
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

            in.readLine();

            EventQueue.invokeLater(() -> frame.updateBoard(board));

        } else if (msg.startsWith("ID gracza:")) {
            if (msg.contains("BLACK")) {
                myColor = Kolor.BLACK;
            } else {
                myColor = Kolor.WHITE;
            }
            updateStatus("Jesteś graczem: " + myColor + ". Oczekiwanie na start...");
            EventQueue.invokeLater(() -> frame.setTitle("Go - Gracz: " + myColor));
        } else if (msg.startsWith(Protocol.MSG_TURN)) {
            String parts[] = msg.split(" ");
            String turnColorStr = parts[1];
            Kolor turnColor = turnColorStr.equals("BLACK") ? Kolor.BLACK : Kolor.WHITE;

            String statusText;

            if (myColor == null) {
                statusText = "Tura: " + turnColor;
            } else if (turnColor == myColor) {
                statusText = "TWÓJ RUCH (" + turnColor + ")...";
            } else {
                statusText = "Ruch przeciwnika (" + turnColor + ")...";
            }
            frame.setTurnInfo(statusText);
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

    /**
     * Wysyła do serwera komendę wykonania ruchu (postawienia kamienia).
     *
     * @param x współrzędna kolumny
     * @param y współrzędna wiersza
     */
    public void sendMove(int x, int y) {
        out.println(Protocol.CMD_MOVE + " " + x + " " + y);
    }

    /**
     * Wysyła do serwera komendę wyjścia.
     */
    public void sendExit() {
        out.println(Protocol.CMD_EXIT);
    }

    /**
     * Wysyła do serwera komendę spasowania (PASS).
     */
    public void sendPass() {
        out.println(Protocol.CMD_PASS);
        updateStatus("Pasujesz.");
    }

    /**
     * Wysyła do serwera komendę poddania gry (FF).
     */
    public void sendSurrender() {
        out.println(Protocol.CMD_FF);
        updateStatus("Poddajesz grę.");
    }

    /**
     * Wysyła do serwera potwierdzenie akceptacji układu martwych kamieni.
     */
    public void sendConfirm() {
        out.println(Protocol.CMD_CONFIRM);
        updateStatus("Akceptujesz układ.");
    }

    /**
     * Wysyła do serwera odrzucenie układu martwych kamieni (wznowienie gry).
     */
    public void sendReject() {
        out.println(Protocol.CMD_REJECT);
        updateStatus("Odrzucasz układ. Wznowienie gry.");
    }

    public void updateStatus(String text) {
        EventQueue.invokeLater(() -> frame.setMessage(text));
    }
}
