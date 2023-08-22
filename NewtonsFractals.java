public class NewtonsFractals extends ImageGenerator {
    private RootPolynomial p;
    private Color[] colors;
      private int[] defaultColors = {0xEF476F, 0xFFD166, 0x06D6A0, 0x118AB2, 0x073B4C, 0xB4A0E5};

    public NewtonsFractals(ComplexNumber[] roots) {
        p = new RootPolynomial(roots);
        colors = generateColors(roots.length);
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

    public Color[] generateColors(int nColors){
        Color[] c = new Color[nColors];

        for (int i = 0; i < nColors; i ++){
            if(i < defaultColors.length){
                c[i] = Color.fromARGB(defaultColors[i]);
                continue;
            }
            c[i] = new Color(Math.random(), Math.random(), Math.random());
        }

        return c;
    }

    public Color getColor(double n, double highest) {
        return colors[(int)n];
    }

}
