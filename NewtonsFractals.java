public class NewtonsFractals extends ImageGenerator {
    private RootPolynomial p;

    public NewtonsFractals(ComplexNumber[] roots){
        p = new RootPolynomial(roots);
    }

    public ColorImage generate(int depth, int width, double fov, double panx,
    double pany){
        double timeUntilProgressUpdate = 0;
        progress(0);

        ColorImage img = new ColorImage(width, width);
        for (int i = 0; i < img.getWidth(); i++) {

            timeUntilProgressUpdate += 20.0 / img.getWidth();
            if (timeUntilProgressUpdate > 1) {
                timeUntilProgressUpdate = 0;
                progress((double) i / img.getWidth());
            }

            for (int j = 0; j < img.getHeight(); j++) {

                ComplexNumber n = new ComplexNumber(panx+(fov *i/width-fov/2), pany+(fov *j/width-fov/2));
                
                ComplexNumber closest = p.newtonsFractalPixel(n, depth);
                img.put(new Color(closest.getReal(),.5, closest.getI()), i, j);
            }
           
        }
        progress(1);
        return img;
    }
}
