import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hangman {

  public static void main(String[] args) {
    try(
      Scanner scn = new Scanner(System.in);
    ) {
      String wordToGuess = promptWord(scn);
      List<Character> guessedChars = new ArrayList<>();
      int wrongGuesses = 0;

      while(true) {
        Character guessedChar = promptChar(scn, guessedChars);
      }
    }
  }

  private static String promptWord(Scanner scn) {
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

  private static boolean isValidWord(String word) {
    if(word == null) {
      return false;
    }

    return word.matches("[A-Z]");
  }

  private static Character promptChar(Scanner scn, List<Character> guessedChars) {
    Character guessedChar = null;
    
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

    return guessedChar;
  }

  private static boolean isValidChar(Character chr) {
    if(chr == null) {
      return false;
    }

    return Character.isLetter(chr);
  }

  private static void draw() {

  }
}
