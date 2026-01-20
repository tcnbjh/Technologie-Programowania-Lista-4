package pl.moje.go.client;

import java.awt.*;
import java.io.*;
import java.net.*;

public class GameClient {

    private final String host;
    private final int port;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private BoardCanvas boardCanvas;
    private java.awt.TextArea messages;

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setBoardCanvas(BoardCanvas boardCanvas) {
        this.boardCanvas = boardCanvas;
    }

    public void setMessages(java.awt.TextArea messages) {
        this.messages = messages;
    }

    public void start() {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = in.readLine()) != null) {
                handleServerMessage(line);
            }

        } catch (IOException e) {
            appendMessage("Błąd połączenia z serwerem");
        }
    }

    public void sendMove(int x, int y) {
        if (out != null) {
            out.println("MOVE " + x + " " + y);
        }
    }

    private void handleServerMessage(String firstLine) {
        // ASCII board zaczyna się od linii z numerami kolumn
        if (firstLine.contains("0") && firstLine.contains("1") && firstLine.contains("18")) { //poprawka
            StringBuilder asciiBoard = new StringBuilder();
            asciiBoard.append(firstLine).append("\n");

            try {
                for (int i = 0; i < 19; i++) {
                    asciiBoard.append(in.readLine()).append("\n");
                }
            } catch (IOException e) {
                return;
            }

            int[][] board = parseAsciiBoard(asciiBoard.toString());
            EventQueue.invokeLater(() -> boardCanvas.updateBoard(board)); //poprawka
        } else {
            appendMessage(firstLine);
        }
    }

    private int[][] parseAsciiBoard(String ascii) {
        int[][] board = new int[19][19];
        String[] lines = ascii.split("\n");

        // linia 0 = nagłówek z numerami kolumn
        for (int y = 1; y <= 19; y++) {
            String line = lines[y];
            int x = 0;

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                if (c == 'X') board[y - 1][x++] = 1;
                else if (c == 'O') board[y - 1][x++] = 2;
                else if (c == '.') board[y - 1][x++] = 0;

                if (x == 19) break;
            }
        }
        return board;
    }

    private void appendMessage(String msg) {
        if (messages != null) {
            messages.append(msg + "\n");
        }
    }
}
