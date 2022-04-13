package main.hangman;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.util.Pair;
import main.terminal.Screen;

/**
 * The frontend of the game.
 * 
 * @author Neufeld-Martin
 * @see main.hangman.Backend
 */
public class Frontend {
  /** Reader for command-line input of the user. */
  private Scanner userInputReader;

  /** The backend of the game. */
  private Backend backend;

  // ############################################################################################ //

  public Frontend(Scanner userInputReader) {
    setUserInputReader(userInputReader);
  }

  // ############################################################################################ //

  /** Sets the {@link #userInputReader reader for command-line input of the user}. */
  private void setUserInputReader(Scanner userInputReader) {
    this.userInputReader = userInputReader;
  }

  /** Returns the {@link #userInputReader reader for command-line input of the user}. */
  public Scanner getUserInputReader() {
    return this.userInputReader;
  }

  /** Sets the {@link #backend backend of the game}. */
  private void setBackend(Backend backend) {
    this.backend = backend;
  }

  /** Returns the {@link #backend backend of the game}. */
  public Backend getBackend() {
    return this.backend;
  }

  // ############################################################################################ //

  /** TODO: add description. */
  public void run() {
    Screen.clear();
    setBackend(new Backend("default".toUpperCase(), "[A-Z]", 8, determinePlayers()));
    play();
  }

  /** TODO: add description. */
  private List<String> determinePlayers() {
    List<String> players = new ArrayList<>();
    for (int i = 0; i < 2; i++ ) {
      System.out.printf("Player %d, please enter your name: ", i);
      players.add(getUserInputReader().next().trim().toUpperCase());
    }
    return players;
  }

  /** TODO: add description. */
  private void play() {
    initializeScreen();

    while (true) {
      letPlayerGuess();
      updateScreen();

      if (getBackend().hasGuessingPlayerWon()) {
        printSeparator();
        System.out.println("Congratulations, you won!");
        break;
      }

      if (getBackend().hasGuessingPlayerLost()) {
        printSeparator();
        System.out.println("Oh no, you lost!");
        break;
      }
    }

    System.out.printf("The word to guess was: %s%n", getBackend().getWordToGuess());
  }

  /** TODO: add description. */
  private void letPlayerGuess() {
    switch (promptGuessMode()) {
      case 1:
        letPlayerGuessChar();
        break;

      case 2:
        letPlayerGuessWord();
        break;
      
      default:
    }
  }

  /**
   * Determines the guess mode by asking the player whether they want to guess
   * a character or a word.
   * 
   * @return One of the following modes represented as integers:<ul>
   *     <li>{@code 1} - guess character.</li>
   *     <li>{@code 2} - guess word.</li>
   *     </ul>
   */
  private int promptGuessMode() {
    System.out.printf(
      "%s%n%s%n%s%n",
      "What do you want to guess?",
      "  type '1' to guess a character",
      "  type '2' to guess a word"
    );
    
    while (true) {
      System.out.print(": ");
      String input = getUserInputReader().next().trim().toLowerCase();
      switch (input.charAt(0)) {
        case '1':
          return 1;

        case '2':
          return 2;

        default:
          System.out.println("Input is invalid. Only '1' and '2' are accepted.");
      }
    }
  }

  /** TODO: add description. */
  private void letPlayerGuessChar() {
    System.out.print("Please guess a character");

    Character guessedChar = null;
    do {
      System.out.print(": ");
      guessedChar = userInputReader.next().trim().toUpperCase().charAt(0);
    } while (!validateCharGuess(guessedChar));

    getBackend().play(guessedChar);
  }

  /** TODO: add description. */
  private boolean validateCharGuess(Character guessedChar) {
    if (getBackend().getGuessedCharsReadOnly().contains(guessedChar)) {
      System.out.println("Input is invalid. You already guessed that word.");
      return false;
    }
    
    if (guessedChar.toString().matches(getBackend().getCharMatchingPattern())) {
      return true;
    }
    System.out.printf(
        "Input is invalid. Your character must match the following pattern: %s%n",
        getBackend().getCharMatchingPattern()
    );
    return false;
  }

  /** TODO: add description. */
  private void letPlayerGuessWord() {  
    System.out.print("Please guess a word");

    String guessedWord = null;
    do {
      System.out.print(": ");
      guessedWord = userInputReader.next().trim().toUpperCase();
    } while (!validateWordGuess(guessedWord));

    getBackend().play(guessedWord);
  }


  /** TODO: add description. */
  private boolean validateWordGuess(String guessedWord) {
    if (getBackend().getGuessedWordsReadOnly().contains(guessedWord)) {
      System.out.println("Input is invalid. You already guessed that word.");
      return false;
    }

    for (int i = 0; i < guessedWord.length(); i++ ) {
      if (guessedWord.substring(i, i + 1).matches(getBackend().getCharMatchingPattern())) {
        return true;
      }
    }
    System.out.printf(
        "Input is invalid. Each character of your word must match the following pattern: %s%n",
        getBackend().getCharMatchingPattern()
    );
    return false;
  }

  /** TODO: add description. */
  private void initializeScreen() {
    Screen.clear();
    printHangedMan();
    System.out.println();
    printStats();
    System.out.println();
  }

  /** TODO: add description. */
  private void printHangedMan() {
    for (int i = 0; i < getDrawingGrid().size(); i++) {
      List<Pair<Integer, String>> currentRow = getDrawingGrid().get(i);

      for (int j = 0; j < currentRow.size(); j++) {
        Pair<Integer, String> currentPair = currentRow.get(j);

        if (currentPair.getKey() <= getBackend().getCurWrongGuesses()) {
          try {
            Thread.sleep(300);
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

  /** TODO: add description. */
  private List<List<Pair<Integer, String>>> getDrawingGrid() {
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

  /** TODO: add description. */
  private void printStats() {
    printPrettyPairOverview(
        new Pair<>(
            "Previously guessed characters",
            getBackend().getGuessedCharsReadOnly().toString()
        ),
        new Pair<>(
            "Previously guessed words",
            getBackend().getGuessedWordsReadOnly().toString()
        ),
        new Pair<>(
            "Wrong guesses",
            new StringBuilder()
                .append(getBackend().getCurWrongGuesses())
                .append(" (")
                .append(getBackend().getMaxWrongGuesses())
                .append(")")
                .toString()
        ),
        new Pair<>(
            "Word to guess",
            new StringBuilder()
                .append(getBackend().getGuessStatus().replaceAll("\\B", " "))
                .append(" (")
                .append(getBackend().getWordToGuess().length())
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

  /** TODO: add description. */
  private void printSeparator() {
    System.out.printf("%n# %s #%n%n", "-".repeat(96));
  }

  /** TODO: add description. */
  private void updateScreen() {
    Screen.setCurorPosition(getDrawingGrid().size(), 0);
    Screen.clearFromCursorToEnd();
    Screen.setCurorPosition(0, 0);
    printHangedMan();
    System.out.println();
    printStats();
    System.out.println();
  }
}
