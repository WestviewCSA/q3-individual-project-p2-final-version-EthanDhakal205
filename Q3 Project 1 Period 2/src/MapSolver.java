import java.util.*;
//has three maze solving algorithms
public class MazeSolver {
    //movement directions include north, south, east, and west
    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, 1, -1};

    //Queue based breadth first search
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
                //if it is a walkway, enqueue the same position on the level
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

    //stack based depth first search
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
                //walkway psuhes the same position on the next level
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

    //a* search alg
    //f = g+h, where g is the steps that are taken and h is the Manhattan distance to the goal.
    public static List<Position> solveOptimal(String[][][] map) {
        Position start = findChar(map,'W');
        Position goal = findChar(map,'$');
        if(start==null || goal == null) {
            return null;
        }

        int levels = map.length;
        int rows = map[0].length;
        int cols = map[0][0].length;
        //gScore tracks cheapest known cost to reach each cell
        //it gets initialized to a large number that is not yet reached
        int[][][] gScore = new int[levels][rows][cols];
        for(int[][] level : gScore) {
            for(int[] row : level) {
                //Max value
                Arrays.fill(row, 2147483647);
            }
        }
        gScore[start.level][start.row][start.col] = 0;
        //priorityqueue will ord3r positions by f = g+h from the lowest
        PriorityQueue<Position> open = new PriorityQueue<>(new Comparator<Position>() {
            public int compare(Position a, Position b) {
                return (a.g + ManhattanDistance(a,goal)) - (b.g + ManhattanDistance(b,goal));
            }
        });
        start.g=0;
        open.add(start);

        while(!open.isEmpty()) {
            Position curr = open.poll();
            //when goal is reached
            if(curr.row == goal.row && curr.col == goal.col && curr.level == goal.level) {
                return buildPath(curr);
            }
            //skip if there is a cheaper path
            if(curr.g > gScore[curr.level][curr.row][curr.col]) {
                continue;
            }
            for(int d = 0; d<4; d++) {
                int nr = curr.row + DR[d];
                int nc = curr.col + DC[d];
                int nl = curr.level; 
                if(!inBounds(nl,nr,nc,levels,rows,cols)) {
                    continue;
                }
                String cell = map[nl][nr][nc];
                if(cell.equals("@")) {
                    continue;
                }
                int newG = curr.g+1;
                //only update if a cheaper way to reach the cell is found
                if(newG < gScore[nl][nr][nc]) {
                    gScore[nl][nr][nc] = newG;
                    Position next = new Position(nl,nr,nc,curr,newG);
                    open.add(next);
                    //when there is a walkway, add the same position on the next level
                    if(cell.equals("|")) {
                        int nextLevel = nl + 1;
                        if(nextLevel<levels && newG+1 < gScore[nextLevel][nr][nc]) {
                            gScore[nextLevel][nr][nc] = newG+1;
                            open.add(new Position(nextLevel, nr,nc, next, newG+1));
                        }
                        
                    }
                }
            }
        }
        return null;
        
    }
    //Estimates steps to goal ignoring walls
    //unlike regular a*, diagonals are not counted
    private static int ManhattanDistance(Position p, Position goal) {
        return Math.abs(p.row - goal.row) + Math.abs(p.col - goal.col);
    }

    //searches the 3d map for the first occurrence of a given character

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
    //returns true if level, row, and col are in the map boundary
    private static boolean inBounds(int level, int row, int col, int levels, int rows, int cols) {
        return level >= 0 && level < levels && row >= 0 && row < rows && col >= 0 && col < cols;
    }

    //reconstructs the path from start to goal by going through parent links
    //also returns the path as a list from index 0 to goal
    private static List<Position> buildPath(Position goal) {
        LinkedList<Position> path = new LinkedList<>();
        Position curr = goal;
        while (curr != null) {
            path.addFirst(curr);
            curr = curr.parent;
        }
        return path;
    }
    //copries map and marks positions
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
