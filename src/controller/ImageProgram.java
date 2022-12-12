package controller;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import model.ImageProcessorModel;
import view.GUIViewImpl;
import view.TextView;
import view.ImageView;

/**
 * This main method class runs through commands of controller.
 */
public class ImageProgram {
  /**
   * Executes commands from users. Has different functions based on given arguments (text file
   * as the Readable, interactive script-command, and GUI).
   *
   * @param args provided arguments.
   */
  public static void main(String[] args) {
    ImageProcessorModel img = new ImageProcessorModel(); // interface and class ?
    ImageView view = new TextView();
    Readable rd = new InputStreamReader(System.in);

    if (args.length > 0) {

      for (int i = 0; i < args.length; i++) {

        //reads a .txt file and runs the commands
        if (args[i].equals("-file")) {
          try {
            rd = new FileReader(args[i + 1]);

            ImageProcessorController contr = new ImageProcessorControllerImpl(img, view, rd);
            contr.runApp();
            return;
          } catch (IOException e) {
            System.out.println("Please input a valid file.");
          }
        }

        //script-command
        if (args[i].equals("-text")) {
          ImageProcessorController contr = new ImageProcessorControllerImpl(img, view, rd);
          contr.runApp();
        }

        //prints a message if the arguments are not applicable
        if (!args[i].equals("-file") && !args[i].equals("-text")) {
          System.out.println("The given arguments are not valid. Please try again.");
          return;
        }
      }

    } else {
      //call the GUI application if no arguments are called
      new GUIViewImpl(img).setVisible(true);
    }
  }
}


