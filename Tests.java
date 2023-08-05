public class Tests {

    public static void gradientImg() {
        int width = 500;
        ColorImage img = new ColorImage(width, width);
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                img.put(new Color((double) j / width, .5, (double) i / width), i, j);
            }
        }
        ColorImage.save("./image.png", img);
    }

    public static void Voronoi() {
        int width = 500;

        ComplexNumber[] arr = { new ComplexNumber(Math.random() * width, Math.random() * width),
                new ComplexNumber(Math.random() * width, Math.random() * width),
                new ComplexNumber(Math.random() * width, Math.random() * width) };
        RootPolynomial func = new RootPolynomial(arr);

        ColorImage img = new ColorImage(width, width);
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                ComplexNumber closest = func.closestRoot(new ComplexNumber(i, j));
                img.put(new Color(closest.getReal() / width, closest.getI() / width, .5), i, j);
            }
        }
        ColorImage.save("./image.png", img);
    }

    public static void polynomialAddition() {
        ComplexNumber[] coeffs1 = { new ComplexNumber(1), new ComplexNumber(2), new ComplexNumber(3),
                new ComplexNumber(4), new ComplexNumber(5) };
        Polynomial p1 = new Polynomial(coeffs1);
        System.out.println(p1);
        System.out.println("correct? " + p1.toString().equals("5.0x^4 + 4.0x^3 + 3.0x^2 + 2.0x + 1.0"));

        Polynomial p2 = p1.add(p1);
        System.out.println(p2);
        System.out.println("correct? " + p2.toString().equals("10.0x^4 + 8.0x^3 + 6.0x^2 + 4.0x + 2.0"));

        ComplexNumber[] coeffs2 = { new ComplexNumber(1, 4), new ComplexNumber(2, 0), new ComplexNumber(3, 6) };
        Polynomial p3 = new Polynomial(coeffs2);
        System.out.println(p3);
        System.out.println("correct? " + p3.toString().equals("(3.0+6.0i)x^2 + 2.0x + (1.0+4.0i)"));

        ComplexNumber[] coeffs3 = { new ComplexNumber(0, 0), new ComplexNumber(-5, 2.5), new ComplexNumber(7, -1),
                ComplexNumber.zero };
        Polynomial p4 = new Polynomial(coeffs3);
        System.out.println(p4);
        System.out.println("correct? " + p4.toString().equals("(7.0+-1.0i)x^2 + (-5.0+2.5i)x"));

        Polynomial p5 = p3.add(p4);
        System.out.println(p5);
        System.out.println("correct? " + p5.toString().equals("(10.0+5.0i)x^2 + (-3.0+2.5i)x + (1.0+4.0i)"));

    }

    public static void polynomialMultiplication() {
        ComplexNumber[] coeffs1 = { new ComplexNumber(2), new ComplexNumber(1) };
        ComplexNumber[] coeffs2 = { new ComplexNumber(-2), new ComplexNumber(1) };
        Polynomial p1 = new Polynomial(coeffs1);
        Polynomial p2 = new Polynomial(coeffs2);
        Polynomial p3 = p1.multiply(p2);
        System.out.println(p3);
        System.out.println("correct? " + p3.toString().equals("1.0x^2 + -4.0"));

    }

    public static void polynomialDerivitive() {
        ComplexNumber[] coeffs1 = { new ComplexNumber(1), new ComplexNumber(2), new ComplexNumber(3) };
        Polynomial p1 = new Polynomial(coeffs1).derivative();
        System.out.println(p1);
        System.out.println("correct? " + p1.toString().equals("6.0x + 2.0x"));

        ComplexNumber[] coeffs2 = { new ComplexNumber(1, 4), new ComplexNumber(2, 0), new ComplexNumber(3, 6),
                new ComplexNumber(-4, 2.5) };
        Polynomial p2 = new Polynomial(coeffs2).derivative();
        System.out.println(p2);
        System.out.println("correct? " + p2.toString().equals("(-12.0+7.5i)x^2 + (6.0+12.0i)x + 2.0"));
    }
}
