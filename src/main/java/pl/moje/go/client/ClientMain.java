package pl.moje.go.client;

public class ClientMain {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;

        GameClient client = new GameClient(host, port);
        client.connect();

        client.runInteractive();
    }
}
