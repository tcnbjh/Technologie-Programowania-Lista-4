package pl.moje.go.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    private final String host;
    private final int port;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private boolean connected = false;

    private volatile boolean running;

    public GameClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void connect(){
        try{
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            System.out.println("Połączono z serwerem " + host + ":" + port);

            String firstLine = in.readLine();

            if (firstLine == null){
                System.out.println("Serwer rozlaczyl sie od razu");
                close();
                return;
            }

            if ("FULL".equals(firstLine)){
                System.out.println("Serwer: gra jest pełna");
                close();
                return;
            }

            System.out.println(firstLine);

            connected = true;



        } catch (IOException e){
            System.out.println("Nie udalo sie polaczyc z serwerem: " + e.getMessage());
        }
    }

    public void sendLine(String line){
        out.println(line);
    }

    public String readLine(){
        try{
            return in.readLine();
        } catch (IOException e){
            return null;
        }
    }

    public void close(){
        try{
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException eignore){}

        connected = false;
    }

    public void runInteractive(){
        if (!connected){
            System.out.println("Nie polaczono z serwerem.");
            return;
        }

        running = true;

        Thread receiver = new Thread(() -> {
            try{
                while (running){
                    String line = readLine();
                    if (line == null){
                        System.out.println("Polacznie z serwerem zostalo przerwane");
                        running = false;
                        break;
                    }

                    if ("BOARD".equals(line)){
                        System.out.println("Aktualna plansza:");
                        while (true){
                            String boardLine = readLine();
                            if (boardLine == null){
                                System.out.println("Polacznie z serwerem zostalo przerwane");
                                running = false;
                                return;
                            }
                            if ("END_BOARD".equals(boardLine)){
                                break;
                            }
                            System.out.println(boardLine);
                        }
                    } else {
                        System.out.println("Serwer: " + line);
                    }
                }
            } catch (Exception e) {
                if (running){
                    System.out.println("Blad podczas odbierania danych: " + e.getMessage());
                }
            }
        });

        receiver.setDaemon(true);
        receiver.start();

        try (Scanner scanner = new Scanner(System.in)) {
            while (running){
                String input = scanner.nextLine();
                sendLine(input);

                if ("EXIT".equals(input)){
                    running = false;
                    break;
                }
            }
        }
    }
}
