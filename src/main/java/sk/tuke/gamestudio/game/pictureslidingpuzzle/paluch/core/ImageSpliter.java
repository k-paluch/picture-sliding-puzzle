package sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageSpliter {

    public ImageSpliter(){
    }

    public  BufferedImage[][] getImages(Image img, int rows, int column) {
        BufferedImage[][] splitImages = new BufferedImage[rows][column];
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, null);
        int width = bi.getWidth();
        int height = bi.getHeight();
        int sub_width = width / column;
        int sub_height = height / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < column; j++) {
                BufferedImage bimg = bi.getSubimage(j * sub_width, i * sub_height, sub_width, sub_height);
                splitImages[i][j] = bimg;
            }
        }
        return splitImages;
    }

    /*public static void main(String args[]) throws IOException {
        if(args.length >= 3){
            BufferedImage bi = ImageIO.read(new File(args[0]));
            int rcount = Integer.parseInt(args[1]);
            int ccount = Integer.parseInt(args[2]);
            Image img = bi.getScaledInstance(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
            BufferedImage[] imgs = ImageSpliter.getImages(img, rcount, ccount);
            for(int i=0; i < imgs.length; i++){
                ImageIO.write(imgs[i], "jpg", new File("img"+i+".jpg"));
            }
        } else {
            System.out.println("Usage: image-file-path rows-count column-count");
        }
    }*/
}
