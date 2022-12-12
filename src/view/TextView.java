package view;

import java.io.IOException;

import model.ImageProcessorModel;

/**
 * View class for ImageProcessor to give feedback to users on what commands they have.
 */
public class TextView implements ImageView {

  private Appendable destination;
  private ImageProcessorModel modelView;

  /**
   * It constructs a text based view with provided inputs.
   */
  public TextView() {
    destination = System.out;
  }

  /**
   * It constructs a text based view according to the given model and appendable output.
   * It uses a default output which is the system.out.
   *
   * @param model the model.
   * @throws IllegalArgumentException if model is null.
   */
  public TextView(ImageProcessorModel model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model is null");
    }
    modelView = model;
    destination = System.out;
  }

  /**
   * It constructs a text based view according to the given model and appendable output.
   *
   * @param model      the model.
   * @param appendable the output source.
   * @throws IllegalArgumentException if either model or appendable is null.
   */
  public TextView(ImageProcessorModel model, Appendable appendable)
          throws IllegalArgumentException {
    if (model == null || appendable == null) {
      throw new IllegalArgumentException("Model or Appendable is null");
    }
    this.destination = appendable;
    this.modelView = model;
  }


  /**
   * Render the message to console.
   *
   * @param message the message.
   * @throws IOException if the message is invalid.
   */
  @Override
  public void renderMessage(String message) throws IOException {
    destination.append(message + System.lineSeparator());
  }

}
