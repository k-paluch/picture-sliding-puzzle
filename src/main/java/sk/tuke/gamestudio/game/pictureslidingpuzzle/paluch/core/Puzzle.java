package sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core;

public class Puzzle {
    private char value;

    Puzzle(char value) {
        this.value = value;
    }

    public String toString(){
        return Character.toString(value);
    }

    char getValue(){
        return this.value;
    }
}
