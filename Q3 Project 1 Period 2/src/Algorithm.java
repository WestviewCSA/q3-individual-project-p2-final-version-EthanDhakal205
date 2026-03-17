import MapReading;
import java.util.Stack;

public class Algorithm {

public static void main(String[] args) {

}

public static void StackBased(String[][] map){
Stack<Position> all = new Stack<>();
Stack<Position> order = new Stack<>();

Position startPos = new Position(-1,-1);
for(int i = 0; i<map.length; i++) {
    for(int j = 0; j<map.length; j++) {
        if(map[i][j].equals("W")) {
            startPos = new Position(i,j);
        }
    }
}

if(startPos.row == -1 || startPos.col == -1) {
    System.out.println("Not in the file");
}
else {
    currentPos = startPos;
    all.push(currentPos);
    while(true) {
        order.push(all.peek());
        if(all.peek().)
    }
}
}

}