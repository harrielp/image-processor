import org.junit.Test;

import controller.ImageProcessorController;
import controller.ImageProcessorControllerImpl;
import model.ImageProcessorModel;
import view.TextView;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the Controller constructors and runApp() method.
 */
public class ControllerTest {

  // test constructors of the controller
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullModel() {
    ImageProcessorModel model = new ImageProcessorModel();
    new ImageProcessorControllerImpl(null, new TextView(model));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullView() {
    ImageProcessorModel model = new ImageProcessorModel();
    new ImageProcessorControllerImpl(model, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullReadable() {
    ImageProcessorModel model = new ImageProcessorModel();
    new ImageProcessorControllerImpl(model, new TextView(model), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalCommand() {
    ImageProcessorModel model = new ImageProcessorModel();
    StringBuilder builder = new StringBuilder();
    TextView view = new TextView(model, builder);
    ImageProcessorController controller1 = new ImageProcessorControllerImpl(model, view,
            new StringReader("jones barbeque q"));
    controller1.runApp();
  }

  @Test
  public void testQuit() {
    ImageProcessorModel model = new ImageProcessorModel();
    StringBuilder builder = new StringBuilder();
    TextView view = new TextView(model, builder);
    ImageProcessorController controller1 = new ImageProcessorControllerImpl(model, view,
            new StringReader("q"));
    controller1.runApp();
    assertEquals("Please enter a command!\n", builder.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    StringBuilder builder = new StringBuilder();
    TextView view = new TextView(null, builder);
    ImageProcessorController controller = new ImageProcessorControllerImpl(null, view,
            new StringReader("q"));
    controller.runApp();
    String[] expectedOutput = builder.toString().split("\n");
    assertEquals("Cannot have invalid model\n", expectedOutput[0]);
  }

  @Test
  public void testValidCommand() {
    ImageProcessorModel model = new ImageProcessorModel();
    StringBuilder builder = new StringBuilder();
    TextView view = new TextView(model, builder);
    ImageProcessorController controller = new ImageProcessorControllerImpl(model, view,
            new StringReader("load res/pixelImgPNG.png testPixels q"));
    controller.runApp();
    assertEquals("Please enter a command!\n", builder.toString());
  }

  @Test
  public void testLoadTransformSaveCommand() {
    ImageProcessorModel model = new ImageProcessorModel();
    StringBuilder builder = new StringBuilder();
    TextView view = new TextView(model, builder);
    ImageProcessorController controller = new ImageProcessorControllerImpl(model, view,
            new StringReader("load res/pixelImg.ppm testPPM luma testPPM testLumaGrayscale "
                    + "save imagesTest/testLuma.ppm testLumaGrayscale q"));
    controller.runApp();
    assertTrue(Files.exists(Paths.get("imagesTest/testLuma.ppm")));
  }
}


