public class RootPolynomial extends Polynomial implements RootsKnown {
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

    public ComplexNumber[] getRoots(){
        return roots;
    }

    public int closestRootIndex(ComplexNumber n) {
        ComplexNumber root = roots[0];
        double minDist = Double.POSITIVE_INFINITY;
        int minIndex = 0;

        for (int i =0; i < roots.length; i ++ ) {
            double dist = n.getDist(roots[i]);
            if (dist > minDist)
                continue;
            minDist = dist;
            root = roots[i];
            minIndex = i;
        }
        return minIndex;
    }

    public ComplexNumber closestRoot(ComplexNumber n){
        return roots[closestRootIndex(n)];
    }
}
