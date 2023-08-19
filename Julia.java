public class Julia extends ImageGenerator {
    private double thresh;
    private ComplexNumber c;

    public Julia(ComplexNumber c,double thresh) {
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

        double val = getPixel(n, depth).abs();

        if(val > thresh || Double.isNaN(val)){
            return new Color(0,0,1);
        }
        else{
            if(val < 0){
                System.out.println(val);
            }
            return new Color(0,0,0);
        }
    }

    public ComplexNumber getPixel(ComplexNumber n, int depth) {
        ComplexNumber z = n;
        for (int i = 0; i < depth; i++) {
           z = z.multiply(z).add(c);
        }
        return z;
    }
}
