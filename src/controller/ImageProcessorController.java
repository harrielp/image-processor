package controller;


/**
 * The controller contain a model and a view.
 * Gives user a way to interact with the model,
 * and use the view to display the images in the model.
 */
public interface ImageProcessorController {

  /**
   * Start running the controller with the given inputs
   * and checks for text method commands and runs
   * respective methods.
   *
   * @throws IllegalArgumentException if invalid commands.
   */
  void runApp() throws IllegalArgumentException;
}
