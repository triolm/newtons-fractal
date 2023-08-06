public class RootPolynomial extends Polynomial {
    ComplexNumber[] roots;

    public RootPolynomial(ComplexNumber[] roots) {
        super(rootsToStandardForm(roots));
        this.roots = roots;
    }

    public static ComplexNumber[] rootsToStandardForm(ComplexNumber[] roots) {
        Polynomial total = Polynomial.one;
        for (ComplexNumber i : roots) {
            ComplexNumber[] arr = { i.scale(-1), new ComplexNumber(1) };
            Polynomial p = new Polynomial(arr);
            total = total.multiply(p);
        }
        return total.getCoeffs();
    }

    public ComplexNumber newtonsFractalPixel(ComplexNumber n, int depth) {
        return closestRoot(newtonsMethod(n, depth));
    }

    public ComplexNumber closestRoot(ComplexNumber n) {
        ComplexNumber root = roots[0];
        double minDist = Double.POSITIVE_INFINITY;

        for (ComplexNumber i : roots) {
            double dist = n.getDist(i);
            if (dist > minDist)
                continue;
            minDist = dist;
            root = i;
        }
        return root;
    }
}
