package model;

import java.util.Objects;

/**
 * This class represents an RGB pixel with a position in an image.
 */
public class RGBPixelImpl implements IPixel {

  private final Position2D position;
  private final IColor color;

  /**
   * Creates a new pixel.
   *
   * @param position of the pixel.
   * @param color    of the pixel.
   * @throws IllegalArgumentException if the position or color is null.
   */
  public RGBPixelImpl(Position2D position, IColor color) throws IllegalArgumentException {
    if (position == null || color == null) {
      throw new IllegalArgumentException("Argument cannot be null.");
    }
    this.position = position;
    this.color = color;
  }

  /**
   * Retrieves the (x, y) coordinate position.
   * @return the given position.
   */
  @Override
  public Position2D getPosition() {
    return new Position2D(position.getX(), position.getY());
  }

  /**
   * Retrieves the color.
   * @return the given color.
   */
  @Override
  public IColor getColor() {
    return color;
  }


  /**
   * Overrides the java equals.
   * @param o object being compared to.
   * @return true, false or the equality of the positions and colors.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RGBPixelImpl)) {
      return false;
    }

    RGBPixelImpl other = (RGBPixelImpl) o;
    return this.position.equals(other.position) && this.color.equals(other.color);
  }

  /**
   * Overrides hashcode for java.
   * @return new hash.
   */
  @Override
  public int hashCode() {
    return Objects.hash(position, color);
  }

}
