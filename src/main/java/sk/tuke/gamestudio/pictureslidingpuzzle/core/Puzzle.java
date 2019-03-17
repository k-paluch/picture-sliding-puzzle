package sk.tuke.gamestudio.pictureslidingpuzzle.core;

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
