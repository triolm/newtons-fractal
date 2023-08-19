public class Main {
    public static void main(String[] args) {
        Mandlebrot m = new Mandlebrot();

        m.generate(50, 6000, 4);

        m.save();

    }
}