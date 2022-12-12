package controller;

/**
 * An interface for the commands that will be assigned to the GUI buttons. Includes all the
 * commands for image modifications so that when a button is clicked, the function will run
 * and the image will update.
 */
public interface ButtonCommands {
  /**
   * Saves an image from the GUI, whether it is modified or not. Utilizes the field "typeOfFile"
   * to save an image with the same format as the loaded image.
   */
  void save();

  /**
   * Downscales an image in the GUI by running the run(ImageProcessorModel model) and rendering
   * the modified image to the view.
   */
  void downscale();

  /**
   * Brightens the image in the GUI by running the run(ImageProcessorModel model) and rendering
   * the modified image to the view.
   */
  void brighten();

  /**
   * Darkens the image in the GUI by running the run(ImageProcessorModel model) and rendering
   * the modified image to the view.
   */
  void darken();

  /**
   * Flips the image horizontally in the GUI by running the run(ImageProcessorModel model) and
   * rendering the modified image to the view.
   */
  void flipHori();

  /**
   * Flips the image vertically in the GUI by running the run(ImageProcessorModel model) and
   * rendering the modified image to the view.
   */
  void flipVert();

  /**
   * Greyscale the image using the red component in the GUI by running the run(ImageProcessorModel
   * model) and rendering the modified image to the view.
   */
  void redComponent();

  /**
   * Greyscale the image using the green component in the GUI by running the run(ImageProcessorModel
   * model) and rendering the modified image to the view.
   */
  void greenComponent();

  /**
   * Greyscale the image using the blue component in the GUI by running the run(ImageProcessorModel
   * model) and rendering the modified image to the view.
   */
  void blueComponent();

  /**
   * Greyscale the image using luma in the GUI by running the run(ImageProcessorModel
   * model) and rendering the modified image to the view.
   */
  void lumaGrayscale();

  /**
   * Greyscale the image using value in the GUI by running the run(ImageProcessorModel
   * model) and rendering the modified image to the view.
   */
  void valueGrayscale();

  /**
   * Greyscale the image using intensity in the GUI by running the run(ImageProcessorModel
   * model) and rendering the modified image to the view.
   */
  void intensityGrayscale();

  /**
   * Blurs the image in the GUI by running the run(ImageProcessorModel model) and rendering
   * the modified image to the view.
   */
  void blur();

  /**
   * Sharpens the image in the GUI by running the run(ImageProcessorModel model) and rendering
   * the modified image to the view.
   */
  void sharpen();

  /**
   * Modifies the image to sepia tone in the GUI by running the run(ImageProcessorModel model)
   * and rendering the modified image to the view.
   */
  void sepia();

  /**
   * Transforms the image to grayscale in the GUI by running the run(ImageProcessorModel model)
   * and rendering the modified image to the view.
   */
  void grayscaleColorTrans();

  /**
   * Refreshes the whole application when its corresponding button is pressed, just in case the
   * user wants to restart.
   */
  void restart();
}
