package pl.moje.go.client;

public class ClientMain {

    public static void main(String[] args) {
        GameClient client = new GameClient("localhost", 50000);
        GoFrame frame = new GoFrame(client);
        frame.setVisible(true);

        new Thread(client::start).start();
    }
}
