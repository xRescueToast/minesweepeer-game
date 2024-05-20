package view;


import java.util.ArrayList;
import java.util.Random;

import backtracker.Backtracker;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.GameState;
import model.Location;
import model.Minesweeper;
import model.MinesweeperObserver;
import model.Square;
import model.SquareHandler;


/**
 * minesweeper GUI view
 */
public class MinesweeperGUI extends Application implements MinesweeperObserver {

    //static variables, consistent through the runtime of the GUI
    private static Minesweeper game;
    private static GridPane view;
    private static HBox window; // lets go hbox
    private static VBox controls; 
    private static Label state, moveCount;
    private static boolean flag_mode = false;

    //single button event handlers

    private EventHandler<ActionEvent> resetGame = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            newGame();
        }
    };

    private EventHandler<ActionEvent> hintHandler = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            getHint();
        }
    };

    //toggles flag mode
    private EventHandler<ActionEvent> flagger = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            Button source = (Button) event.getSource();
            flag_mode = !flag_mode;

            if(flag_mode) {
               source.setText("Toggle Flags: ON");
               
            }

            else {
                source.setText("Toggle Flags: OFF");
            }
            
        }
    };

    private EventHandler<ActionEvent> solver = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            solveBoard();
        }
    };

    
    //more static variables, constants
    private static final int ROWS, COLS, MINE_COUNT;
    private static final Image MINE = new Image("media/images/mine24.png");
    private static final Image FLAG = new Image("media/images/flag24.png");

    static {
        ROWS = 12;
        COLS = 15;
        MINE_COUNT = (1 - ROWS) * (1 - COLS) / 4;
    }
    
    //start function, sets up display window
    @Override
    public void start(Stage stage) throws Exception {

        game = new Minesweeper(ROWS, COLS, MINE_COUNT);
        stage.setTitle("minesweeper");

        //game board
        view  = new GridPane();

        for(int r = 0; r < ROWS; r++){
            for(int c = 0; c < COLS; c++){
                view.add(makeButton(new Location(r, c)), c, r);
            }
        }

        //controls + labels for info
        controls = new VBox();

        Button reset = new Button("RESET"); //reset game
        reset.setPrefSize(100, 50);
        reset.setFont(new Font(20));
        reset.setOnAction(resetGame);

        Button hint = new Button("HINT"); //give a hint
        hint.setPrefSize(100, 50);
        hint.setFont(new Font(20));
        hint.setOnAction(hintHandler);

        Button flag = new Button("Toggle Flags: OFF"); //toggle flags
        flag.setPrefSize(100, 50);
        flag.setFont(new Font(10));
        flag.setOnAction(flagger);

        Button solve = new Button("SOLVE");
        solve.setPrefSize(100, 50);
        solve.setFont(new Font(20));
        solve.setOnAction(solver);

        state = new Label(); //shows state of current game
        state.setAlignment(Pos.CENTER);
        state.setPrefSize(100, 50);
        state.setFont(new Font(15));
        state.setText(game.getGameState().toString());

        moveCount = new Label(); //shows number of moves made in current game
        moveCount.setAlignment(Pos.CENTER);
        moveCount.setPrefSize(100, 50);
        moveCount.setFont(new Font(15));
        moveCount.setText("Moves: " + game.getMoveCount());

        Label mines = new Label(MINE_COUNT + " MINES"); //shows number of mines in game
        mines.setAlignment(Pos.CENTER);
        mines.setPrefSize(100, 50);
        mines.setFont(new Font(15));


        controls.getChildren().addAll(reset, hint, flag, state, moveCount, mines, solve);

        //full window
        window = new HBox();
        window.getChildren().addAll(controls, view);

        stage.setScene(new Scene(window));
        stage.show();
    }

    private void newGame() {
        game = new Minesweeper(ROWS, COLS, MINE_COUNT);

        for(int r = 0; r < ROWS; r++) {
            for(int c = 0; c < COLS; c++) {
                Location l = new Location(r, c);
                Button b = (Button)getGridButton(l, view);
                b.setText("");
                b.setGraphic(null);
                b.setBackground(
                    new Background(
                    new BackgroundFill(Color.LIGHTGREY, 
                    null,
                    null
                        )
                    )
                );
        b.setStyle("-fx-border-color: #000000; -fx-border-width: 1px;");
                b.setOnAction(
                    new SquareHandler(l, game, this)
                );
            }
        }

        cellUpdated(new Location(0, 0));
    }

    //highlights one valid square
    private void getHint() {
        Random rand = new Random();
        ArrayList<Location> hints = (ArrayList<Location>)game.getPossibleSelections();
        int size = hints.size();

        int selection = rand.nextInt(size);
        Location hint = hints.get(selection);

        Button h = (Button) getGridButton(hint, view);

        h.setBackground(
            new Background(
                new BackgroundFill(Color.LIGHTGREEN, 
                null,
                null
                )
            )
        );
    }

    //makes a game button representing a square at some location
    private Button makeButton(Location location){
        
        Button button = new Button("");
        button.setPrefSize(60, 60);
        button.setFont(new Font(24));
        button.setBackground(
            new Background(
                new BackgroundFill(Color.LIGHTGREY, 
                null,
                null
                )
            )
        );
        button.setStyle("-fx-border-color: #000000; -fx-border-width: 1px;");
        button.setOnAction(new SquareHandler(location, game, this));
        return button;
    }

    //getter for flag mode
    public boolean isFlagMode() {
        return flag_mode;
    }

    //the goat
    public static Node getGridButton(Location location, GridPane pane) {
        int row = location.getRow();
        int col = location.getCol();

        for(Node n : pane.getChildren()) {
            if(GridPane.getRowIndex(n).equals(row) && GridPane.getColumnIndex(n).equals(col)) {
                return n;
            }
        }

        return null;

    }

    //called when flag button event is handled
    public void flag(Location location) {
        Button b = (Button) getGridButton(location, view);
        if(b.getGraphic() == null) {
            b.setGraphic(new ImageView(FLAG));
            b.setText(null);
        }
        else {
            b.setGraphic(null);
            if(game.getSymbol(location) != '-') {
                b.setText(String.valueOf(game.getSymbol(location)));
            }
            
        }
        
    }

    public void solveBoard() {
        Location l = new Location(0, 0);
        Backtracker backtracker = new Backtracker(false);
        if(game.getMoveCount() == 0) {
            game.makeSelection(l);
            cellUpdated(l);
        }
        game = (Minesweeper)backtracker.solve(game);
        cellUpdated(l);
    }

    //Board observer function, updates visuals
    @Override
    public void cellUpdated(Location location) {

        Button source = (Button) getGridButton(location, view);
        source.setBackground(
            new Background(
                new BackgroundFill(Color.LIGHTGREY, 
                null,
                null
                )
            )
        );
        source.setStyle("-fx-border-color: #000000; -fx-border-width: 1px;");

        //update labels
        state.setText(game.getGameState().toString());
        moveCount.setText("Moves: " + game.getMoveCount());

        Square[][] board = game.getBoard(); // get board to make updating each square easier

        //end of game conditions
        if(game.getGameState().equals(GameState.LOST) || game.getGameState().equals(GameState.WON)) {
            game.uncoverAll();
        } 
        
        //loops over all squares and updates newly uncovered squares
        for(int r = 0; r < ROWS; r++) {
            for(int c = 0; c < COLS; c++) {
                Square s = board[r][c];
                if(!game.isCovered(s.getLocation())) {
                    Button button = (Button) getGridButton(s.getLocation(), view);
                    String symbol = String.valueOf(game.getSymbol(s.getLocation()));
                    if(button != null) {
                        if(s.isMine()) {
                            button.setGraphic(new ImageView(MINE));
                        }
                        else {
                            button.setText(symbol);
                            button.setGraphic(null);
                        }
                    }
                    
                }
                
            }
        }

       
    }

    public static void main(String[] args) {
        launch(args);
    }

}