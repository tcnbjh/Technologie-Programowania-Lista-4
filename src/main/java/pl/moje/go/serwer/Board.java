package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;


public class Board {

    private final int size = 19;
    private final Kolor[][] fields;

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

        fields[x][y] = color;
        return true;
    }

    private void setField(int x, int y, Kolor color) {
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

    public int countBreaths(int x, int y) {
        if (!isOnBoard(x, y)){
            throw new IllegalArgumentException("Poza plansza");
        }

        if (fields[x][y] == Kolor.NONE){
            return 0;
        }

        int breaths = 0;

        if (isOnBoard(x, y - 1) && fields[x][y - 1] == Kolor.NONE) breaths++;
        if (isOnBoard(x, y + 1) && fields[x][y + 1] == Kolor.NONE) breaths++;
        if (isOnBoard(x - 1, y) && fields[x - 1][y] == Kolor.NONE) breaths++;
        if (isOnBoard(x + 1, y) && fields[x + 1][y] == Kolor.NONE) breaths++;

        return breaths;
    }

    public void removeStone(int x, int y) {
        if (isOnBoard(x, y)){
            setField(x, y, Kolor.NONE);
        }
    }

    public void removeStonesAround(int x, int y, Kolor opponentColor) {
        int[][] dirs = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

        for (int[] d : dirs) {
            int nx = x + d[0];
            int ny = y + d[1];

            if (isOnBoard(nx, ny) && fields[nx][ny] == opponentColor){
                if (countBreaths(nx, ny) == 0){
                    removeStone(nx, ny);
                }
            }
        }
    }

    public boolean isSuicide(int x, int y){
        return countBreaths(x, y) == 0;
    }
}
