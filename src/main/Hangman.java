package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;

/**
 * This class serves the purpose of playing a game of Hangman in a terminal.
 * 
 * @author Neufeld-Martin
 */
public class Hangman {

  /**
   * The word to be guessed. Only letters A-Z are supported. Whitespaces will
   * be trimmed and the word will be converted to upper case.  
   */
  private static String wordToGuess = null;

  /**
   * The word to guess with the correctly guessed characters revealed.
   * Characters that are not yet guessed will be replaced by an underscore.
   */
  private static String guessStatus = null;

  /**
   * All guessed characters. Serves as reference for the guessing player to
   * know which guesses were already made.
   */
  private static List<Character> guessedChars = new ArrayList<>();

  /**
   * All guessed words. Serves as reference for the guessing player to know
   * which guesses were already made.
   */
  private static List<String> guessedWords = new ArrayList<>();

  /** Current amount of wrong guesses. */
  private static int wrongGuesses = 0;

  /** Maximum amount of wrong guesses. */
  private static int maxGuesses = 8;

  /**
   * A two-dimensional list that defines which character is when printed when
   * drawing the hanged man. The information is stored in form of Key-Value
   * {@link javafx.util.Pair Pairs} {@code Pair<Integer I, String S>} where
   * {@code I} is the amount of wrong guesses to match or exceed and {@code S}
   * the part of the hanged man to print.
   */
  private static final List<List<Pair<Integer, String>>> DRAWING_GRID = readDrawingGrid();

  /**
   * Executes the game.
   * 
   * @param args Command-line arguments.
   */
  public static void main(String[] args) {
    clearScreen();

    try (
      // Scanner for command-line input.
      Scanner scn = new Scanner(System.in);
    ) {
      wordToGuess = promptWordToGuess(scn);
      guessStatus = wordToGuess.replaceAll(".", "_");

      while (true) {
        clearScreen();
        printHangedMan();
        System.out.println();
        printStats();
        System.out.println();
        letPlayerGuess(scn);
        
        if (guessStatus.equals(wordToGuess)) {
          printSeparator();
          System.out.println("Congratulations, you won!");
          break;
        }

        if (wrongGuesses > maxGuesses) {
          printSeparator();
          printHangedMan();
          System.out.println();
          System.out.println("Oh no, you lost!");
          break;
        }
      }
    }

    System.out.printf("The word to guess was: %s%n", wordToGuess);
  }

  /** Clears the console screen (bash). */
  public static void clearScreen() {
    try {
      new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks if the provided character is valid in the context of this game.
   * 
   * <p>A guessed character is valid if it is a letter between A-Z.
   * 
   * @param chr Character to check.
   * @return {@code true} if the provided character is valid, {@code false}
   *     otherwise.
   */
  private static boolean isValidChar(Character chr) {
    if (chr == null) {
      return false;
    }

    return Character.isLetter(chr);
  }

  /**
   * Checks if the provided word is valid in the context of this game.
   * 
   * <p>A provided word is valid if it contains only letters between A-Z and has
   * a length greater than or equal to 1.
   * 
   * @param chr Word to check.
   * @return {@code true} if the provided character is valid, {@code false}
   *     otherwise.
   */
  private static boolean isValidWord(String word) {
    if (word == null) {
      return false;
    }

    return word.matches("[A-Z]+");
  }

  /**
   * Lets player two make a guess. The player will be asked to either guess
   * a character or a word.
   * 
   * @param scn Scanner for command-line input.
   */
  private static void letPlayerGuess(Scanner scn) {
    switch (promptGuessMode(scn)) {
      case 1:
        letPlayerGuessChar(scn);
        break;

      case 2:
        letPlayerGuessWord(scn);
        break;
      
      default:
    }
  }

  /**
   * Lets player two guess a character. A wrong character guess causes an
   * increase of the wrong guesses counter by one.
   * 
   * @param scn Scanner for command-line input.
   */
  private static void letPlayerGuessChar(Scanner scn) {  
    Character guessedChar = promptCharGuess(scn);

    guessedChars.add(guessedChar);
    guessedChars.sort(Comparator.naturalOrder());

    if (!updateGuessStatus(guessedChar)) {
      wrongGuesses++;
    }
  }

  /**
   * Lets player two guess a word. A wrong word guess causes an
   * increase of the wrong guesses counter by two.
   * 
   * @param scn Scanner for command-line input.
   */
  private static void letPlayerGuessWord(Scanner scn) {  
    String guessedWord = promptWordGuess(scn);

    guessedWords.add(guessedWord);
    guessedWords.sort(Comparator.naturalOrder());

    if(guessedWord.equals(wordToGuess)) {
      guessStatus = guessedWord;
    } else {
      wrongGuesses += 2;
    }
  }

  /**
   * Prints the hanged man.
   * 
   * @see {@link #drawingGrid}.
   */
  private static void printHangedMan() {
    for(int i = 0; i < DRAWING_GRID.size(); i++) {
      List<Pair<Integer, String>> currentRow = DRAWING_GRID.get(i);

      for(int j = 0; j < currentRow.size(); j++) {
        Pair<Integer, String> currentPair = currentRow.get(j);

        if(wrongGuesses >= currentPair.getKey()) {
          try {
            Thread.sleep(150L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.print(currentPair.getValue());
        }
      }

      System.out.println();
    }
  }

  /** Prints a horizontal separator line to the terminal. */
  private static void printSeparator() {
    System.out.printf("%n# %s #%n%n", "-".repeat(96));
  }

  /** Prints information about the current round to the terminal. */
  private static void printStats() {
    System.out.printf("%-29s : %s%n", "Previously guessed characters", guessedChars.toString());
    System.out.printf("%-29s : %s%n", "Previously guessed words", guessedWords.toString());
    System.out.printf("%-29s : %d (%d)%n", "Wrong guesses", wrongGuesses, maxGuesses);
    System.out.printf("%-29s : %s (%d)%n", "Word to guess", guessStatus.replaceAll("\\B", " "), wordToGuess.length());
  }

  /**
  * Prompts player two to guess a character. The guessed character must be
  * {@link #isValidChar(Character) valid} and must not be guessed already.
  *
  * @param scn Scanner for command-line input.
  * @return The guessed character.
  */
  private static Character promptCharGuess(Scanner scn) {
    Character guessedChar = null;
    
    while (true) {
      System.out.print("Please guess a character: ");
      guessedChar = scn.next().trim().toUpperCase().charAt(0);

      if (!isValidChar(guessedChar)) {
        System.out.println("Input is invalid. Your guess must be a letter.");
      } else if (guessedChars.contains(guessedChar)) {
        System.out.println("Input is invalid. You already guessed that letter.");
      } else {
        return guessedChar;
      }
    }
  }

  /**
   * Determines the guess mode by asking the player whether they want to guess
   * a character or a word.
   * 
   * @param scn Scanner for command-line input.
   * @return One of the following modes represented as integers:<ul>
   *     <li>{@code 1} - guess character.</li>
   *     <li>{@code 2} - guess word.</li>
   *     </ul>
   */
  private static int promptGuessMode(Scanner scn) {
    Character answer = null;

    while (true) {
      System.out.print("What do you want to guess? ['c' = character, 'w' = word]: ");
      answer = scn.next().trim().toLowerCase().charAt(0);

      switch (answer) {
        case 'c':
          return 1;

        case 'w':
          return 2;

        default:
          System.out.println("Input is invalid. Only 'c' and 'w' are accepted.");
      }
    }
  }

  /**
   * Prompts player two to guess a character.
   * <p>
   * The guessed word must be {@link #isValidWord(String) valid} and must not be guessed already.
   * <p>
   * This function is also responsible for updating the fields
   * {@link #guessedWords}, {@link #wrongGuesses} and {@link #guessStatus} if necessary.
   * 
   * @param scn Scanner for command-line input.
   */
  private static String promptWordGuess(Scanner scn) {
    String guessedWord = null;

    while(true) {
      System.out.print("Please guess a word: ");
      guessedWord = scn.next().trim().toUpperCase();

      if (!isValidWord(guessedWord)) {
        System.out.println("Input is invalid. Your word must only consist of letters and contain at least one letter.");
      } else if (guessedWord.length() != wordToGuess.length()) {
        System.out.println("Input is invalid. Your word must be of the same length as the word to guess.");
      } else if (guessedWords.contains(guessedWord)) {
        System.out.println("Input is invalid. You already guessed that word.");
      } else {
        return guessedWord;
      }
    }
  }

  /**
   * Retrieves {@link #wordToGuess} from player one via command-line.
   * 
   * @param scn Scanner for command-line input.
   * @return {@link #wordToGuess}
   */
  private static String promptWordToGuess(Scanner scn) {
    while (true) {
      System.out.print("Please enter the word to guess: ");
      wordToGuess = scn.next().trim().toUpperCase();
  
      if (isValidWord(wordToGuess)) {
        return wordToGuess;
      }

      System.out.println("Input is invalid. Your word must only consist of letters and contain at least one letter.");
    }
  }

  /** 
   * TODO: read DRAWING_GRID from JSON.
   * 
   * Returns an object to initialize {@link #DRAWING_GRID}.
   */
  private static List<List<Pair<Integer, String>>> readDrawingGrid() {
    List<List<Pair<Integer, String>>> drawingGrid = new ArrayList<>(8);
    List<Pair<Integer, String>> drawingGridRowOne = new ArrayList<>(6);
    List<Pair<Integer, String>> drawingGridRowTwo = new ArrayList<>(6);
    List<Pair<Integer, String>> drawingGridRowThree = new ArrayList<>(6);
    List<Pair<Integer, String>> drawingGridRowFour = new ArrayList<>(6);
    List<Pair<Integer, String>> drawingGridRowFive = new ArrayList<>(6);
    List<Pair<Integer, String>> drawingGridRowSix = new ArrayList<>(6);
    List<Pair<Integer, String>> drawingGridRowSeven = new ArrayList<>(1);
    List<Pair<Integer, String>> drawingGridRowEight = new ArrayList<>(1);

    drawingGridRowOne.add(new Pair<>(4, "+"));
    drawingGridRowOne.add(new Pair<>(5, "-"));
    drawingGridRowOne.add(new Pair<>(5, "-"));
    drawingGridRowOne.add(new Pair<>(6, "-"));
    drawingGridRowOne.add(new Pair<>(6, "-"));
    drawingGridRowOne.add(new Pair<>(7, "+"));
    drawingGrid.add(drawingGridRowOne);

    drawingGridRowTwo.add(new Pair<>(4, "|"));
    drawingGridRowTwo.add(new Pair<>(7, " "));
    drawingGridRowTwo.add(new Pair<>(7, " "));
    drawingGridRowTwo.add(new Pair<>(7, " "));
    drawingGridRowTwo.add(new Pair<>(7, " "));
    drawingGridRowTwo.add(new Pair<>(7, "|"));
    drawingGrid.add(drawingGridRowTwo);

    drawingGridRowThree.add(new Pair<>(3, "|"));
    drawingGridRowThree.add(new Pair<>(8, " "));
    drawingGridRowThree.add(new Pair<>(8, " "));
    drawingGridRowThree.add(new Pair<>(8, " "));
    drawingGridRowThree.add(new Pair<>(8, " "));
    drawingGridRowThree.add(new Pair<>(8, "O"));
    drawingGrid.add(drawingGridRowThree);

    drawingGridRowFour.add(new Pair<>(3, "|"));
    drawingGridRowFour.add(new Pair<>(8, " "));
    drawingGridRowFour.add(new Pair<>(8, " "));
    drawingGridRowFour.add(new Pair<>(8, " "));
    drawingGridRowFour.add(new Pair<>(8, "-"));
    drawingGridRowFour.add(new Pair<>(8, "+-"));
    drawingGrid.add(drawingGridRowFour);

    drawingGridRowFive.add(new Pair<>(2, "|"));
    drawingGridRowFive.add(new Pair<>(9, " "));
    drawingGridRowFive.add(new Pair<>(9, " "));
    drawingGridRowFive.add(new Pair<>(9, " "));
    drawingGridRowFive.add(new Pair<>(9, " "));
    drawingGridRowFive.add(new Pair<>(9, "|"));
    drawingGrid.add(drawingGridRowFive);

    drawingGridRowSix.add(new Pair<>(2, "|"));
    drawingGridRowSix.add(new Pair<>(9, " "));
    drawingGridRowSix.add(new Pair<>(9, " "));
    drawingGridRowSix.add(new Pair<>(9, " "));
    drawingGridRowSix.add(new Pair<>(9, "/"));
    drawingGridRowSix.add(new Pair<>(9, " \\"));
    drawingGrid.add(drawingGridRowSix);

    drawingGridRowSeven.add(new Pair<>(1, "|"));
    drawingGrid.add(drawingGridRowSeven);

    drawingGridRowEight.add(new Pair<>(1, "="));
    drawingGrid.add(drawingGridRowEight);

    return drawingGrid;
  }

  /**
   * Updates {@link #guessStatus} by revealing each character that matches the
   * guessed character.
   * 
   * @param guessedChar Guessed character to reveal in {@link #guessStatus}.
   * @return {@code true} if the character is contained in {@link #wordToGuess},
   *     {@code false} otherwise.
   */
  private static boolean updateGuessStatus(Character guessedChar) {
    StringBuilder stringBuilderGuessStatus = new StringBuilder(guessStatus);

    if (!wordToGuess.contains(guessedChar.toString())) {
      return false;
    }

    for (int i = 0; i < wordToGuess.length(); i++) {
      if(wordToGuess.charAt(i) == guessedChar) {
        stringBuilderGuessStatus.setCharAt(i, guessedChar);
      }
    }
    guessStatus = stringBuilderGuessStatus.toString();
    
    return true;
  }
}
