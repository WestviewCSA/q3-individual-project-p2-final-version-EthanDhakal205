import java.io.FileNotFoundException;
import java.util.List;

public class p1 {

    public static void main(String[] args) {

        boolean stackBase = false;
        boolean queueBase = false;
        boolean optimal = false;
        boolean showTime = false;
        boolean inCoord  = false;
        boolean outCoord = false;
        boolean helpFlag = false;
        String mapFile = null;

        try {
            if (args.length == 0) {
                throw new IllegalCommandLineInputsException("No arguments provided. You can put --Help");
            }

            mapFile = args[args.length - 1];

            for (int i = 0; i < args.length - 1; i++) {
                switch (args[i]) {
                    case "--Stack": stackBase = true; break;
                    case "--Queue": queueBase = true; break;
                    case "--Opt": optimal = true; break;
                    case "--Time": showTime = true; break;
                    case "--Incoordinate": inCoord = true; break;
                    case "--Outcoordinate":outCoord = true; break;
                    case "--Help": helpFlag = true; break;
                    default:
                        throw new IllegalCommandLineInputsException("Unknown argument: " + args[i]);
                }
            }

            if (helpFlag) {
                printHelp();
                System.exit(0);
            }

            int modeCount = (stackBase ? 1 : 0) + (queueBase ? 1 : 0) + (optimal ? 1 : 0);
            if (modeCount != 1) {
                throw new IllegalCommandLineInputsException("--Stack, --Queue, or --Opt must be chosen.");
            }

        } 
        catch (IllegalCommandLineInputsException e) {
            System.err.println("Command Line Error" + e.getMessage());
            System.exit(-1);
        }

        String[][][] map = null;
        try {
            if(inCoord) {
                map = MapReading.getCoordinateBasedMap(mapFile);
            } 
            else {
                map = MapReading.getTextBasedMap(mapFile);
            }
        } 
        catch (FileNotFoundException e) {
            System.err.println("File not found" + mapFile);
            System.exit(1);
        } 
        catch (IncorrectMapFormatException e) {
            System.err.println("Incorrect map format" + e.getMessage());
            System.exit(1);
        } 
        catch (IncompleteMapException e) {
            System.err.println("Incomplete map" + e.getMessage());
            System.exit(1);
        } 
        catch (IllegalMapCharacterException e) {
            System.err.println("Illegal map character" + e.getMessage());
            System.exit(1);
        }

        long startTime = System.nanoTime();

        List<Position> path = null;
        if (stackBase) {
            path = MazeSolver.solveStack(map);
        } 
        else if (queueBase) {
            path = MazeSolver.solveQueue(map);
        } 
        else {
            path = MazeSolver.solveOptimal(map);
        }

        long endTime = System.nanoTime();

        if (path == null) {
            System.out.println("The Wolverine Store close.");
        } else {
            if (outCoord) {
                for (Position p : path) {
                    String cell = map[p.level][p.row][p.col];
                    if (!cell.equals("W")) {
                        System.out.println("+ " + p.row + " " + p.col + " " + p.level);
                    }
                }
            } 
            else {
                String[][][] solvedMap = MazeSolver.applyPathToMap(map, path);
                MapReading.printMap(solvedMap);
            }
        }

        if (showTime) {
            double seconds = (endTime-startTime)/1000000000.0;
            System.out.printf("Total Runtime: %.9f seconds%n", seconds);
        }
    }

    private static void printHelp() {
        System.out.println("Switches(exactly one of --Stack, --Queue, --Opt is necessary):");
        System.out.println("--Stack: Use stack-based DFS");
        System.out.println("--Queue: Use queue-based BFS");
        System.out.println("--Opt: Find the optimal path");
        System.out.println("--Time: Runtime of the search algorithm");
        System.out.println("--Incoordinate: Input map is in coordinate-based format");
        System.out.println("--Outcoordinate: Output path in coordinate-based format");
        System.out.println("--Help: Show help message");
        System.out.println();
        System.out.println("Map characters:");
        System.out.println(" W  Wolverine's starting position");
        System.out.println(" $  Diamond Wolverine Coin");
        System.out.println(" .  Open walkable area");
        System.out.println(" @  Wall");
        System.out.println(" |  Open walkway to next maze level");
        System.out.println("+  Path taken");
        System.exit(0);
    }
}