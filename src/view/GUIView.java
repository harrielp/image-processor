package view;

import java.awt.image.BufferedImage;

import controller.ButtonCommands;
import controller.commands.Load;

/**
 * Represents a non-text view that visually represents our application through GUI. Contains
 * methods that will render an image, refresh a page, render a message?, and attach actions
 * to different buttons representing our commands.
 */
public interface GUIView {
  /**
   * A getter method to retrieve the Load command applied whenever a new image is loaded onto
   * the GUI panel.
   *
   * @return a type Load.
   */
  Load getNewLoadCommand();

  /**
   * A setter method to set a new Load command applied whenever a new image is loaded onto
   * the GUI panel.
   *
   * @param cmd the ImageProcessingCommand to set the field to.
   */
  void setNewLoadCommand(Load cmd);

  /**
   * Renders an image to the GUI panel.
   *
   * @param img of type BufferedImage, the image to be displayed on the screen.
   */
  void renderImage(BufferedImage img);

  /**
   * Renders a message to the GUI panel, whether it be errors or clarification.
   *
   * @param msg a String to display to the user.
   */
  void renderMessage(String msg);

  /**
   * Applies the 14 different image processor commands to their corresponding buttons in the
   * GUI panel. Allows the buttons to trigger change to the displayed image.
   *
   * @param commands the different commands as specified in the GUIController.
   */
  void applyActionEventsToButtons(ButtonCommands commands);
}
