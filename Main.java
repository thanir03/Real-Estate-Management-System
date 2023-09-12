import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
 * Start point of the application 
 * Prompt user type and enter to their respective application menu if the authentication is valid
 */

public class Main {
  // One instance of scanner is used throughout the application
  public static Scanner terminal = new Scanner(System.in);

  public static ArrayList<Appointment> appointmentList = AppointmentDatabase.read();

  public static ArrayList<Property> propertyList = PropertyDatabase.read();
  public static ArrayList<Buyer> buyerList = BuyerDatabase.read();
  public static ArrayList<Seller> sellerList = SellerDatabase.read();

  public static void main(String[] args) {
    boolean toContinue = true;
    while (toContinue) {
      UI.clearTerminal();
      UI.showMenuTitle("MAIN MENU");
      // Assign the appointment to completed if the current time exceed the
      // appointment date
      Helper.filterCompletedAppointment();

      ArrayList<String> mainMenu = new ArrayList<>(Arrays.asList("Buyer", "Seller"));
      int userChoice = UI.displayMenu(mainMenu, "Please enter user type");

      if (userChoice == 1) {
        Buyer currentBuyer = (Buyer) User.authenticate(true);
        if (currentBuyer != null) {
          currentBuyer.menu();
        }
      } else {
        Seller currentSeller = (Seller) User.authenticate(false);
        if (currentSeller != null) {
          currentSeller.menu();
        }
      }
      UI.clearTerminal();
      toContinue = UI.requestUserToContinue("Do you want to continue the application");
    }
    terminal.close();
  }
}