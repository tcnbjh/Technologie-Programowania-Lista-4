package pl.moje.go.serwer;

import java.net.Socket;
import java.io.*;

public class ClientHandler implements Runnable {

    private Socket socket;

    ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        System.out.println("Obsługuje klienta w watku: " + Thread.currentThread().getName());

        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String line;
            while((line = in.readLine()) != null){
                System.out.println("[" + socket.getInetAddress() + "] " + line);

                out.println("ECHO: " + line);

                if("EXIT".equalsIgnoreCase(line)){
                    System.out.println("Klient zamkniety");
                    break;
                }
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
