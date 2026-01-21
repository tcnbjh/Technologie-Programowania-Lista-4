package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

/**
 * Reprezentuje gracza w sesji gry.
 * Przechowuje identyfikator gracza oraz przypisany kolor kamieni.
 */
public class Player{
    private final int id;
    Kolor kolor;

    public Player(int id, Kolor kolor) {
        this.id = id;
        this.kolor = kolor;
    }

    public int getId() {
        return id;
    }

    public Kolor getKolor() {
        return kolor;
    }
}
