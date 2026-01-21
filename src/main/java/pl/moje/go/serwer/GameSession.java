package pl.moje.go.serwer;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje sesję gry na serwerze.
 * Przechowuje podłączonych klientów (ClientHandler), zarządza rejestracją graczy
 * oraz deleguje logikę rozgrywki do GameController.
 * Odpowiada za rozsyłanie aktualnej planszy i komunikatów do wszystkich klientów.
 */
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

    public synchronized void confirm(Player player){
        String resultMsg = gameController.confirm(player.getKolor());

        if (resultMsg != null){
            for (ClientHandler handler : clients){
                handler.sendMessage(resultMsg);
            }
        }
        broadcastBoard();
    }

    public synchronized void reject(Player player){
        gameController.reject(player.getKolor());
    }
}
