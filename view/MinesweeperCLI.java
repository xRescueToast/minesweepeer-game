package view;


import java.util.ArrayList;

import java.util.Random;
import java.util.Scanner;

import backtracker.Backtracker;
import model.GameState;
import model.Location;
import model.Minesweeper;

public class MinesweeperCLI {
    private static Minesweeper game;
    private static int rows, cols, mineCount;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number of rows: ");
        rows = scanner.nextInt();
        System.out.print("Enter a number of cols: ");
        cols = scanner.nextInt();
        mineCount = (rows - 1) * (cols - 1) / 4; // mine count is 1/4 the maximum allowed minesweeper density

        game = new Minesweeper(rows, cols, mineCount);
        System.out.println(game.toString());
        scanner.nextLine();

        //input loop
        while (game.getGameState() != GameState.WON && game.getGameState() != GameState.LOST) {
            System.out.print("Enter a command: ");
            String input = scanner.nextLine();
            checkInput(input);
        }
        
        if(game.getGameState() == GameState.LOST){ game.uncoverAll(); }
        System.out.println(game.toString());

        scanner.close();

    }


    //checks all input cases
    public static void checkInput(String command) {
        String[] data = command.toLowerCase().split(" ");
        if (data[0].equals("help")) {
            help();
        }
        else if (data[0].equals("hint")) {
            hint();
        }
        else if(data[0].equals("pick")){
            try{
                int row = Integer.parseInt(data[1]);
                int col = Integer.parseInt(data[2]);
                pick(row, col);
            }
            catch(IndexOutOfBoundsException e){
                System.out.println("required input not provided, try: (pick) (row) (col)");
            }
        }
        else if(data[0].equals("reset")){
            reset();
        }
        else if(data[0].equals("quit")){
            quit();
        }
        else if(data[0].equals("solve")) {
            solve();
        }
        else{
            System.out.println("invalid command: " + command);
        }

    }

    // all following functions are input functions

    public static void help() {
        System.out.println("Commands:");
        System.out.println("\thelp: Lists all other commands");
        System.out.println("\thint: Displays an avaliable move");
        System.out.println("\tpick (row) (col): Picks a square to uncover (space between the two numbers when calling)");
        System.out.println("\treset: Resets the board");
        System.out.println("\tsolve; solves the current board");
        System.out.println("\tquit: Quits the game");

    }

    public static void hint() {
        Random rand = new Random();
        ArrayList<Location> hints = (ArrayList<Location>)game.getPossibleSelections();
        int size = hints.size();

        int selection = rand.nextInt(size);
        Location hint = hints.get(selection);
        System.out.println("hint: row - " + hint.getRow() + " col - " + hint.getCol());
    }

    public static void pick(int row, int col){
        Location selection = new Location(row, col);
        game.makeSelection(selection);
        System.out.println(game.toString());
    }

    public static void reset(){
        game = new Minesweeper(rows, cols, mineCount);
        System.out.println(game.toString());
    }

    public static void quit(){
        game.quitGame();
    }

    public static void solve() {
        Backtracker backtracker = new Backtracker(false);
        if(game.getMoveCount() == 0) {
            game.makeSelection(new Location(0, 0));
        }

        game = (Minesweeper)backtracker.solve(game);
    }

}
