package main.terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Convenience class for executing bash commands.
 * 
 * @author Neufeld-Martin
 */
public class Bash {
  /**
   * Composes a {@link java.lang.ProcessBuilder ProcessBuilder}
   * for executing a bash command.
   * 
   * @param command Bash command.
   * @return The {@code ProcessBuilder}.
   */
  private static ProcessBuilder getProcessBuilder(String command) {
    return new ProcessBuilder("bash", "-c", command);
  }

  /** 
   * Starts a {@link java.lang.ProcessBuilder ProcessBuilder}.
   * Any occuring {@link java.lang.Exception Exception}
   * will cause the whole programm to be shut down.
   * 
   * @param processBuilder The {@code ProcessBuilder} to start.
   * @return The resulting {@code Process}.
   * @throws Error on a non-zero exit value.
   */
  private static Process start(ProcessBuilder processBuilder) {
    Process process = null;

    try {
      process = processBuilder.start();
      if (process.waitFor() != 0) {
        throw new Error(
            String.format(
                "status=%d, cause=%s",
                process.exitValue(),
                processBuilder.command().toString()
            )
        );
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }

    return process;
  }

  /**
   * Executes a bash command. 
   * I/O are the same as those of the current Java process.
   * 
   * @param command Bash command to execute.
   * @see java.lang.ProcessBuilder#inheritIO()
   */
  public static void exec(String command) {
    start(getProcessBuilder(command).inheritIO());
  }

  /**
   * Executes a bash command.
   * I/O are piped to those of the current Java process.
   * 
   * @param command Bash command to execute.
   * @return Lines of {@code stdout}.
   * @see java.lang.ProcessBuilder.Redirect#PIPE
   */
  public static List<String> execAndReadStdout(String command) {
    InputStream stdout = start(getProcessBuilder(command)).getInputStream();
    List<String> result = new ArrayList<>();

    try (
      BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
    ) {
      String line;
      while ((line = stdoutReader.readLine()) != null) {
        result.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }
}
