package test;

public class Sandbox {
  public static void main(String[] args) throws Exception {
    for (int i = 1; i <= 5; i++) {
      System.out.printf("%d/5%n", i);
      Thread.sleep(1000L);
    }
  }
}
