package model;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Interface for ImageProcessorModel, representing the methods for the model. Used to run
 * each specific user command.
 */
public interface IImageProcessorModel {

  /**
   * Brightens or darkens a given image by adding a specific amount to each RGB component of
   * each pixel. Creates a copy of the modified image by traversing through the original image
   * and stores it into a HashMap, representing the stored images the user has loaded and modified,
   * with the parameter modifiedImage as the key and the copy of the image as its value.
   *
   * @param increment     positive integer to be added or subtracted from the RGB components.
   * @param name          of image the user wants to modify.
   * @param modifiedImage name of the new image that has been modified.
   */
  void brighten(int increment, String name, String modifiedImage);

  /**
   * Flip a given image either horizontally or vertically based on the given FlipMode enum.
   * Creates a copy of the modified image by traversing through the original image and stores it
   * into a HashMap, representing the stored images the user has loaded and modified, with the
   * parameter modifiedImage as the key and the copy of the image as its value.
   *
   * @param flip          enum representing horizontal and vertical.
   * @param name          of image the user wants to modify.
   * @param modifiedImage name of the new image that has been modified.
   */
  void flip(ImageProcessorModel.FlipMode flip, String name, String modifiedImage);

  /**
   * Performs a grayscale on an image, based on the given GrayscaleMode enum, by matching all
   * RGB components with either each pixel's red, green, or blue component value. Creates a copy of
   * the modified image by traversing through the original image and stores it into a HashMap,
   * representing the stored images the user has loaded and modified, with the parameter
   * modifiedImage as the key and the copy of the image as its value.
   *
   * @param grayscale     enum representing the 3 RGB variations.
   * @param name          of image the user wants to modify.
   * @param modifiedImage name of the new image that has been modified.
   */
  void grayscale(ImageProcessorModel.GrayscaleMode grayscale, String name, String modifiedImage);

  /**
   * Loads the desired image to an ASCII PPM file by converting the PPM file's text to individual
   * RGB pixels, and creating an Image (catering to our model representation). Stores this created
   * image into a HashMap, representing the stored images the user will load and modify, with the
   * parameter newFileName as the key and the image as the value.
   *
   * @param pathName    the source path of the file the user wants to work on.
   * @param newFileName the name the user wants to give to the image.
   * @throws IllegalArgumentException when the given file is not a PPM file.
   */
  void load(String pathName, String newFileName) throws IllegalArgumentException;

  /**
   * Loads the desired image of any format by converting the given BufferedImage (initialized in
   * the Load model.commands class) to individual RGB pixels, and creating an Image (catering to
   * our model representation). Stores this created image into a HashMap, representing the stored
   * images the user will load and modify, with the parameter newFileName as the key and the image
   * as the value.
   *
   * @param image       the BufferedImage the user wants to work on.
   * @param newFileName the name the user wants to give to the image.
   * @throws IllegalArgumentException when the image is null.
   */
  void load(BufferedImage image, String newFileName) throws IllegalArgumentException;

  /**
   * Saves the desired image (found through the inputted source path) to an ASCII PPM file by
   * traversing through the text of the modified ppm file and returning a new PPM file with the
   * same text.
   *
   * @param pathName the name of the file path the user wants to save.
   * @param name     the name of the file that the user wants to save.
   * @return File a new File (a duplicate of the most recent image modification) in the path
   *         name that the user inputs.
   * @throws IllegalArgumentException if invalid image file.
   */
  File save(String pathName, String name) throws IllegalArgumentException;

  /**
   * Saves the modified image to an image of any format by converting the image, found through the
   * inputted source path, to a BufferedImage. Does this by taking the pixels of the modified image
   * and setting its RGB values to the new BufferedImage.
   *
   * @param pathName the source file the user wants to save the file to.
   * @param name     the name of the file the user want to save.
   * @return a new BufferedImage that is a duplicate of the most recent image modification.
   */
  BufferedImage saveImage(String pathName, String name);

  /**
   * Applies a blur or sharpen filter onto an image based on the given FilterMode enum. A set
   * matrix, representing the filter that will be applied to each pixel, is multiplied to each
   * individual RGB pixel. Creates a copy of the modified image by traversing through the original
   * image and stores it into a HashMap, representing the stored images the user has loaded and
   * modified, with the parameter modifiedImage as the key and the copy of the image as its value.
   *
   * @param filter        an enum representing the two filter transformations.
   * @param name          of image.
   * @param modifiedImage image name after applying the filter.
   */
  void filter(ImageProcessorModel.FilterMode filter, String name, String modifiedImage);

  /**
   * Applies a sepia or grayscale color transformation based on the given ColorTransformationsMode
   * enum. A set matrix, representing the filter that will be applied to each pixel, is multiplied
   * to each individual RGB pixel. Creates a copy of the modified image by traversing through the
   * original image and stores it into a HashMap, representing the stored images the user has
   * loaded and modified, with the parameter modifiedImage as the key and the copy of the image as
   * its value.
   *
   * @param colorTransform an enum representing the two color transformations.
   * @param name           of image.
   * @param modifiedImage  image name after modifying it.
   */
  void colorTransformations(ImageProcessorModel.ColorTransformationsMode colorTransform,
                            String name, String modifiedImage);

  /**
   * Downscales an image based on width and height.
   *
   * @param heightNew     desired height for image.
   * @param widthNew      desired width for image.
   * @param name          of image.
   * @param modifiedImage new smaller image.
   */
  void downscale(int heightNew, int widthNew, String name, String modifiedImage);

  /**
   * Method to find the image of pixels through the storedImages HashMap.
   *
   * @param name of image that methods want to find.
   * @return the found image.
   * @throws IllegalArgumentException if the image cannot be found.
   */
  ImageInterface findImage(String name) throws IllegalArgumentException;
}
