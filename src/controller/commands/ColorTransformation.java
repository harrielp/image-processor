package controller.commands;

import model.ImageProcessorModel;

/**
 * Applies a grayscale color transformation onto an image.
 */
public class ColorTransformation implements ImageProcessingCommand {

  private final ImageProcessorModel.ColorTransformationsMode colorTransformation;
  private final String name;
  private final String newName;

  /**
   * Constructor to perform a grayscale transformation on an image by
   * altering the rgb values.
   *
   * @param colorTransformation an enum representing the different color transformations.
   * @param name    of the image the user wants to change.
   * @param newName the name the user wants to give to the modified image.
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public ColorTransformation(ImageProcessorModel.ColorTransformationsMode colorTransformation,
                             String name, String newName) throws IllegalArgumentException {
    if (colorTransformation == null || name == null || newName == null) {
      throw new IllegalArgumentException("Cannot have a null argument");
    }
    this.colorTransformation = colorTransformation;
    this.name = name;
    this.newName = newName;
  }

  @Override
  public void run(ImageProcessorModel model) {
    model.colorTransformations(this.colorTransformation, this.name, this.newName);
  }
}
