/* Generated By:JavaCC: Do not edit this line. TokenMgrError.java Version 5.0 */
/* JavaCCOptions: */
package org.basex.query.regex.parse;

/** Token Manager Error. */
public class TokenMgrError extends Error {
  /**
   * The version identifier for this Serializable class.
   * Increment only if the <i>serialized</i> form of the
   * class changes.
   */
  private static final long serialVersionUID = 1L;

  /*
   * Ordinals for various reasons why an Error of this type can be thrown.
   */

  /**
   * Lexical error occurred.
   */
  static final int LEXICAL_ERROR = 0;

  /**
   * An attempt was made to create a second instance of a static token manager.
   */
  static final int STATIC_LEXER_ERROR = 1;

  /**
   * Tried to change to an invalid lexical state.
   */
  static final int INVALID_LEXICAL_STATE = 2;

  /**
   * Detected (and bailed out of) an infinite loop in the token manager.
   */
  static final int LOOP_DETECTED = 3;

  /**
   * Indicates the reason why the exception is thrown. It will have
   * one of the above 4 values.
   */
  int errorCode;

  /**
   * Replaces unprintable characters by their escaped (or unicode escaped)
   * equivalents in the given string.
   * @param str string to be escaped
   * @return escaped string
   */
  protected static String addEscapes(final String str) {
    final StringBuilder retval = new StringBuilder();
    for (int i = 0; i < str.length(); i++) {
      char ch;
      switch (str.charAt(i)) {
        case 0 :
          continue;
        case '\b':
          retval.append("\\b");
          continue;
        case '\t':
          retval.append("\\t");
          continue;
        case '\n':
          retval.append("\\n");
          continue;
        case '\f':
          retval.append("\\f");
          continue;
        case '\r':
          retval.append("\\r");
          continue;
        case '\"':
          retval.append("\\\"");
          continue;
        case '\'':
          retval.append("\\\'");
          continue;
        case '\\':
          retval.append("\\\\");
          continue;
        default:
          if((ch = str.charAt(i)) < 0x20 || ch > 0x7e) {
            final String s = "0000" + Integer.toString(ch, 16);
            retval.append("\\u" + s.substring(s.length() - 4, s.length()));
          } else {
            retval.append(ch);
          }
          continue;
      }
    }
    return retval.toString();
  }

  /**
   * Returns a detailed message for the Error when it is thrown by the
   * token manager to indicate a lexical error.
   * Note: You can customize the lexical error message by modifying this method.
   * @param eof   indicates if EOF caused the lexical error
   * @param state lexical state in which this error occurred
   * @param line  line number when the error occurred
   * @param col   column number when the error occurred
   * @param after prefix that was seen before this error occurred
   * @param curr  the offending character
   * @return error message
   */
  protected static String lexicalError(final boolean eof, final int state, final int line,
      final int col, final String after, final char curr) {
    return "Lexical error at line " + line + ", column " + col + ".  Encountered: " +
      (eof ? "<EOF> " : '"' + addEscapes(String.valueOf(curr)) + '"' +
        " (" + curr + "), ") + "after : \"" + addEscapes(after) + '"';
  }

  /**
   * You can also modify the body of this method to customize your error messages.
   * For example, cases like LOOP_DETECTED and INVALID_LEXICAL_STATE are not
   * of end-users concern, so you can return something like :
   *
   *     "Internal Error : Please file a bug report .... "
   *
   * from this method for such cases in the release version of your parser.
   */
  @Override
  public String getMessage() {
    return super.getMessage();
  }

  /*
   * Constructors of various flavors follow.
   */

  /** No arg constructor. */
  public TokenMgrError() {
  }

  /**
   * Constructor with message and reason.
   * @param message error message
   * @param reason one of {@link #LEXICAL_ERROR}, {@link #INVALID_LEXICAL_STATE},
   *   {@link #STATIC_LEXER_ERROR}, {@link #LOOP_DETECTED}
   */
  public TokenMgrError(final String message, final int reason) {
    super(message);
    errorCode = reason;
  }

  /**
   * Full Constructor.
   * @param eof end of file seen?
   * @param state lexer state
   * @param line error line
   * @param col error column
   * @param after string after which the error occured
   * @param curr current character
   * @param reason one of {@link #LEXICAL_ERROR}, {@link #INVALID_LEXICAL_STATE},
   *   {@link #STATIC_LEXER_ERROR}, {@link #LOOP_DETECTED}
   */
  public TokenMgrError(final boolean eof, final int state, final int line, final int col,
      final String after, final char curr, final int reason) {
    this(lexicalError(eof, state, line, col, after, curr), reason);
  }
}
/* JavaCC - OriginalChecksum=ba630fd1bc74645b6bed17f33f702196 (do not edit this line) */
