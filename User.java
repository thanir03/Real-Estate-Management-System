import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

// Base class for Buyer and Seller
// Common properties and methods are defined here

public abstract class User {
  // final fields can still be assigned in constructor

  private final String fullName;
  private final String emailAddress;
  private final String phoneNum;
  private final LocalDateTime dob;

  private final Credential credential;

  public User(String fullName, String emailAddress, String phoneNum, LocalDateTime dob, Credential credential) {
    this.fullName = fullName;
    this.emailAddress = emailAddress;
    this.phoneNum = phoneNum;
    this.dob = dob;
    this.credential = credential;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public String getPhoneNum() {
    return phoneNum;
  }

  public LocalDateTime getDOB() {
    return dob;
  }

  public Credential getCredential() {
    return credential;
  }

  public static User authenticate(boolean isBuyer) {
    String userType = isBuyer ? "Buyer" : "Seller";
    UI.clearTerminal();
    UI.showMenuTitle(userType + " Authentication");

    ArrayList<String> authMenu = new ArrayList<>(Arrays.asList("Login", "Register", "Back"));
    int authChoice = UI.displayMenu(authMenu, "Enter authentication choice");
    User user = null;
    if (authChoice == 1) {
      user = login(isBuyer);
    } else if (authChoice == 2) {
      boolean isRegistered = User.register(isBuyer);
      if (isRegistered) {
        user = login(isBuyer);
      }
    } else if (authChoice == 3) {
      return null;
    }
    return user;
  }

  // Login into the system if credential is correct
  public static User login(boolean isBuyer) {
    String userType = isBuyer ? "Buyer" : "Seller";
    User user = null;
    boolean continueNext = true;
    while (continueNext) {
      UI.clearTerminal();
      UI.showMenuTitle(userType + "'s Login");
      System.out.print("\nEnter username : ");
      String username = Main.terminal.next();
      System.out.print("\nEnter password : ");
      String password = Main.terminal.next();
      // down-casting
      user = Credential.isValidCredentials(username, password, isBuyer);
      boolean isValidCredentials = user != null;
      if (isValidCredentials) {
        System.out.println("Successfully logged in as " + user.getFullName());
        UI.pause();
        continueNext = false;
      } else {
        System.out.println("Invalid username or password");
        continueNext = UI.requestUserToContinue("Do you want to try again");
      }
    }
    return user;
  }

  // Registration of new user
  public static boolean register(boolean isBuyer) {
    String userType = isBuyer ? "Buyer" : "Seller";
    UI.clearTerminal();
    UI.showMenuTitle(userType + " Registration");
    // Prompt registration details
    Main.terminal.nextLine();
    System.out.print("Enter username : ");
    String username = Main.terminal.nextLine();
    boolean isValidUsername = Credential.validateUsername(username, isBuyer);
    while (!isValidUsername) {
      System.out.println("Username already exists or invalid username");
      System.out.print("Enter username : ");
      username = Main.terminal.nextLine();
      isValidUsername = Credential.validateUsername(username, isBuyer);
    }
    System.out.print("Enter password : ");
    String password = Main.terminal.nextLine();
    boolean isValidPassword = Credential.validatePassword(password);
    while (!isValidPassword) {
      System.out.println("Invalid password");
      System.out.print("Enter password : ");
      password = Main.terminal.nextLine();
      isValidPassword = Credential.validatePassword(password);
    }

    System.out.print("Enter your Full Name: ");
    String fullName = Main.terminal.nextLine();

    boolean continueNextEmail = true;
    String emailAddress = "";
    while (continueNextEmail) {
      System.out.print("Enter your email address: ");
      emailAddress = Main.terminal.nextLine();
      if (!emailAddress.contains("@") || !emailAddress.contains(".")) {
        System.out.println("Invalid email address");
      } else
        continueNextEmail = false;
    }
    // prompt phone number
    boolean continueNextPhone = true;
    String phoneNum = "";
    while (continueNextPhone) {
      System.out.print("Enter your phone number: ");
      phoneNum = Main.terminal.nextLine();
      continueNextPhone = phoneNum.length() < 8;
      for (int i = 0; i < phoneNum.length(); i++) {
        if (!Character.isDigit(phoneNum.charAt(i))) {
          continueNextPhone = true;
          break;
        }
      }
      if (continueNextPhone) {
        System.out.println("Invalid phone number");
      }
    }
    // prompt dob
    boolean continueNextDob = true;
    LocalDateTime dob = null;
    while (continueNextDob) {
      try {
        System.out.println("Enter your date of birth (DD-MM-YYYY) : ");
        String dobString = Main.terminal.nextLine();
        dob = LocalDateTime.parse(dobString + " 00:00", Helper.dateFormat);
        continueNextDob = false;
      } catch (DateTimeParseException e) {
        System.out.println(e.getMessage());
        System.out.println("Invalid date format");
      }
    }
    int years = (int) dob.until(LocalDateTime.now(), ChronoUnit.YEARS);
    if (years < 18) {
      System.out.println("You are not eligible to register as a seller");
      System.out.println("Unsuccessful registration");
      UI.pause();
      return false;
    }
    // creating new seller's object
    Credential newCredential = new Credential(username, Credential.encryptString(password));
    if (isBuyer) {
      Buyer newBuyer = new Buyer(fullName, emailAddress, phoneNum, dob, newCredential);
      newBuyer.setAppointmentIdList(new ArrayList<>());
      Main.buyerList.add(newBuyer);
      BuyerDatabase.write(Main.buyerList);
    } else {
      Seller newSeller = new Seller(fullName, emailAddress, phoneNum, dob, newCredential);
      newSeller.setPropertyList(new ArrayList<>());
      Main.sellerList.add(newSeller);
      SellerDatabase.write(Main.sellerList);
    }
    System.out.println("Successfully registered");
    UI.pause();
    return true;
  }

  abstract public void menu();

  abstract public String fileString();
}
