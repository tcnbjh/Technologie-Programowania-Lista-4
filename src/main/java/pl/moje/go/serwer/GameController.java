package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

public class GameController {
    private final Board board;
    private Kolor turn = Kolor.BLACK;

    public GameController() {
        this.board = new Board();
    }

    public synchronized boolean makeMove(int x, int y, Kolor kolorGracza) {
        if (kolorGracza != turn){
            return false;
        }

        if (!board.placeStone(x, y, kolorGracza)){
            return false;
        }

        Kolor opponent = (kolorGracza == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;

        board.removeStonesAround(x, y, opponent);

        if (board.isSuicide(x, y)){
            board.removeStone(x, y);
            return false;
        }

        System.out.println("Aktualna plansza po ruchu gracza " + kolorGracza + ":");
        board.printBoard();

        turn = opponent;
        return true;
    }

    public Board getBoard() {
        return board;
    }
}
