package pl.moje.go.serwer;

import java.net.Socket;


class ServerMain{
    private Socket socket;
    public static void main(String[] args) {
        int port = 12345;

        GameServer server = new GameServer(port);
        server.start();
    }
}


