package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

public class GameController {
    private final Board board = new  Board();
    private Kolor turn = Kolor.BLACK;

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

        turn = opponent;
        return true;
    }

    public synchronized String boardSnapshotAscii(){
        return board.toAscii();
    }
}
