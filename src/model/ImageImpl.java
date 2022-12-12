package model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class representing a simple implementation of an Image, which is a 2D array of pixels. This array
 * is represented by a 2D ArrayList.
 */
public class ImageImpl implements ImageInterface {

  private final ArrayList<ArrayList<IPixel>> pixels;

  /**
   * Creates a new image with the given 2d arraylist of pixels.
   *
   * @param pixels Pixels to create the image with.
   * @throws IllegalArgumentException If the list of pixels is null.
   */
  public ImageImpl(ArrayList<ArrayList<IPixel>> pixels) throws IllegalArgumentException {
    if (pixels == null) {
      throw new IllegalArgumentException("List of pixels cannot be null");
    }
    this.pixels = this.deepCopyPixel(pixels);
  }

  /**
   * Retrieves the pixels of the arraylist.
   *
   * @return a new arraylist of pixels.
   */
  @Override
  public ArrayList<ArrayList<IPixel>> getPixels() {
    return new ArrayList<>(pixels);
  }

  /**
   * Overrides equals for java.
   *
   * @param o object to be compared to.
   * @return true, false or the equality of the pixels.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ImageImpl)) {
      return false;
    }

    ImageImpl other = (ImageImpl) o;
    return this.pixels.equals(other.pixels);
  }

  /**
   * Overrides hashcode for java.
   *
   * @return new hash.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.pixels);
  }

  /**
   * Creates a deep copy of the given 2d arraylist of pixels.
   *
   * @param pixels Pixels to be copied.
   * @return The deep copy of the list.
   */
  public ArrayList<ArrayList<IPixel>> deepCopyPixel(ArrayList<ArrayList<IPixel>> pixels) {
    ArrayList<ArrayList<IPixel>> pixelsCopy = new ArrayList<>();
    for (ArrayList<IPixel> row : pixels) {
      ArrayList<IPixel> rowCopy = new ArrayList<>();
      for (IPixel pixel : row) {
        rowCopy.add(new RGBPixelImpl(pixel.getPosition(), pixel.getColor()));
      }
      pixelsCopy.add(rowCopy);
    }
    return pixelsCopy;
  }
}
