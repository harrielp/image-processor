package controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import controller.commands.BrightenDarken;
import controller.commands.Downscale;
import controller.commands.Filter;
import controller.commands.Flip;
import controller.commands.Grayscale;
import controller.commands.ColorTransformation;
import controller.commands.ImageProcessingCommand;
import controller.commands.Load;
import controller.commands.Save;
import model.ImageProcessorModel;
import view.ImageView;

import model.ImageProcessorModel.FlipMode;
import model.ImageProcessorModel.GrayscaleMode;

/**
 * Controller for the image processor which
 * reads text inputs from Readable and writes string outputs.
 * Gives user a way to interact with the model.
 */
public class ImageProcessorControllerImpl implements ImageProcessorController {

  private final Readable rd;
  private final ImageProcessorModel model;
  private final ImageView view;
  private Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands;

  /**
   * The controller constructor that take in a model and view.
   * The input readable is in default the System.in.
   *
   * @param model the model
   * @param view  the view
   */
  public ImageProcessorControllerImpl(ImageProcessorModel model, ImageView view) {
    this(model, view, new InputStreamReader(System.in));
  }

  /**
   * The controller constructor that take in a model and view and a readable.
   * Init all commands.
   *
   * @param model the model
   * @param view  the view
   * @param rd    readable input
   * @throws IllegalArgumentException when either argument is null
   */
  public ImageProcessorControllerImpl(ImageProcessorModel model, ImageView view,
                                      Readable rd) throws IllegalArgumentException {
    if (model == null || view == null | rd == null) {
      throw new IllegalArgumentException("Invalid model, view or input");
    }
    this.model = model;
    this.view = view;
    this.rd = rd;
    knownCommands = new HashMap<>();
    knownCommands.put("brighten", s -> new BrightenDarken(s.nextInt(), s.next(), s.next()));
    knownCommands.put("darken", s -> new BrightenDarken(s.nextInt() * -1,
            s.next(), s.next()));
    knownCommands.put("flip-horizontal", s -> new Flip(FlipMode.Horizontal, s.next(), s.next()));
    knownCommands.put("flip-vertical", s -> new Flip(FlipMode.Vertical, s.next(), s.next()));
    knownCommands.put("greyscale-red", s -> new Grayscale(GrayscaleMode.Red,
            s.next(), s.next()));
    knownCommands.put("greyscale-green", s -> new Grayscale(GrayscaleMode.Green,
            s.next(), s.next()));
    knownCommands.put("greyscale-blue", s -> new Grayscale(GrayscaleMode.Blue,
            s.next(), s.next()));
    knownCommands.put("save", s -> new Save(s.next(), s.next()));
    knownCommands.put("load", s -> new Load(s.next(), s.next()));
    knownCommands.put("luma", s -> new Grayscale(GrayscaleMode.Luma, s.next(), s.next()));
    knownCommands.put("value", s -> new Grayscale(GrayscaleMode.Value, s.next(), s.next()));
    knownCommands.put("intensity", s -> new Grayscale(GrayscaleMode.Intensity, s.next(), s.next()));
    knownCommands.put("blur", s -> new Filter(ImageProcessorModel.FilterMode.Blur,
            s.next(), s.next()));
    knownCommands.put("sharpen", s -> new Filter(ImageProcessorModel.FilterMode.Sharpen,
            s.next(), s.next()));
    knownCommands.put("grayscale-transformation", s ->
            new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Grayscale,
            s.next(), s.next()));
    knownCommands.put("sepia", s ->
            new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Sepia,
            s.next(), s.next()));
    knownCommands.put("downscale", s -> new Downscale(s.nextInt(),
            s.nextInt(), s.next(), s.next()));
  }

  /**
   * Start running the text-based controller.
   *
   * @throws IllegalStateException if command is invalid.
   */
  @Override
  public void runApp() throws IllegalArgumentException {
    Scanner scan = new Scanner(this.rd);

    try {
      this.view.renderMessage("Please enter a command!");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    while (scan.hasNext()) {
      String inputCommand = scan.next();

      if (inputCommand.equalsIgnoreCase("q")) {
        return;
      }

      if (inputCommand.equalsIgnoreCase("save")) {
        try {
          this.view.renderMessage("Successfully saved the file to the desired directory :)");
        } catch (IOException e) {
          throw new IllegalArgumentException("Unable to transmit");
        }
      }

      Function<Scanner, ImageProcessingCommand> c = knownCommands.getOrDefault(inputCommand, null);
      if (c == null) {
        throw new IllegalArgumentException();
      } else {
        ImageProcessingCommand com = c.apply(scan);
        com.run(model);
      }
    }
  }
}