import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// Start of the application
// Request user to select user type

public class Main {
  public static Scanner terminal = new Scanner(System.in);

  public static void main(String[] args) {
    boolean toContinue = true;
    while (toContinue) {
      UI.clearTerminal();
      UI.changeColor();
      UI.showMenuTitle("MAIN MENU");

      ArrayList<String> mainMenu = new ArrayList<>(Arrays.asList("Buyer", "Seller"));

      int userChoice = UI.displayMenu(mainMenu, "Please enter user type");

      if (userChoice == mainMenu.size()) {
        System.out.println("Thank you for using the application");
        return;
      }

      if (userChoice == 1) {
        Buyer user = BuyerApp.authenticate();

      } else {
        Seller user = SellerApp.authenticate();
      }

      toContinue = UI.requestUserToContinue("Do you want to continue the application");
    }
  }

}
