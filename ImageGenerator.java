public abstract class ImageGenerator {
    protected String filename;
    protected ColorImage img;

    public static void progress(double progress) {
        progress *= 20;
        System.out.print("\r");
        System.out.print("\u2588".repeat((int) progress));
        System.out.print("\u2591".repeat(20 - (int) progress));
    }

    public ColorImage generate(int depth, int width, double fov, double panx,
            double pany) {
        double highest = 0;

        double[][] pixelRelVals = new double[width][width];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                pixelRelVals[i][j] = getPixelColorMapVal(panx + (fov * i / width - fov / 2),
                        pany + (fov * j / width - fov / 2), depth);

                if (Double.isFinite(pixelRelVals[i][j])) {
                    highest = Math.max(pixelRelVals[i][j], highest);
                }
            }
        }

        ColorImage img = new ColorImage(width, width);
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                img.put(getColor(pixelRelVals[i][j], highest), i, j);
            }

        }
        this.img = img;
        return img;
    }

    public static ComplexNumber transformPoint(ComplexNumber c, double panx, double pany, double fov, int width) {
        Double newReal = panx + (fov * c.getReal() / width - fov / 2);
        Double newI = panx + (fov * (1 - c.getI()) / width - fov / 2);
        return new ComplexNumber(newReal, newI);
    }

    public static ComplexNumber transformPoint(ComplexNumber c, double panx, double pany, double fov) {
        return transformPoint(c, panx, pany, fov, 1);
    }

    public abstract double getPixelColorMapVal(double x, double y, int depth);

    public ColorImage generate(int depth, int width, double fov) {
        return generate(depth, width, fov, 0, 0);
    }

    public ColorImage generate(int depth, int width) {
        return generate(depth, width, 2, 0, 0);
    }

    public Color getColor(double n, double highest) {
        if (n == 0) {
            return new Color(0, 0, 0);
        }
        if (Double.isInfinite(n)) {
            return new Color(1, 1, 1);
        }
        // System.out.println(n);
        return new Color(Math.sqrt(n) / Math.sqrt(highest),
                Math.sqrt(n) / Math.sqrt(highest), 1);
    }

    public String save() {
        System.out.println("Saving image");
        String savedName = "./output/" + filename + "-" + System.currentTimeMillis() + ".png";
        ColorImage.save(savedName, img);
        System.out.println("Saved image as " + savedName);

        return savedName;
    }
}
