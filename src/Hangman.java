import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * This class serves the purpose of playing a game of Hangman in a Terminal.
 * 
 * @author Neufeld-Martin
 */
public class Hangman {

  /**
   * The word to be guessed. Only letters A-Z are supported.
   * <p>
   * Whitespaces will be trimmed and the word will be converted to upper case.
   *   
   */
  private static String wordToGuess = null;

  /**
   * The word to guess with the correctly guessed characters revealed.
   * <p>
   * Characters that are not yet guessed will be replaced by an underscore.
   */
  private static String guessStatus = null;

  /**
   * All guessed characters.
   * <p>
   * Serves as reference for the guessing player to know which guesses were already made.
   */
  private static List<Character> guessedChars = new ArrayList<>();

  /**
   * Current amount of wrong guesses.
   */
  private static int wrongGuesses = 0;

  /**
   * Maximum allowed amount of wrong guesses.
   */
  private static int maxGuesses = 5;

  /**
   * Executes the game.
   * 
   * @param args Command-line arguments.
   */
  public static void main(String[] args) {
    try(
      // Scanner for command-line input.
      Scanner scn = new Scanner(System.in);
    ) {
      wordToGuess = getWord(scn);
      guessStatus = wordToGuess.replaceAll(".", "_");

      for(int curNdx = 0; curNdx < 100; curNdx++) {
        System.out.println();
      }

      while(true) {
        printStats();
        letPlayerGuess(scn);
        printSeparator();

        if(guessStatus.equals(wordToGuess)) {
          System.out.println("Congratulations, you won!");
          break;
        }

        if(wrongGuesses > maxGuesses) {
          System.out.println("Oh no, you lost!");
          break;
        }
      }
    }

    System.out.printf("The word to guess was: %s", wordToGuess);

    System.out.println();
    System.out.println("*** DONE ***");
  }

  /**
   * Retrieves {@link #wordToGuess} from player one via command-line.
   * 
   * @param scn Scanner for command-line input.
   * @return {@link #wordToGuess}.
   */
  private static String getWord(Scanner scn) {
    String wordToGuess = null;
    
    while(wordToGuess == null) {
      System.out.print("Please enter the word to guess: ");
      wordToGuess = scn.next().trim().toUpperCase();
  
      if(!isValidWord(wordToGuess)) {
        wordToGuess = null;
        System.out.println("Input is invalid. Your word must only consist of letters and contain at least one letter.");
      }
    }

    return wordToGuess;
  }

  /**
   * Checks if the provided character is valid in the context of this game.
   * <p>
   * A guessed character is valid if it is a letter between A-Z.
   * 
   * @param chr Character to check.
   * @return {@code true} if the provided character is valid, {@code false} otherwise.
   */
  private static boolean isValidChar(Character chr) {
    if(chr == null) {
      return false;
    }

    return Character.isLetter(chr);
  }

  /**
   * Checks if the provided word is valid in the context of this game.
   * <p>
   * A provided word is valid if it contains only letter between A-Z and has length greater than or equal to 1.
   * 
   * @param chr Word to check.
   * @return {@code true} if the provided character is valid, {@code false} otherwise.
   */
  private static boolean isValidWord(String word) {
    if(word == null) {
      return false;
    }

    return word.matches("[A-Z]+");
  }

  /**
   * Prompts player two to guess a character.
   * <p>
   * The guessed character must be {@link #isValidChar(Character) valid} and must not be guessed already.
   * <p>
   * This function is also responsible for updating the fields
   * {@link #guessedChars}, {@link #wrongGuesses} and {@link #guessStatus} if necessary.
   * 
   * @param scn Scanner for command-line input.
   */
  private static void letPlayerGuess(Scanner scn) {
    Character guessedChar = null;
    StringBuilder guessStatusBuilder = new StringBuilder(guessStatus);
    int replacedChars = 0;
    
    while(guessedChar == null) {
      System.out.print("Please guess a letter: ");
      guessedChar = scn.next().trim().toUpperCase().charAt(0);

      if(!isValidChar(guessedChar)) {
        guessedChar = null;
        System.out.println("Input is invalid. Your guess must be a letter.");
      }

      if(guessedChars.contains(guessedChar)) {
        guessedChar = null;
        System.out.println("Input is invalid. You already guessed that letter.");
      }
    }

    guessedChars.add(guessedChar);
    guessedChars.sort(Comparator.naturalOrder());

    for(int curNdx = 0; curNdx < wordToGuess.length(); curNdx++) {
      if(wordToGuess.charAt(curNdx) == guessedChar) {
        guessStatusBuilder.setCharAt(curNdx, guessedChar);
        replacedChars++;
      }
    }

    if(replacedChars == 0) {
      wrongGuesses++;
    } else {
      guessStatus = guessStatusBuilder.toString();
    }
  }

  /**
   * Prints a separator to the Terminal for increased readability of the game. 
   */
  private static void printSeparator() {
    System.out.printf("%n# %s #%n%n", "-".repeat(46));
  }

  /**
   * Prints information about the current round to the Terminal for the guessing player.
   */
  private static void printStats() {
    System.out.printf("Previous guesses: %s%n", guessedChars.toString());
    System.out.printf("Wrong guesses: %d (%d)%n", wrongGuesses, maxGuesses);
    System.out.println();

    System.out.printf("Word to guess (%d): ", wordToGuess.length());
    for(int curNdx = 0; curNdx < guessStatus.length(); curNdx++) {
      System.out.print(guessStatus.charAt(curNdx));

      if(curNdx != (guessStatus.length() - 1)) {
        System.out.print(" ");
      }
    }
    System.out.println();
  }

  // TODO - print hanged man
}
