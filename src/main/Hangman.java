package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javafx.util.Pair;

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
   * All guessed words.
   * <p>
   * Serves as reference for the guessing player to know which guesses were already made.
   */
  private static List<String> guessedWords = new ArrayList<>();

  /**
   * Current amount of wrong guesses.
   */
  private static int wrongGuesses = 0;

  /**
   * Maximum amount of wrong guesses.
   */
  private static int maxGuesses = 8;

  /**
   * A two-dimensional grid that defines which character is when printed when drawing the hanged man.
   * <p>
   * The information is stored in form of Key-Value {@link javafx.util.Pair Pairs} {@code Pair<Integer I, String S>}
   * where {@code I} is the amount of wrong guesses to match or exceed and {@code S} the part of the hanged man to print.
   */
  private static List<List<Pair<Integer, String>>> drawingGrid = getDrawingGrid();

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
      wordToGuess = getWordToGuess(scn);
      guessStatus = wordToGuess.replaceAll(".", "_");

      for(int curNdx = 0; curNdx < 100; curNdx++) {
        System.out.println();
      }

      while(true) {
        printStats();

        switch(getGuessMode(scn)) {
          case 1:
            letPlayerGuessChar(scn);
            break;
          
          case 2:
            letPlayerGuessWord(scn);
            break;
        }

        printHangedMan();
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

    System.out.printf("The word to guess was: %s%n", wordToGuess);

    System.out.println();
    System.out.println("*** DONE ***");
  }

  /**
   * @return {@link #drawingGrid}
   */
  private static List<List<Pair<Integer, String>>> getDrawingGrid() {
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
   * Determines the guess mode by asking the player whether they want to guess a character or a word.
   * 
   * @param scn Scanner for command-line input.
   * @return {@code 1} for character guess or {@code 2} for word guess.
   */
  private static int getGuessMode(Scanner scn) {
    int guessMode = 0;
    Character answer = null;

    while(answer == null) {
      System.out.print("What do you want to guess? ['c' = character, 'w' = word]: ");
      answer = scn.next().trim().toLowerCase().charAt(0);

      switch(answer) {
        case 'c':
          guessMode = 1;
          break;

        case 'w':
          guessMode = 2;
          break;

        default:
          answer = null;
          System.out.println("Input is invalid. Only 'c' and 'w' are accepted.");
          break;
      }
    }
    
    return guessMode;
  }

  /**
   * @return {@link #guessStatus} with a whitespace after each character.
   */
  private static String getGuessStatusForPrinting() {
    StringBuilder stringBuilderGuessStatus = new StringBuilder();

    for(int i = 0; i < guessStatus.length(); i++) {
      stringBuilderGuessStatus.append(guessStatus.charAt(i));

      if(i != (guessStatus.length() - 1)) {
        stringBuilderGuessStatus.append(' ');
      }
    }

    return stringBuilderGuessStatus.toString();
  }

  /**
   * Retrieves {@link #wordToGuess} from player one via command-line.
   * 
   * @param scn Scanner for command-line input.
   * @return {@link #wordToGuess}
   */
  private static String getWordToGuess(Scanner scn) {
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
  private static void letPlayerGuessChar(Scanner scn) {
    Character guessedChar = null;
    StringBuilder stringBuilderGuessStatus = new StringBuilder(guessStatus);
    int replacedChars = 0;
    
    while(guessedChar == null) {
      System.out.print("Please guess a character: ");
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
        stringBuilderGuessStatus.setCharAt(curNdx, guessedChar);
        replacedChars++;
      }
    }

    if(replacedChars == 0) {
      wrongGuesses++;
    } else {
      guessStatus = stringBuilderGuessStatus.toString();
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
  private static void letPlayerGuessWord(Scanner scn) {
    String guessedWord = null;

    while(guessedWord == null) {
      System.out.print("Please guess a word: ");
      guessedWord = scn.next().trim().toUpperCase();

      if(!isValidWord(guessedWord)) {
        guessedWord = null;
        System.out.println("Input is invalid. Your word must only consist of letters and contain at least one letter.");
      }

      if(guessedWord.length() != wordToGuess.length()) {
        guessedWord = null;
        System.out.println("Input is invalid. Your word must be of the same length as the word to guess.");
      }

      if(guessedWords.contains(guessedWord)) {
        guessedWord = null;
        System.out.println("Input is invalid. You already guessed that word.");
      }
    }

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
    System.out.println();

    for(int i = 0; i < drawingGrid.size(); i++) {
      List<Pair<Integer, String>> currentRow = drawingGrid.get(i);

      for(int j = 0; j < currentRow.size(); j++) {
        Pair<Integer, String> currentPair = currentRow.get(j);

        if(wrongGuesses >= currentPair.getKey()) {
          System.out.print(currentPair.getValue());
        }
      }

      System.out.println();
    }
  }

  /**
   * Prints a separator to the Terminal for increased readability of the game. 
   */
  private static void printSeparator() {
    System.out.printf("%n# %s #%n%n", "-".repeat(96));
  }

  /**
   * Prints information about the current round to the Terminal for the guessing player.
   */
  private static void printStats() {
    System.out.printf("%-30s : %s%n", "Previously guessed characters", guessedChars.toString());
    System.out.printf("%-30s : %s%n", "Previously guessed words", guessedWords.toString());
    System.out.printf("%-30s : %d (%d)%n", "Wrong guesses", wrongGuesses, maxGuesses);
    System.out.printf("%-30s : %s (%d)%n%n", "Word to guess", getGuessStatusForPrinting(), wordToGuess.length());
  }
}
