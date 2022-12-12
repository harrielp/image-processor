package controller.commands;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.ImageProcessorModel;

/**
 * Loads an image.
 */
public class Load implements ImageProcessingCommand {
  private final String path;
  private final String newName;

  /**
   * Constructor to load in an image.
   *
   * @param path    of image.
   * @param newName new name given.
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public Load(String path, String newName) throws IllegalArgumentException {
    if (path == null || newName == null) {
      throw new IllegalArgumentException("Cannot have a null argument");
    }
    this.path = path;
    this.newName = newName;
  }

  /**
   * A method that gets the type of file of the loaded image.
   * @return
   */
  public String typeOfFile() {
    //gets the type of file the user inputs -> "png", "jpg", etc.
    return this.path.substring(this.path.lastIndexOf(".") + 1);
  }

  /**
   * If the given source path is a PPM file, the runPPM method will run, which calls the original
   * load method strictly for PPM files. Otherwise, a new BufferedImage is created of the source
   * path and inputted to the load method that takes in a BufferedImage and a String.
   *
   * @param model of type ImageProcessorModel accesses the model in this program
   */
  @Override
  public void run(ImageProcessorModel model) {

    if (this.typeOfFile().equalsIgnoreCase("ppm")) {
      runPPM(model);
    } else {
      BufferedImage image;
      try {
        image = ImageIO.read(new FileInputStream(this.path)); //converts image to a BufferedImage
        model.load(image, this.newName);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * A method specifically ran for PPM files.
   *
   * @param model the model fed into the method to further load the image.
   */
  private void runPPM(ImageProcessorModel model) {
    model.load(this.path, this.newName);
  }
}
