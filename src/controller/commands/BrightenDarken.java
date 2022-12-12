package controller.commands;

import model.ImageProcessorModel;

/**
 * Brightens or darkens an image by increasing or decreasing the RGB values
 * with a constant value.
 */
public class BrightenDarken implements ImageProcessingCommand {

  // constant
  private final int increment;
  private final String name;
  private final String newName;

  /**
   * Constructor to brighten or darken the given image.
   *
   * @param increment constant value to be either added or subtracted.
   * @param name      of the image user wants to change.
   * @param newName   name user wants to give to the new image.
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public BrightenDarken(int increment, String name,
                        String newName) throws IllegalArgumentException {
    if (name == null || newName == null) {
      throw new IllegalArgumentException("Cannot have a null argument");
    }
    this.increment = increment;
    this.name = name;
    this.newName = newName;
  }

  @Override
  public void run(ImageProcessorModel model) {
    model.brighten(this.increment, this.name, this.newName);
  }
}