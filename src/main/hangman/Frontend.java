package main.hangman;

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
public abstract class Frontend {
  /** TODO: add description. */
  public static void play(Scanner userInputReader, Backend game) {
    initializeScreen(game);

    while (true) {
      letPlayerGuess(userInputReader, game);
      updateScreen(game);

      if (game.hasGuessingPlayerWon()) {
        printSeparator();
        System.out.println("Congratulations, you won!");
        break;
      }

      if (game.hasGuessingPlayerLost()) {
        printSeparator();
        System.out.println("Oh no, you lost!");
        break;
      }
    }

    System.out.printf("The word to guess was: %s%n", game.getWordToGuess());
  }

  /** TODO: add description. */
  private static void letPlayerGuess(Scanner userInputReader, Backend game) {
    switch (promptGuessMode(userInputReader)) {
      case 1:
        letPlayerGuessChar(userInputReader, game);
        break;

      case 2:
        letPlayerGuessWord(userInputReader, game);
        break;
      
      default:
    }
  }

  /**
   * Determines the guess mode by asking the player whether they want to guess
   * a character or a word.
   * 
   * @param userInputReader The reader for command-line input of the user.
   * 
   * @return One of the following modes represented as integers:<ul>
   *     <li>{@code 1} - guess character.</li>
   *     <li>{@code 2} - guess word.</li>
   *     </ul>
   */
  private static int promptGuessMode(Scanner userInputReader) {
    System.out.printf(
      "%s%n%s%n%s%n",
      "What do you want to guess?",
      "  type '1' to guess a character",
      "  type '2' to guess a word"
    );
    
    while (true) {
      System.out.print(": ");
      String input = userInputReader.next().trim().toLowerCase();
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
  private static void letPlayerGuessChar(Scanner userInputReader, Backend game) {
    System.out.print("Please guess a character");

    Character guessedChar = null;
    do {
      System.out.print(": ");
      guessedChar = userInputReader.next().trim().toUpperCase().charAt(0);
    } while (!validateCharGuess(game, guessedChar));

    game.play(guessedChar);
  }

  /** TODO: add description. */
  private static boolean validateCharGuess(Backend game, Character guessedChar) {
    if (game.getGuessedCharsReadOnly().contains(guessedChar)) {
      System.out.println("Input is invalid. You already guessed that word.");
      return false;
    }
    
    if (guessedChar.toString().matches(game.getCharMatchingPattern())) {
      return true;
    }
    System.out.printf(
        "Input is invalid. Your character must match the following pattern: %s%n",
        game.getCharMatchingPattern()
    );
    return false;
  }

  /** TODO: add description. */
  private static void letPlayerGuessWord(Scanner userInputReader, Backend game) {  
    System.out.print("Please guess a word");

    String guessedWord = null;
    do {
      System.out.print(": ");
      guessedWord = userInputReader.next().trim().toUpperCase();
    } while (!validateWordGuess(game, guessedWord));

    game.play(guessedWord);
  }


  /** TODO: add description. */
  private static boolean validateWordGuess(Backend game, String guessedWord) {
    if (game.getGuessedWordsReadOnly().contains(guessedWord)) {
      System.out.println("Input is invalid. You already guessed that word.");
      return false;
    }

    for (int i = 0; i < guessedWord.length(); i++ ) {
      if (guessedWord.substring(i, i + 1).matches(game.getCharMatchingPattern())) {
        return true;
      }
    }
    System.out.printf(
        "Input is invalid. Each character of your word must match the following pattern: %s%n",
        game.getCharMatchingPattern()
    );
    return false;
  }

  /** TODO: add description. */
  private static void initializeScreen(Backend game) {
    Screen.clear();
    printHangedMan(game);
    System.out.println();
    printStats(game);
    System.out.println();
  }

  /** TODO: add description. */
  private static void printHangedMan(Backend game) {
    for (int i = 0; i < getDrawingGrid().size(); i++) {
      List<Pair<Integer, String>> currentRow = getDrawingGrid().get(i);

      for (int j = 0; j < currentRow.size(); j++) {
        Pair<Integer, String> currentPair = currentRow.get(j);

        if (currentPair.getKey() <= game.getCurWrongGuesses()) {
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
  private static List<List<Pair<Integer, String>>> getDrawingGrid() {
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
  private static void printStats(Backend game) {
    printPrettyPairOverview(
        new Pair<>(
            "Previously guessed characters",
            game.getGuessedCharsReadOnly().toString()
        ),
        new Pair<>(
            "Previously guessed words",
            game.getGuessedWordsReadOnly().toString()
        ),
        new Pair<>(
            "Wrong guesses",
            new StringBuilder()
                .append(game.getCurWrongGuesses())
                .append(" (")
                .append(game.getMaxWrongGuesses())
                .append(")")
                .toString()
        ),
        new Pair<>(
            "Word to guess",
            new StringBuilder()
                .append(game.getGuessStatus().replaceAll("\\B", " "))
                .append(" (")
                .append(game.getWordToGuess().length())
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
  private static void printPrettyPairOverview(Pair<String, String>... pairs) {
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
  private static void printSeparator() {
    System.out.printf("%n# %s #%n%n", "-".repeat(96));
  }

  /** TODO: add description. */
  private static void updateScreen(Backend game) {
    Screen.setCurorPosition(getDrawingGrid().size(), 0);
    Screen.clearFromCursorToEnd();
    Screen.setCurorPosition(0, 0);
    printHangedMan(game);
    System.out.println();
    printStats(game);
    System.out.println();
  }
}
