package main.hangman;

import java.util.Scanner;
import main.terminal.Screen;

/**
 * This calls serves the purpose of launching a game of Hangman.
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
    try (
      Scanner scanner = new Scanner(System.in);
    ) {
      Screen.clear();

      Game game = new Game(scanner, 8);

      game.play();
    }
  }
}