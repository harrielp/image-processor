import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

import model.ImageProcessorModel;
import view.ImageView;
import view.TextView;

/**
 * tests for the View.
 */
public class ViewTest {

  // test constructor of TextBasedView
  // null model
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor1() {
    new TextView(null);
  }

  // test constructor of TextBasedView
  // null appendable
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor2() {
    new TextView(new ImageProcessorModel(), null);
  }

  // test renderMessage method
  @Test
  public void testRenderMessage() {
    Appendable out = new StringBuilder();
    ImageProcessorModel model = new ImageProcessorModel();
    ImageView view = new TextView(model, out);
    try {
      view.renderMessage("hi");
    } catch (IOException e) {
      throw new IllegalArgumentException("Error!");
    }
    assertEquals(out.toString(), "hi\n");
  }
}



