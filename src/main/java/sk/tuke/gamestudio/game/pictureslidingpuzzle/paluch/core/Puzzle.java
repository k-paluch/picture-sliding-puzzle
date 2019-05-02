package sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Puzzle {
    private char value;
    private String imgValue;
    private BufferedImage bufferedImage;

    Puzzle(char value) {
        this.value = value;
    }

    Puzzle(BufferedImage bufferedImage, String imgValue){
        this.bufferedImage = bufferedImage;
        this.imgValue = imgValue;
    }

    public String imgValue(){
        return this.imgValue;
    }


    public String toString(){
        return Character.toString(value);
    }

    public BufferedImage getBufferedImage (){
        return this.bufferedImage;
    }

    public byte[] getBytes() throws IOException {
        byte[] imageBytes = Files.readAllBytes((Path) getBufferedImage());
        return imageBytes;
    }

    char getValue(){
        return this.value;
    }
}
