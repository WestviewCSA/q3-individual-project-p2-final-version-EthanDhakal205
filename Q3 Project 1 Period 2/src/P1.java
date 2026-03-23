import java.io.FileNotFoundException;
import java.util.List;
//this class has the main method
//comand line arguments are here and it runs the specific algorithm given
public class P1 {

    public static void main(String[] args) {

        //it flags when there is a command line switch
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
            //last argument is the map file path 
            mapFile = args[args.length - 1];

            //pase the switches
            for (int i = 0; i < args.length - 1; i++) {
                switch (args[i]) {
                    case "--Stack": 
                        stackBase = true; 
                        break;
                    case "--Queue": 
                        queueBase = true; 
                        break;
                    case "--Opt": 
                        optimal = true; 
                        break;
                    case "--Time": 
                        showTime = true; 
                        break;
                    case "--Incoordinate": 
                        inCoord = true; 
                        break;
                    case "--Outcoordinate":
                        outCoord = true; 
                        break;
                    case "--Help": 
                        helpFlag = true; 
                        break;
                    default:
                        throw new IllegalCommandLineInputsException("Unknown argument: " + args[i]);
                }
            }

            if (helpFlag) {
                printHelp();
                System.exit(0);
            }

            //only one of --Stack, --Queue, --Opt can be set at time
            int modeCount = (stackBase ? 1 : 0) + (queueBase ? 1 : 0) + (optimal ? 1 : 0);
            if (modeCount != 1) {
                throw new IllegalCommandLineInputsException("--Stack, --Queue, or --Opt must be chosen.");
            }

        } 
        catch (IllegalCommandLineInputsException e) {
            System.err.println("Command Line Error" + e.getMessage());
            System.exit(-1);
        }
        //map reading for either coordinate or text format
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

        //starts to time search algo
        long startTime = System.nanoTime();


        //run the specific algo
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

        //result printed
        if (path == null) {
            //no path
            System.out.println("The Wolverine Store close.");
        } 
        else {
            if (outCoord) {
                //coordinate based output
                for (Position p : path) {
                    String cell = map[p.level][p.row][p.col];
                    if (!cell.equals("W")) {
                        System.out.println("+ " + p.row + " " + p.col + " " + p.level);
                    }
                }
            } 
            else {
                //text based output
                String[][][] solvedMap = MazeSolver.applyPathToMap(map, path);
                MapReading.printMap(solvedMap);
            }
        }
        //runtime if the --Time was set
        if (showTime) {
            double seconds = (endTime-startTime)/1000000000.0;
            System.out.printf("Total Runtime: %.9f seconds%n", seconds);
        }
    }

    //help function

    private static void printHelp() {
        System.out.println("Switches(exactly one of --Stack, --Queue, --Opt is necessary):");
        System.out.println("--Stack: Use stack-based DFS");
        System.out.println("--Queue: Use queue-based BFS");
        System.out.println("--Opt: Find the optimal path");
        System.out.println("--Time: Runtime of the search algorithm");
        System.out.println("--Incoordinate: Input map is in coordinate-based format");
        System.out.println("--Outcoordinate: Output path in coordinate-based format");
        System.out.println("--Help: Show help message and leaves");
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
