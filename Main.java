import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
 * Start point of the application 
 * Prompt user type and enter to their respective application menu if the authentication is valid
 */

public class Main {
  public static Scanner terminal = new Scanner(System.in);

  public static void main(String[] args) {
    boolean toContinue = true;
    while (toContinue) {
      UI.clearTerminal();
      UI.showMenuTitle("MAIN MENU");
      Helper.filterCompletedAppointment();

      ArrayList<String> mainMenu = new ArrayList<>(Arrays.asList("Buyer", "Seller"));
      int userChoice = UI.displayMenu(mainMenu, "Please enter user type");

      if (userChoice == 1) {
        Buyer currentBuyer = BuyerApp.authenticate();
        if (currentBuyer != null) {
          BuyerApp.buyerMenu(currentBuyer);
        }
      } else {
        Seller currentSeller = SellerApp.authenticate();
        if (currentSeller != null) {
          SellerApp.sellerMenu(currentSeller);
        }
      }
      UI.clearTerminal();
      toContinue = UI.requestUserToContinue("Do you want to continue the application");
    }
  }
}
