import java.io.File;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ColorImage {
    Color[][] arr;

    public ColorImage(int cols,int rows){
        arr = new Color[cols][rows];
    }

    public void put(Color c, int x, int y){
        arr[x][y] = c;
    }

    public Color get(int x, int y){
        return arr[x][y];
    }

    public int getWidth(){
        return arr.length;
    }

    public int getHeight(){
        return arr[0].length;
    }


     public BufferedImage toBufferedImage() {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                bi.setRGB(x, getHeight() - 1 - y, get(x, y).toARGB());
                // bi.setRGB(x,y,image.getColor(x,y).toARGB());
            }
        }
        return bi;
    }

    // Write image to file
    public static void save(String filename, ColorImage image) {
        try {

            BufferedImage bi = image.toBufferedImage();
            File f = new File(filename);
            f.mkdirs();
            ImageIO.write(bi, "PNG", f);

        } catch (Exception e) {
            System.out.println("Problem saving image: " + filename);
            System.out.println(e);
            System.exit(1);
        }
    }
}


