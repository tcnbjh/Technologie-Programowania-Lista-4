package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

public class GameController {
    private final Board board = new  Board();
    private Kolor turn = Kolor.BLACK;
    private int passCounter = 0;
    private Kolor gameWinner;

    public synchronized boolean makeMove(int x, int y, Kolor kolorGracza) {
        if (kolorGracza != turn){
            return false;
        }

        if (!board.placeStone(x, y, kolorGracza)){
            return false;
        }

        Kolor opponent = (kolorGracza == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;

        turn = opponent;
        passCounter = 0;
        return true;
    }

    public synchronized void pass(Kolor kolorGracza){
        if(turn == kolorGracza){
            passCounter += 1;
            board.getValidator().clearBlocked(kolorGracza);
            turn = (kolorGracza == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;
            if (passCounter == 2){
                gameEnd();
            }
        }
    }

    private synchronized void gameEnd(){}

    public synchronized String boardSnapshotAscii(){
        return board.toAscii();
    }

    public synchronized void ff(Kolor kolorGracza){
        gameWinner = (kolorGracza == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;
            gameEnd();
    }
}
