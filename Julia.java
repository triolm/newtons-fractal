public class Julia extends ImageGenerator {
    private double thresh;
    private ComplexNumber c;

    public Julia(ComplexNumber c, double thresh) {
        this.thresh = thresh;
        this.c = c;
        this.filename = "julia";
    }

    public Julia(ComplexNumber c) {
        this.thresh = 2;
        this.c = c;
        this.filename = "julia";

    }

    public Color getPixelColor(double x, double y, int depth) {

        ComplexNumber n = new ComplexNumber(x, y);
        double val = getPixel(n, depth);

        if (val > 0) {
            return new Color((double) val / depth, (double) val / depth, 1);
        }
        if (val > -2) {
            return new Color(0, 0, 0);
        }
        return new Color(1, 1, 1);

    }

    public double getPixel(ComplexNumber n, int depth) {
        ComplexNumber z = n;
        for (int i = 0; i < depth; i++) {
            z = z.multiply(z);
            z = z.add(c);
            if (z.abs() == Double.POSITIVE_INFINITY) {
                return i;
            }
        }
        return -z.abs();
    }
}
