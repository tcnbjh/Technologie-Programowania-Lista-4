package pl.moje.go.serwer;

import java.net.Socket;
import java.io.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    private Player player;
    private PlayerRegistry registry; // potrzebuje registry, żeby usunąć gracza po wyjściu
    private GameController gameController;

    ClientHandler(Socket socket, Player player,  PlayerRegistry registry, GameController gameController) {
        this.socket = socket;
        this.player = player;
        this.registry = registry;
        this.gameController = gameController;
    }

    @Override
    public void run(){
        System.out.println("Obsługuje klienta w watku: " + Thread.currentThread().getName());

        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("ID gracza: " + player.getId() + ", kolor: " + player.getKolor());

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
            try{
                socket.close();
                System.out.println("Socket zamkniety");
            } catch (IOException eignore) {}

            registry.removePlayer(player); // usuwam gracza z tablicy jak wyjdzie
        }
    }

}
