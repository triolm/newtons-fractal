public class NewtonsFractals extends ImageGenerator {
    private RootPolynomial p;

    public NewtonsFractals(ComplexNumber[] roots) {
        p = new RootPolynomial(roots);
        this.filename = "newton";
    }

    public double getPixelColorMapVal(double x, double y, int depth) {
        ComplexNumber n = new ComplexNumber(x, y);

        double closest = p.closestRootIndex(newtonsMethod(n, depth));
        return closest;
    }

    public ComplexNumber newtonsMethod(ComplexNumber n, int depth) {
        for (int i = 0; i < depth; i++) {
            n = n.subtract(p.evaluate(n).divide(p.derivative().evaluate(n)));
        }
        return n;
    }

}
