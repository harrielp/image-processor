package controller.commands;

import model.ImageProcessorModel;

/**
 * Command class to perform a downscale transformation on an image. Changes the
 * image's dimensions.
 */
public class Downscale implements ImageProcessingCommand {

  private String name;
  private String newName;
  private int heightNew;
  private int widthNew;

  /**
   * Constructor to perform a grayscale on an image by
   * making all the pixels either all red, all green or all blue.
   *
   * @param name      of String.
   * @param newName   modified vers.
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public Downscale(int heightNew, int widthNew, String name, String newName)
          throws IllegalArgumentException {
    if (name == null || newName == null) {
      throw new IllegalArgumentException("Cannot have a null argument");
    }
    this.heightNew = heightNew;
    this.widthNew = widthNew;
    this.name = name;
    this.newName = newName;
  }


  @Override
  public void run(ImageProcessorModel model) {
    model.downscale(this.heightNew, this.widthNew, this.name, this.newName);
  }
}
