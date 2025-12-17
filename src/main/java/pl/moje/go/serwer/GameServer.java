package pl.moje.go.serwer;

import pl.moje.go.common.Protocol;

import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;

public class GameServer {

    private final int port;
    private boolean running = true;
    private final GameSession session = new GameSession();

    GameServer(int port){
        this.port = port;
    }

    public void start(){
        System.out.println("Serwer uruchomiony na porcie " + port);

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (running) {
                System.out.println("Czekam na klienta");
                Socket clientSocket = serverSocket.accept();

                Player player = session.registerPlayer();

                if(player == null){
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println(Protocol.MSG_FULL);
                    clientSocket.close();
                    continue;
                }

                ClientHandler handler = new ClientHandler(clientSocket, session, player);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Serwer zamkniety");
    }
}
