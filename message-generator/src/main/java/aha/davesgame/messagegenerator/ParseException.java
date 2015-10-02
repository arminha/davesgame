package aha.davesgame.messagegenerator;

/**
 * Indicates that a message definition file cannot be parsed.
 */
public class ParseException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a {@linkplain ParseException} with the specified detail message.
   * 
   * @param message
   *          the detail message
   */
  public ParseException(String message) {
    super(message);
  }

}
