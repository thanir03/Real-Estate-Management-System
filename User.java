import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

// Base class for Buyer and Seller
// Common properties and methods are defined here

public abstract class User {
  private String fullName;
  private String emailAddress;
  private String phoneNum;
  private LocalDateTime dob;
  private Credential credential;

  public User(String fullName, String emailAddress, String phoneNum, LocalDateTime dob, Credential credential) {
    this.fullName = fullName;
    this.emailAddress = emailAddress;
    this.phoneNum = phoneNum;
    this.dob = dob;
    this.credential = credential;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setPhoneNum(String phoneNum) {
    this.phoneNum = phoneNum;
  }

  public void setDob(LocalDateTime dob) {
    this.dob = dob;
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
    ArrayList<User> userList = new ArrayList<>();
    if (isBuyer) {
      ArrayList<Buyer> buyerList = BuyerDatabase.read();
      userList.addAll(buyerList);
    } else {
      ArrayList<Seller> sellerList = SellerDatabase.read();
      userList.addAll(sellerList);
    }

    User user = null;
    boolean continueNext = true;
    while (continueNext) {
      UI.clearTerminal();
      UI.showMenuTitle(userType + "'s Login");
      System.out.print("\nEnter username : ");
      String username = Main.terminal.next();
      System.out.print("\nEnter password : ");
      String password = Main.terminal.next();
      // downcasting
      user = Credential.isValidCredentials(username, password, isBuyer);
      boolean isValidCredentials = user != null;
      if (isValidCredentials) {
        System.out.println("Successfully logged in as " + user.getFullName());
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
    System.out.print("Enter username : ");
    String username = Main.terminal.next();
    boolean isValidUsername = Credential.validateUsername(username, false);
    while (!isValidUsername) {
      System.out.println("Username already exists or invalid username");
      System.out.print("Enter username : ");
      username = Main.terminal.next();
      isValidUsername = Credential.validateUsername(username, false);
    }
    System.out.print("Enter password : ");
    String password = Main.terminal.next();
    boolean isValidPassword = Credential.validatePassword(password);
    while (!isValidPassword) {
      System.out.println("Invalid password");
      System.out.print("Enter password : ");
      password = Main.terminal.next();
      isValidPassword = Credential.validatePassword(password);
    }

    System.out.print("Enter your fullname: ");
    Main.terminal.nextLine();
    String fullName = Main.terminal.nextLine();

    boolean continueNextEmail = true;
    String emailAddress = "";
    while (continueNextEmail) {
      System.out.print("Enter your email address: ");
      emailAddress = Main.terminal.nextLine();
      if (emailAddress.indexOf("@") == -1 || emailAddress.indexOf(".") == -1) {
        System.out.println("Invalid email address");
        continueNextEmail = true;
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
        String dobString = Main.terminal.next();
        dob = LocalDateTime.parse(dobString + " 00:00", Helper.dateFormat);
        continueNextDob = false;
      } catch (DateTimeParseException e) {
        System.out.println(e.getMessage());
        System.out.println("Invalid date format");
        continueNextDob = true;
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
      ArrayList<Buyer> buyers = BuyerDatabase.read();
      buyers.add(newBuyer);
      BuyerDatabase.write(buyers);
    } else {
      Seller newSeller = new Seller(fullName, emailAddress, phoneNum, dob, newCredential);
      newSeller.setPropertyList(new ArrayList<String>());
      ArrayList<Seller> sellers = SellerDatabase.read();
      sellers.add(newSeller);
      SellerDatabase.write(sellers);
    }
    System.out.println("Successfullly registered");
    UI.pause();
    return true;
  }

  abstract public void menu();

}
