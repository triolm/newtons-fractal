public abstract class ImageGenerator {
    private String filename;
    private ColorImage img;

    public static void progress(double progress) {
        progress *= 20;
        System.out.print("\r");
        System.out.print("\u2588".repeat((int) progress));
        System.out.print("\u2591".repeat(20 - (int) progress));
    }

    public ColorImage generate(int depth, int width, double fov, double panx,
            double pany) {
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
                img.put(getPixelColor(panx + (fov * i / width - fov / 2),
                        pany + (fov * j / width - fov / 2), depth), i, j);
            }

        }
        progress(1);
        return img;
    }

    public abstract Color getPixelColor(double x, double y, int depth);

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
