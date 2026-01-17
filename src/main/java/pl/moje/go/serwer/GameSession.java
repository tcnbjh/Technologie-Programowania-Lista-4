package pl.moje.go.serwer;

import java.util.ArrayList;
import java.util.List;

public class GameSession {

    private final List<ClientHandler> clients = new ArrayList<>();
    private final GameController gameController = new GameController();
    private final PlayerRegistry playerRegistry = new PlayerRegistry();

    public synchronized Player registerPlayer(){
        return playerRegistry.registerNewPlayer();
    }

    public synchronized void unregisterPlayer(Player player){
        playerRegistry.removePlayer(player);
    }

    public synchronized void registerClient(ClientHandler handler){
        clients.add(handler);
    }

    public synchronized void unregisterClient(ClientHandler handler){
        clients.remove(handler);
    }

    public synchronized void broadcastBoard(){
        String ascii = gameController.boardSnapshotAscii();
        for (ClientHandler handler : clients){
            handler.sendBoard(ascii);
        }
    }

    public synchronized boolean makeMove(int x, int y, Player player){
        boolean ok = gameController.makeMove(x, y, player.getKolor());
        if (ok){
            broadcastBoard();
        }
        return ok;
    }

    public synchronized void pass(Player player){
        gameController.pass(player.getKolor());
    }

    public synchronized void ff(Player player){
        gameController.ff(player.getKolor());
    }
}
