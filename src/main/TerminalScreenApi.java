package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;

/**
 * This class serves as an API between a terminal screen and Java.
 * 
 * @author Neufeld-Martin
 */
public abstract class TerminalScreenApi {
  /** 
   * Starts a ProcessBuilder and returns the resulting process. If the process
   * has a non-zero exit value, an error will be thrown.
   * 
   * @param processBuilder The ProcessBuilder to start.
   * @return The resulting process.
   */
  public static Process startProcessBuilder(ProcessBuilder processBuilder) {
    Process process = null;

    try {
      process = processBuilder.start();

      if (process.waitFor() != 0) {
        throw new Error(String.format(
            "status=%d, cause=%s",
            process.exitValue(),
            processBuilder.command().toString()
        ));
      }
    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    return process;
  }

  /** 
   * Executes a bash command. If the exit status of the bash command is
   * non-zero, an error will be thrown.
   * 
   * @param command The command to execute.
   * @return The read output.
   * @see #startProcessBuilder(ProcessBuilder)
   */
  public static void exec(String command) {
    startProcessBuilder(
        new ProcessBuilder("bash", "-c", command)
        .redirectOutput(Redirect.INHERIT)
    );
  }

  /** 
   * Executes a bash command and reads its output. If the exit status of the
   * bash command is non-zero, an error will be thrown.
   * 
   * @param command The command to execute.
   * @return The read output.
   * @see #startProcessBuilder(ProcessBuilder)
   */
  public static String execAndReadOutput(String command) {
    InputStream processOutput = 
        startProcessBuilder(new ProcessBuilder("bash", "-c", command))
        .getInputStream()
    ;

    try {
      return new BufferedReader(new InputStreamReader(processOutput)).readLine();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    return null;
  }

  /**
   * Sets the cursor position.
   * 
   * @param line Destination line.
   * @param column Destination column.
   */
  public static void setCurorPosition(int line, int column) {
    exec(String.format("tput cup %d %d", line, column));
  }

  /** Returns the amount of lines currently visible on the screen. */
  public static int getLines() {
    return Integer.parseInt(execAndReadOutput("tput lines"));
  }

  /** Returns the amount of columns currently visible on the screen. */
  public static int getColumns() {
    return Integer.parseInt(execAndReadOutput("tput cols"));
  }

  /** Clears the screen. */
  public static void clearScreen() {
    exec("tput clear");
  }

  /** Clears the screen from the cursor to the end of the screen. */
  public static void clearScreenFromCursorToEnd() {
    exec("tput ed");
  }
}
