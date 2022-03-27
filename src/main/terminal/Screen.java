package main.terminal;

/**
 * Convenience class for manipulating the terminal screen.
 * 
 * @author Neufeld-Martin
 */
public class Screen {
  /**
   * Sets the cursor position.
   * 
   * @param line Destination line.
   * @param column Destination column.
   */
  public static void setCurorPosition(int line, int column) {
    Bash.exec(String.format("tput cup %d %d", line, column));
  }

  /** Returns the amount of lines currently visible on the screen. */
  public static int getLines() {
    return Integer.parseInt(Bash.execAndReadStdout("tput lines").get(0));
  }

  /** Returns the amount of columns currently visible on the screen. */
  public static int getColumns() {
    return Integer.parseInt(Bash.execAndReadStdout("tput cols").get(0));
  }

  /** Clears the screen. */
  public static void clear() {
    Bash.exec("tput clear");
  }

  /** Clears the screen from the cursor to the end of the screen. */
  public static void clearFromCursorToEnd() {
    Bash.exec("tput ed");
  }
}
