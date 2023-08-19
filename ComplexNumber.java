public class ComplexNumber {
    private double real, i;
    public static ComplexNumber zero = new ComplexNumber(0, 0);
    public static ComplexNumber[] zeroArr = { new ComplexNumber(0, 0) };

    public ComplexNumber(double real, double i) {
        this.real = real;
        this.i = i;
    }

    public ComplexNumber(double real) {
        this.real = real;
        this.i = 0;
    }

    public double getReal() {
        return real;
    }

    public double getI() {
        return i;
    }

    public ComplexNumber add(ComplexNumber other) {
        return new ComplexNumber(real + other.getReal(), i + other.getI());
    }

    public ComplexNumber subtract(ComplexNumber other) {
        return new ComplexNumber(real - other.getReal(), i - other.getI());
    }

    public ComplexNumber multiply(ComplexNumber other) {
        double f, o, i, l;
        f = real * other.getReal();
        o = real * other.getI();
        i = this.i * other.getReal();
        l = this.i * other.getI() * -1;

        return new ComplexNumber(f + l, o + i);
    }

    public ComplexNumber divide(ComplexNumber other) {
        ComplexNumber conj = new ComplexNumber(other.getReal(), -other.getI());
        ComplexNumber top = this.multiply(conj);
        double bottom = Math.pow(other.getReal(), 2) + Math.pow(other.getI(), 2);
        return top.scale(1 / bottom);
    }

    public ComplexNumber scale(double scalar) {
        return new ComplexNumber(real * scalar, i * scalar);
    }

    // TODO: janky
    public ComplexNumber power(int pow) {
        ComplexNumber product = new ComplexNumber(1, 0);
        for (int i = 0; i < pow; i++) {
            product = product.multiply(this);
        }
        return product;
    }

    public boolean equals(ComplexNumber other) {
        return (real == other.getReal()) && (i == other.getI());
    }

    public double getDist(ComplexNumber other) {
        return Math.sqrt(
                Math.pow(real - other.getReal(), 2) +
                        Math.pow(i - other.getI(), 2));
    }

    public double abs() {
        if(Double.isInfinite(i) || Double.isInfinite(real) || Double.isNaN(i) || Double.isNaN(real)){
            return Double.POSITIVE_INFINITY;
        }
        return getDist(zero);
    }

    public String toString() {
        if (i == 1) {
            return "(" + real + "+i)";
        }
        if (i != 0) {
            return "(" + real + "+" + i + "i)";
        }
        return real + "";
    }

}
