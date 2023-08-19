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
           z = z.multiply(z).add(n);
        }
        return z;
    }
}
