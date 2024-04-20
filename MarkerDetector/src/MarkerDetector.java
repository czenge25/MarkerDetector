import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MarkerDetector {

    private static BufferedImage readImage(String filePath) throws IOException {
        File imageFile = new File(filePath);
        return ImageIO.read(imageFile);
    }

    private static BufferedImage resizeImage(BufferedImage image, int targetWidth, int targetHeight) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        double aspectRatio = (double) originalWidth / originalHeight;

        if (originalWidth > originalHeight) {
            targetHeight = (int) (targetWidth / aspectRatio);
        } else {
            targetWidth = (int) (targetHeight * aspectRatio);
        }

        Image tmp = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resizedImage;
    }

    public static BufferedImage processImage(BufferedImage image) {

        BufferedImage grayImage = convertToGrayscale(image);
        BufferedImage gaussianImage = applyGaussianBlur(grayImage);
        BufferedImage sobelImage[] = applySobelOperator(gaussianImage);

        return gaussianImage;
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

    private static BufferedImage applyGaussianBlur(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage blurredImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        double[][] kernel = {
                {1.0 / 16, 1.0 / 8, 1.0 / 16},
                {1.0 / 8, 1.0 / 4, 1.0 / 8},
                {1.0 / 16, 1.0 / 8, 1.0 / 16}
        };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                double sum = 0.0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixel = image.getRGB(x + i, y + j) & 0xFF;
                        sum += kernel[i + 1][j + 1] * pixel;
                    }
                }
                int blurredPixel = (int) Math.round(sum);
                blurredImage.setRGB(x, y, blurredPixel << 16 | blurredPixel << 8 | blurredPixel);
            }
        }

        return blurredImage;
    }

    private static BufferedImage[] applySobelOperator(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage gradientMagnitudeImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage gradientDirectionImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Sobel kernels
        int[][] sobelX = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] sobelY = {
                {-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}
        };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int gx = 0;
                int gy = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixel = image.getRGB(x + i, y + j) & 0xFF;
                        gx += sobelX[i + 1][j + 1] * pixel;
                        gy += sobelY[i + 1][j + 1] * pixel;
                    }
                }
                int gradientMagnitude = (int) Math.round(Math.sqrt(gx * gx + gy * gy));
                gradientMagnitudeImage.setRGB(x, y, gradientMagnitude << 16 | gradientMagnitude << 8 | gradientMagnitude);

                double gradientDirection = Math.atan2(gy, gx);
                int direction = (int) Math.round(Math.toDegrees(gradientDirection));
                gradientDirectionImage.setRGB(x, y, direction << 16 | direction << 8 | direction);
            }
        }

        return new BufferedImage[]{gradientMagnitudeImage, gradientDirectionImage};
    }

    public static void main(String[] args) {

        try {
            Scanner s = new Scanner(System.in);
            System.out.print("Give unprocessed file name: ");
            String fileName = s.nextLine();
            BufferedImage image = readImage(fileName);

            int width = image.getWidth();
            int height = image.getHeight();
            System.out.println("Image width: " + width + " pixels");
            System.out.println("Image height: " + height + " pixels");

            int targetWidth = 300;
            int targetHeight = 200;

            BufferedImage resizedImage = resizeImage(image, targetWidth, targetHeight);
            BufferedImage processedImage = processImage(resizedImage);

            File outputImageFile = new File("processedImage.jpg");

            ImageIO.write(processedImage, "jpg", outputImageFile);

            System.out.println("Processed image saved successfully!");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
