import java.util.ArrayList;

// Class for all utility functions for console interface 

public class UI {

  public static void clearTerminal() {
    System.out.println("\033c");
    displayMainTitle();
  }
  

  public static void showMenuTitle(String menuTitle) {
    System.out.format("=== %s === \n", menuTitle);
  }

  public static int displayMenu(ArrayList<String> menuOptions, String instruction) {
    System.out.println("");
    for (int i = 0; i < menuOptions.size(); i++) {
      System.out.format("<%d> %s\n", i + 1, menuOptions.get(i).toUpperCase());
    }
    System.out.println(instruction + ":");
    int userChoice = Main.terminal.nextInt();
    while (userChoice < 1 || userChoice > menuOptions.size() + 1) {
      System.out.println("Invalid input");
      System.out.println(instruction + ":");
      userChoice = Main.terminal.nextInt();
    }
    return userChoice;
  }

  public static void changeColor() {
    System.out.println("\u001B[31m");
  }

  private static void displayMainTitle() {
    System.out.println("Real Estate Management System\n");
  }

  public static boolean requestUserToContinue(String condition) {
    System.out.println(condition + " : (y/n)");
    String response = Main.terminal.next();
    while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) {
      System.out.println("Do you want to continue : (y/n)");
      System.out.println("Invalid input");
      response = Main.terminal.next();
    }
    return response.equalsIgnoreCase("y");
  }
}
