package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * ImageProcessorModel class for the ImageProcessor.
 * Contains enums for different variations for flip method (v, h), filter method (b, s),
 * color transformation method (s, g), and grayscale method (r,g,b).
 * Contains methods for commands.
 */
public class ImageProcessorModel implements IImageProcessorModel {

  /**
   * An enum for different variations of the flip method, Vertical and Horizontal.
   */
  public enum FlipMode {
    Vertical, Horizontal
  }

  /**
   * An enum for different variations of the grayscale method based on the color component.
   */
  public enum GrayscaleMode {
    Red, Green, Blue, Intensity, Value, Luma
  }

  /**
   * Enum that represents the two
   * color transformations which alter
   * RGB values of a specific pixel.
   */
  public enum ColorTransformationsMode {
    Grayscale, Sepia
  }

  /**
   * Enum that represents the two different filter options
   * to apply to an image which alters in correspondence
   * with the surrounding pixels.
   */
  public enum FilterMode {
    Blur, Sharpen
  }

  /**
   * Enum that represents four different components for the Histogram in the GUI.
   */
  public enum Component {
    Red, Green, Blue, Intensity
  }

  private Map<String, ImageInterface> storedImages;

  /**
   * A zero argument constructor that initializes the HashMap that will store all the user's images.
   */
  public ImageProcessorModel() {
    storedImages = new HashMap<>();
  }

  @Override
  public void brighten(int increment, String name, String modifiedImage) {
    ImageInterface image = findImage(name);
    ArrayList<ArrayList<IPixel>> img = image.getPixels();

    // creates new image copy to alter
    ArrayList<ArrayList<IPixel>> pixelsCopy = new ArrayList<>();

    for (ArrayList<IPixel> row : img) {
      ArrayList<IPixel> rowCopy = new ArrayList<>();
      for (IPixel pixel : row) {
        IColor color = pixel.getColor();
        rowCopy.add(new RGBPixelImpl(pixel.getPosition(),
                new ColorImpl(clampValues(color.getRed() + increment),
                        clampValues(color.getGreen() + increment),
                        clampValues(color.getBlue() + increment))));
      }
      pixelsCopy.add(rowCopy);
    }
    ImageInterface newImage = new ImageImpl(pixelsCopy);
    storedImages.put(modifiedImage, newImage);
  }

  @Override
  public void flip(FlipMode flip, String name, String modifiedImage) {
    ImageInterface image = findImage(name);
    ArrayList<ArrayList<IPixel>> img = image.getPixels();
    ArrayList<ArrayList<IPixel>> pixelsCopy = new ArrayList<>();

    for (ArrayList<IPixel> row : img) {
      ArrayList<IPixel> rowCopy = new ArrayList<>();
      for (IPixel pixel : row) {
        IColor color = pixel.getColor();
        rowCopy.add(new RGBPixelImpl(pixel.getPosition(),
                new ColorImpl(color.getRed(), color.getGreen(),
                        color.getBlue())));
      }
      pixelsCopy.add(rowCopy);
    }

    if (flip == FlipMode.Vertical) {
      Collections.reverse(pixelsCopy);
    }

    if (flip == FlipMode.Horizontal) {
      for (ArrayList<IPixel> img2 : pixelsCopy) {
        Collections.reverse(img2);
      }
    }

    ImageInterface newImage = new ImageImpl(pixelsCopy);
    storedImages.put(modifiedImage, newImage);
  }

  @Override
  public void grayscale(GrayscaleMode grayscale, String name, String modifiedImage) {
    ImageInterface image = findImage(name);
    ArrayList<ArrayList<IPixel>> img = image.getPixels();

    // creates new image copy to alter
    ArrayList<ArrayList<IPixel>> pixelsCopy = new ArrayList<>();
    for (ArrayList<IPixel> row : img) {
      ArrayList<IPixel> rowCopy = new ArrayList<>();
      for (IPixel pixel : row) {
        IColor color = pixel.getColor();

        // convert image to greyscale by making all rgb pixels the respective color
        switch (grayscale) {
          case Red:
            rowCopy.add(new RGBPixelImpl(pixel.getPosition(),
                    new ColorImpl(color.getRed(), color.getRed(),
                            color.getRed())));
            break;
          case Green:
            rowCopy.add(new RGBPixelImpl(pixel.getPosition(),
                    new ColorImpl(color.getGreen(), color.getGreen(),
                            color.getGreen())));
            break;
          case Blue:
            rowCopy.add(new RGBPixelImpl(pixel.getPosition(),
                    new ColorImpl(color.getBlue(), color.getBlue(),
                            color.getBlue())));
            break;
          // avg of 3 components for each pixel
          case Intensity:
            int value = clampValues((color.getRed() + color.getGreen() + color.getBlue()) / 3);
            rowCopy.add(new RGBPixelImpl(pixel.getPosition(),
                    new ColorImpl(value, value, value)));
            // max value of 3 components for each pixel
            break;
          case Value:
            int maxVal = Math.max(Math.max(color.getRed(), color.getBlue()), color.getGreen());
            rowCopy.add(new RGBPixelImpl(pixel.getPosition(),
                    new ColorImpl(maxVal, maxVal, maxVal)));
            break;
          // the weighted sum calculation
          case Luma:
            rowCopy.add(new RGBPixelImpl(pixel.getPosition(),
                    new ColorImpl((int) (color.getRed() * 0.2126),
                            (int) (color.getGreen() * 0.7152),
                            (int) (color.getBlue() * 0.0722))));
            break;
          default:
        }
      }
      pixelsCopy.add(rowCopy);
    }
    ImageInterface newImage = new ImageImpl(pixelsCopy);
    storedImages.put(modifiedImage, newImage);
  }

  @Override
  public void load(String pathName, String newFileName) throws IllegalArgumentException {
    String ppmText = "";

    try {
      ppmText = Files.readString(Paths.get(pathName));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Scanner scanner = new Scanner(ppmText);

    if (!scanner.nextLine().equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }

    String next = scanner.nextLine();

    if (next.startsWith("#")) {
      next = scanner.nextLine();
    }

    String[] eachNum = next.split(" ");
    int width = Integer.parseInt(eachNum[0]);
    int height = Integer.parseInt(eachNum[1]);
    int maxVal = scanner.nextInt();

    ArrayList<ArrayList<IPixel>> pixels = new ArrayList<>();
    for (int i = 0; i < height; i++) {
      ArrayList<IPixel> rowCopy = new ArrayList<>();
      for (int j = 0; j < width; j++) {
        rowCopy.add(new RGBPixelImpl(new Position2D(i, j),
                new ColorImpl(scanner.nextInt(), scanner.nextInt(), scanner.nextInt())));
      }
      pixels.add(rowCopy);
    }
    ImageInterface newImage = new ImageImpl(pixels);
    storedImages.put(newFileName, newImage);
  }

  @Override
  public void load(BufferedImage image, String newFileName) throws IllegalArgumentException {
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null.");
    }

    ArrayList<ArrayList<IPixel>> pixelCopy = new ArrayList<>();
    for (int i = image.getMinY(); i < image.getHeight(); i++) {
      ArrayList<IPixel> rowCopy = new ArrayList<>();
      for (int j = image.getMinX(); j < image.getWidth(); j++) {
        rowCopy.add(new RGBPixelImpl(new Position2D(i, j),
                new ColorImpl(new Color(image.getRGB(j, i)).getRed(),
                        new Color(image.getRGB(j, i)).getGreen(),
                        new Color(image.getRGB(j, i)).getBlue())));
      }
      pixelCopy.add(rowCopy);
    }
    ImageInterface newImage = new ImageImpl(pixelCopy);
    storedImages.put(newFileName, newImage);
  }

  @Override
  public File save(String pathName, String name) throws IllegalArgumentException {
    ImageInterface image = findImage(name);
    ArrayList<ArrayList<IPixel>> img = image.getPixels();

    StringBuilder str = new StringBuilder();
    str.append("P3\n")
            .append("# Created by GIMP version 2.10.30 PNM plug-in\n")
            .append(img.get(0).size() + " ")
            .append(img.size() + "\n")
            .append(255 + "\n");
    for (ArrayList<IPixel> iPixels : img) {
      for (int j = 0; j < img.get(0).size(); j++) {
        str.append(iPixels.get(j).getColor().getRed()).append("\n");
        str.append(iPixels.get(j).getColor().getGreen()).append("\n");
        str.append(iPixels.get(j).getColor().getBlue()).append("\n");
      }
    }
    File file = new File(pathName);
    FileOutputStream stream = null;
    try {
      stream = new FileOutputStream(file);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException();
    }
    try {
      stream.write(str.toString().getBytes());
    } catch (IOException e) {
      throw new IllegalArgumentException();
    }
    return file;
  }

  @Override
  public BufferedImage saveImage(String pathName, String name) {
    ImageInterface image = findImage(name);
    ArrayList<ArrayList<IPixel>> img = image.getPixels();

    BufferedImage save = new BufferedImage(img.get(0).size(),
            img.size(), BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < img.size(); i++) {
      for (int j = 0; j < img.get(i).size(); j++) {
        save.setRGB(j, i, new Color(img.get(i).get(j).getColor().getRed(),
                img.get(i).get(j).getColor().getGreen(),
                img.get(i).get(j).getColor().getBlue()).getRGB());
      }
    }
    return save;
  }

  /**
   * A helper method for the colorTransformation method that applies a matrix to the given image's
   * individual RGB pixels.
   *
   * @param matrix a matrix representing values to apply to each pixel of an image.
   * @param image  the image the user wants to modify
   * @return a 2D arraylist of pixels that represent a copy of the modified image.
   */
  private ArrayList<ArrayList<IPixel>> colorTransHelper(double[][] matrix, ImageInterface image) {
    ArrayList<ArrayList<IPixel>> img = image.getPixels();

    ArrayList<ArrayList<IPixel>> pixelsCopy = new ArrayList<>();
    for (ArrayList<IPixel> row : img) {
      ArrayList<IPixel> rowCopy = new ArrayList<>();
      for (IPixel pixel : row) {

        int oldRed = pixel.getColor().getRed();
        int oldGreen = pixel.getColor().getGreen();
        int oldBlue = pixel.getColor().getBlue();

        int newRed = (int) ((oldRed * matrix[0][0])
                + (oldGreen * matrix[0][1])
                + (oldBlue * matrix[0][2]));
        int newGreen = (int) ((oldRed * matrix[1][0])
                + (oldGreen * matrix[1][1])
                + (oldBlue * matrix[1][2]));
        int newBlue = (int) ((oldRed * matrix[2][0])
                + (oldGreen * matrix[2][1])
                + (oldBlue * matrix[2][2]));

        rowCopy.add(new RGBPixelImpl(pixel.getPosition(),
                new ColorImpl((Math.max(0, Math.min(newRed, 255))),
                        (Math.max(0, Math.min(newGreen, 255))),
                        (Math.max(0, Math.min(newBlue, 255))))));

      }
      pixelsCopy.add(rowCopy);
    }
    return pixelsCopy;
  }

  @Override
  public void colorTransformations(ColorTransformationsMode colorTransform,
                                   String name, String modifiedImage) {
    ImageInterface image = findImage(name);
    ArrayList<ArrayList<IPixel>> pixelsCopy = new ArrayList<>();

    if (colorTransform == ColorTransformationsMode.Grayscale) {
      double[][] matrix = {{0.2126, 0.7152, 0.0722},
                           {0.2126, 0.7152, 0.0722},
                           {0.2126, 0.7152, 0.0722}};

      pixelsCopy = colorTransHelper(matrix, image);
    }

    if (colorTransform == ColorTransformationsMode.Sepia) {
      double[][] matrix = {{0.393, 0.769, 0.189},
                           {0.349, 0.686, 0.168},
                           {0.272, 0.534, 0.131}};
      pixelsCopy = colorTransHelper(matrix, image);
    }

    ImageInterface newImage = new ImageImpl(pixelsCopy);
    storedImages.put(modifiedImage, newImage);
  }

  /**
   * Used to set new RGB values to filter an image using the given matrix and found image.
   *
   * @param matrix a matrix representing values to apply to each pixel of an image.
   * @param img    the image the user wants to modify
   * @return a 2D arraylist of pixels that represent a copy of the modified image.
   */
  private ArrayList<ArrayList<IPixel>> filterHelper(double[][] matrix, ImageInterface img) {
    ArrayList<ArrayList<IPixel>> pixels = img.getPixels();

    int newRed = 0;
    int newGreen = 0;
    int newBlue = 0;

    ArrayList<ArrayList<IPixel>> pixelsCopy = new ArrayList<>();
    for (ArrayList<IPixel> row : pixels) {
      ArrayList<IPixel> rowCopy = new ArrayList<>(); //create new rows of RGB pixels
      for (IPixel pixel : row) {

        for (int x = 0; x < matrix.length; x++) { //applies the matrix values to each pixel
          int xPosn = (pixel.getPosition().getX() + x) - ((matrix.length - 1) / 2);
          if (xPosn >= 0 && xPosn < pixels.size()) {
            for (int y = 0; y < matrix[0].length; y++) {
              int yPosn = (pixel.getPosition().getY() + y) - ((matrix[0].length - 1) / 2);
              if (yPosn >= 0 && yPosn < pixels.size()) {

                newRed += pixel.getColor().getRed() * matrix[x][y];
                newGreen += pixel.getColor().getGreen() * matrix[x][y];
                newBlue += pixel.getColor().getBlue() * matrix[x][y];

              }
            }
          }
        }
        rowCopy.add(new RGBPixelImpl(pixel.getPosition(),
                new ColorImpl((Math.max(0, Math.min(newRed, 255))),
                        (Math.max(0, Math.min(newGreen, 255))),
                        (Math.max(0, Math.min(newBlue, 255))))));
      }
      pixelsCopy.add(rowCopy);
    }
    return pixelsCopy; //returns a duplicate set of pixels of the modified image
  }

  @Override
  public void filter(FilterMode filter, String name, String modifiedImage) {
    ImageInterface image = findImage(name);
    double[][] matrix;

    ArrayList<ArrayList<IPixel>> pixelsCopy = new ArrayList<>();

    if (filter == FilterMode.Blur) {
      matrix = new double[][]{
              {0.0625, 0.125, 0.0625},
              {0.125, 0.25, 0.125},
              {0.0625, 0.125, 0.0625}};

      pixelsCopy = filterHelper(matrix, image);
    }

    if (filter == FilterMode.Sharpen) {
      matrix = new double[][]{
              {-0.125, -0.125, -0.125, -0.125, -0.125},
              {-0.125, 0.25, 0.25, 0.25, -0.125},
              {-0.125, 0.25, 1, 0.25, -0.125},
              {-0.125, 0.25, 0.25, 0.25, -0.125},
              {-0.125, -0.125, -0.125, -0.125, -0.125}};

      pixelsCopy = filterHelper(matrix, image);
    }
    ImageInterface newImage = new ImageImpl(pixelsCopy);
    storedImages.put(modifiedImage, newImage);
  }

  /**
   * Initializes the RGB and intensity values of the image. Takes in an enum (R, G, B, or
   * Intensity) and extracts the RGB values from the BufferedImage that the user has loaded.
   * Used to make a histogram to be displayed in the GUI version of our image processor.
   *
   * @param comp an enum representing an R, G, B, or intensity component.
   * @return an array of integers representing 256 component values.
   */
  public int[] initializeHistogramValues(Component comp) {
    BufferedImage img = saveImage("", "name");
    int[] values = new int[256];

    switch (comp) {
      case Intensity:
        for (int i = img.getMinY(); i < img.getHeight(); i++) {
          for (int j = img.getMinX(); j < img.getWidth(); j++) {
            values[(int) Math.round((new Color(img.getRGB(j, i)).getRed()
                    + new Color(img.getRGB(j, i)).getBlue()
                    + new Color(img.getRGB(j, i)).getGreen()) / 3.0)]++;
          }
        }
        break;
      case Red:
        for (int i = img.getMinY(); i < img.getHeight(); i++) {
          for (int j = img.getMinX(); j < img.getWidth(); j++) {
            values[new Color(img.getRGB(j, i)).getRed()]++;
          }
        }
        break;
      case Green:
        for (int i = img.getMinY(); i < img.getHeight(); i++) {
          for (int j = img.getMinX(); j < img.getWidth(); j++) {
            values[new Color(img.getRGB(j, i)).getGreen()]++;
          }
        }
        break;
      case Blue:
        for (int i = img.getMinY(); i < img.getHeight(); i++) {
          for (int j = img.getMinX(); j < img.getWidth(); j++) {
            values[new Color(img.getRGB(j, i)).getBlue()]++;
          }
        }
        break;
      default:
    }
    return values; //returns an array of values that will be displayed in the histogram
  }

  @Override
  public void downscale(int heightNew, int widthNew, String name, String modifiedImage) {
    ImageInterface image = findImage(name);
    ArrayList<ArrayList<IPixel>> img = image.getPixels();

    int originalWidth = img.get(0).size();
    int originalHeight = img.size();

    if (widthNew * heightNew > originalWidth * originalHeight) {
      throw new IllegalArgumentException("Dimensions cannot be larger than inputted image");
    }
    ArrayList<ArrayList<IPixel>> downscaled = this
            .downscaleArrayList(img, originalWidth,
                    originalHeight, widthNew, heightNew);

    ImageInterface newImage = new ImageImpl(downscaled);
    storedImages.put(modifiedImage, newImage);

  }

  /**
   * Returns the entire 2d arraylist of pixels for the
   * downscaled image.
   *
   * @param originalImg    original image.
   * @param originalWidth  original width of image.
   * @param originalHeight original height of image.
   * @param widthNew       new width of image.
   * @param heightNew      new height of image.
   * @return the array of pixels for the downscaled modification.
   */
  private ArrayList<ArrayList<IPixel>> downscaleArrayList(ArrayList<ArrayList<IPixel>> originalImg,
                                                          int originalWidth, int originalHeight,
                                                          int widthNew, int heightNew) {
    ArrayList<ArrayList<IPixel>> downscaledImage = new ArrayList<>();
    double xPrime;
    double yPrime;
    IPixel originalPix;
    IPixel newPix;

    for (int i = 0; i < heightNew; i++) {
      ArrayList<IPixel> newImageRow = new ArrayList<>();
      for (int j = 0; j < widthNew; j++) {
        xPrime = (j * (double) originalWidth) / widthNew;
        yPrime = (i * (double) originalHeight) / heightNew;

        if ((int) xPrime == xPrime || (int) yPrime == yPrime) {
          originalPix = originalImg.get((int) yPrime).get((int) xPrime);
          newPix = new RGBPixelImpl(originalPix.getPosition(),
                  new ColorImpl(originalPix.getColor().getRed(), originalPix.getColor().getGreen(),
                          originalPix.getColor().getBlue()));
        } else {
          int[] colors = this.getColorComponents(originalImg, xPrime, yPrime);
          newPix = new RGBPixelImpl(new Position2D(j, i),
                  new ColorImpl(colors[0], colors[1], colors[2]));
        }
        newImageRow.add(newPix);
      }
      downscaledImage.add(newImageRow);
    }
    return downscaledImage;
  }

  /**
   * Returns rgb color components for the pixels in the downscaled image.
   *
   * @param original image.
   * @param xPrime   x position num.
   * @param yPrime   y position num.
   * @return new rbg values.
   */
  private int[] getColorComponents(ArrayList<ArrayList<IPixel>> original, double xPrime,
                                   double yPrime) {
    int[] newRGB = new int[3];
    IPixel aPix;
    IPixel bPix;
    IPixel cPix;
    IPixel dPix;

    Position2D aPos = new Position2D((int) Math.floor(xPrime), (int) Math.floor(yPrime));
    Position2D bPos = new Position2D((int) Math.ceil(xPrime), (int) Math.floor(yPrime));
    Position2D cPos = new Position2D((int) Math.floor(xPrime), (int) Math.ceil(yPrime));
    Position2D dPos = new Position2D((int) Math.ceil(xPrime), (int) Math.ceil(yPrime));

    try {
      aPix = original.get(aPos.getY()).get(aPos.getX());
      bPix = original.get(bPos.getY()).get(bPos.getX());
      cPix = original.get(cPos.getY()).get(cPos.getX());
      dPix = original.get(dPos.getY()).get(dPos.getX());
    } catch (IndexOutOfBoundsException e) {
      aPix = original.get((int) yPrime).get((int) xPrime);
      bPix = original.get((int) yPrime).get((int) xPrime);
      cPix = original.get((int) yPrime).get((int) xPrime);
      dPix = original.get((int) yPrime).get((int) xPrime);
    }


    double mRedComponent = (bPix.getColor().getRed() * (xPrime - Math.floor(xPrime)))
            + (aPix.getColor().getRed() * (Math.ceil(xPrime) - xPrime));
    double mGreenComponent = (bPix.getColor().getGreen() * (xPrime - Math.floor(xPrime)))
            + (aPix.getColor().getGreen() * (Math.ceil(xPrime) - xPrime));
    double mBlueComponent = (bPix.getColor().getBlue() * (xPrime - Math.floor(xPrime)))
            + (aPix.getColor().getBlue() * (Math.ceil(xPrime) - xPrime));

    double nRedComponent = (dPix.getColor().getRed() * (xPrime - Math.floor(xPrime)))
            + (cPix.getColor().getRed() * (Math.ceil(xPrime) - xPrime));
    double nGreenComponent = (dPix.getColor().getGreen() * (xPrime - Math.floor(xPrime)))
            + (cPix.getColor().getGreen() * (Math.ceil(xPrime) - xPrime));
    double nBlueComponent = (dPix.getColor().getBlue() * (xPrime - Math.floor(xPrime)))
            + (cPix.getColor().getBlue() * (Math.ceil(xPrime) - xPrime));

    double pRedComponent = (nRedComponent * (yPrime - Math.floor(yPrime)))
            + (mRedComponent * (Math.ceil(yPrime) - yPrime));
    double pGreenComponent = (nGreenComponent * (yPrime - Math.floor(yPrime)))
            + (mGreenComponent * (Math.ceil(yPrime) - yPrime));
    double pBlueComponent = (nBlueComponent * (yPrime - Math.floor(yPrime)))
            + (mBlueComponent * (Math.ceil(yPrime) - yPrime));

    newRGB[0] = (int) pRedComponent;
    newRGB[1] = (int) pGreenComponent;
    newRGB[2] = (int) pBlueComponent;

    return newRGB;
  }

  @Override
  public ImageInterface findImage(String name) throws IllegalArgumentException {
    ImageInterface image = storedImages.getOrDefault(name, null);
    if (image == null) {
      throw new IllegalArgumentException("Image " + name + " not found.");
    }
    return image;
  }

  /**
   * "Clamps" the values of invalid rgb values if > 255 or are < 0.
   *
   * @param rgb value to clamp.
   * @return The clamped value.
   */
  private int clampValues(int rgb) {
    if (rgb > 255) {
      return 255;
    } else if (rgb < 0) {
      return 0;
    }
    return rgb;
  }
}
