package pl.moje.go.serwer;

/** Punkt wejścia serwera (main) – uruchamia GameServer. */

class ServerMain{
    public static void main(String[] args) {
        int port = 50000;

        GameServer server = new GameServer(port);
        server.start();
    }
}
