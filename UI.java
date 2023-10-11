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
    static String fractalType = "Julia";

    static int defPreviewDepth = 30;
    static int defPreviewDepthNewton = 10;

    static int defRenderDepth = 300;
    static int defRenderDepthNewton = 20;

    static int previewDepth;
    static int previewWidth = 500;

    static int renderDepth;
    static int renderWidth = 6000;

    public static void main(String[] args) throws Exception {
        int imageRes = 400;

        current = new NewtonsFractals(ComplexNumber.zeroArr);

        nextCoords = defaultCoords();

        JLabel canvas = new JLabel();

        resetCoordinateData(canvas, imageRes);

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ComplexNumber c = ImageGenerator.transformPoint(
                        new ComplexNumber((double) e.getX() / imageRes, (double) e.getY() / imageRes), panx, pany, fov);
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
                ImageGenerator toRender = current;
                toRender.generate(renderDepth, renderWidth, fov, panx, pany);
                toRender.save();
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
        fractalTypeSelect.setSelectedItem(fractalType);
        fractalTypeSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fractalType = (String) fractalTypeSelect.getSelectedItem();
                resetCoordinateData(canvas, imageRes);
            }
        });

        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show a dialog here
                showSettingsDialog(settingsButton);
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
        col2.add(settingsButton);

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
            renderDepth = defRenderDepthNewton;
            previewDepth = defPreviewDepthNewton;
            if (nextCoords.size() != 0) {
                current = new NewtonsFractals(nextCoords.toArray(new ComplexNumber[0]));
            } else if (nextCoords.size() == 0 && !(current instanceof NewtonsFractals)) {
                current = new NewtonsFractals(defaultCoords().toArray(new ComplexNumber[0]));
            }
        } else if (fractalType.equals("Mandelbrot")) {
            renderDepth = defRenderDepth;
            previewDepth = defPreviewDepth;
            current = new Mandlebrot();
        } else if (fractalType.equals("Julia")) {
            renderDepth = defRenderDepth;
            previewDepth = defPreviewDepth;
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
        ColorImage newImg = current.generate(previewDepth, previewWidth, fov, panx, pany);
        setIcon(canvas, newImg, imageRes);
    }

    private static JPanel fovControls(JLabel canvas, int imageRes) {
        JPanel fovControls = new JPanel();

        JButton decreaseFov = new JButton("-");
        decreaseFov.addActionListener(e -> {
            fov *= fovStep;

            renderCurrent(canvas, imageRes);
        });
        JButton resetFov = new JButton("reset");
        resetFov.addActionListener(e -> {
            if (fov == defaultFov) {
                return;
            }
            fov = defaultFov;
            renderCurrent(canvas, imageRes);
        });
        JButton increaseFov = new JButton("+");
        increaseFov.addActionListener(e -> {

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

    private static void showSettingsDialog(Component parent) {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(6, 1));
    
        // Create spinner components for existing variables...
        SpinnerModel renderWidthModel = new SpinnerNumberModel(renderWidth, 100, 10000, 100);
        JSpinner renderWidthSpinner = new JSpinner(renderWidthModel);
        JLabel renderWidthLabel = new JLabel("Rendered Image Width");
        JPanel renderWidthPanel = new JPanel();
        renderWidthPanel.add(renderWidthLabel);
        renderWidthPanel.add(renderWidthSpinner);
        settingsPanel.add(renderWidthPanel);
    
        SpinnerModel renderDepthModel = new SpinnerNumberModel(renderDepth, 1, 1000, 10);
        JSpinner renderDepthSpinner = new JSpinner(renderDepthModel);
        JLabel renderDepthLabel = new JLabel("Rendered Image Depth");
        JPanel renderDepthPanel = new JPanel();
        renderDepthPanel.add(renderDepthLabel);
        renderDepthPanel.add(renderDepthSpinner);
        settingsPanel.add(renderDepthPanel);
    
        SpinnerModel previewWidthModel = new SpinnerNumberModel(previewWidth, 100, 10000, 100);
        JSpinner previewWidthSpinner = new JSpinner(previewWidthModel);
        JLabel previewWidthLabel = new JLabel("Preview Image Width");
        JPanel previewWidthPanel = new JPanel();
        previewWidthPanel.add(previewWidthLabel);
        previewWidthPanel.add(previewWidthSpinner);
        settingsPanel.add(previewWidthPanel);
    
        SpinnerModel previewDepthModel = new SpinnerNumberModel(previewDepth, 1, 1000, 10);
        JSpinner previewDepthSpinner = new JSpinner(previewDepthModel);
        JLabel previewDepthLabel = new JLabel("Preview Image Depth");
        JPanel previewDepthPanel = new JPanel();
        previewDepthPanel.add(previewDepthLabel);
        previewDepthPanel.add(previewDepthSpinner);
        settingsPanel.add(previewDepthPanel);
    
        // Create spinner components for newtonRenderDepth and newtonPreviewDepth
        SpinnerModel newtonRenderDepthModel = new SpinnerNumberModel(defRenderDepthNewton, 1, 1000, 10);
        JSpinner newtonRenderDepthSpinner = new JSpinner(newtonRenderDepthModel);
        JLabel newtonRenderDepthLabel = new JLabel("Newton Render Depth");
        JPanel newtonRenderDepthPanel = new JPanel();
        newtonRenderDepthPanel.add(newtonRenderDepthLabel);
        newtonRenderDepthPanel.add(newtonRenderDepthSpinner);
        settingsPanel.add(newtonRenderDepthPanel);
    
        SpinnerModel newtonPreviewDepthModel = new SpinnerNumberModel(defPreviewDepthNewton, 1, 1000, 10);
        JSpinner newtonPreviewDepthSpinner = new JSpinner(newtonPreviewDepthModel);
        JLabel newtonPreviewDepthLabel = new JLabel("Newton Preview Depth");
        JPanel newtonPreviewDepthPanel = new JPanel();
        newtonPreviewDepthPanel.add(newtonPreviewDepthLabel);
        newtonPreviewDepthPanel.add(newtonPreviewDepthSpinner);
        settingsPanel.add(newtonPreviewDepthPanel);
    
        int option = JOptionPane.showConfirmDialog(
                parent,
                settingsPanel,
                "Settings",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
    
        if (option == JOptionPane.OK_OPTION) {
            // Retrieve settings values from spinners...
            renderWidth = (int) renderWidthSpinner.getValue();
            renderDepth = (int) renderDepthSpinner.getValue();
            previewWidth = (int) previewWidthSpinner.getValue();
            previewDepth = (int) previewDepthSpinner.getValue();
            defRenderDepthNewton = (int) newtonRenderDepthSpinner.getValue();
            defPreviewDepthNewton = (int) newtonPreviewDepthSpinner.getValue();
    
            // Perform any actions based on settings values...
        }
    }
    
}
