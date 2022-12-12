package controller.commands;

import model.ImageProcessorModel;

/**
 * Creates a grayscale filter for an image varying for each RGB value.
 */
public class Grayscale implements ImageProcessingCommand {

  private final ImageProcessorModel.GrayscaleMode grayscale;
  private final String name;
  private final String newName;

  /**
   * Constructor to perform a grayscale on an image by
   * making all the pixels either all red, all green or all blue.
   *
   * @param grayscale enum representing the 3 RGB variations.
   * @param name    of the image the user wants to change.
   * @param newName the name the user wants to give to the modified image.
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public Grayscale(ImageProcessorModel.GrayscaleMode grayscale,
                   String name, String newName) throws IllegalArgumentException {
    if (grayscale == null || name == null || newName == null) {
      throw new IllegalArgumentException("Cannot have a null argument");
    }
    this.name = name;
    this.newName = newName;
    this.grayscale = grayscale;
  }

  @Override
  public void run(ImageProcessorModel model) {
    model.grayscale(this.grayscale, this.name, this.newName);
  }
}