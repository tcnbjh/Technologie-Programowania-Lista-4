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
            socket.close();
        } catch (IOException eignore){}
    }

    public void runInteractive(){
        try(Scanner scanner = new Scanner(System.in)){
            String line;
            while(true){
                System.out.print("Ty: ");
                line = scanner.nextLine();
                sendLine(line);

                String response = readLine();
                System.out.println("Serwer: " + response);
                if("EXIT".equalsIgnoreCase(line)){
                    break;
                }
            }
        } finally {
            close();
        }
    }
}
