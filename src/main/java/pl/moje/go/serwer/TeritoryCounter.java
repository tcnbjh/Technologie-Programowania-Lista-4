package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

import java.util.ArrayDeque;

/**
 * Licznik terytorium w grze Go.
 * Wyszukuje spójne obszary pustych pól (NONE) i sprawdza, czy są otoczone
 * wyłącznie przez kamienie jednego koloru. Jeśli tak, dodaje je do terytorium
 * danego gracza.
 */
public class TeritoryCounter {
    TeritoryCounter(){}

    private static final int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

    int[] count(Kolor[][] fields){
        int n = fields.length;
        int blackTerritory = 0;
        int whiteTerritory = 0;

        int[] territory = new int[2];
        boolean[][] visited = new boolean[n][n];

        for(int y = 0; y < n; y++){
            for(int x = 0; x < n; x++){
                if(!visited[y][x]) {
                    if (fields[y][x] == Kolor.NONE) {
                        visited[y][x] = true;
                        Kolor color = Kolor.NONE;
                        int counter = 1;

                        ArrayDeque<int[]> stack = new ArrayDeque<>();

                        stack.push(new int[]{y, x});

                        while (!stack.isEmpty()) {
                            int[] p = stack.pop();
                            int cy = p[0], cx = p[1];

                            for (int[] d : DIRS) {
                                int ny = cy + d[0];
                                int nx = cx + d[1];

                                if (nx < 0 || nx >= n || ny < 0 || ny >= n) continue;

                                Kolor k = fields[ny][nx];

                                if(!visited[ny][nx]) {
                                    if (k == Kolor.NONE) {
                                        counter++;
                                        visited[ny][nx] = true;
                                        stack.push(new int[]{ny, nx});
                                    } else {
                                        if (color == Kolor.NONE) {
                                            color = k;
                                        } else if(color != k && color != null){
                                            color = null;
                                        }
                                    }
                                }
                            }
                        }
                        switch(color){
                            case Kolor.BLACK:
                                blackTerritory += counter;
                                break;
                            case Kolor.WHITE:
                                whiteTerritory += counter;
                                break;
                            case null, default:
                                break;
                        }
                    }
                }
            }
        }

        territory[0] = blackTerritory;
        territory[1] = whiteTerritory;
        return territory;
    }
}
