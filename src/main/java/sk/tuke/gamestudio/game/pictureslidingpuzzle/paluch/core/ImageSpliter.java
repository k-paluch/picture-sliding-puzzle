package sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageSpliter {
private int height;
private int width;
    public ImageSpliter(){
    }

    public  BufferedImage[][] getImages(Image img, int rows, int column) throws IOException {
        BufferedImage[][] splitImages = new BufferedImage[rows][column];
        img = img.getScaledInstance(500,500,Image.SCALE_DEFAULT);
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, null);
        int width = bi.getWidth();
        int height = bi.getHeight();
        int sub_width = width / column;
        this.width = sub_width;
        int sub_height = height / rows;
        this.height = sub_height;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < column; j++) {
                BufferedImage bimg = bi.getSubimage(j * sub_width, i * sub_height, sub_width, sub_height);
                splitImages[i][j] = bimg;
                //ImageIO.write(splitImages[i][j],"png", new File("src\\main\\resources\\static\\images\\pictureslidingpuzzle\\paluch\\" + i + j + ".png"));
            }
        }
        return splitImages;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
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
