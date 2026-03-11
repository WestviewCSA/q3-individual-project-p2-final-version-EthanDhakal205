import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MapReading {
	private static final String fine_char = ".@W$|+";
	
	public static String[][][] getTextBasedMap(String fileName) throws FileNotFoundException, IncorrectMapFormatException, IncompleteMapException, IllegalMapCharactersException {
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
	public static String[][][] getCoordinateBasedMap(String fileName) throws FileNotFoundException {
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
        	if (row < 0 || row >= rows || col < 0 || col >= columns || level < 0 || level >= levels) {
                scanner.close();
                throw new IncompleteMapException("Coordinate not in bounds");
            }
        	mapping[level][row][column] = character;
        }
        
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
