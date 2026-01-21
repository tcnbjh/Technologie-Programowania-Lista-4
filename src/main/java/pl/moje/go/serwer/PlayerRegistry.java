package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

/** Zarządza graczami : max 2, przydział koloru i generowanie ID. */

public class PlayerRegistry {
    private Player[] players = new Player[2];
    private int nextId = 0;

    public synchronized boolean isFull(){
        return players[0] != null && players[1] != null;
    }

    /**
     * Metoda zwraca pierwszy dostepny kolor
     */
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

    /**
     * rejestruje nowego gracza
     * @return zwraca czy udalo sie zarejestrowac nowego gracza
     */
    public synchronized Player registerNewPlayer(){
        if (isFull()){
            return null;
        } else {
            nextId++;
            Player player = new Player(nextId, getAvailableColor());
            addPlayer(player);
            return player;
        }
    }

    /**
     * Usuwa gracza z rejestru.
     * @param player gracz
     */
    public synchronized void removePlayer(Player player){
        if (player == players[0]){
            players[0] = null;
        } else if (player == players[1]){
            players[1] = null;
        }
    }
}
