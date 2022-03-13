package main;

import java.util.List;
import javafx.util.Pair;

/** 
 * This class serves the purpose of printing information about a Hangman game.
 * 
 * @author Neufeld-Martin
 */
public class Printer {
  /** The game this printer serves. */
  private Game game;

  /** Sets {@link #game the game this printer serves}. */
  private void setGame(Game game) {
    this.game = game;
  }

  /** Returns {@link #game the game this printer serves}. */
  private Game getGame() {
    return this.game;
  }

  /**
   * A two-dimensional list that defines which character is when printed when
   * drawing the hanged man. The information is stored in form of Key-Value
   * {@link javafx.util.Pair Pairs} {@code Pair<Integer I, String S>} where
   * {@code I} is the amount of wrong guesses to match or exceed and {@code S}
   * the part of the hanged man to print.
   */
  private List<List<Pair<Integer, String>>> drawingGrid;

  /** Sets {@link #drawingGrid the drawing grid}. */
  private void setDrawingGrid(List<List<Pair<Integer, String>>> drawingGrid) {
    this.drawingGrid = drawingGrid;
  }

  /** Returns {@link #drawingGrid the drawing grid}. */
  private List<List<Pair<Integer, String>>> getDrawingGrid() {
    return this.drawingGrid;
  }

  /**
   * Constructs an instance of this class.
   * 
   * @param game {@link #game the game this printer serves}.
   */
  public Printer(Game game) {
    setGame(game);
    setDrawingGrid(readDrawingGrid());
  }

  /**
   * TODO: read from file.
   * 
   * Composes a {@link #drawingGrid drawing grid}.
   */
  private List<List<Pair<Integer, String>>> readDrawingGrid() {
    return List.of(
        List.of(
            new Pair<>(4, "+"), new Pair<>(5, "-"), new Pair<>(5, "-"),
            new Pair<>(6, "-"), new Pair<>(6, "-"), new Pair<>(7, "+")
        ),
        List.of(
            new Pair<>(4, "|"), new Pair<>(7, " "), new Pair<>(7, " "),
            new Pair<>(7, " "), new Pair<>(7, " "), new Pair<>(7, "|")
        ),
        List.of(
            new Pair<>(3, "|"), new Pair<>(8, " "), new Pair<>(8, " "),
            new Pair<>(8, " "), new Pair<>(8, " "), new Pair<>(8, "O")
        ),
        List.of(
            new Pair<>(3, "|"), new Pair<>(8, " "), new Pair<>(8, " "),
            new Pair<>(8, " "), new Pair<>(8, "-"), new Pair<>(8, "+-")
        ),
        List.of(
            new Pair<>(2, "|"), new Pair<>(9, " "), new Pair<>(9, " "),
            new Pair<>(9, " "), new Pair<>(9, " "), new Pair<>(9, "|")
        ),
        List.of(
            new Pair<>(2, "|"), new Pair<>(9, " "), new Pair<>(9, " "),
            new Pair<>(9, " "), new Pair<>(9, "/"), new Pair<>(9, " \\")
        ),
        List.of(
            new Pair<>(1, "|")
        ),
        List.of(
            new Pair<>(1, "=")
        )
    );
  }

  /** Initializes the screen for playing. */
  public void initializeScreen() {
    TerminalScreenApi.clearScreen();
    printHangedMan();
    System.out.println();
    printStats();
    System.out.println();
  }

  /** Updates the information displayed on the screen. */
  public void updateScreen() {
    TerminalScreenApi.clearScreen();
    printHangedMan();
    System.out.println();
    printStats();
    System.out.println();
  }

  /** Prints the hanged man. */
  public void printHangedMan() {
    for (int i = 0; i < getDrawingGrid().size(); i++) {
      List<Pair<Integer, String>> currentRow = getDrawingGrid().get(i);

      for (int j = 0; j < currentRow.size(); j++) {
        Pair<Integer, String> currentPair = currentRow.get(j);

        if (getGame().getWrongGuesses() >= currentPair.getKey()) {
          try {
            Thread.sleep(150L);
          } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
          }
          System.out.print(currentPair.getValue());
        }
      }

      System.out.println();
    }
  }

  /** Prints a horizontal separator line. */
  public void printSeparator() {
    System.out.printf("%n# %s #%n%n", "-".repeat(96));
  }

  /** Prints information about the current round. */
  public void printStats() {
    printPrettyPairOverview(
        new Pair<>(
            "Previously guessed characters",
            getGame().getGuessedCharsReadOnly().toString()
        ),
        new Pair<>(
            "Previously guessed words",
            getGame().getGuessedWordsReadOnly().toString()
        ),
        new Pair<>(
            "Wrong guesses",
            new StringBuilder()
                .append(getGame().getWrongGuesses())
                .append(" (")
                .append(getGame().getMaxGuesses())
                .append(")")
                .toString()
        ),
        new Pair<>(
            "Word to guess",
            new StringBuilder()
                .append(getGame().getGuessStatus().replaceAll("\\B", " "))
                .append(" (")
                .append(getGame().getWordToGuess().length())
                .append(")")
                .toString()
        )
    );
  }

  /**
   * Prints {@link javafx.util.Pair key-value pairs} in a formatted overview.
   * 
   * @param pairs The key-value pairs to print.
   */
  @SafeVarargs
  private void printPrettyPairOverview(Pair<String, String>... pairs) {
    int greatestKeyLength = 0;

    for (Pair<String, String> pair : pairs) {
      int curKeyLength = pair.getKey().length();

      if (curKeyLength > greatestKeyLength) {
        greatestKeyLength = curKeyLength;
      }
    }

    String format = new StringBuilder()
        .append("%-")
        .append(greatestKeyLength)
        .append("s : %s%n")
        .toString()
    ;

    for (Pair<String, String> pair : pairs) {
      System.out.printf(format, pair.getKey(), pair.getValue());
    }
  }
}
