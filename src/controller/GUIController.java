package controller;

import controller.commands.BrightenDarken;
import controller.commands.ColorTransformation;
import controller.commands.Downscale;
import controller.commands.Filter;
import controller.commands.Flip;
import controller.commands.Grayscale;
import controller.commands.ImageProcessingCommand;
import controller.commands.Save;
import model.ImageProcessorModel;
import view.GUIView;
import view.GUIViewImpl;

/**
 * A controller for the GUI application of our program. Allows our previous methods
 * for image modifications to be used on the GUI implementation. Takes in a model and a view.
 */
public class GUIController implements ButtonCommands {
  private ImageProcessorModel model;
  private GUIView view;
  private ImageProcessingCommand command;
  private String typeOfFile;

  /**
   * A constructor that initializes the type of file to be saved, the model, and the view.
   *
   * @param model of type ImageProcessorModel.
   * @param view  of type GUIView.
   * @throws IllegalArgumentException when the model or view is null.
   */
  public GUIController(ImageProcessorModel model, GUIView view) throws IllegalArgumentException {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model or view cannot be null.");
    }
    this.model = model;
    this.view = view;
    //gets the type of file from whichever photo the user wants to utilize
    this.typeOfFile = this.view.getNewLoadCommand().typeOfFile();
  }

  /**
   * A method called to apply action events to the command buttons created in the GUIView.
   * Allows the user to make changes to the loaded image through buttons.
   */
  public void runGUI() {
    this.view.applyActionEventsToButtons(this);
  }

  @Override
  public void save() {
    this.command = new Save("modifiedImage." + typeOfFile, "name");
    this.command.run(this.model);
    this.view.renderMessage("A new file, \"modifiedImage\", was saved to the current directory.");
  }

  @Override
  public void downscale() {
    this.command = new Downscale(model.saveImage("", "name").getHeight() / 3,
            model.saveImage("", "name").getWidth() / 2, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void brighten() {
    this.command = new BrightenDarken(30, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void darken() {
    this.command = new BrightenDarken(-30, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void flipHori() {
    this.command = new Flip(ImageProcessorModel.FlipMode.Horizontal, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void flipVert() {
    this.command = new Flip(ImageProcessorModel.FlipMode.Vertical, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void redComponent() {
    this.command = new Grayscale(ImageProcessorModel.GrayscaleMode.Red, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void greenComponent() {
    this.command = new Grayscale(ImageProcessorModel.GrayscaleMode.Green, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void blueComponent() {
    this.command = new Grayscale(ImageProcessorModel.GrayscaleMode.Blue, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void lumaGrayscale() {
    this.command = new Grayscale(ImageProcessorModel.GrayscaleMode.Luma, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void valueGrayscale() {
    this.command = new Grayscale(ImageProcessorModel.GrayscaleMode.Value, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void intensityGrayscale() {
    this.command = new Grayscale(ImageProcessorModel.GrayscaleMode.Intensity, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void blur() {
    this.command = new Filter(ImageProcessorModel.FilterMode.Blur, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void sharpen() {
    this.command = new Filter(ImageProcessorModel.FilterMode.Sharpen, "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void sepia() {
    this.command = new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Sepia,
            "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void grayscaleColorTrans() {
    this.command = new ColorTransformation(ImageProcessorModel.ColorTransformationsMode.Grayscale,
            "name", "name");
    this.command.run(this.model);
    this.view.renderImage(model.saveImage("", "name"));
  }

  @Override
  public void restart() {
    new GUIViewImpl(this.model).setVisible(true);
  }
}
