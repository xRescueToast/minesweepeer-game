package model;

import java.util.ArrayList;

public class Square {
    private boolean mine;
    private boolean covered;
    private Location location;


    private final int[][] neighborOffset = {
            { -1, -1 },
            { -1, 0 },
            { -1, 1 },
            { 0, -1 },
            { 0, 1 },
            { 1, -1 },
            { 1, 0 },
            { 1, 1 }
    };

    // covered is always set to true because the initial state of the board consists
    // entirely of covered squares
    Square(boolean mine, Location location) {
        this.mine = mine;
        this.covered = true;
        this.location = location;
    }

    //copy constructor for squares
    Square(Square square) {
        this.mine = square.mine;
        this.covered = square.covered;
        this.location = square.location;
    }

    // returns the adjacent squares in an arraylist (useful for displaying adjacent
    // mine count when a square is selected and uncovering multiple squares at once)
    public ArrayList<Square> getAdjacents(Square[][] board) {
        final int row = location.getRow();
        final int col = location.getCol();
        // algorithm here
        // needs to check for edge cases where the amount of adjacents is effected by
        // the square being in an edge or corner
        ArrayList<Square> adjacents = new ArrayList<>();

        for (int i = 0; i < 8; i++) {

            try {
                adjacents.add(board[row + neighborOffset[i][0]][col + neighborOffset[i][1]]);
            } catch (IndexOutOfBoundsException e) {
            }

        }

        return adjacents;
    }

    // uncoveres a square. if the square has no mine adjacent it will uncover those
    // squares as well. this will continue until an adjacent mine is found
    public boolean uncover(Square[][] board) {
        this.covered = false;
        // if getNum is 0 then call uncover on all those adjacents as well
        if (!mine && getNum(board) == 0) {
            ArrayList<Square> adjacents = getAdjacents(board);
            for (Square s : adjacents) {
                if (s.isCovered()) {
                    s.uncover(board);
                }
            }
        }
        return mine;
    }

    // returns the number of mines around this square
    public int getNum(Square[][] board) {
        ArrayList<Square> adjacents = getAdjacents(board);
        int mines = 0;
        for (Square s : adjacents) {
            if (s.isMine()) {
                mines++;
            }
        }
        return mines;
    }

    // returns the location information of the square
    public Location getLocation() {
        return location;
    }

    // returns if the square contains a mine or not
    public boolean isMine() {
        return mine;
    }

    // returns if the square is covered or not (useful for toString and
    // MineSweeper.getPossibleSelections())
    public boolean isCovered() {
        return covered;
    }

    public void setMine(boolean state){
        mine = state;
    }

    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(getClass())) {
            return false;
        }
        return location.equals(((Square) o).getLocation());
    }
}
