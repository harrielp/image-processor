package controller.commands;

import model.ImageProcessorModel;

/**
 * Flips an image horizontally or vertically by altering the width and height values.
 */
public class Flip implements ImageProcessingCommand {

  private final ImageProcessorModel.FlipMode flip;
  private final String name;
  private final String newName;

  /**
   * Constructor to flip a given image either horizontally or vertically.
   *
   * @param flip    enum representing horizontal or vertical, depending on what the user wants.
   * @param name    of the image the user wants to change.
   * @param newName the name the user wants to give to the modified image.
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public Flip(ImageProcessorModel.FlipMode flip,
              String name, String newName) throws IllegalArgumentException {
    if (flip == null || name == null || newName == null) {
      throw new IllegalArgumentException("Cannot have a null argument");
    }
    this.flip = flip;
    this.name = name;
    this.newName = newName;
  }

  @Override
  public void run(ImageProcessorModel model) {
    model.flip(this.flip, this.name, this.newName);
  }
}
