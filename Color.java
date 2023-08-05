public class Color {
    public double r,g,b;

    public Color(double r, double g, double b){
        this.r = clamp255(r);
        this.g = clamp255(g);
        this.b = clamp255(b);
    }

    public static double clamp255(double n){
        if(n < 0) return 0;
        if (n> 1) return 1;
        return n;
    }

    public double getR(){
        return r;
    }
    public double getG(){
        return g;
    }
    public double getB(){
        return b;
    }

    public int toARGB() {
        int ir = (int) (Math.min(Math.max(r, 0), 1) * 255 + 0.1);
        int ig = (int) (Math.min(Math.max(g, 0), 1) * 255 + 0.1);
        int ib = (int) (Math.min(Math.max(b, 0), 1) * 255 + 0.1);
        return (ir << 16) | (ig << 8) | (ib << 0);
    }
}
