package sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Puzzle {
    private char value;
    private String imgValue;
    private BufferedImage bufferedImage;
    private int high;
    private int width;

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
    public String encode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(this.bufferedImage,"png",baos);
        String data = DatatypeConverter.printBase64Binary(baos.toByteArray());
        String imageString = "data:image/png;base64," + data;
        return "<img src='" + imageString + "'";
    }


    public void setHigh(int high){
        this.high = high;
    }
    public void setWidth(int width){
        this.width = width;
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
