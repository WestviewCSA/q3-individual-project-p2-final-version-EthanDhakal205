public class Position {
    //g is the cost from the start, which means it only gets used by the A*
    //reference of parent psoition is stored so that the full path can be reconstructed
    public int level;
    public int row;
    public int col;
    public Position parent;
    public int g;

    
    public Position(int level, int row, int col, Position parent) {
        this.level = level;
        this.row = row;
        this.col = col;
        this.parent = parent;
        this.g = 0;
    }

    //only a* uses this one because the g gets set
    public Position(int level, int row, int col, Position parent, int g) {
        this.level = level;
        this.row = row;
        this.col = col;
        this.parent = parent;
        this.g = g;
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
}
    
