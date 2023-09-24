public class Test {
    public static void main(String[] args){
        TrigFunction f = new TrigFunction(true, 1, 2*Math.PI);

        System.out.println(f.evaluate(new ComplexNumber(Math.PI)));

        NewtonsFractals nf = new NewtonsFractals(f);
        nf.generate(30, 500,100);
        nf.save();

    }
}
