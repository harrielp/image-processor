import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import controller.commands.BrightenDarken;
import controller.commands.Filter;
import controller.commands.ColorTransformation;
import controller.commands.Flip;
import controller.commands.Grayscale;
import controller.commands.ImageProcessingCommand;
import controller.commands.Load;
import controller.commands.Save;
import model.IPixel;
import model.ImageInterface;
import model.ImageProcessorModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests all the new additions for Assignment 5 (new file formats, filters, color transformations).
 */
public class ModelNewFeaturesTest {
  //NOTE: ALL SAVED IMAGES FROM TESTING GO TO THE "imagesTest/" DIRECTORY
  private ImageProcessorModel model;
  private String pixelChange;
  private String ppmBlur;
  private String ppmSharpen;
  private String ppmSepia;
  private String ppmGreyscale;
  ImageProcessingCommand blur;
  ImageProcessingCommand sharpen;
  ImageProcessingCommand sepia;
  ImageProcessingCommand grayscaleTransformation;
  ImageProcessingCommand load;
  ImageProcessingCommand save;

  @Before
  public void setUp() {
    try {
      //original, blurred, sharpened, sepia tone, greyscale tone of EACH ppm, png, bmp, and jpg
      ppmBlur = Files.readString(Paths.get("res/pixelPPMBlur.ppm"));
      ppmSharpen = Files.readString(Paths.get("res/pixelPPMSharpen.ppm"));
      ppmSepia = Files.readString(Paths.get("res/pixelPPMSepia.ppm"));
      ppmGreyscale = Files.readString(Paths.get("res/pixelPPMGrayscaleTrans.ppm"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Determines if two buffered images are equal based on RGB components.
   *
   * @param img1 an image.
   * @param img2 another image to compare to img1.
   * @return true if the images have the same pixels, width, and height
   */
  private boolean equalBuffImages(BufferedImage img1, BufferedImage img2) {
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

  /**
   * Determines if two images (ppm and a non-ppm file) are equal based on RGB components.
   *
   * @param img1 an image.
   * @param img2 another image to compare to img1.
   * @return true if the images have the same pixels, width, and height
   */
  private boolean equalPPMAndNonPPM(ImageInterface img1, BufferedImage img2) {
    //in the testing methods, findImage(name) is used from the model's methods to return a type
    //ImageInterface that represents the image loaded from a ppm file --> represents img1
    ArrayList<ArrayList<IPixel>> pixels = img1.getPixels();
    if (pixels.get(0).size() == img2.getHeight() && pixels.size() == img2.getWidth()) {
      for (int i = 0; i < img2.getHeight(); i++) {
        for (int j = 0; j < img2.getWidth(); j++) {
          if (new Color(pixels.get(i).get(j).getColor().getRed(),
                  pixels.get(i).get(j).getColor().getGreen(),
                  pixels.get(i).get(j).getColor().getBlue()).getRGB() != img2.getRGB(j, i)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullImageInLoadMethod() {
    model = new ImageProcessorModel();
    BufferedImage buffImg = null;
    model.load(buffImg, "oops! image is null!");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindImageCantFindImage() {
    model = new ImageProcessorModel();
    model.findImage("FileDoesNotExist");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgsForFilterConstructor() {
    new Filter(null, "illegal", "not good");
    new Filter(ImageProcessorModel.FilterMode.Blur, null, "not good");
    new Filter(ImageProcessorModel.FilterMode.Blur, "illegal", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgsForColorTransformationConstructor() {
    new ColorTransformation(null, "illegal", "not good");
    new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Grayscale, null,
            "not good");
    new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Sepia, "illegal", null);
  }

  @Test
  public void testLoadNonPPM() {
    //if load works as intended on a non-PPM file, the save method will return a duplicate
    // image of the same RBG components but as a different file type
    try {
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgPNG.png", "testing");
      save = new Save("imagesTest/testPixelImgJPG.jpg", "testing");
      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelImgPNG.png"));

      load.run(model);
      save.run(model);

      BufferedImage saved = model.saveImage("imagesTest/testPixelImgJPG.jpg", "testing");

      assertTrue(equalBuffImages(image, saved));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testLoadAndSaveBetweenNonPPMFileTypes() {
    try {
      //test to see if we can save a non-ppm file to another non-ppm file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgPNG.png", "testing");
      save = new Save("imagesTest/testPixelImgBMP.bmp", "testing");
      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelImgPNG.png"));

      load.run(model);
      save.run(model);

      BufferedImage saved = model.saveImage("imagesTest/testPixelImgBMP.bmp", "testing");

      assertTrue(equalBuffImages(image, saved));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testLoadAndSavePPMToNonPPMFile() {
    //test to see if we can save a ppm file to a non-ppm file
    model = new ImageProcessorModel();
    load = new Load("res/pixelImg.ppm", "testing");
    save = new Save("imagesTest/testPixelImgPNG.png", "testing");

    load.run(model);
    save.run(model);

    BufferedImage savedNonPPM = model.saveImage("imagesTest/testPixelImgPNG.png", "testing");

    assertTrue(equalPPMAndNonPPM(model.findImage("testing"), savedNonPPM));
  }

  @Test
  public void testLoadAndSaveNonPPMToPPMFile() {
    try {
      //test to see if we can save a non-ppm file to a ppm file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgBMP.bmp", "testing");
      save = new Save("imagesTest/testPixelImgPPM.ppm", "testing");
      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelImgBMP.bmp"));

      load.run(model);
      save.run(model);

      assertTrue(equalPPMAndNonPPM(model.findImage("testing"), image));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //following three test methods: check if the previous implemented methods work with non-PPM files
  @Test
  public void testNonPPMBrighten() {
    model = new ImageProcessorModel();
    load = new Load("res/pixelImg.ppm", "testBMP");
    ImageProcessingCommand darken = new BrightenDarken(-10, "testBMP", "darken");
    save = new Save("imagesTest/testDarkenNonPPM.bmp", "darken");

    load.run(model);
    darken.run(model);
    save.run(model);

    BufferedImage savedNonPPM = model.saveImage("imagesTest/testDarkenNonPPM.bmp", "darken");
    //loads a ppm file with the same RGB components, used to compare to find equality between the
    //modified BMP file and the corresponding PPM file
    model.load("res/pixelImg-darken-by-10.ppm", "darkened");

    assertTrue(equalPPMAndNonPPM(model.findImage("darkened"), savedNonPPM));
  }

  @Test
  public void testNonPPMFlip() {
    model = new ImageProcessorModel();
    load = new Load("res/pixelImgBMP.bmp", "testPNG");
    ImageProcessingCommand horizontalFlip = new Flip(ImageProcessorModel.FlipMode.Horizontal,
            "testPNG", "flip");
    save = new Save("imagesTest/testHorizontalNonPPM.png", "flip");

    load.run(model);
    horizontalFlip.run(model);
    save.run(model);

    BufferedImage savedNonPPM = model.saveImage("imagesTest/testHorizontalNonPPM.png", "flip");
    //loads a ppm file with the same RGB components, used to compare to find equality between the
    //modified BMP file and the corresponding PPM file
    model.load("res/pixelImg-horizontal-flip.ppm", "flipped");

    assertTrue(equalPPMAndNonPPM(model.findImage("flipped"), savedNonPPM));
  }

  @Test
  public void testNonPPMGreyscaleRGB() {
    model = new ImageProcessorModel();
    load = new Load("res/pixelImgPNG.png", "testBMP");
    ImageProcessingCommand blueGreyscale = new Grayscale(ImageProcessorModel.GrayscaleMode.Blue,
            "testBMP", "blueComp");
    save = new Save("imagesTest/testRGBGrayscaleNonPPM.bmp", "blueComp");

    load.run(model);
    blueGreyscale.run(model);
    save.run(model);

    BufferedImage savedNonPPM = model.saveImage("imagesTest/testRGBGrayscaleNonPPM.bmp",
            "blueComp");
    //loads a ppm file with the same RGB components, used to compare to find equality between the
    //modified BMP file and the corresponding PPM file
    model.load("res/pixelImg-blue-grayscale.ppm", "blueGray");

    assertTrue(equalPPMAndNonPPM(model.findImage("blueGray"), savedNonPPM));
  }

  @Test
  public void testBlurPPM() {
    try {
      //on a PPM file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImg.ppm", "testBlur");
      blur = new Filter(ImageProcessorModel.FilterMode.Blur, "testBlur", "blur");
      save = new Save("imagesTest/testPixelPPMBlur.ppm", "blur");

      load.run(model);
      blur.run(model);
      save.run(model);

      //SAME LOGIC APPLIES TO ALL PPM TESTS BELOW!!! :
      //reads the blurred photo from res/ directory and checks if the created image is equal to it
      pixelChange =
              Files.readString(Paths.get(this.model.save("imagesTest/testPixelPPMBlur.ppm",
                      "blur").getAbsolutePath()));
      assertEquals(pixelChange, ppmBlur);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testBlurPNG() {
    try {
      //on a PNG file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgBMP.bmp", "testBlur");
      blur = new Filter(ImageProcessorModel.FilterMode.Blur, "testBlur", "blur");
      save = new Save("imagesTest/testPixelPNGBlur.png", "blur");

      load.run(model);
      blur.run(model);
      save.run(model);

      //SAME LOGIC APPLIES TO ALL PNG, BMP, AND JPG TESTS BELOW!! :
      //tests to see if the blurred image in the res/ directory equals the created image
      BufferedImage saved = model.saveImage("imagesTest/testPixelPNGBlur.png", "blur");
      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelPNGBlur.png"));

      assertTrue(equalBuffImages(image, saved));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //  @Test
  //  public void testBlurJPG() {
  //    try {
  //      //on a JPG file
  //      model = new ImageProcessorModel();
  //      load = new Load("res/pixelImgPNG.png", "testBlur");
  //      blur = new Filter(ImageProcessorModel.FilterMode.Blur, "testBlur", "blur");
  //      save = new Save("imagesTest/testPixelJPGBlur.jpg", "blur");
  //
  //      load.run(model);
  //      blur.run(model);
  //      save.run(model);
  //
  //      BufferedImage saved = model.saveImage("imagesTest/testPixelJPGBlur.jpg", "blur");
  //      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelJPGBlur.jpg"));
  //
  //      assertTrue(equalNonPPMs(image, saved));
  //
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //    }
  //  }

  @Test
  public void testBlurBMP() {
    try {
      //on a BMP file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgPNG.png", "testBlur");
      blur = new Filter(ImageProcessorModel.FilterMode.Blur, "testBlur", "blur");
      save = new Save("imagesTest/testPixelBMPBlur.bmp", "blur");

      load.run(model);
      blur.run(model);
      save.run(model);

      BufferedImage saved = model.saveImage("imagesTest/testPixelBMPBlur.bmp", "blur");
      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelBMPBlur.bmp"));

      assertTrue(equalBuffImages(image, saved));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSharpenPPM() {
    try {
      //on a PPM file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgPNG.png", "testSharp");
      sharpen = new Filter(ImageProcessorModel.FilterMode.Sharpen, "testSharp", "sharp");
      save = new Save("imagesTest/testPixelPPMSharpen.ppm", "sharp");

      load.run(model);
      sharpen.run(model);
      save.run(model);

      pixelChange =
              Files.readString(Paths.get(this.model.save("imagesTest/testPixelPPMSharpen.ppm",
                      "sharp").getAbsolutePath()));
      assertEquals(pixelChange, ppmSharpen);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSharpenPNG() {
    try {
      //on a PNG file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgBMP.bmp", "testSharp");
      sharpen = new Filter(ImageProcessorModel.FilterMode.Sharpen, "testSharp", "sharp");
      save = new Save("imagesTest/testPixelPNGSharpen.png", "sharp");

      load.run(model);
      sharpen.run(model);
      save.run(model);

      BufferedImage saved = model.saveImage("imagesTest/testPixelPNGSharpen.png", "sharp");
      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelPNGSharpen.png"));

      assertTrue(equalBuffImages(image, saved));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //  @Test
  //  public void testSharpenJPG() {
  //    try {
  //      //on a JPG file
  //      model = new ImageProcessorModel();
  //      load = new Load("res/pixelImgPNG.png", "testSharpen");
  //      sharpen = new Blur(ImageProcessorModel.FilterMode.Sharpen, "testSharpen", "sharp");
  //      save = new Save("imagesTest/testPixelJPGSharpen.jpg", "sharp");
  //
  //      load.run(model);
  //      sharpen.run(model);
  //      save.run(model);
  //
  //      BufferedImage saved = model.saveImage("imagesTest/testPixelJPGSharpen.jpg", "sharp");
  //      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelJPGSharpen.jpg"));
  //
  //      assertTrue(equalNonPPMs(image, saved));
  //
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //    }
  //  }

  @Test
  public void testSharpenBMP() {
    try {
      //on a BMP file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgPNG.png", "testSharp");
      sharpen = new Filter(ImageProcessorModel.FilterMode.Sharpen, "testSharp", "sharp");
      save = new Save("imagesTest/testPixelBMPSharpen.bmp", "sharp");

      load.run(model);
      sharpen.run(model);
      save.run(model);

      BufferedImage saved = model.saveImage("imagesTest/testPixelBMPSharpen.bmp", "sharp");
      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelBMPSharpen.bmp"));

      assertTrue(equalBuffImages(image, saved));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSepiaPPM() {
    try {
      //on a PPM file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgPNG.png", "testSepia");
      sepia = new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Sepia,
              "testSepia", "sepia");
      save = new Save("imagesTest/testPixelPPMSepia.ppm", "sepia");

      load.run(model);
      sepia.run(model);
      save.run(model);

      pixelChange =
              Files.readString(Paths.get(this.model.save("imagesTest/testPixelPPMSepia.ppm",
                      "sepia").getAbsolutePath()));
      assertEquals(pixelChange, ppmSepia);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSepiaPNG() {
    try {
      //on a PNG file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgBMP.bmp", "testSepia");
      sepia = new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Sepia,
              "testSepia", "sepia");
      save = new Save("imagesTest/testPixelPNGSepia.png", "sepia");

      load.run(model);
      sepia.run(model);
      save.run(model);

      BufferedImage saved = model.saveImage("imagesTest/testPixelPNGSepia.png", "sepia");
      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelPNGSepia.png"));

      assertTrue(equalBuffImages(image, saved));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //  @Test
  //  public void testSepiaJPG() {
  //    try {
  //      //on a JPG file
  //      model = new ImageProcessorModel();
  //      load = new Load("res/pixelImgPNG.png", "testSepia");
  //      sepia = new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Sepia,
  //      "testSepia", "sepia");
  //      save = new Save("imagesTest/testPixelJPGSepia.jpg", "sepia");
  //
  //      load.run(model);
  //      sepia.run(model);
  //      save.run(model);
  //
  //      BufferedImage saved = model.saveImage("imagesTest/testPixelJPGSepia.jpg", "sepia");
  //      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelJPGSepia.jpg"));
  //
  //      assertTrue(equalNonPPMs(image, saved));
  //
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //    }
  //  }

  @Test
  public void testSepiaBMP() {
    try {
      //on a BMP file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgPNG.png", "testSepia");
      sepia = new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Sepia,
              "testSepia", "sepia");
      save = new Save("imagesTest/testPixelBMPSepia.bmp", "sepia");

      load.run(model);
      sepia.run(model);
      save.run(model);

      BufferedImage saved = model.saveImage("imagesTest/testPixelBMPSepia.bmp", "sepia");
      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelBMPSepia.bmp"));

      assertTrue(equalBuffImages(image, saved));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGrayscaleTransformationPPM() {
    try {
      //on a PPM file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImg.ppm", "testTrans");
      grayscaleTransformation =
              new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Grayscale,
                      "testTrans", "grayscale");
      save = new Save("imagesTest/testPixelPPMGrayTransformation.ppm", "grayscale");

      load.run(model);
      grayscaleTransformation.run(model);
      save.run(model);

      pixelChange = Files.readString(Paths.get(
              this.model.save("imagesTest/testPixelPPMGrayTransformation.ppm",
                      "grayscale").getAbsolutePath()));
      assertEquals(pixelChange, ppmGreyscale);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGrayscaleTransformationPNG() {
    try {
      //on a PNG file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgBMP.bmp", "testTrans");
      grayscaleTransformation =
              new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Grayscale,
                      "testTrans", "grayscale");
      save = new Save("imagesTest/testPixelPNGGrayTransformation.png", "grayscale");

      load.run(model);
      grayscaleTransformation.run(model);
      save.run(model);

      BufferedImage saved = model.saveImage("imagesTest/testPixelPNGGrayTransformation.png",
              "grayscale");
      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelPNGGrayscaleTrans.png"));

      assertTrue(equalBuffImages(image, saved));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //  @Test
  //  public void testGrayscaleTransformationJPG() {
  //    try {
  //      //on a JPG file
  //      model = new ImageProcessorModel();
  //      load = new Load("res/pixelImgBMP.bmp", "testTrans");
  //      grayscaleTransformation =
  //              new GrayscaleTransformation(
  //              ImageProcessorModel.ColorTransformationsMode.Grayscale,
  //              "testTrans", "grayscale");
  //      save = new Save("imagesTest/testPixelJPGGrayTransformation.jpg", "grayscale");
  //
  //      load.run(model);
  //      grayscaleTransformation.run(model);
  //      save.run(model);
  //
  //      BufferedImage saved = model.saveImage("imagesTest/testPixelJPGGrayTransformation.jpg",
  //              "grayscale");
  //      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelJPGGrayscaleTrans.jpg"));
  //
  //      assertTrue(equalNonPPMs(image, saved));
  //
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //    }
  //  }

  @Test
  public void testGrayscaleTransformationBMP() {
    try {
      //on a BMP file
      model = new ImageProcessorModel();
      load = new Load("res/pixelImgPNG.png", "testTrans");
      grayscaleTransformation =
              new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Grayscale,
                      "testTrans", "grayscale");
      save = new Save("imagesTest/testPixelBMPGrayTransformation.bmp", "grayscale");

      load.run(model);
      grayscaleTransformation.run(model);
      save.run(model);

      BufferedImage saved = model.saveImage("imagesTest/testPixelBMPGrayTransformation.bmp",
              "grayscale");
      BufferedImage image = ImageIO.read(new FileInputStream("res/pixelBMPGrayscaleTrans.bmp"));

      assertTrue(equalBuffImages(image, saved));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
