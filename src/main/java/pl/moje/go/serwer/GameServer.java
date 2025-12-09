package pl.moje.go.serwer;

import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;


public class GameServer {

    private final int port;
    private boolean running = true;
    private final PlayerRegistry playerRegistry = new PlayerRegistry();

    GameServer(int port){
        this.port = port;
    }

    public void start(){
        System.out.println("Serwer uruchomiony na porcie " + port);

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (running) {
                System.out.println("Czekam na klienta");
                Socket clientSocket = serverSocket.accept();

                Player player = playerRegistry.registerNewPlayer();

                if(player == null){
                    System.out.println("Gra jest już pełna -> odrzucam");
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("FULL");
                    clientSocket.close();
                } else {
                    System.out.println("Nowy klient: " + clientSocket.getInetAddress());
                    ClientHandler handler = new ClientHandler(clientSocket, player);
                    Thread thread = new Thread(handler);
                    thread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Serwer zamkniety");
    }

    public void stop(){
        running = false;
    }

}
