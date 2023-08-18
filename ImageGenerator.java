public abstract class ImageGenerator {
    private String filename;
    private ColorImage img;

    public static void progress(double progress) {
        progress *= 20;
        System.out.print("\r");
        System.out.print("\u2588".repeat((int) progress));
        System.out.print("\u2591".repeat(20 - (int) progress));
    }

    public abstract ColorImage generate(int depth, int width, double fov, double panx,
            double pany);

    public ColorImage generate(int depth, int width, double fov) {
        return generate(depth, width, fov, 0, 0);
    }

    public ColorImage generate(int depth, int width) {
        return generate(depth, width, 2, 0, 0);
    }

    public String save() {
        String savedName = filename + "-" + System.currentTimeMillis() + ".png";
        ColorImage.save(savedName, img);
        return savedName;
    }
}
