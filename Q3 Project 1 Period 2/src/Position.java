public class Position {
    public int level;
    public int row;
    public int col;
    public Position parent;

    public Position(int level, int row, int col, Position parent) {
        this.level = level;
        this.row = row;
        this.col = col;
        this.parent = parent;
    }
    public boolean equals(Position o) {
      if(this==o) {
        return true;
      }
      return level==o.level && row==o.row && col==o.col;
    }

  public String toString() {
    return "(" + level + ", " + row + ", " + col + ")";
  }
    
