public class Polynomial extends Function {
    private ComplexNumber[] coeffs;
    private Polynomial deriv;
    public static Polynomial one = new Polynomial();

    public Polynomial(ComplexNumber[] coeffs) {
        this.coeffs = coeffs;
    }
    private Polynomial() {
        this.coeffs = new ComplexNumber[1];
        this.coeffs[0] = new ComplexNumber(1);
    }

    public ComplexNumber evaluate(ComplexNumber n) {
        ComplexNumber total = ComplexNumber.zero;
        for (int i = 0; i < coeffs.length; i++) {
            total = total.add(evaluateTerm(n, coeffs[i], i));
        }
        return total;
    }

    public ComplexNumber evaluateTerm(ComplexNumber n, ComplexNumber coeff, int power) {
        return coeff.multiply(n.power(power));
    }

    public ComplexNumber[] getCoeffs() {
        return coeffs;
    }

    public int getPower() {
        return coeffs.length - 1;
    }

    public ComplexNumber getCoeffAtPower(int n) {
        return coeffs.length > n ? coeffs[n] : ComplexNumber.zero;
    }

    public Polynomial increasePower(int pow) {
        ComplexNumber[] full = new ComplexNumber[pow + coeffs.length];
        for (int i = 0; i < pow; i++) {
            full[i] = ComplexNumber.zero;
        }
        for (int i = 0; i < coeffs.length; i++) {
            full[i + pow] = coeffs[i];
        }
        return new Polynomial(full);
    }

    public Polynomial decreasePower(int pow) {
        ComplexNumber[] full = new ComplexNumber[coeffs.length - pow];

        for (int i = 0; i < full.length; i++) {
            full[i] = coeffs[i + pow];
        }
        return new Polynomial(full);
    }

    public Polynomial scaleBy(ComplexNumber scalar) {
        ComplexNumber[] newCoeffs = new ComplexNumber[coeffs.length];
        for (int i = 0; i < coeffs.length; i++) {
            newCoeffs[i] = coeffs[i].multiply(scalar);
        }
        return new Polynomial(newCoeffs);
    }

    public Polynomial multiplyByTerm(ComplexNumber coeff, int power) {
        return this.scaleBy(coeff).increasePower(power);
    }

    public Polynomial add(Polynomial other) {
        ComplexNumber[] coeffsSum = new ComplexNumber[Math.max(coeffs.length, other.getCoeffs().length)];
        for (int i = 0; i < coeffsSum.length; i++) {
            coeffsSum[i] = getCoeffAtPower(i).add(other.getCoeffAtPower(i));
        }
        return new Polynomial(coeffsSum);
    }

    public Polynomial multiply(Polynomial other) {
        ComplexNumber[] arr = { ComplexNumber.zero };
        Polynomial total = new Polynomial(arr);
        for (int i = 0; i < other.getCoeffs().length; i++) {
            total = total.add(this.multiplyByTerm(other.getCoeffs()[i], i));
        }
        return total;
    }

    public Polynomial derivative() {
        if(deriv != null){
            return deriv;
        }
        ComplexNumber[] coeffs = this.decreasePower(1).getCoeffs();
        for (int i = 0; i < coeffs.length; i++) {
            coeffs[i] = coeffs[i].scale(i + 1);
        }
        deriv = new Polynomial(coeffs);
        return deriv;
    }

   

    public String toString() {
        String str = "";
        for (int i = coeffs.length - 1; i >= 0; i--) {
            if (coeffs[i].equals(ComplexNumber.zero))
                continue;
            if (str.length() != 0)
                str += " + ";
            str += coeffs[i].toString();
            if (i == 0)
                continue;
            str += "x";
            if (i == 1)
                continue;
            str += "^" + i;
        }
        return str;
    }

}
