package pl.moje.go.serwer;

import java.util.List;

public class PlayerRegistry {
    private Player[] players = new Player[2];
    private int nextId = 0;

    public boolean isFull(){
        return players[0] != null && players[1] != null;
    }

    public Kolor getAvailableColor(){
        if (players[0] == null){
            return Kolor.BLACK;
        } else if (players[1] == null){
            return Kolor.WHITE;
        } else {
            return Kolor.NONE;
        }
    }

    public void addPlayer(Player player){
        if (player.getKolor() == Kolor.BLACK){
            players[0] = player;
        } else{
            players[1] = player;
        }
    }

    public Player registerNewPlayer(){
        if (isFull()){
            return null;
        } else {
            nextId++;
            Player player = new Player(nextId, getAvailableColor());
            addPlayer(player);
            return player;
        }
    }

    public void removePlayer(Player player){
        if (player == players[0]){
            players[0] = null;
        } else if (player == players[1]){
            players[1] = null;
        }
    }
}
