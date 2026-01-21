package pl.moje.go.serwer;

import pl.moje.go.command.Command;
import pl.moje.go.command.CommandFactory;
import pl.moje.go.command.ExitCommand;

import java.net.Socket;
import java.io.*;

import static  pl.moje.go.common.Protocol.*;

/** Wątek obsługi klienta: odbiera komendy z socketu i wysyła wiadomości/stan planszy. */

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final GameSession session;
    private final Player player;

    private PrintWriter out;

    ClientHandler(Socket socket, GameSession session, Player player) {
        this.socket = socket;
        this.session = session;
        this.player = player;
    }

    public synchronized void sendBoard(String asciiBoard) {
        out.println(BOARD_BEGIN);
        for (String row : asciiBoard.split("\n")) {
            out.println(row);
        }
        out.println(BOARD_END);
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    @Override
    public void run(){
        System.out.println("Obsługuje klienta w watku: " + Thread.currentThread().getName());

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {

            this.out = new PrintWriter(socket.getOutputStream(), true);

            ClientContext context = new ClientContext(session, player, out);

            session.registerClient(this);

            out.println("ID gracza: " + player.getId() + ", kolor: " + player.getKolor());

            session.broadcastBoard();

            String line;
            while((line = in.readLine()) != null){

                Command command = CommandFactory.fromLine(line);
                command.execute(context);

                if (command.shouldTerminate()) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Błąd połączenia z klientem" + e.getMessage());
        } finally {
            session.unregisterClient(this);
            session.unregisterPlayer(player);
            try{
                socket.close();
            } catch (Exception ignored) {}
        }
    }

}
