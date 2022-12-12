import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import controller.commands.BrightenDarken;
import controller.commands.Flip;
import controller.commands.Grayscale;
import controller.commands.ImageProcessingCommand;
import controller.commands.Load;
import controller.commands.Save;
import model.ColorImpl;
import model.ImageImpl;
import model.ImageProcessorModel;
import model.Position2D;
import model.RGBPixelImpl;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the model's images and commands.
 */
public class ModelPPMTest {
  //NOTE: ALL SAVED IMAGES FROM TESTING GO TO THE "imagesTest/" DIRECTORY
  private ImageProcessorModel model;
  private String pixelImgBrighter;
  private String pixelImgDarker;
  private String pixelImgGrayscaleRed;
  private String pixelImgGrayscaleGreen;
  private String pixelImgGrayscaleBlue;
  private String pixelImgVertical;
  private String pixelImgHorizontal;
  private String pixelImgIntensity;
  private String pixelImgLuma;
  private String pixelImgValue;

  private String pixelChange;

  ImageProcessingCommand brighten;
  ImageProcessingCommand darken;
  ImageProcessingCommand vertical;
  ImageProcessingCommand horizontal;
  ImageProcessingCommand grayscaleRed;
  ImageProcessingCommand grayscaleGreen;
  ImageProcessingCommand grayscaleBlue;
  ImageProcessingCommand intensityGrayscale;
  ImageProcessingCommand valueGrayscale;
  ImageProcessingCommand lumaGrayscale;

  @Before
  public void setUp() {
    try {
      //convert each existing file into a String to compare files
      pixelImgBrighter = Files.readString(Paths.get("res/pixelImg-brighter-by-20.ppm"));
      pixelImgDarker = Files.readString(Paths.get("res/pixelImg-darken-by-10.ppm"));
      pixelImgVertical = Files.readString(Paths.get("res/pixelImg-verticalFlip.ppm"));
      pixelImgHorizontal = Files.readString(Paths.get("res/pixelImg-horizontal-flip.ppm"));
      pixelImgGrayscaleRed = Files.readString(Paths.get("res/pixelImg-red-grayscale.ppm"));
      pixelImgGrayscaleGreen = Files.readString(Paths.get("res/pixelImg-green-grayscale.ppm"));
      pixelImgGrayscaleBlue = Files.readString(Paths.get("res/pixelImg-blue-grayscale.ppm"));
      pixelImgIntensity = Files.readString(Paths.get("res/pixelImg-intensity-grayscale.ppm"));
      pixelImgLuma = Files.readString(Paths.get("res/pixelImg-luma-grayscale.ppm"));
      pixelImgValue = Files.readString(Paths.get("res/pixelImg-value-grayscale.ppm"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //make a copy of the original image just for testing
  private void loadTest() {
    model = new ImageProcessorModel();
    model.load("res/pixelImg.ppm", "testPPM");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBelowZeroColorComponents() {
    new ColorImpl(-3, 245, 233);
    new ColorImpl(123, -9, 76);
    new ColorImpl(45, 2, -9);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAbove255ColorComponents() {
    new ColorImpl(345, 245, 233);
    new ColorImpl(123, 789, 76);
    new ColorImpl(45, 2, 378);
  }

  @Test(expected = NullPointerException.class)
  public void testFindImageNullImage() {
    model.findImage("peepeepoopoo");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullImage() {
    new ImageImpl(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRGBPixelColor() {
    new RGBPixelImpl(new Position2D(4, 5), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRGBPixelPosn() {
    new RGBPixelImpl(null, new ColorImpl(233, 233, 233));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullGrayscaleConstructor() {
    new Grayscale(null, "doesn't work", "hehe");
    new Grayscale(ImageProcessorModel.GrayscaleMode.Red, null, "hehe");
    new Grayscale(ImageProcessorModel.GrayscaleMode.Blue, "doesn't work", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFlipConstructor() {
    new Flip(null, "doesn't work", "hehe");
    new Flip(ImageProcessorModel.FlipMode.Vertical, null, "hehe");
    new Flip(ImageProcessorModel.FlipMode.Horizontal, "doesn't work", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullBrightenDarkenConstructor() {
    new BrightenDarken(21, null, "hehe");
    new BrightenDarken(1, "doesn't work", null);
  }

  @Test(expected = NullPointerException.class)
  public void testInvalidPPMImageNotFound() {
    model.findImage("oopsies"); //image does not exist in the storedImages
    model.load("res/pixelImgPNG.png", "lelz"); //NOT a ppm file
    model.save("FileCannotBeFound!", "naur");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveNullArg() {
    new Save(null, "not valid!");
    new Save("imagesTest/testBrighten.ppm", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadNullArgs() {
    new Load(null, null);
  }

  //essentially:
  //load testPPM --> assign values to pixels in image
  //change testPPM --> calls method
  //save testPPM to another name
  //compare already existing PPM file that corresponds to the change
  @Test
  public void testBrighten() {
    this.loadTest();
    brighten = new BrightenDarken(20, "testPPM", "testBrighten");
    brighten.run(this.model);
    try {
      pixelChange = Files.readString(Paths.get(this.model.save("imagesTest/brighten.ppm",
              "testBrighten").getAbsolutePath()));
      assertEquals(pixelChange, pixelImgBrighter);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDarken() {
    this.loadTest();
    darken = new BrightenDarken(-10, "testPPM", "testDarken");
    darken.run(this.model);
    try {
      pixelChange = Files.readString(Paths.get(this.model.save("imagesTest/darken.ppm",
              "testDarken").getAbsolutePath()));
      assertEquals(pixelChange, pixelImgDarker);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testIntensityGrayscale() {
    this.loadTest();
    intensityGrayscale = new Grayscale(ImageProcessorModel.GrayscaleMode.Intensity,
            "testPPM", "testIntensityGrayscale");
    intensityGrayscale.run(this.model);
    try {
      pixelChange =
              Files.readString(Paths.get(this.model.save("imagesTest/testIntensity.ppm",
                      "testIntensityGrayscale").getAbsolutePath()));
      assertEquals(pixelChange, pixelImgIntensity);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testValueGrayscale() {
    this.loadTest();
    valueGrayscale = new Grayscale(ImageProcessorModel.GrayscaleMode.Value,
            "testPPM", "testValueGrayscale");
    valueGrayscale.run(this.model);
    try {
      pixelChange =
              Files.readString(Paths.get(this.model.save("imagesTest/testValue.ppm",
                      "testValueGrayscale").getAbsolutePath()));
      assertEquals(pixelChange, pixelImgValue);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testLumaGrayscale() {
    this.loadTest();
    lumaGrayscale = new Grayscale(ImageProcessorModel.GrayscaleMode.Luma,
            "testPPM", "testLumaGrayscale");
    lumaGrayscale.run(this.model);
    try {
      pixelChange =
              Files.readString(Paths.get(this.model.save("imagesTest/testLuma.ppm",
                      "testLumaGrayscale").getAbsolutePath()));
      assertEquals(pixelChange, pixelImgLuma);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRedGrayscale() {
    this.loadTest();
    grayscaleRed = new Grayscale(ImageProcessorModel.GrayscaleMode.Red,
            "testPPM", "testRedGrayscale");
    grayscaleRed.run(this.model);
    try {
      pixelChange =
              Files.readString(Paths.get(this.model.save("imagesTest/testRedComp.ppm",
                      "testRedGrayscale").getAbsolutePath()));
      assertEquals(pixelChange, pixelImgGrayscaleRed);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGreenGrayscale() {
    this.loadTest();
    grayscaleGreen = new Grayscale(ImageProcessorModel.GrayscaleMode.Green,
            "testPPM", "testGreenGrayscale");
    grayscaleGreen.run(this.model);
    try {
      pixelChange =
              Files.readString(Paths.get(this.model.save("imagesTest/testGreenComp.ppm",
                      "testGreenGrayscale").getAbsolutePath()));
      assertEquals(pixelChange, pixelImgGrayscaleGreen);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testBlueGrayscale() {
    this.loadTest();
    grayscaleBlue = new Grayscale(ImageProcessorModel.GrayscaleMode.Blue,
            "testPPM", "testBlueGrayscale");
    grayscaleBlue.run(this.model);
    try {
      pixelChange =
              Files.readString(Paths.get(this.model.save("imagesTest/testBlueComp.ppm",
                      "testBlueGrayscale").getAbsolutePath()));
      assertEquals(pixelChange, pixelImgGrayscaleBlue);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testVerticalFlip() {
    this.loadTest();
    vertical = new Flip(ImageProcessorModel.FlipMode.Vertical,
            "testPPM", "testVertical");
    vertical.run(this.model);
    try {
      pixelChange = Files.readString(Paths.get(this.model.save("imagesTest/testVertical.ppm",
              "testVertical").getAbsolutePath()));
      assertEquals(pixelChange, pixelImgVertical);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testHorizontalFlip() {
    this.loadTest();
    horizontal = new Flip(ImageProcessorModel.FlipMode.Horizontal,
            "testPPM", "testHorizontal");
    this.horizontal.run(this.model);
    try {
      pixelChange = Files.readString(Paths.get(this.model.save("imagesTest/testHorizontal.ppm",
              "testHorizontal").getAbsolutePath()));
      assertEquals(pixelChange, pixelImgHorizontal);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

