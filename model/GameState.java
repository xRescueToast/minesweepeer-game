package model;

public enum GameState {
    NOT_STARTED ("Not Started"),
    IN_PROGRESS ("In Progress"),
    WON ("Won"),
    LOST ("Lost");

    private String name;

    private GameState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


}
