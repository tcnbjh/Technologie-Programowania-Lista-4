package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;
import pl.moje.go.common.Protocol;

public class GameController {
    private final Board board = new  Board();
    private Kolor turn = Kolor.BLACK;
    private int passCounter = 0;
    int pass_turn = 0;
    private Kolor gameWinner;
    private boolean deadStonesCollecting = false;
    private double blackPoints = 0;
    private double whitePoints = 6.5;

    public synchronized boolean makeMove(int x, int y, Kolor kolorGracza) {
        if (kolorGracza != turn){
            return false;
        }

        if(!deadStonesCollecting) {
            int capt = board.placeStone(x, y, kolorGracza);
            if (capt == -1) {
                return false;
            }

            if(turn == Kolor.BLACK){
                blackPoints += capt;
            } else{
                whitePoints += capt;
            }

            turn = (kolorGracza == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;
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


    public synchronized String boardSnapshotAscii(){
        return board.toAscii();
    }

    public synchronized void ff(Kolor kolorGracza){
        gameWinner = (kolorGracza == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;
            gameEnd();
    }

    public synchronized String confirm(Kolor kolorGracza){
        if(kolorGracza != turn) {
            return null;
        }

        if (pass_turn == 0) {
            pass_turn = 1;
            turn = (kolorGracza == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;
            return null;
        } else if (pass_turn == 1) {
            return gameEnd();
        }
        return null;
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

    private synchronized String gameEnd(){
        int[] captured = board.removeDeadStones();
        whitePoints += captured[0];
        blackPoints += captured[1];


        int[] Teritory = board.countTeritory();
        whitePoints += Teritory[1];
        blackPoints += Teritory[0];

        if (blackPoints > whitePoints){
            gameWinner = Kolor.BLACK;
        } else {
            gameWinner = Kolor.WHITE;
        }

        return Protocol.MSG_GAME_OVER + " " + gameWinner + " " + blackPoints + " " + whitePoints;
    }
}
