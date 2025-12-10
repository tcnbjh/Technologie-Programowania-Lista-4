package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

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
