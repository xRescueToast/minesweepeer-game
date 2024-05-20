package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import backtracker.Backtracker;
import backtracker.Configuration;

/**
 * Minesweeper model
 * 
 * @author Coray Bennett
 * @author Jon Rodriguez
 * @author Carick De Rensis-Williams
 * @version 1.0
 * 
 */
public class Minesweeper implements Configuration {
    private final char MINE = 'M';
    private final char COVERED = '-';
    private final int ROWS, COLS;

    private GameState state;
    private int mineCount, moveCount;
    private Square[][] board;
    private Set<Location> mineLocations;

    /**
     * Minesweeper constructor
     * 
     * @param rows      : number of rows in the game board
     * @param cols      : number of columns in the game board
     * @param mineCount : number of mines placed on the board
     */
    public Minesweeper(int rows, int cols, int mineCount) {
        ROWS = rows;
        COLS = cols;
        this.mineCount = mineCount;
        this.state = GameState.NOT_STARTED;
        this.board = new Square[ROWS][COLS];
        this.mineLocations = new HashSet<>();
        initBoard();

    }

    public Minesweeper(Minesweeper minesweeper) {
        this.ROWS = minesweeper.ROWS;
        this.COLS = minesweeper.COLS;
        this.mineCount = minesweeper.mineCount;
        this.moveCount = minesweeper.getMoveCount();
        this.board = new Square[ROWS][COLS];
        for(int r = 0; r < ROWS; r++) {
            for(int c = 0; c < COLS; c++) {
                
                this.board[r][c] = new Square(minesweeper.board[r][c]);
            }
        }
        this.state = minesweeper.state;
        this.mineLocations = minesweeper.mineLocations;
    }

    /**
     * 
     * determines where mines will be placed on the board
     * 
     * @param square : the first square selected in a game
     */
    private void placeMines(Square square) {
        Random rand = new Random();
        int minesPlaced = 0;
        ArrayList<Square> adjacents = square.getAdjacents(board);

        while (minesPlaced < mineCount) { 
            int r = rand.nextInt(ROWS);
            int c = rand.nextInt(COLS);
            Location location = new Location(r, c);

            boolean add = true;
            if (square.getLocation().equals(location)) {
                add = false;
            }
            for (Square s : adjacents) {
                if (s.getLocation().equals(location)) {
                    add = false;
                    break;
                }
            }
            if (add) { // location only added if it is not adjacent or equal to the starting square
                mineLocations.add(location);
            }
            minesPlaced = mineLocations.size();
        }

    }

    //initializes the board with covered squares (no mines)
    private void initBoard() {

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Location location = new Location(r, c);
                board[r][c] = new Square(false, location);
            }
        }

    }

    public int getMoveCount() {
        return moveCount;
    }

    public GameState getGameState() {
        return state;
    }

    public Square[][] getBoard() {
        return board;
    }

    //selects and uncovers a single square, updating the game state accordingly
    public void makeSelection(Location location) {

        int row = location.getRow();
        int col = location.getCol();
        Square square;

        try {
            square = board[row][col];
        } catch (IndexOutOfBoundsException e) {
            //
            return;
        }

        boolean mine;
        if (moveCount == 0) {
            placeMines(square);
            for (Location l : mineLocations) {
                board[l.getRow()][l.getCol()].setMine(true);
            }
        }

        if (square.isCovered()) {
            mine = square.uncover(board);
        } else {
            //
            return;
        }

        if (mine) {
            state = GameState.LOST;
        }

        Collection<Location> selections = getPossibleSelections();
        boolean won = true;
        for (Location l : selections) {
            if (!board[l.getRow()][l.getCol()].isMine()) {
                won = false;
                break;
            }

        }
        if (won) {
            state = GameState.WON;
        }

        moveCount++;
        if (moveCount == 1) {
            state = GameState.IN_PROGRESS;
        }

    }

    /**
     * @return returns a collection (array list) of all possible next moves (covered squares)
     */
    public Collection<Location> getPossibleSelections() {

        Collection<Location> selections = new ArrayList<>();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c].isCovered()) {
                    selections.add(board[r][c].getLocation());
                }
            }
        }

        return selections;
    }

    //uncovers all squares on board
    public void uncoverAll(){
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c].uncover(board);
            }
        }
    }

    //setter for game state, sets to lost
    public void quitGame(){
        state = GameState.LOST;
    }

    public char getSymbol(Location location) {
        char symbol;
        int r = location.getRow();
        int c = location.getCol();
        Square square = board[r][c];
        
        if(square.isCovered()) {
            symbol = COVERED;
        }

        else if(square.isMine()) {
            symbol = MINE;
        }

        else {
            symbol = String.valueOf(square.getNum(board)).charAt(0);
        }

        return symbol;
    }

    public boolean isCovered(Location location) {
        int r = location.getRow();
        int c = location.getCol();
        Square square = board[r][c];

        return square.isCovered();
    }

    /**
     * EXAMPLE:
     * 
     * GAME IS : {in progress, lost, won, not started}
     * MOVES MADE : moveCount
     * MINES : mineCount
     *    0  1  2  3  4
     * 0 [-][-][-][-][-]
     * 1 [1][1][1][3][-]
     * 2 [0][0][0][1][-]
     * 3 [1][1][2][1][-]
     * 4 [-][-][-][-][-]
     * 
     * 
     */
    public String toString() {

        String str = "";

        str += "GAME IS : " + state.toString() + "\n";
        str += "MOVES MADE: " + moveCount + "\n";
        str += "MINES : " + mineCount + "\n";

        int rowLen = String.valueOf(ROWS).length();
        int colLen = String.valueOf(COLS).length();
        
        for(int i = 0; i < rowLen + 1; i++) {
            str += " ";
        }
        
        for (int i = 0; i < COLS; i++) {
            str += i;
            for(int k = 0; k < (colLen + 2) - String.valueOf(i).length(); k++) {
                str += " ";
            }
        }
        str += "\n";

        for (int r = 0; r < ROWS; r++) {
            
            String rowLabel = String.valueOf(r);
            int whitespace = rowLen - rowLabel.length();
            for(int i = 0; i < whitespace ; i++) {rowLabel += " ";}
            str += rowLabel;

            for (int c = 0; c < COLS; c++) {

                Square square = board[r][c];

                str += "[";

                if (square.isCovered()) {
                    str += COVERED;
                    str += "]";
                }

                else if (!square.isMine()) {
                    str += square.getNum(board);
                    str += "]";
                }

                else {
                    str += MINE;
                    str += "]";
                }
                
                for(int i = 0; i < colLen - 1; i++) {
                    str += " ";
                }

            }
            str += "\n";
        }

        return str;
    }

    public static void main(String[] args) {
        Minesweeper m = new Minesweeper(4, 4, 10);
        m.makeSelection(new Location(0, 0));
        Backtracker backtracker = new Backtracker(false);
        System.out.println(backtracker.solve(m));
        }

    @Override
    public Collection<Configuration> getSuccessors() {
        Collection<Configuration> successors = new ArrayList<>();
        for(Location loc : getPossibleSelections()) {
            Minesweeper m = new Minesweeper(this);
            m.makeSelection(loc);
            if(m.isValid()) {
                successors.add(m);
            }
        }

        return successors;
    }

    @Override
    public boolean isValid() {
        return !this.state.equals(GameState.LOST);
    }

    @Override
    public boolean isGoal() {
        return this.state.equals(GameState.WON);
    }

}
