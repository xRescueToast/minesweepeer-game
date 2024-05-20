package model;

public class MinesweeperException extends Exception{
    //possible cases for selection exceptions:
    //selecting a previously selected tile
    //selecting an element not inside the playable field
    public MinesweeperException(String message){
        super(message);
    }
}