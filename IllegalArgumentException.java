///////////////////////////////////////////////////////////////////////////////
//
// Title: IllegalArgumentException
// Files: IllegalArgumentException.java
//
// Author: Finn Van Order
// Email: finnvanorder@gmail.com
//
///////////////////////////////////////////////////////////////////////////////

/**
 * Checked exception thrown when a user attempts to insert or get a null key.
 */
@SuppressWarnings("serial")
class IllegalArgumentException extends Exception {
  public IllegalArgumentException(String s) {
    super(s);
  }
}
