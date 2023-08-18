public class NewtonsFractals extends ImageGenerator {
    private RootPolynomial p;

    public NewtonsFractals(ComplexNumber[] roots) {
        p = new RootPolynomial(roots);
    }

    public Color getPixelColor(double x, double y, int depth) {
        ComplexNumber n = new ComplexNumber(x, y);

        ComplexNumber closest = getPixel(p, n, depth);
        return new Color(closest.getReal(), .5, closest.getI());
    }

    private ComplexNumber getPixel(RootPolynomial p, ComplexNumber n, int depth) {
        return p.closestRoot(newtonsMethod(p, n, depth));
    }

    public ComplexNumber newtonsMethod(Polynomial p, ComplexNumber n, int depth) {
        for (int i = 0; i < depth; i++) {
            n = n.subtract(p.evaluate(n).divide(p.derivative().evaluate(n)));
        }
        return n;
    }
}
