package pl.moje.go.serwer;

import java.net.Socket;
import java.io.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    private Player player;

    ClientHandler(Socket socket, Player player){
        this.socket = socket;
        this.player = player;
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

                if("EXIT".equalsIgnoreCase(line)){
                    System.out.println("Klient zamkniety");
                    break;
                }

                out.println("ECHO: " + line);
            }

        } catch (IOException e) {
            System.out.println("Błąd połączenia z klientem" + e.getMessage());
        } finally {
            try{
                socket.close();
                System.out.println("Socket zamkniety");
            } catch (IOException eignore) {}
        }
    }

}
