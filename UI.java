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
    static double fov = 1;
    static double panx = 0;
    static double pany = 0;
    static double fovStep = 1.5;

    public static void main(String[] args) throws Exception {
        int imageRes = 400;

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
                ColorImage newImg = Tests.newtonsFractals(currentCoords.toArray(new ComplexNumber[0]), 20, 6000, fov,
                        panx, pany);
                ColorImage.save("./output/output" + System.currentTimeMillis() + ".png", newImg);
            });
            executorService.shutdown(); // Remember to shut down the ExecutorService when done

        });

        JPanel renderBtns = new JPanel();
        renderBtns.add(preview);
        renderBtns.add(render);

        JFrame f = new JFrame();// creating instance of JFrame
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setSize(500, 700);

        f.setLayout(new FlowLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JPanel canvasHolder = new JPanel();
        canvasHolder.add(canvas);

        panel.add(canvasHolder);
        panel.add(renderBtns);

        panel.add(fovControls(canvas, imageRes));
        panel.add(panControls(canvas, imageRes));

        f.add(panel);

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
            nextCoords = currentCoords;

        currentCoords = nextCoords;
        nextCoords = new ArrayList<ComplexNumber>();
        renderCurrent(canvas, imageRes);
        System.out.println("image updated");
    }

    private static void renderCurrent(JLabel canvas, int imageRes) {
        ColorImage newImg = Tests.newtonsFractals(currentCoords.toArray(new ComplexNumber[0]), 10, 500, fov, panx,
                pany);
        setIcon(canvas, newImg, imageRes);
    }

    private static JPanel fovControls(JLabel canvas, int imageRes) {
        JPanel fovControls = new JPanel();

        JButton decreaseFov = new JButton("-");
        decreaseFov.addActionListener(e -> {
            double panDiff = fov  * (1-fovStep) /2;
            panx += panDiff;
            pany += panDiff;

            fov *= fovStep;

            renderCurrent(canvas, imageRes);
        });
        JButton resetFov = new JButton("reset");
        resetFov.addActionListener(e -> {
            if (fov == 1) {
                return;
            }
            fov = 1;
            renderCurrent(canvas, imageRes);
        });
        JButton increaseFov = new JButton("+");
        increaseFov.addActionListener(e -> {
            double panDiff = fov  * (1-(1/fovStep)) /2;
            panx += panDiff;
            pany += panDiff;

            fov /= fovStep;
            renderCurrent(canvas, imageRes);
        });

        fovControls.add(decreaseFov);
        fovControls.add(resetFov);
        fovControls.add(increaseFov);

        return fovControls;
    }

    private static JPanel panControls(JLabel canvas, int imageRes) {
        JPanel panControls = new JPanel();
        panControls.setLayout(new BoxLayout(panControls, BoxLayout.PAGE_AXIS));

        JButton resetPan = new JButton("reset");
        resetPan.addActionListener(e -> {
            if (panx == 0 && pany == 0) {
                return;
            }
            panx = 0;
            pany = 0;
            renderCurrent(canvas, imageRes);
        });

        JButton panNegX = new JButton("<");
        panNegX.addActionListener(e -> {
            panx -= .25 * fov;
            renderCurrent(canvas, imageRes);
        });

        JButton panPosX = new JButton(">");
        panPosX.addActionListener(e -> {
            panx += .25 * fov;
            renderCurrent(canvas, imageRes);
        });

        JPanel xPan = new JPanel();
        xPan.add(panNegX);
        xPan.add(resetPan);
        xPan.add(panPosX);

        
        JButton panNegY = new JButton("v");
        panNegY.addActionListener(e -> {
            pany -= .25 * fov;
            renderCurrent(canvas, imageRes);
        });
        JPanel panNegYContainer = new JPanel();
        panNegYContainer.add(panNegY);
        
        JButton panPosY = new JButton("^");
        panPosY.addActionListener(e -> {
            pany += .25 * fov;
            renderCurrent(canvas, imageRes);
        });
        JPanel panPosYContainer = new JPanel();
        panPosYContainer.add(panPosY);

        panControls.add(panPosYContainer);
        panControls.add(xPan);
        panControls.add(panNegYContainer);

        return panControls;
    }
}
