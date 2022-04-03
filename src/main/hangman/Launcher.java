package main.hangman;

import java.io.IOException;
import java.util.Scanner;
import main.terminal.Screen;

/**
 * The Launcher of the game.
 * 
 * @author Neufeld-Martin
 */
public class Launcher {
  /**
   * Launches a game of Hangman.
   * 
   * @param args Command-line arguments.
   */
  public static void main(String[] args) {   
    Screen.clear();
    try (
      Scanner userInputReader = new Scanner(System.in);
    ) {
      Frontend.play(userInputReader, new Backend("default".toUpperCase(), "[A-Z]", 8));
      System.out.printf("%nPress enter to exit ...");
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
