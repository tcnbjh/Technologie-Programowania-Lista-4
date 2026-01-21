package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

/**
 * Usuwa z planszy kamienie oznaczone jako martwe (DEAD_WHITE / DEAD_BLACK).
 * Zastępuje je stanem NONE i zwraca liczbę usuniętych kamieni dla obu kolorów.
 */
public class DeadStonesRemover {
    DeadStonesRemover(){}

    /**
     * Usuwa kamienie oznaczone jako martwe z planszy.
     * @param fields plansza
     * @return tablica: [0] = liczba usuniętych DEAD_BLACK, [1] = liczba usuniętych DEAD_WHITE
     */
    public int[] removeDeadStones(Kolor[][] fields){
        int[] ammountCaptured = new int[2];
        ammountCaptured[0] = 0;
        ammountCaptured[1] = 0;

        for(int y = 0; y < fields.length; y++){
            for(int x = 0; x < fields.length; x++){
                switch(fields[y][x]){
                    case DEAD_BLACK:
                        ammountCaptured[0] += 1;
                        fields[y][x] = Kolor.NONE;
                        break;
                    case DEAD_WHITE:
                        ammountCaptured[1] += 1;
                        fields[y][x] = Kolor.NONE;
                        break;
                    default:
                        break;
                }
            }
        }


        return ammountCaptured;
    };
}
