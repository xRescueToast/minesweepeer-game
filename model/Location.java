package model;

/**
 * simple class to represent the location of an object in a 2D grid
 */
public class Location {
    private int row;
    private int col;

    public Location(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public int hashCode(){
        String str = "" + row + col;
        return Integer.parseInt(str);
    }

    @Override
    public boolean equals(Object o) {
        if(!this.getClass().equals(o.getClass())) {return false;}
        return this.row == ((Location) o).getRow() && this.col == ((Location) o).getCol();
    }

    @Override
    public String toString(){
        return "" + row + col;
    }   

}