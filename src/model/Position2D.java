package model;

import java.util.Objects;

/**
 * This class represents (x, y) coordinates for image placements.
 */
public class Position2D {

  private final int x;
  private final int y;

  /**
   * Creates a 2D point given its x and y coordinates.
   *
   * @param x x-coordinate of the point
   * @param y y-coordinate of the point
   */
  public Position2D(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Retrieves the x-coordinate.
   *
   * @return the x-coordinate as an integer
   */
  public int getX() {
    return this.x;
  }

  /**
   * Retrieves the y-coordinate.
   *
   * @return the y-coordinate as an integer.
   */
  public int getY() {
    return this.y;
  }

  /**
   * Overrides the java equals.
   *
   * @param o object being compared to.
   * @return true, false or the equality of the positions.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Position2D)) {
      return false;
    }

    Position2D other = (Position2D) o;
    return this.x == other.x && this.y == other.y;
  }

  /**
   * Overrides hashcode for java.
   *
   * @return new hash.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }
}

