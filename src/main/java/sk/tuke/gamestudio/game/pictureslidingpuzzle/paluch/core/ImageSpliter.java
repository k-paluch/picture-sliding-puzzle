package sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageSpliter {

    public ImageSpliter(Image img, int rows, int column){
        this.getImages(img,rows,column);
    }

    public  BufferedImage[] getImages(Image img, int rows, int column) {
        BufferedImage[] splittedImages = new BufferedImage[rows * column];
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, null);
        int width = bi.getWidth();
        int height = bi.getHeight();
        int pos = 0;
        int swidth = width / column;
        int sheight = height / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < column; j++) {
                BufferedImage bimg = bi.getSubimage(j * swidth, i * sheight, swidth, sheight);
                splittedImages[pos] = bimg;
                pos++;
            }
        }

        return splittedImages;
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
