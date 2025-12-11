package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;


public class Board {

    private final int size = 19;
    private final Kolor[][] fields;

    Board () {
        this.fields = new Kolor[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j] = Kolor.NONE;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Kolor getField(int x, int y) {
        if (!isOnBoard(x, y)){
            throw new IllegalArgumentException("Poza plansza: (" + x + "," + y + ")");
        }
        return fields[x][y];
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

        fields[x][y] = color;
        return true;
    }

    private void setField(int x, int y, Kolor color) {
        fields[x][y] = color;
    }

    public String toAscii() {
        StringBuilder sb = new StringBuilder();

        sb.append("  ");
        for (int i = 0; i < size; i++) {
            sb.append(i);
        }
        sb.append("\n");

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
            }

            sb.append("\n");
        }
        return sb.toString();
    }

    public String toString(){
        return toAscii();
    }

    public void printBoard(){
        System.out.println(toAscii());
    }
}
