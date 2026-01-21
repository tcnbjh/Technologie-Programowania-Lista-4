package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Klasa pomocnicza do zdejmowania zbitych kamieni.
 * Po wykonaniu ruchu sprawdza sąsiednie grupy przeciwnika, liczy ich oddechy
 * i usuwa z planszy grupy bez oddechów (zbijane).
 */

public class StoneRemover {

    private static final int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

    StoneRemover(){}

    /**
     * Metoda sprawdza łancuchy wokół pola i wywołuje ich ewentualne usuniecie
     * @return zwraca talblice z zablokowanymi polami i sume zbitych kamieni
     */
    public ArrayList<Integer> removeAround(int x, int y, Kolor[][] fields, Kolor color){
        ArrayList<Integer> list = new ArrayList<>();
        int sum_cap = 0;
        int n = fields.length;

        for (int[] d : DIRS) {
            int nx = x + d[0];
            int ny = y + d[1];

            if (nx >= 0 && nx < n && ny >= 0 && ny < n) {
                Kolor oppCol = (color == Kolor.BLACK) ? Kolor.WHITE : Kolor.BLACK;
                if(fields[nx][ny] == oppCol){
                    int num_cap = numbOfCaptured(nx, ny, fields, oppCol);
                    if(num_cap == 1){
                        list.add(nx);
                        list.add(ny);
                    }
                    sum_cap += num_cap;
                }
            }
        }
        list.add(sum_cap);
        return list;
    }

    /**
     * Metoda usuwa grupe kamieni
     * @param x x
     * @param y y
     * @param fields aktualna plansza
     * @return zwraca ilosc zbitych kamieni
     */
    int numbOfCaptured(int x, int y, Kolor[][] fields, Kolor color){
        int n = fields.length;

        boolean[][] visited = new boolean[n][n];      // kamienie grupy
        boolean[][] libertySeen = new boolean[n][n];  // puste pola (oddechy)
        ArrayDeque<int[]> stack = new ArrayDeque<>(); // stos na kolejne pola do sprawdzenia

        visited[x][y] = true; // ustawienie odwiedzenia poczatkowego pola na true
        stack.push(new int[]{x, y}); // dodanie poczatkowego pola na stos

        int liberties = 0;
        int captured = 0;

        while (!stack.isEmpty()) {
            int[] p = stack.pop(); // sciaga ze stosu i zwraca wartosc
            int cx = p[0], cy = p[1];

            for (int[] d : DIRS) {
                int nx = cx + d[0];
                int ny = cy + d[1];

                if (nx < 0 || nx >= n || ny < 0 || ny >= n) continue; // poza plansza

                Kolor k = fields[nx][ny];

                if (k == Kolor.NONE) {
                    if (!libertySeen[nx][ny]) {
                        libertySeen[nx][ny] = true;
                        liberties++;
                    }
                } else if (k == color && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    stack.push(new int[]{nx, ny});
                }
            }
        }

        if(liberties == 0){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    if(visited[i][j]){
                        fields[i][j] = Kolor.NONE;
                        captured++;
                    }
                }
            }
            return captured;
        } else{return 0;}
    }

}
