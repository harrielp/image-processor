package controller.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.ImageProcessorModel;

/**
 * Saves an image.
 */
public class Save implements ImageProcessingCommand {

  private final String path;
  private final String name;


  /**
   * Constructor for save command method.
   *
   * @param path of where the new file should go.
   * @param name name of the file the user wants to save.
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public Save(String path, String name) throws IllegalArgumentException {
    if (path == null || name == null) {
      throw new IllegalArgumentException("Cannot have a null argument");
    }
    this.path = path;
    this.name = name;
  }

  /**
   * If the given source path is a PPM file, the runPPM method will run, which calls the original
   * save method strictly for PPM files. Otherwise, a new BufferedImage is created from the
   * saveImage method and a new File is created with the given source path.
   *
   * @param model of type ImageProcessorModel accesses the model in this program
   */
  @Override
  public void run(ImageProcessorModel model) {
    //gets the type of file the user inputs -> "png", "jpg", etc.
    String typeOfFile = this.path.substring(this.path.lastIndexOf(".") + 1);

    if (typeOfFile.equalsIgnoreCase("ppm")) {
      //if ppm file, use old load method
      runPPM(model);
    } else {
      try {
        BufferedImage result = model.saveImage(this.path, this.name);
        File outPutFile = new File(this.path); //saves to the path
        ImageIO.write(result, typeOfFile, outPutFile);
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
    model.save(this.path, this.name);
  }
}
