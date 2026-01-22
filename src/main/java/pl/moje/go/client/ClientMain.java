package pl.moje.go.client;

import java.io.*;
import java.net.Socket;

/**
 * Główna klasa uruchomieniowa aplikacji klienta gry Go.
 * <p>
 * Odpowiada za nawiązanie połączenia TCP z serwerem,
 * inicjalizację graficznego interfejsu użytkownika (GUI)
 * oraz uruchomienie wątku obsługującego komunikację sieciową.
 */
public class ClientMain {

    /**
     * Punkt wejścia do aplikacji klienta.
     *
     * @param args argumenty wiersza poleceń (nieużywane)
     * @throws Exception w przypadku błędu połączenia z serwerem lub wejścia/wyjścia
     */
    public static void main(String[] args) throws Exception {

        int size = 19;
        String host = "localhost";
        int port = 50000;

        GoFrame frame = new GoFrame(size);

        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);

        GameClient client = new GameClient(frame, in, out, size);
        frame.setClient(client);

        client.start();
    }
}
