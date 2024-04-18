import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MarkerDetector {

    private static BufferedImage readImage(String filePath) throws IOException {
        File imageFile = new File(filePath);
        return ImageIO.read(imageFile);
    }

    private static BufferedImage resizeImage(BufferedImage image, int targetWidth, int targetHeight) {
        Image tmp = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resizedImage;
    }

    public static BufferedImage processImage(BufferedImage image) {

        BufferedImage grayImage = convertToGrayscale(image);

        return grayImage;
    }

    private static BufferedImage convertToGrayscale(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int grayValue = (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);
                int grayPixel = (grayValue << 16) | (grayValue << 8) | grayValue;
                grayImage.setRGB(x, y, grayPixel);
            }
        }

        return grayImage;
    }

    public static void main(String[] args) {

        try {
            BufferedImage image = readImage("input.jpg");

            int width = image.getWidth();
            int height = image.getHeight();
            System.out.println("Image width: " + width + " pixels");
            System.out.println("Image height: " + height + " pixels");

            int targetWidth = 300;
            int targetHeight = 200;

            BufferedImage resizedImage = resizeImage(image, targetWidth, targetHeight);
            BufferedImage processedImage = processImage(resizedImage);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
