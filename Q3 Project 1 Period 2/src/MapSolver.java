import java.util.*;

public class MazeSolver {

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, 1, -1};

    public static List<Position> solveQueue(String[][][] map) {
        Position start = findChar(map, 'W');
        if (start == null) {
            return null;
        }
        int levels = map.length;
        int rows = map[0].length;
        int cols = map[0][0].length;

        boolean[][][] visited = new boolean[levels][rows][cols];
        Queue<Position> queue = new LinkedList<>();

        visited[start.level][start.row][start.col] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            Position curr = queue.poll();
            for (int d = 0; d < 4; d++) {
                int nr = curr.row + DR[d];
                int nc = curr.col + DC[d];
                int nl = curr.level;

                if (!inBounds(nl, nr, nc, levels, rows, cols)){
                    continue;
                }
                if (visited[nl][nr][nc]) {
                    continue;
                }

                String cell = map[nl][nr][nc];
                if (cell.equals("@")) {
                    continue;
                }
                Position next = new Position(nl, nr, nc, curr);

                if (cell.equals("$")) {
                    return buildPath(next);
                }

                visited[nl][nr][nc] = true;

                if (cell.equals("|")) {
                    queue.add(next);
                    int nextLevel = nl + 1;
                    if (nextLevel < levels && !visited[nextLevel][nr][nc]) {
                        visited[nextLevel][nr][nc] = true;
                        queue.add(new Position(nextLevel, nr, nc, next));
                    }
                } 
                else {
                    queue.add(next);
                }
            }
        }

        return null;
    }

    public static List<Position> solveStack(String[][][] map) {
        Position start = findChar(map, 'W');
        if (start == null) {
            return null;
        }
        int levels = map.length;
        int rows = map[0].length;
        int cols = map[0][0].length;

        boolean[][][] visited = new boolean[levels][rows][cols];
        Stack<Position> stack = new Stack<>();

        visited[start.level][start.row][start.col] = true;
        stack.push(start);

        while (!stack.isEmpty()) {
            Position curr = stack.pop();

            for (int d = 0; d < 4; d++) {
                int nr = curr.row + DR[d];
                int nc = curr.col + DC[d];
                int nl = curr.level;

                if (!inBounds(nl, nr, nc, levels, rows, cols)) {
                    continue;
                }
                if (visited[nl][nr][nc]) {
                    continue;
                }
                String cell = map[nl][nr][nc];
                if (cell.equals("@")) {
                    continue;
                }
                Position next = new Position(nl, nr, nc, curr);

                if (cell.equals("$")) {
                    return buildPath(next);
                }

                visited[nl][nr][nc] = true;

                if (cell.equals("|")) {
                    stack.push(next);
                    int nextLevel = nl + 1;
                    if (nextLevel < levels && !visited[nextLevel][nr][nc]) {
                        visited[nextLevel][nr][nc] = true;
                        stack.push(new Position(nextLevel, nr, nc, next));
                    }
                } else {
                    stack.push(next);
                }
            }
        }

        return null;
    }

    public static List<Position> solveOptimal(String[][][] map) {
        return solveQueue(map);
    }

    public static Position findChar(String[][][] map, char target) {
        String t = String.valueOf(target);
        for (int l = 0; l < map.length; l++) {
            for (int r = 0; r < map[l].length; r++) {
                for (int c = 0; c < map[l][r].length; c++) {
                    if (map[l][r][c].equals(t)) {
                        return new Position(l, r, c, null);
                    }
                }
            }
        }
        return null;
    }

    private static boolean inBounds(int level, int row, int col, int levels, int rows, int cols) {
        return level >= 0 && level < levels && row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private static List<Position> buildPath(Position goal) {
        LinkedList<Position> path = new LinkedList<>();
        Position curr = goal;
        while (curr != null) {
            path.addFirst(curr);
            curr = curr.parent;
        }
        return path;
    }

    public static String[][][] applyPathToMap(String[][][] original, List<Position> path) {
        int levels = original.length;
        int rows = original[0].length;
        int cols = original[0][0].length;
        String[][][] result = new String[levels][rows][cols];
        for (int l = 0; l < levels; l++)
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++)
                    result[l][r][c] = original[l][r][c];

        for (Position p : path) {
            String cell = result[p.level][p.row][p.col];
            if (!cell.equals("W") && !cell.equals("$")) {
                result[p.level][p.row][p.col] = "+";
            }
        }

        return result;
    }
}