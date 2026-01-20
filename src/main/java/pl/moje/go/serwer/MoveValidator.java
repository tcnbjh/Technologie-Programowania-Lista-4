package pl.moje.go.serwer;

import java.util.ArrayDeque;
import java.util.ArrayList;

import pl.moje.go.common.Kolor;

public class MoveValidator {
    private static final int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};
    ArrayList<int[]> wBlocked = new ArrayList<>();
    ArrayList<int[]> bBlocked = new ArrayList<>();

    public MoveValidator(){}

    public boolean isValidMove(Kolor[][] fields, int x, int y, Kolor color){

        if(haveBreath(x, y, fields)){
            return true;
        }

        if(color == Kolor.WHITE){
            for(int[] pair : wBlocked){
                if(x == pair[0] && y == pair[1]){
                    if(!koRule(x, y, Kolor.BLACK, fields)){
                        return false;
                    }
                }
            }
        } else {
            for(int[] pair : bBlocked){
                if(x == pair[0] && y == pair[1]){
                    if(!koRule(x, y, Kolor.WHITE, fields)){
                        return false;
                    }
                }
            }
        }


        if(chainsAroundBreaths(x, y, color, fields)){
            return true;
        }

        if(enemyChainsBreaths(x, y, color, fields)){
            return true;
        }


        return false;
    }

    private boolean haveBreath(int x, int y, Kolor[][] fields) {
        int n = fields.length;

        for (int[] d : DIRS) {
            int nx = x + d[0];
            int ny = y + d[1];

            if (nx >= 0 && nx < n && ny >= 0 && ny < n) {
                if (fields[nx][ny] == Kolor.NONE) {
                    return true;
                }
            }
        }
        return false;
    }


    private int countChainLiberties(Kolor[][] fields, int x, int y) {
        int n = fields.length;

        Kolor color = fields[x][y];
        if (color == Kolor.NONE) return 0;

        boolean[][] visited = new boolean[n][n];      // kamienie grupy
        boolean[][] libertySeen = new boolean[n][n];  // puste pola (oddechy)
        ArrayDeque<int[]> stack = new ArrayDeque<>(); // stos na kolejne pola do sprawdzenia

        visited[x][y] = true; // ustawienie odwiedzenia poczatkowego pola na true
        stack.push(new int[]{x, y}); // dodanie poczatkowego pola na stos

        int liberties = 0;

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
                    stack.push(new int[]{nx, ny}); // dodanie nowego pola do odwiedzenia na stos
                }
            }
        }

        return liberties;
    }

    private boolean chainsAroundBreaths(int x, int y, Kolor color, Kolor[][] fields){
        int n = fields.length;

        for (int[] d : DIRS) {
            int nx = x + d[0];
            int ny = y + d[1];

            if (nx >= 0 && nx < n && ny >= 0 && ny < n) {
                if (fields[nx][ny] == color) {
                    if(countChainLiberties(fields, nx, ny) >= 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean enemyChainsBreaths(int x, int y, Kolor color, Kolor[][] fields){
        int n = fields.length;

        for (int[] d : DIRS) {
            int nx = x + d[0];
            int ny = y + d[1];

            if (nx >= 0 && nx < n && ny >= 0 && ny < n) {
                if (fields[nx][ny] != color && fields[nx][ny] != Kolor.NONE) {
                    if(countChainLiberties(fields, nx, ny) == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void clearBlocked(Kolor color){
        if(color == Kolor.WHITE){
            bBlocked.clear();
        } else {
            wBlocked.clear();
        }
    }

    public void setBlock(int x, int y, Kolor color){
        if(color == Kolor.WHITE){
            bBlocked.add(new int[]{x, y});
        } else {
            wBlocked.add(new int[]{x, y});
        }
    }

    private boolean koRule(int x, int y, Kolor color, Kolor[][] fields){
        int n = fields.length;

        for (int[] d : DIRS) {
            int nx = x + d[0];
            int ny = y + d[1];

            if (nx >= 0 && nx < n && ny >= 0 && ny < n) {
                if (fields[nx][ny] == color) {
                    if(chainAroundBreathsKO(fields, nx, ny)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean chainAroundBreathsKO(Kolor[][] fields, int x, int y) {
        int n = fields.length;

        Kolor color = fields[x][y];
        if (color == Kolor.NONE) return false;

        boolean[][] visited = new boolean[n][n];      // kamienie grupy
        boolean[][] libertySeen = new boolean[n][n];  // puste pola (oddechy)
        ArrayDeque<int[]> stack = new ArrayDeque<>(); // stos na kolejne pola do sprawdzenia

        visited[x][y] = true; // ustawienie odwiedzenia poczatkowego pola na true
        stack.push(new int[]{x, y}); // dodanie poczatkowego pola na stos

        int lenght = 1;
        int liberties = 0;

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
                    stack.push(new int[]{nx, ny});// dodanie nowego pola do odwiedzenia na stos
                    lenght++;
                }
            }
        }

        if(liberties == 1 && lenght >= 2) {
            return true;
        }
        return false;
    }
}
