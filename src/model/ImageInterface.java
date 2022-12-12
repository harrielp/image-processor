package model;

import java.util.ArrayList;

/**
 * Interface to represent an image, constructed of pixels. The image is represented by a 2D
 * arraylist of the pixels.
 */
public interface ImageInterface {

  /**
   * Returns the 2D arraylist of the pixels of the image.
   *
   * @return The pixels of the image.
   */
  ArrayList<ArrayList<IPixel>> getPixels();

}
