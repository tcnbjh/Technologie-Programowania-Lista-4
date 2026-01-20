package pl.moje.go.client;

import java.io.*;
import java.net.Socket;

public class ClientMain {

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
