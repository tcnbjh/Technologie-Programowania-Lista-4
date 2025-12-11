package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

public class GameController {
    private final Board board;
    private Kolor turn = Kolor.BLACK;

    public GameController() {
        this.board = new Board();
    }

    public Kolor getTurn() {
        return turn;
    }

    public synchronized boolean makeMove(int x, int y, Kolor kolorGracza) {
        if (kolorGracza != turn){
            return false;
        }

        boolean placed = board.placeStone(x, y, kolorGracza);

        if (!placed){
            return false;
        } else{
            System.out.println("Aktualna plansza po ruchu gracza " + kolorGracza + ":");
            board.printBoard();
        }

        turn = (turn == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;
        return true;
    }

}
