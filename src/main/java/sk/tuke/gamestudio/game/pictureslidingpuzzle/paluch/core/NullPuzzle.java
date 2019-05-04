package sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NullPuzzle {
    private int row;
    private int column;
    private BufferedImage bufferedImage;
    NullPuzzle(int row, int column, BufferedImage bufferedImage){
        this.row = row;
        this.column = column;
        this.bufferedImage = bufferedImage;
    }
    int getRow(){
        return this.row;
    }

    int getColumn(){
        return this.column;
    }

    public String encode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(this.bufferedImage,"png",baos);
        String data = DatatypeConverter.printBase64Binary(baos.toByteArray());
        String imageString = "data:image/png;base64," + data;
        return "<img src='" + imageString + "'";
    }

    void setRow(int row){
        this.row = row;
    }

    void setColumn(int column){
        this.column = column;
    }
}
