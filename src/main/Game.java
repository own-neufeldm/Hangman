package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * This class serves the purpose of playing a game of Hangman on a terminal
 * screen.
 * 
 * @author Neufeld-Martin
 */
public class Game {
  /** Scanner for command-line input. */
  Scanner scanner;

  /** Sets the {@link #scanner scanner} for command-line input. */
  private void setScanner(Scanner scanner) {
    this.scanner = scanner;
  }

  /** Returns the {@link #scanner scanner} for command-line input. */
  public Scanner getScanner() {
    return this.scanner;
  }

  /** The printer to output information about the game. */
  Printer printer;

  /** Sets the {@link #printer printer} to output information about the game. */
  private void setPrinter(Printer printer) {
    this.printer = printer;
  }

  /** Returns the {@link #printer printer} to output information about the game. */
  private Printer getPrinter() {
    return this.printer;
  }

  /**
   * The word to guess. Only letters A-Z are supported. Whitespaces will be
   * trimmed and the word will be converted to upper case.  
   */
  private String wordToGuess;

  /** Sets the {@link #wordToGuess word to guess}. */
  private void setWordToGuess(String wordToGuess) {
    this.wordToGuess = wordToGuess;
  }

  /** Returns the {@link #wordToGuess word to guess}. */
  public String getWordToGuess() {
    return this.wordToGuess;
  }

  /**
   * The guess status. Correctly guessed characters are revealed, whereas
   * characters that are not yet guessed are replaced by an underscore.
   */
  private String guessStatus = null;

  /** Sets the {@link #guessStatus guess status}. */
  private void setGuessStatus(String guessStatus) {
    this.guessStatus = guessStatus;
  }

  /** Returns the {@link #guessStatus guess status}. */
  public String getGuessStatus() {
    return this.guessStatus;
  }

  /**
   * All guessed characters. Serves as reference for the guessing player to
   * know which guesses were already made.
   */
  private List<Character> guessedChars;

  /** Sets all {@link #guessedChars guessed characters}. */
  private void setGuessedChars(List<Character> guessedChars) {
    this.guessedChars = guessedChars;
  }

  /** Returns all {@link #guessedChars guessed characters}. */
  private List<Character> getGuessedChars() {
    return this.guessedChars;
  }

  /** Returns all {@link #guessedChars guessed characters} as read-only. */
  public List<Character> getGuessedCharsReadOnly() {
    return Collections.unmodifiableList(getGuessedChars());
  }

  /**
   * All guessed words. Serves as reference for the guessing player to know
   * which guesses were already made.
   */
  private List<String> guessedWords;

  /** Sets all {@link #guessedWords guessed words}. */
  private void setGuessedWords(List<String> guessedWords) {
    this.guessedWords = guessedWords;
  }

  /** Returns all {@link #guessedWords guessed words}. */
  private List<String> getGuessedWords() {
    return this.guessedWords;
  }

  /** Returns all {@link #guessedWords guessed words} as read-only. */
  public List<String> getGuessedWordsReadOnly() {
    return Collections.unmodifiableList(getGuessedWords());
  }

  /** Current amount of wrong guesses. */
  private int wrongGuesses;

  /** Sets the {@link #wrongGuesses current amount of wrong guesses}. */
  private void setWrongGuesses(int wrongGuesses) {
    this.wrongGuesses = wrongGuesses;
  }

  /** Returns the {@link #wrongGuesses current amount of wrong guesses}. */
  public int getWrongGuesses() {
    return this.wrongGuesses;
  }

  /** Maximum amount of wrong guesses. */
  private int maxGuesses;

  /** Sets the {@link #maxGuesses maximum amount of wrong guesses}. */
  private void setMaxGuesses(int maxGuesses) {
    this.maxGuesses = maxGuesses;
  }

  /** Returns the {@link #maxGuesses maximum amount of wrong guesses}. */
  public int getMaxGuesses() {
    return this.maxGuesses;
  }

  /**
   * Constructs an instance of this class.
   * 
   * @param maxGuesses the {@link #maxGuesses maximum amount of wrong guesses}.
   */
  public Game(Scanner scanner, int maxGuesses) {
    setScanner(scanner);
    setPrinter(new Printer(this));
    setWordToGuess(promptWordToGuess());
    setGuessStatus(getWordToGuess().replaceAll(".", "_"));
    setGuessedChars(new ArrayList<>());
    setGuessedWords(new ArrayList<>());
    setMaxGuesses(maxGuesses);
  }

  /** Plays the game until either player has won. */
  public void play() {
    getPrinter().initializeScreen();

    while (true) {
      letPlayerGuess();
      getPrinter().updateScreen();

      if (playerWon()) {
        getPrinter().printSeparator();
        System.out.println("Congratulations, you won!");
        break;
      }

      if (playerLost()) {
        getPrinter().printSeparator();
        System.out.println("Oh no, you lost!");
        break;
      }
    }

    System.out.printf("The word to guess was: %s%n", getWordToGuess());
  }

  /**
   * Lets player two make a guess. The player will be asked to either guess
   * a character or a word.
   */
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
   * Prompts player one to provide a {@link #wordToGuess word to guess}.
   * 
   * @return the {@link #wordToGuess word to guess}.
   */
  private String promptWordToGuess() {
    while (true) {
      System.out.print("Please enter the word to guess: ");
      String wordToGuess = getScanner().next().trim().toUpperCase();
  
      if (isValidWord(wordToGuess)) {
        return wordToGuess;
      }

      System.out.println(
          "Input is invalid." +
          "Your word must only consist of letters and contain at least one letter."
      );
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
    while (true) {
      System.out.print("What do you want to guess? ['c' = character, 'w' = word]: ");
      Character answer = getScanner().next().trim().toLowerCase().charAt(0);

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
   * Lets player two guess a character. A wrong character guess causes an
   * increase of the wrong guesses counter by one.
   */
  private void letPlayerGuessChar() {
    Character guessedChar = promptCharGuess();

    getGuessedChars().add(guessedChar);
    getGuessedChars().sort(Comparator.naturalOrder());

    if (getWordToGuess().contains(guessedChar.toString())) {
      StringBuilder stringBuilderGuessStatus = new StringBuilder(getGuessStatus());

      for (int i = 0; i < getWordToGuess().length(); i++) {
        if(getWordToGuess().charAt(i) == guessedChar) {
          stringBuilderGuessStatus.setCharAt(i, guessedChar);
        }
      }
      setGuessStatus(stringBuilderGuessStatus.toString());
    } else {
      setWrongGuesses(getWrongGuesses() + 1);
    }
  }

  /**
  * Prompts player two to guess a character. The guessed character must be
  * {@link #isValidChar(Character) valid} and must not be guessed already.
  * @return The guessed character.
  */
  private Character promptCharGuess() {    
    while (true) {
      System.out.print("Please guess a character: ");
      Character guessedChar = getScanner().next().trim().toUpperCase().charAt(0);

      if (!isValidChar(guessedChar)) {
        System.out.println("Input is invalid. Your guess must be a letter.");
      } else if (getGuessedChars().contains(guessedChar)) {
        System.out.println("Input is invalid. You already guessed that letter.");
      } else {
        return guessedChar;
      }
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
  private boolean isValidChar(Character chr) {
    if (chr == null) {
      return false;
    }

    return Character.isLetter(chr);
  }

  /**
   * Lets player two guess a word. A wrong word guess causes an increase of the
   * wrong guesses counter by two.
   */
  private void letPlayerGuessWord() {  
    String guessedWord = promptWordGuess();

    getGuessedWords().add(guessedWord);
    getGuessedWords().sort(Comparator.naturalOrder());

    if(guessedWord.equals(getWordToGuess())) {
      setGuessStatus(guessedWord);
    } else {
      setWrongGuesses(getWrongGuesses() + 2);
    }
  }

  /**
   * Prompts player two to guess a character. The guessed word must be
   * {@link #isValidWord(String) valid} and must not be guessed already.
   */
  private String promptWordGuess() {
    while(true) {
      System.out.print("Please guess a word: ");
      String guessedWord = getScanner().next().trim().toUpperCase();

      if (!isValidWord(guessedWord)) {
        System.out.println("Input is invalid. Your word must only consist of letters and contain at least one letter.");
      } else if (guessedWord.length() != getWordToGuess().length()) {
        System.out.println("Input is invalid. Your word must be of the same length as the word to guess.");
      } else if (getGuessedWords().contains(guessedWord)) {
        System.out.println("Input is invalid. You already guessed that word.");
      } else {
        return guessedWord;
      }
    }
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
  private boolean isValidWord(String word) {
    if (word == null) {
      return false;
    }

    return word.matches("[A-Z]+");
  }

  /** Returns {@code true} if the word is guessed, {@code false} otherwise. */
  private boolean playerWon() {
    return getGuessStatus().equals(getWordToGuess());
  }

  /**
   * Returns {@code true} if the {@link #maxGuesses maximum amount of wrong
   * guesses} is exceeded, {@code false} otherwise.
   */
  private boolean playerLost() {
    return getWrongGuesses() > getMaxGuesses();
  }
}
