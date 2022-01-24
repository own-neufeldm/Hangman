import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * TODO - comment
 * 
 * @author Neufeld-Martin
 */
public class Hangman {

  // TODO - comment
  private static String wordToGuess = null;

  // TODO - comment
  private static String guessStatus = null;

  // TODO - comment
  private static List<Character> guessedChars = new ArrayList<>();

  // TODO - comment
  private static int wrongGuesses = 0;

  // TODO - comment
  private static int maxGuesses = 5;

  /**
   * TODO - comment
   * 
   * @param args
   */
  public static void main(String[] args) {
    try(
      Scanner scn = new Scanner(System.in);
    ) {
      wordToGuess = determineWord(scn);
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
   * TODO - comment
   * 
   * @param scn
   * @return
   */
  private static String determineWord(Scanner scn) {
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
   * TODO - comment
   * 
   * @param chr
   * @return
   */
  private static boolean isValidChar(Character chr) {
    if(chr == null) {
      return false;
    }

    return Character.isLetter(chr);
  }

  /**
   * TODO - comment
   * 
   * @param word
   * @return
   */
  private static boolean isValidWord(String word) {
    if(word == null) {
      return false;
    }

    return word.matches("[A-Z]+");
  }

  /**
   * TODO - comment
   * 
   * @param scn
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
   * TODO - comment 
   */
  private static void printSeparator() {
    System.out.printf("%n# %s #%n%n", "-".repeat(46));
  }

  /**
   * TODO - comment
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

    // TODO - print Hangman
  }
}
