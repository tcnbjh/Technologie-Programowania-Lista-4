package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

public class DeadStonesRemover {
    DeadStonesRemover(){}

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
