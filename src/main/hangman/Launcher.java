package main.hangman;

import java.io.IOException;
import java.util.Scanner;
import main.terminal.Screen;

/**
 * This class serves the purpose of launching a game of Hangman.
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
      Scanner scanner = new Scanner(System.in);
    ) {
      Game game = new Game(scanner, 8);
      game.play();

      System.out.printf("%nPress enter to exit ...");
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
