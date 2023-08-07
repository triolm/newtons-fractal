import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class UI {
    static ArrayList<ComplexNumber> nextCoords;
    static ArrayList<ComplexNumber> currentCoords;

    public static void main(String[] args) throws Exception {
        int imageRes = 500;

        currentCoords = new ArrayList<ComplexNumber>();

        nextCoords = new ArrayList<ComplexNumber>();
        nextCoords.add(new ComplexNumber(0, 1));
        nextCoords.add(new ComplexNumber(1, 0));
        nextCoords.add(new ComplexNumber(0, 0));
        nextCoords.add(new ComplexNumber(1, 1));

        JLabel canvas = new JLabel();
        BufferedImage img = ImageIO.read(new File("./output/image2.png"));
        setIcon(canvas, img, imageRes);

        resetCoordinateData(canvas, imageRes);

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ComplexNumber c = new ComplexNumber((double) e.getX() / imageRes, (double) e.getY() / imageRes);
                System.out.println(c);
                nextCoords.add(c);
            }

        });

        JButton preview = new JButton("preview");
        preview.addActionListener(e -> {
            resetCoordinateData(canvas, imageRes);
        });
        JButton render = new JButton("Render");
        render.addActionListener(e -> {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                ColorImage newImg = Tests.newtonsFractals(currentCoords.toArray(new ComplexNumber[0]), 20, 6000);
                ColorImage.save("./output/output" + System.currentTimeMillis() + ".png", newImg);
            });
            executorService.shutdown(); // Remember to shut down the ExecutorService when done

        });

        JFrame f = new JFrame();// creating instance of JFrame
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setSize(600, 600);
        f.setLayout(new FlowLayout());
        f.add(canvas);
        f.add(preview);
        f.add(render);
        f.setVisible(true);
    }

    private static void setIcon(JLabel canvas, ColorImage img, int imageRes) {
        setIcon(canvas, img.toBufferedImage(), imageRes);
    }

    private static void setIcon(JLabel canvas, BufferedImage img, int imageRes) {
        canvas.setIcon(new ImageIcon(img.getScaledInstance(imageRes, imageRes, java.awt.Image.SCALE_SMOOTH)));
    }

    private static void resetCoordinateData(JLabel canvas, int imageRes) {
        if (nextCoords.size() == 0)
            return;

        ColorImage newImg = Tests.newtonsFractals(nextCoords.toArray(new ComplexNumber[0]), 10, 500);
        setIcon(canvas, newImg, imageRes);
        currentCoords = nextCoords;
        nextCoords = new ArrayList<ComplexNumber>();
        System.out.println("image updated");
    }
}
