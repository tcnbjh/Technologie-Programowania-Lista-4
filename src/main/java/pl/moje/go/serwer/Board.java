package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;
import java.util.ArrayList;


public class Board {

    private final int size = 19;
    private final Kolor[][] fields;
    private final MoveValidator validator = new MoveValidator();
    private final StoneRemover remover = new StoneRemover();
    private final StonePainter painter = new StonePainter();

    Board() {
        fields = new Kolor[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j] = Kolor.NONE;
            }
        }
    }

    private boolean isOnBoard(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public boolean placeStone(int x, int y, Kolor color) {
        if (!isOnBoard(x, y)){
            return false;
        }

        if (fields[x][y] != Kolor.NONE){
            return false;
        }

        if(!validator.isValidMove(fields, x, y, color)){
            return false;
        }

        validator.clearBlocked(color);

        setField(x, y, color);

        ArrayList<Integer> list = remover.removeAround(x, y, fields, color);

        int dlg = list.size();

        System.out.println(list);

        for(int i = 0; i < dlg - 1; i = i + 2){
            validator.setBlock(list.get(i), list.get(i+1), color);
        }

        return true;
    }

    public MoveValidator getValidator(){
        return validator;
    }

    public void setDeadGroup(int x, int y){
        Kolor color = fields[x][y];
        switch (color) {
            case Kolor.BLACK:
                painter.setGroupColorTo(Kolor.DEAD_BLACK, x, y, fields);
            case Kolor.DEAD_BLACK:
                painter.setGroupColorTo(Kolor.BLACK, x, y, fields);
            case Kolor.DEAD_WHITE:
                painter.setGroupColorTo(Kolor.WHITE, x, y, fields);
            case Kolor.WHITE:
                painter.setGroupColorTo(Kolor.DEAD_WHITE, x, y, fields);
            default:
                break;
        }
    }



    private void setField(int x, int y, Kolor color){
        fields[x][y] = color;
    }


    public String toAscii() {
        StringBuilder sb = new StringBuilder();

        sb.append("   ");
        for (int i = 0; i < size; i++) {
            sb.append(i).append(" ");
        }
        sb.append("\n");

        for (int i = 0; i < size; i++) {
            if (i < 10){
                sb.append(" ");
            }
            sb.append(i).append(" ");

            for (int j = 0; j < size; j++) {
                Kolor k = fields[i][j];
                char c;

                switch (k){
                    case BLACK -> c = 'B';
                    case WHITE -> c = 'W';
                    default -> c = '□';
                }

                sb.append(c).append(" ");
                if (j >= 10){
                    sb.append(" ");
                }
            }

            sb.append("\n");
        }
        return sb.toString();
    }

    public void setDeadToAlive(){
        painter.setToAlive(fields);
    }

}
