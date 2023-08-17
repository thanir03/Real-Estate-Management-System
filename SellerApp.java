import java.util.ArrayList;
import java.util.Arrays;

public class SellerApp {

  public static Seller authenticate() {
    UI.clearTerminal();
    UI.showMenuTitle("Seller Authentication");

    ArrayList<String> authMenu = new ArrayList<>(Arrays.asList("Login", "Register"));
    int authChoice = UI.displayMenu(authMenu, "Enter authentication choice");
    Seller seller;
    if (authChoice == 1) {
      seller = login();
    } else if (authChoice == 2) {
      register();
      seller = login();
    } else {
      // Exit seller application
      return null;
    }
    return seller;
  }

  public static Seller login() {
    return null;
  }

  public static void register() {

  }

}
