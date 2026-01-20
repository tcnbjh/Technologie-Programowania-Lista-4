package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

public class GameController {
    private final Board board = new  Board();
    private Kolor turn = Kolor.BLACK;
    private int passCounter = 0;
    int pass_turn = 0;
    private Kolor gameWinner;
    private boolean deadStonesCollecting = false;

    public synchronized boolean makeMove(int x, int y, Kolor kolorGracza) {
        if (kolorGracza != turn){
            return false;
        }

        if(!deadStonesCollecting) {
            if (!board.placeStone(x, y, kolorGracza)) {
                return false;
            }

            Kolor opponent = (kolorGracza == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;

            turn = opponent;
            passCounter = 0;
            return true;
        } else{
            if(pass_turn == 0) {
                board.setDeadGroup(x, y);
                return true;
            } else{
                return false;
            }
        }
    }

    public synchronized void pass(Kolor kolorGracza){
        if(turn == kolorGracza && !deadStonesCollecting){
            passCounter += 1;
            board.getValidator().clearBlocked(kolorGracza);
            turn = (kolorGracza == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;
            if (passCounter == 2){
                deadStonesCollecting = true;
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

    public synchronized void confirm(Kolor kolorGracza){
        if(kolorGracza != turn) {
            return;
        }

        if (pass_turn == 0) {
            pass_turn = 1;
            turn = (kolorGracza == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;
        } else if (pass_turn == 1) {
            gameEnd();
        }
    }

    public synchronized void reject(Kolor kolorGracza){
        if(kolorGracza != turn || pass_turn != 1){
            return;
        }

        pass_turn = 0;
        passCounter = 0;
        deadStonesCollecting = false;
        board.setDeadToAlive();
        turn = (kolorGracza == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;
    }
}
