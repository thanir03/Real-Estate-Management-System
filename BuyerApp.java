import java.util.ArrayList;
import java.util.Arrays;

// Buyer App
// all the methods are static since 
public class BuyerApp {

  public static Buyer authenticate() {
    UI.clearTerminal();
    UI.showMenuTitle("Buyer Authentication");

    ArrayList<String> authMenu = new ArrayList<>(Arrays.asList("Login", "Register"));
    int authChoice = UI.displayMenu(authMenu, "Enter authentication choice");
    Buyer buyer = null;
    if (authChoice == 1) {
      buyer = login();
    } else if (authChoice == 2) {
      register();
      buyer = login();
    }
    return buyer;
  }

  public static void showUserMenu(Buyer buyer) {
    System.out.println("Welcome " + buyer.getFullName());
  }

  public static void register() {
    UI.clearTerminal();
    UI.showMenuTitle("Buyer Registration");
  }

  public static Buyer login() {
    // read buyer file
    // check if the username and password is matched
    // if invalid
    boolean continueNext = true;
    while (continueNext) {
      UI.clearTerminal();
      UI.showMenuTitle("Buyer's Login");
      System.out.println("Enter username : ");
      String username = Main.terminal.next();
      System.out.println("Enter password : ");
      String password = Main.terminal.next();
      System.out.println("Invalid username or password");
      continueNext = UI.requestUserToContinue("Do you want to login again : ");
    }

    return null;
  }

}
