package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;

import java.util.ArrayDeque;

public class StonePainter {

    private static final int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

    StonePainter(){}

    public void setGroupColorTo(Kolor paintColor, int x, int y, Kolor[][] fields){
        int n = fields.length;
        Kolor color = fields[x][y];

        ArrayDeque<int[]> stack = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        visited[x][y] = true;

        stack.push(new int[]{x, y});
        fields[x][y] = paintColor;

        while (!stack.isEmpty()) {
            int[] p = stack.pop();
            int cx = p[0], cy = p[1];

            for (int[] d : DIRS) {
                int nx = cx + d[0];
                int ny = cy + d[1];

                if (nx < 0 || nx >= n || ny < 0 || ny >= n) continue;

                Kolor k = fields[nx][ny];

                if (k == color && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    stack.push(new int[]{nx, ny});
                    fields[nx][ny] = paintColor;
                }
            }
        }
    }

    public void setToAlive(Kolor[][] fields){
        for (int r = 0; r < fields.length; r++) {
            for (int c = 0; c < fields.length; c++) {
                switch (fields[r][c]){
                    case Kolor.DEAD_BLACK:
                        fields[r][c] = Kolor.BLACK;
                        break;
                    case Kolor.DEAD_WHITE:
                        fields[r][c] = Kolor.WHITE;
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
