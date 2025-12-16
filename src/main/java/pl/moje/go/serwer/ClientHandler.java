package pl.moje.go.serwer;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    private static final List<ClientHandler> handlers = new ArrayList<>();

    private static synchronized void registerHandler(ClientHandler handler) {
        handlers.add(handler);
    }

    private static synchronized void unregisterHandler(ClientHandler handler) {
        handlers.remove(handler);
    }

    public static synchronized void broadcastBoard(GameController gameController) {
        String ascii = gameController.getBoard().toAscii();
        for (ClientHandler handler : handlers) {
            handler.sendBoard(ascii);
        }
    }

    private Socket socket;
    private Player player;
    private PlayerRegistry registry;
    private GameController gameController;
    private PrintWriter out;

    ClientHandler(Socket socket, Player player,  PlayerRegistry registry, GameController gameController) {
        this.socket = socket;
        this.player = player;
        this.registry = registry;
        this.gameController = gameController;
    }

    void sendBoard(String asciiBoard) {
        if (out == null) {
            return;
        }
        out.println("BOARD");
        for (String row : asciiBoard.split("\n")) {
            out.println(row);
        }
        out.println("END_BOARD");
    }

    @Override
    public void run(){
        System.out.println("Obsługuje klienta w watku: " + Thread.currentThread().getName());

        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            this.out = writer;

            registerHandler(this);

            out.println("ID gracza: " + player.getId() + ", kolor: " + player.getKolor());

            ClientHandler.broadcastBoard(gameController);

            String line;
            while((line = in.readLine()) != null){
                System.out.println("[" + socket.getInetAddress() + "] " + line);

                String trimmedLine = line.trim();
                if(trimmedLine.isEmpty()){
                    continue;
                }

                if("EXIT".equalsIgnoreCase(line)){
                    System.out.println("Klient zamkniety");
                    out.println("BYE");
                    break;
                }

                String[] parts = trimmedLine.split(" ");
                String command = parts[0].toUpperCase();

                switch (command){
                    case "MOVE":
                        if(parts.length != 3){
                            out.println("Niepoprawna komenda, sprobój: MOVE X Y");
                            break;
                        }
                        try{
                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);

                            boolean ok = gameController.makeMove(x, y, player.getKolor());

                            if (ok){
                                out.println("Wykonano ruch.");
                                ClientHandler.broadcastBoard(gameController);
                            } else {
                                out.println("MOVE_INVALID");
                            }
                        } catch (NumberFormatException e){
                            out.println("x i y musza byc liczbami całkowitymi");
                        }
                        break;

                    default:
                        out.println("ERROR: Nieznana komenda: " + command);
                }
            }

        } catch (IOException e) {
            System.out.println("Błąd połączenia z klientem" + e.getMessage());
        } finally {
            unregisterHandler(this);
            registry.removePlayer(player);
            try{
                socket.close();
                System.out.println("Socket zamkniety");
            } catch (IOException eignore) {}
        }
    }

}
