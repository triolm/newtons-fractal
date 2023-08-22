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
    
    public double getPixelColorMapVal(double x, double y, int depth) {

        ComplexNumber n = new ComplexNumber(x, y);
        double val = getConvergenceIterations(n, depth);

        if (val > 0) {
            return val;
        }
        if (val > -thresh) {
            return 0;
        }
        return Double.POSITIVE_INFINITY;

    }

    public double getConvergenceIterations(ComplexNumber n, int depth) {
        ComplexNumber z = n;
        for (int i = 0; i < depth; i++) {
            z = z.multiply(z);
            z = z.add(c);
            if (Double.isInfinite(z.abs())) {
                return i;
            }
        }
        return -z.abs();
    }
}
