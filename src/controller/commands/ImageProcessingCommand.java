package controller.commands;

import model.ImageProcessorModel;

/**
 * Interface for ImageProcessingCommand to use commands.
 */
public interface ImageProcessingCommand {

  /**
   * Method to run the given commands with the desired model.
   * @param m model of ImageProcessor.
   */
  void run(ImageProcessorModel m);
}
