public class Mandlebrot extends ImageGenerator {
    private double thresh;

    public Mandlebrot(double thresh) {
        this.thresh = thresh;
        this.filename = "mandlebrot";
    }

    public Mandlebrot() {
        this.thresh = 2;
        this.filename = "mandlebrot";

    }

    public double getPixelColorMapVal(double x, double y, int depth) {
        ComplexNumber n = new ComplexNumber(x, y);

        double val = getConvergenceIterations(n, depth);

        if (val > 0) {
            return val;
        } else if(val > -thresh) {
            return 0;
        }
        return Double.POSITIVE_INFINITY;

    }

    public double getConvergenceIterations(ComplexNumber n, int depth) {
        ComplexNumber z = n;
        for (int i = 0; i < depth; i++) {
            z = z.multiply(z).add(n);
            if(Double.isInfinite(z.abs())){
                return i;
            }
        }
        return -z.abs();
    }
}
