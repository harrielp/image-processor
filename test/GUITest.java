import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import controller.GUIController;
import controller.commands.BrightenDarken;
import controller.commands.ColorTransformation;
import controller.commands.Downscale;
import controller.commands.Flip;
import controller.commands.Grayscale;
import controller.commands.ImageProcessingCommand;
import controller.commands.Load;
import model.ImageProcessorModel;
import view.GUIView;
import view.GUIViewImpl;

import static org.junit.Assert.assertTrue;

/**
 * Class for testing the GUI controller and view.
 */
public class GUITest {
  //test null model in GUIViewImpl constructor
  @Test(expected = IllegalArgumentException.class)
  public void testNullModelInView() {
    new GUIViewImpl(null);
  }

  // test null model in GUI controller constructor
  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    ImageProcessorModel m = new ImageProcessorModel();
    GUIView view = new GUIViewImpl(m);
    new GUIController(null, view); //should not open the GUI
  }

  // test null view in GUI controller constructor
  @Test(expected = IllegalArgumentException.class)
  public void testNullView() {
    ImageProcessorModel m = new ImageProcessorModel();
    new GUIController(m, null); //should not open the GUI
  }

  /**
   * Determines if two buffered images are equal based on RGB components.
   *
   * @param img1 an image.
   * @param img2 another image to compare to img1.
   * @return true if the images have the same pixels, width, and height
   */
  private boolean equalImages(BufferedImage img1, BufferedImage img2) {
    if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
      for (int i = 0; i < img1.getWidth(); i++) {
        for (int j = 0; j < img1.getHeight(); j++) {
          if (img1.getRGB(i, j) != img2.getRGB(i, j)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  //test if the code associated with each JButton works as intended
  @Test
  public void testActionListenerInButtons() {
    ImageProcessingCommand command;
    BufferedImage saved;
    ImageProcessorModel m = new ImageProcessorModel();

    //this test represents the functionality for the buttons, since the code for each button
    //functions the same way (only difference is which command class they create)

    //A FEW EXAMPLES:
    //for brighten/darken command
    try {
      BufferedImage image = ImageIO.read(new FileInputStream("res/exampleImgBrighten.bmp"));
      //load example image
      ImageProcessingCommand load = new Load("exampleImg.bmp", "name");
      load.run(m);

      //code from the GUIController -> tests if the feature attached to the "image modification"
      //buttons work correctly by changing the model
      command = new BrightenDarken(30, "name", "name");
      command.run(m);
      //tests if renderedImage displays the correct image after the modification
      saved = m.saveImage("", "name");

      //is the image created by the GUI and image created through the model equal?
      assertTrue(equalImages(saved, image));

    } catch (IOException e) {
      e.printStackTrace();
    }

    //for color transformation
    try {
      BufferedImage image = ImageIO.read(new FileInputStream("res/exampleImgSepia.bmp"));
      //load example image
      ImageProcessingCommand load = new Load("exampleImg.bmp", "name");
      load.run(m);

      //code from the GUIController -> tests if the feature attached to the "image modification"
      //buttons work correctly by changing the model
      command = new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Sepia,
              "name", "name");
      command.run(m);
      //tests if renderedImage displays the correct image after the modification
      saved = m.saveImage("", "name");

      //is the image created by the GUI and image created through the model equal?
      assertTrue(equalImages(saved, image));

    } catch (IOException e) {
      e.printStackTrace();
    }

    //for flip
    try {
      BufferedImage image = ImageIO.read(new FileInputStream("res/exampleImgFlip.bmp"));
      //load example image
      ImageProcessingCommand load = new Load("exampleImg.bmp", "name");
      load.run(m);

      //code from the GUIController -> tests if the feature attached to the "image modification"
      //buttons work correctly by changing the model
      command = new Flip(ImageProcessorModel.FlipMode.Vertical,
              "name", "name");
      command.run(m);
      //tests if renderedImage displays the correct image after the modification
      saved = m.saveImage("", "name");

      //is the image created by the GUI and image created through the model equal?
      assertTrue(equalImages(saved, image));

    } catch (IOException e) {
      e.printStackTrace();
    }

    //for grayscale (six options : RGB, LUMA, VALUE, INTENSITY)
    try {
      BufferedImage image = ImageIO.read(new FileInputStream("res/exampleImgIntensity.bmp"));
      //load example image
      ImageProcessingCommand load = new Load("exampleImg.bmp", "name");
      load.run(m);

      //code from the GUIController -> tests if the feature attached to the "image modification"
      //buttons work correctly by changing the model
      command = new Grayscale(ImageProcessorModel.GrayscaleMode.Intensity,
              "name", "name");
      command.run(m);
      //tests if renderedImage displays the correct image after the modification
      saved = m.saveImage("", "name");

      //is the image created by the GUI and image created through the model equal?
      assertTrue(equalImages(saved, image));

    } catch (IOException e) {
      e.printStackTrace();
    }

    //for downscale (800x600 --> 400x200)
    try {
      BufferedImage image = ImageIO.read(new FileInputStream("res/exampleImgDownscale1.png"));
      //load example image
      ImageProcessingCommand load = new Load("exampleImgPNGCopy.png", "name");
      load.run(m);

      //code from the GUIController -> tests if the feature attached to the "image modification"
      //buttons work correctly by changing the model
      command = new Downscale(400, 200,
              "name", "name");
      command.run(m);
      //tests if renderedImage displays the correct image after the modification
      saved = m.saveImage("", "name");

      //is the image created by the GUI and image created through the model equal?
      assertTrue(equalImages(saved, image));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}