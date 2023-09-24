public class TrigFunction extends Function implements RootsKnown {
    boolean isSine;
    double amplitude;
    double period;

    public TrigFunction(boolean isSine, double amplitude, double period) {
        this.isSine = isSine;
        this.amplitude = amplitude;
        this.period = period;
    }

    public ComplexNumber evaluate(ComplexNumber c) {
        double real = c.getReal();
        double i = c.getI();

        double periodover2pi = period / (2 * Math.PI);

        double outReal = amplitude * Math.sin(real * periodover2pi) * Math.cosh(i * periodover2pi);
        double outI = amplitude * Math.cos(real * periodover2pi) * Math.sinh(i * periodover2pi);
        if (isSine)
            return new ComplexNumber(outReal, outI);
        return new ComplexNumber(outI, outReal);

    }

    public ComplexNumber closestRoot(ComplexNumber c) {
        return new ComplexNumber(closestRootIndex(c) * period);
    }

    public int closestRootIndex(ComplexNumber c) {
        return Math.round((float) ((evaluate(c).getReal()) / period));
    }

    public TrigFunction derivative() {
        return new TrigFunction(!isSine, amplitude * period / (2 * Math.PI), period);
    }

    public String toString() {
        return amplitude + (isSine ? "sin" : "cos") + "(" + period / (2 * Math.PI) + ")";
    }

}
