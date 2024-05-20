package model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import view.MinesweeperGUI;

public class SquareHandler implements EventHandler<ActionEvent> {

    private Location location;
    private Minesweeper game;
    private MinesweeperGUI gui;

    public SquareHandler(Location location, Minesweeper game, MinesweeperGUI gui) {
        this.location = location; 
        this.game = game;
        this.gui = gui;
    }

    @Override
    public void handle(ActionEvent event) {

        if(!gui.isFlagMode()) {
            game.makeSelection(location);
            gui.cellUpdated(location);
        }

        else {
            gui.flag(location);
        }
        
       
    }


}
