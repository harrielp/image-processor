package view;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JComponent;

import model.ImageProcessorModel;

/**
 * Represents the histogram graphics for the loaded image. Overrides JComponent's paintComponent
 * method to draw a histogram based on the component values of the user's chosen image.
 */
public class HistogramGraphics extends JComponent {
  private ImageProcessorModel model;

  /**
   * A constructor that takes in a model of type ImageProcessorModel and sets the size of
   * the histogram.
   *
   * @param model of type ImageProcessorModel that initializes the histogram values
   * @throws IllegalArgumentException when the model is null
   */
  public HistogramGraphics(ImageProcessorModel model) throws IllegalArgumentException {
    setPreferredSize(new Dimension(820, 300));
    this.model = model;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponents(g);

    //gets width and height of this class's dimensions
    int width = getWidth();
    int height = getHeight();

    //draws the x-axis
    g.drawLine(30, height - 45, width - 30, height - 45);

    int[] rVals = this.model.initializeHistogramValues(ImageProcessorModel.Component.Red);
    int[] gVals = this.model.initializeHistogramValues(ImageProcessorModel.Component.Green);
    int[] bVals = this.model.initializeHistogramValues(ImageProcessorModel.Component.Blue);
    int[] intensityVals =
            this.model.initializeHistogramValues(ImageProcessorModel.Component.Intensity);

    //draws each bar set
    paintHelper(rVals, g, Color.RED);
    paintHelper(gVals, g, Color.GREEN);
    paintHelper(bVals, g, Color.BLUE);
    paintHelper(intensityVals, g, Color.GRAY);
  }

  /**
   * A helper method to draw the bars for each set (R, G, B, Intensity).
   *
   * @param values an array of values to represent in the histogram.
   * @param g the Graphics, to be passed in through the paintComponent class.
   * @param color the Color of the bars.
   */
  private void paintHelper(int[] values, Graphics g, Color color) {
    g.setColor(color);
    int height = getHeight();
    int maxVal = 0;

    //gets the max value of the values
    for (int i = 0; i < values.length; i++) {
      if (maxVal < values[i]) {
        maxVal = values[i];
      }
    }

    //draws the bars and histogram title
    for (int i = 0; i < values.length; i++) {
      int barHeight = (int) (((double) values[i] / (double) maxVal) * (height - 55));

      g.drawRect(30 + i * 3, height - 45 - barHeight, 3, barHeight);

      g.drawString("R, G, B, and Intensity", 350, height - 30);
    }
  }
}
