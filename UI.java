import java.util.ArrayList;

// Class for all utility functions for console interface 
public class UI {

  public static void displayLine() {
    System.out.println("==================================================");
  }

  public static void clearTerminal() {
    System.out.println("\033c");
    displayMainTitle();
  }

  public static void showMenuTitle(String menuTitle) {
    System.out.format("=== %s ===\n\n", menuTitle);
  }

  public static int displayMenu(ArrayList<String> menuOptions, String instruction) {
    for (int i = 0; i < menuOptions.size(); i++) {
      System.out.format("<%d> %s", i + 1, menuOptions.get(i).toUpperCase());
      System.out.println("\n");
    }
    boolean continueNext = true;
    int userChoice = 0;

    while (continueNext) {
      try {
        System.out.print(instruction + ": ");
        userChoice = Main.terminal.nextInt();
        System.out.println("\n");
        if (userChoice < 1 || userChoice > menuOptions.size()) {
          System.out.println("Invalid input\n");
          continueNext = true;
        } else {
          continueNext = false;
        }
      } catch (Exception e) {
        System.out.println("Invalid input\n");
        Main.terminal.next();
        continueNext = true;
      }
    }
    return userChoice;

  }

  private static void displayMainTitle() {
    System.out.println("Real Estate Management System\n");
  }

  public static boolean requestUserToContinue(String condition) {
    System.out.println("\n" + condition + " : (y/n)");
    String response = Main.terminal.next();
    while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) {
      System.out.println("Do you want to continue : (y/n)");
      System.out.println("Invalid input");
      response = Main.terminal.next();
    }
    return response.equalsIgnoreCase("y");
  }

  public static void pause() {
    System.out.print("Please any key to continue.");
    Main.terminal.nextLine();
    Main.terminal.nextLine();
  }
}
