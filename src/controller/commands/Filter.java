package controller.commands;

import model.ImageProcessorModel;

/**
 * Sharpen filter on an image.
 */
public class Filter implements ImageProcessingCommand {

  private final ImageProcessorModel.FilterMode filter;
  private final String name;
  private final String newName;

  /**
   * Constructor to blur or sharpen an image.
   *
   * @param filter an enum representing the two filter transformations.
   * @param name    of image.
   * @param newName new name given.
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public Filter(ImageProcessorModel.FilterMode filter,
                 String name, String newName) throws IllegalArgumentException {
    if (filter == null || name == null || newName == null) {
      throw new IllegalArgumentException("Cannot have a null argument");
    }
    this.filter = filter;
    this.name = name;
    this.newName = newName;
  }

  @Override
  public void run(ImageProcessorModel model) {
    model.filter(this.filter, this.name, this.newName);
  }
}
