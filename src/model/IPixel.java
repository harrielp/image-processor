package model;

/**
 * Interface representing a pixel of an image that
 * will help retrieve a given pixel's (x, y) position
 * and RGB color.
 */
public interface IPixel {

  /**
   * Returns the position of the pixel.
   *
   * @return the pixel's position
   */
  Position2D getPosition();

  /**
   * Gets the RGB color of the pixel.
   *
   * @return the pixel's color.
   */
  IColor getColor();

}
