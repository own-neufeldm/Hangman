package main.hangman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * The backend of the game.
 * 
 * @author Neufeld-Martin
 * @see main.hangman.Frontend
 */
public class Backend {
  /** The word to guess. */
  private String wordToGuess;

  /**
   * The guess status. Correctly guessed characters are revealed, whereas
   * characters that are not yet guessed are replaced by an underscore.
   */
  private String guessStatus;

  /**
   * All guessed characters. Serves as reference for the
   * guessing player to know which guesses were already made.
   */
  private List<Character> guessedChars;

  /**
   * All guessed words. Serves as reference for the
   * guessing player to know which guesses were already made.
   */
  private List<String> guessedWords;

  /** The character matching pattern. */
  private String charMatchingPattern;

  /** Current amount of wrong guesses. */
  private int curWrongGuess;

  /** Maximum amount of wrong guesses. */
  private int maxWrongGuesses;

    /**
   * The players of the game.
   * <p>
   * Index {@code 0} is the player who provides the word to guess,
   * index {@code 1} is the player who guesses.
   */
  List<String> players;

  // ############################################################################################ //

  /**
   * Constructs an instance of this class.
   * 
   * @param wordToGuess
   *     the {@link #wordToGuess word to guess}.
   *     Must not be null.
   * @param charMatchingPattern
   *     the {@link #charMatchingPattern character matching pattern}.
   *     Must not be null.
   * @param maxWrongGuesses
   *     the {@link #maxWrongGuesses maximum amount of wrong guesses}.
   *     must be between 0 and 50 (inclusive).
   */
  public Backend(String wordToGuess, String charMatchingPattern, int maxWrongGuesses, List<String> players) {
    Objects.requireNonNull(wordToGuess);
    Objects.requireNonNull(charMatchingPattern);
    Objects.checkIndex(maxWrongGuesses, 51);
    Objects.requireNonNull(players);

    setWordToGuess(wordToGuess);
    setGuessStatus(getWordToGuess().replaceAll(".", "_"));
    setGuessedChars(new ArrayList<>());
    setGuessedWords(new ArrayList<>());
    setCharMatchingPattern(charMatchingPattern);
    setMaxWrongGuesses(maxWrongGuesses);
    setPlayers(new ArrayList<>());
  }

  // ############################################################################################ //

  /** Sets the {@link #wordToGuess word to guess}. */
  private void setWordToGuess(String wordToGuess) {
    this.wordToGuess = wordToGuess;
  }

  /** Returns the {@link #wordToGuess word to guess}. */
  public String getWordToGuess() {
    return this.wordToGuess;
  }

  /** Sets the {@link #guessStatus guess status}. */
  private void setGuessStatus(String guessStatus) {
    this.guessStatus = guessStatus;
  }

  /** Returns the {@link #guessStatus guess status}. */
  public String getGuessStatus() {
    return this.guessStatus;
  }

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

  /** Sets the {@link #charMatchingPattern character matching pattern}. */
  private void setCharMatchingPattern(String charMatchingPattern) {
    this.charMatchingPattern = charMatchingPattern;
  }

  /** Returns the {@link #charMatchingPattern character matching pattern}. */
  public String getCharMatchingPattern() {
    return this.charMatchingPattern;
  }

  /** Sets the {@link #curWrongGuess current amount of wrong guesses}. */
  private void setCurWrongGuesses(int curWrongGuesses) {
    this.curWrongGuess = curWrongGuesses;
  }

  /** Returns the {@link #curWrongGuess current amount of wrong guesses}. */
  public int getCurWrongGuesses() {
    return this.curWrongGuess;
  }

  /** Sets the {@link #maxWrongGuesses maximum amount of wrong guesses}. */
  private void setMaxWrongGuesses(int maxWrongGuesses) {
    this.maxWrongGuesses = maxWrongGuesses;
  }

  /** Returns the {@link #maxWrongGuesses maximum amount of wrong guesses}. */
  public int getMaxWrongGuesses() {
    return this.maxWrongGuesses;
  }

  /** Sets the {@link #players players}. */
  private void setPlayers(List<String> players) {
    this.players = players;
  }

  /** Returns the {@link #players players}. */
  private List<String> getPlayers() {
    return this.players;
  }

  /** Returns the {@link #players players} as read-only. */
  public List<String> getPlayersReadOnly() {
    return Collections.unmodifiableList(getPlayers());
  }

  // ############################################################################################ //

  /**
   * Plays a single round of the game.
   * <p>
   * All parameters are expected to be valid. This method does not conduct
   * validity checks. Appropriate handling for unexpected or invalid input
   * must therefore be made outside this method.
   * <p>
   * Additionally, this method does not check whether the game is over or not.
   * 
   * @param guessedChar The character that was guessed.
   */
  public void play(Character guessedChar) {
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
      setCurWrongGuesses(getCurWrongGuesses() + 1);
    }
  }

  /**
   * Plays a single round of the game.
   * <p>
   * All parameters are expected to be valid. This method does not conduct
   * validity checks. Appropriate handling for unexpected or invalid input
   * must therefore be made outside this method.
   * <p>
   * Additionally, this method does not check whether the game is over or not.
   * 
   * @param guessedWord The word that was guessed.
   */
  public void play(String guessedWord) {
    getGuessedWords().add(guessedWord);
    getGuessedWords().sort(Comparator.naturalOrder());

    if(guessedWord.equals(getWordToGuess())) {
      setGuessStatus(guessedWord);
    } else {
      setCurWrongGuesses(getCurWrongGuesses() + 2);
    }
  }

  /**
   * Returns {@code true} if the guessing
   * player has won, {@code false} otherwise.
   * <p>
   * The guessing player wins by having guessed all expected
   * characters without exceeding the maximum amount of wrong guesses.
   */
  public boolean hasGuessingPlayerWon() {
    return getGuessStatus().equals(getWordToGuess());
  }

  /**
   * Returns {@code true} if the guessing
   * player has lost, {@code false} otherwise.
   * <p>
   * The guessing player loses by exceeding the maximum amount
   * of wrong guesses without having guessed all expected characters.
   */
  public boolean hasGuessingPlayerLost() {
    return (getCurWrongGuesses() == getMaxWrongGuesses());
  }
}
