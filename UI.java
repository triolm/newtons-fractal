import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class UI {
    static ArrayList<ComplexNumber> nextCoords;
    static ImageGenerator current;
    final static double defaultFov = 4;
    static double fov = defaultFov;
    final static double fovStep = 1.5;
    static double panx = 0;
    static double pany = 0;
    final static double panStep = .25;
    static String fractalType = "Newton";

    public static void main(String[] args) throws Exception {
        int imageRes = 400;

        current = new NewtonsFractals(ComplexNumber.zeroArr);

        nextCoords = defaultCoords();

        JLabel canvas = new JLabel();

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

        JButton render = new JButton("render canvas");
        render.addActionListener(e -> {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                current.generate(20, 6000, fov, panx, pany);
                current.save();
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

        JPanel canvasHolder = new JPanel();
        canvasHolder.add(canvas);

        String[] fractalTypes = { "Newton", "Mandelbrot", "Julia" };
        JComboBox<String> fractalTypeSelect = new JComboBox<String>(fractalTypes);
        fractalTypeSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fractalType = (String) fractalTypeSelect.getSelectedItem();
                resetCoordinateData(canvas, imageRes);
            }
        });

        JPanel col1 = new JPanel();
        col1.setLayout(new BoxLayout(col1, BoxLayout.PAGE_AXIS));

        col1.add(renderBtns);

        col1.add(fovControls(canvas, imageRes));
        col1.add(panControls(canvas, imageRes));

        JPanel col2 = new JPanel();
        col2.setLayout(new BoxLayout(col2, BoxLayout.PAGE_AXIS));
        col2.add(fractalTypeSelect);

        JPanel controls = new JPanel();

        controls.add(col1);
        controls.add(col2);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(canvasHolder);
        panel.add(controls);

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

        if (fractalType.equals("Newton")) {
            if (nextCoords.size() != 0) {
                current = new NewtonsFractals(nextCoords.toArray(new ComplexNumber[0]));
            } else if (nextCoords.size() == 0 && !(current instanceof NewtonsFractals)) {
                current = new NewtonsFractals(defaultCoords().toArray(new ComplexNumber[0]));
            }
        } else if (fractalType.equals("Mandelbrot")) {
            current = new Mandlebrot();
        } else if (fractalType.equals("Julia")) {
            if (nextCoords.size() != 0) {
                current = new Julia(nextCoords.get(nextCoords.size() - 1));
            } else if (nextCoords.size() == 0 && !(current instanceof Julia)) {
                current = new Julia(ComplexNumber.zero);
            }
        }
        nextCoords = new ArrayList<ComplexNumber>();
        renderCurrent(canvas, imageRes);
        System.out.println("image updated");
    }

    private static void renderCurrent(JLabel canvas, int imageRes) {
        ColorImage newImg = current.generate(10, 500, fov, panx, pany);
        setIcon(canvas, newImg, imageRes);
    }

    private static JPanel fovControls(JLabel canvas, int imageRes) {
        JPanel fovControls = new JPanel();

        JButton decreaseFov = new JButton("-");
        decreaseFov.addActionListener(e -> {
            fov *= fovStep;

            System.out.println(fov);
            renderCurrent(canvas, imageRes);
        });
        JButton resetFov = new JButton("reset");
        resetFov.addActionListener(e -> {
            if (fov == defaultFov) {
                return;
            }
            fov = defaultFov;
            System.out.println(fov);
            renderCurrent(canvas, imageRes);
        });
        JButton increaseFov = new JButton("+");
        increaseFov.addActionListener(e -> {

            fov /= fovStep;
            System.out.println(fov);
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
            panx -= panStep * fov;
            renderCurrent(canvas, imageRes);
        });

        JButton panPosX = new JButton(">");
        panPosX.addActionListener(e -> {
            panx += panStep * fov;
            renderCurrent(canvas, imageRes);
        });

        JPanel xPan = new JPanel();
        xPan.add(panNegX);
        xPan.add(resetPan);
        xPan.add(panPosX);

        JButton panNegY = new JButton("v");
        panNegY.addActionListener(e -> {
            pany -= panStep * fov;
            renderCurrent(canvas, imageRes);
        });
        JPanel panNegYContainer = new JPanel();
        panNegYContainer.add(panNegY);

        JButton panPosY = new JButton("^");
        panPosY.addActionListener(e -> {
            pany += panStep * fov;
            renderCurrent(canvas, imageRes);
        });
        JPanel panPosYContainer = new JPanel();
        panPosYContainer.add(panPosY);

        panControls.add(panPosYContainer);
        panControls.add(xPan);
        panControls.add(panNegYContainer);

        return panControls;
    }

    public static ArrayList<ComplexNumber> defaultCoords() {
        ArrayList<ComplexNumber> n = new ArrayList<ComplexNumber>();
        n.add(new ComplexNumber(-1, 1));
        n.add(new ComplexNumber(1, -1));
        n.add(new ComplexNumber(-1, -1));
        n.add(new ComplexNumber(1, 1));
        return n;
    }
}
