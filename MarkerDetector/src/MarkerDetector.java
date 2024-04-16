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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
