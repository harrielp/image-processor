package view;

import java.io.IOException;

/**
 * The view prints out the messages to user.
 */
public interface ImageView {

  /**
   * Render the message to console.
   *
   * @param message the given message.
   * @throws IOException if the message is invalid.
   */
  void renderMessage(String message) throws IOException;

}
