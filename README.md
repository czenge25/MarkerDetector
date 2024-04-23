# MarkerDetector

Marker Detection with Image Processing
This Java program demonstrates object detection to detect EXPO markers. It reads an image file, processes it to detect markers, and outputs the result.

*Instructions*
Installation: Ensure you have Java installed on your system.
Compile: Compile the Java source code using the Java compiler.
Run: Execute the compiled program. Provide the filename of the image you want to process when prompted.
The program will prompt you to enter the filename of the unprocessed image.
It will then perform marker detection on the image.
The processed image will be saved as "processedImage.jpg" in the current directory.

*Features*
Image Processing: Converts the image to grayscale, applies Gaussian blur, and uses the Sobel operator for edge detection.
Thresholding: Applies thresholding to convert the grayscale image to a binary image for marker detection.
Resize: Resizes the image to a specified target width and height.
Visualization: Prints a visual representation of the thresholding process.

*Usage Notes*
Ensure the image file exists and is accessible to the program.
The program outputs visual representations of the image processing steps and saves the processed image as "processedImage.jpg".

*Dependencies*
The program does not rely on any external libraries and is implemented using only standard Java libraries.
