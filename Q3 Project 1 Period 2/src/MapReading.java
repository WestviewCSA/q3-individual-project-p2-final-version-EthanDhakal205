import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//reads text and coordinate based maps from files
public class MapReading {
	//all valid characters
	private static final String fine_char = ".@W$|+";

	//reads textbased map file
	public static String[][][] getTextBasedMap(String fileName) throws FileNotFoundException, IncorrectMapFormatException, IncompleteMapException, IllegalMapCharacterException {
		File file = new File(fileName);
        Scanner scanner = new Scanner(file);

		//first line needs to have three positive integers withs rows cols and levels
		if(!scanner.hasNextInt()) {
			scanner.close();
			throw new IncorrectMapFormatException("Does not work.");
		}
        int rows = scanner.nextInt();
		if(!scanner.hasNextInt()) {
			scanner.close();
			throw new IncorrectMapFormatException("Does not work.");
		}
        int columns = scanner.nextInt();
		if(!scanner.hasNextInt()) {
			scanner.close();
			throw new IncorrectMapFormatException("Does not work.");
		}
        int levels = scanner.nextInt();
        if(rows<=0 || columns<=0 || levels<=0) {
			scanner.close();
			throw new IncorrectMapFormatException("Map dimensions must be positive int.");
		}

		//3d array with levels rows columns
        String[][][] mapping = new String[levels][rows][columns];
        for(int i = 0; i<levels; i++) {
        	for(int j = 0; j<rows; j++) {
				if(!scanner.hasNext()) {
					scanner.close();
					throw new IncompleteMapException("Not enough rows");
				}
        		String line = scanner.next();
				if(line.length()<columns) {
					scanner.close();
					throw new IncompleteMapException("Row doesn't match given size");
				}
        		for(int h = 0; h<columns; h++) {
        			String character = line.substring(h,h+1);
					if(!fine_char.contains(character)) {
						scanner.close();
						throw new IllegalMapCharacterException("Character doesn't work");
					}
					mapping[i][j][h]=character;
        		}
        	}
        }
        scanner.close();
        return mapping;
		
	}
	//coordinate based map reading file
	//unspecified cell atuomatically gets set to a "."
	public static String[][][] getCoordinateBasedMap(String fileName) throws FileNotFoundException,IncorrectMapFormatException,IllegalMapCharacterException,IncompleteMapException {
		
		File file = new File(fileName);
        Scanner scanner = new Scanner(file);
		if(!scanner.hasNextInt()) {
			scanner.close();
			throw new IncorrectMapFormatException("Does not work.");
		}
        int rows = scanner.nextInt();
		if(!scanner.hasNextInt()) {
			scanner.close();
			throw new IncorrectMapFormatException("Does not work.");
		}
        int columns = scanner.nextInt();
		if(!scanner.hasNextInt()) {
			scanner.close();
			throw new IncorrectMapFormatException("Does not work.");
		}
        int levels = scanner.nextInt();
        if(rows<=0 || columns<=0 || levels<=0) {
			scanner.close();
			throw new IncorrectMapFormatException("Map dimensions must be positive int.");
		}
        String[][][] mapping = new String[levels][rows][columns];
        
        //read each coordinate entry and ultimately place it in the array
        while(scanner.hasNext()) {
        	String character = scanner.next();
			if(!fine_char.contains(character)) {
				scanner.close();
				throw new IllegalMapCharacterException("Character doesn't work");
			}
			if(!scanner.hasNextInt()) {
				scanner.close();
				break;
			}
        	int row = Integer.parseInt(scanner.next());
			if(!scanner.hasNextInt()) {
				scanner.close();
				break;
			}
        	int column = Integer.parseInt(scanner.next());
			if(!scanner.hasNextInt()) {
				scanner.close();
				break;
			}
        	int level = Integer.parseInt(scanner.next());
			//coordinates must be int the map bound
        	if (row < 0 || row >= rows || column < 0 || column >= columns || level < 0 || level >= levels) {
                scanner.close();
                throw new IncompleteMapException("Coordinate not in bounds");
            }
        	mapping[level][row][column] = character;
        }

		//fill unspecified cells with a dot
        for(int i = 0; i<levels; i++) {
        	for(int j = 0; j<rows; j++) {
        		for(int h = 0; h<columns; h++) {
        			if(mapping[i][j][h] == null) {
        				mapping[i][j][h] = ".";
        			}
        		}
        	}
        }
        scanner.close();
        return mapping;
		
	}
	//prints either the solved or unsolved map in the text based format
	public static void printMap(String[][][] map) {
		for(int i = 0; i<map.length; i++) {
			for(int j = 0; j<map[i].length; j++) {
				for(int h = 0; h<map[i][j].length; h++) {
					System.out.print(map[i][j][h]);
				}
				System.out.println();				
			}
		}
	} 

}