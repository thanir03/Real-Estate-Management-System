import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.UUID;

/*
 * Seller's App Functions
 * 1. Login 
 * 2. Register
 * 3. View Appointments
 * 4. Edit Appointments
 * 5. View Properties
 * 6. Add Property
 * 7. Edit Property
 * 8. Logout 
 */

public class SellerApp {

  // Request seller to choose to register or login
  public static Seller authenticate() {
    UI.clearTerminal();
    UI.showMenuTitle("Seller Authentication");

    ArrayList<String> authMenu = new ArrayList<>(Arrays.asList("Login", "Register", "Back"));
    int authChoice = UI.displayMenu(authMenu, "Enter authentication choice");
    Seller seller = null;
    if (authChoice == 1) {
      seller = login();
    } else if (authChoice == 2) {
      boolean isRegistered = register();
      if (isRegistered) {
        seller = login();
      }
    } else if (authChoice == 3) {
      return null;
    }
    return seller;
  }

  // Login into the system if credential is correct
  public static Seller login() {
    ArrayList<Seller> sellerList = SellerDatabase.read();
    ArrayList<User> userList = new ArrayList<>();
    userList.addAll(sellerList);
    Seller seller = null;
    boolean continueNext = true;
    while (continueNext) {
      UI.clearTerminal();
      UI.showMenuTitle("Seller's Login");
      System.out.print("\nEnter username : ");
      String username = Main.terminal.next();
      System.out.print("\nEnter password : ");
      String password = Main.terminal.next();
      // downcasting
      seller = (Seller) Credential.isValidCredentials(username, password, userList);
      boolean isValidCredentials = seller != null;
      if (isValidCredentials) {
        System.out.println("Successfully logged in as " + seller.getFullName());
        continueNext = false;
      } else {
        System.out.println("Invalid username or password");
        continueNext = UI.requestUserToContinue("Do you want to try again");
      }
    }
    return seller;
  }

  // Registration of new seller
  public static boolean register() {
    UI.clearTerminal();
    UI.showMenuTitle("Seller Registration");
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
    Seller newSeller = new Seller(fullName, emailAddress, phoneNum, dob, newCredential);
    newSeller.setPropertyList(new ArrayList<String>());
    ArrayList<Seller> sellers = SellerDatabase.read();
    sellers.add(newSeller);
    SellerDatabase.write(sellers);
    System.out.println("Successfullly registered");
    UI.pause();
    return true;
  }

  // main menu of the seller
  public static void sellerMenu(Seller currentSeller) {
    boolean continueNext = true;
    while (continueNext) {
      UI.clearTerminal();
      UI.showMenuTitle("Seller's Menu");
      System.out.println("Welcome " + currentSeller.getFullName() + "\n");
      List<String> menu = Arrays.asList("View Appointments", "Edit Appointments", "View Properties", "Add Property",
          "Edit Property", "Logout");
      ArrayList<String> sellerMenu = new ArrayList<>(menu);
      int menuOption = UI.displayMenu(sellerMenu, "Please choose your menu option");
      switch (menuOption) {
        case 1:
          viewAppointment(currentSeller);
          break;
        case 2:
          editAppointment(currentSeller);
          break;
        case 3:
          viewProperties(currentSeller);
          break;
        case 4:
          addProperty(currentSeller);
          break;
        case 5:
          editProperty(currentSeller);
          break;
        case 6:
          System.out.println("Sucessfully logout");
          continueNext = false;
        default:
          break;
      }
    }
  }

  // View all property owned by the seller
  public static void viewProperties(Seller currentSeller) {
    UI.clearTerminal();
    UI.showMenuTitle("View Properties");
    ArrayList<Property> properties = currentSeller.getPropertyList();
    System.out.println("Total Properties : " + properties.size() + "\n");
    // Display property details
    for (int i = 0; i < properties.size(); i++) {
      System.out.println("Property " + (i + 1));
      UI.displayLine();
      System.out.println(properties.get(i).display());
      System.out.println("Number of Appointments : " + properties.get(i).getAppointmentIdList().size() + "\n");
      System.out.println("\n");
    }
    UI.pause();
  }

  // View appointments that the property of the seller has
  public static void viewAppointment(Seller currentSeller) {
    UI.clearTerminal();
    UI.showMenuTitle("View Appointment");
    ArrayList<Appointment> appointments = AppointmentDatabase.read();
    ArrayList<Appointment> sellerAppointments = new ArrayList<>();
    // filter the appointment belongs to the seller
    for (Appointment appointment : appointments) {
      if (appointment.getProperty().getSellerId().equals(currentSeller.getCredential().getUsername())) {
        sellerAppointments.add(appointment);
      }
    }

    // Display appointment
    for (int i = 0; i < sellerAppointments.size(); i++) {
      System.out.println("\n");
      System.out.println("Appointment " + (i + 1));
      UI.displayLine();
      System.out.println(sellerAppointments.get(i).display());
      if (sellerAppointments.get(i).getStatus().equals(Appointment.PENDING_STATUS)) {
        System.out.println("Reminder : You have not approve this appointment yet");
      }
      System.out.println("\n");
    }
    if (sellerAppointments.size() == 0) {
      System.out.println("You have no appointments");
    }
    UI.pause();
  }

  // Edit appointment details
  // 1. Edit appointment details
  // 2. Cancel appointment
  // 3. Confirm pending appointment
  public static void editAppointment(Seller currentSeller) {
    UI.clearTerminal();
    UI.showMenuTitle("Edit Appointment");
    ArrayList<Appointment> appointments = AppointmentDatabase.read();
    ArrayList<Appointment> sellerAppointments = new ArrayList<>();

    // filter the appointment belongs to the seller and appointment that are still
    // pending or on going
    for (Appointment appointment : appointments) {
      if (appointment.getProperty().getSellerId().equals(currentSeller.getCredential().getUsername())
          && !appointment.getStatus().equals(Appointment.CANCELLED_STATUS)
          && !appointment.getStatus().equals(Appointment.COMPLETED_STATUS)) {
        sellerAppointments.add(appointment);
      }
    }
    if (sellerAppointments.size() == 0) {
      System.out.println("You have no ongoing or pending appointments \n");
      UI.pause();
      return;
    }
    for (int i = 0; i < sellerAppointments.size(); i++) {
      System.out.println("\n");
      System.out.println("Appointment " + (i + 1));
      UI.displayLine();
      System.out.println(sellerAppointments.get(i).display());
      if (sellerAppointments.get(i).getStatus().equals(Appointment.PENDING_STATUS)) {
        System.out.println("Reminder : You have not approve this appointment yet");
      }
      System.out.println("\n");
    }
    // selecting appointment
    int appointmentId = 0;
    boolean continueNext = true;
    while (continueNext) {
      try {
        System.out.println("Enter appointment number (it can be found above) to edit : ");
        appointmentId = Main.terminal.nextInt();
        if (appointmentId < 1 || appointmentId > sellerAppointments.size()) {
          System.out.println("Invalid appointment id");
          continueNext = true;
        } else
          continueNext = false;
      } catch (InputMismatchException e) {
        System.out.println("Invalid appointment id");
        Main.terminal.nextLine();
        continueNext = true;
      }
    }
    Appointment selectedAppointment = sellerAppointments.get(appointmentId - 1);
    // showing appointment edit menu
    UI.clearTerminal();
    UI.showMenuTitle("Edit Appointment");
    System.out.println("Selected appointment");
    System.out.println(selectedAppointment.display());

    int option = UI.displayMenu(
        new ArrayList<>(
            Arrays.asList("Update Date Of Appointment", "Cancel Appointment", "Confirm Appointment", "Back")),
        "Select Action");

    if (option == 1) {
      LocalDateTime appointmentDate = null;
      boolean continueNextDate = true;
      Buyer buyer = selectedAppointment.getBuyer();
      while (continueNextDate) {
        appointmentDate = Helper.promptDateAndTime();
        if (appointmentDate.equals(selectedAppointment.getDateOfAppointment())) {
          System.out.println("Appointment Date and time is the same");
          continueNextDate = true;
        } else {
          continueNextDate = !(Helper.isDateInValidTimeSlot(appointmentDate, buyer, currentSeller,
              selectedAppointment.getAppointmentId()));
          if (continueNextDate) {
            System.out.println("Date and time is already booked in either buyer's slot or seller's slot");
          }
        }
      }
      selectedAppointment.setDateOfAppointment(appointmentDate);
      System.out.println("Successfully updated appointment date");

    } else if (option == 2) {
      selectedAppointment.setStatus(Appointment.CANCELLED_STATUS);
      System.out.println("Successfully cancelled appointment");
    } else if (option == 3) {
      if (selectedAppointment.getStatus().equals(Appointment.PENDING_STATUS)) {
        selectedAppointment.setStatus(Appointment.ONGOING_STATUS);
        System.out.println("Successfully confirmed appointment on "
            + selectedAppointment.getDateOfAppointment().format(Helper.dateFormat));
      } else {
        System.out.println("This appointment is either cancelled or completed or on-going");
      }
    } else if (option == 4) {
      return;
    }

    // Update the edited appointment to the files
    for (int i = 0; i < appointments.size(); i++) {
      if (appointments.get(i).getAppointmentId().equals(selectedAppointment.getAppointmentId())) {
        appointments.set(i, selectedAppointment);
      }
    }
    AppointmentDatabase.write(appointments);
    UI.pause();
  }

  // adding new property to the system
  public static void addProperty(Seller currentSeller) {
    UI.clearTerminal();
    UI.showMenuTitle("Add Property");
    System.out.println("Property details \n");
    // requesting property details
    int numberOfRooms = Helper.promptRoomNumber();
    int floorSize = Helper.promptFloorSize();
    Address address = Helper.promptAddress();
    Integer[] priceRange = Helper.promptPriceRange();
    ArrayList<String> facilityList = Helper.promptFacilityList();
    boolean isListed = Helper.promptIsListed();
    String propertyId = UUID.randomUUID().toString();
    // creating property object
    Property property = new Property(currentSeller.getCredential().getUsername(), propertyId, numberOfRooms, priceRange,
        floorSize, isListed, facilityList);
    property.setAddress(address);
    property.setAppointmentList(new ArrayList<>());
    // adding property to the files
    ArrayList<Property> properties = PropertyDatabase.read();
    properties.add(property);
    ArrayList<Seller> sellers = SellerDatabase.read();
    for (int i = 0; i < sellers.size(); i++) {
      if (sellers.get(i).getCredential().getUsername().equals(currentSeller.getCredential().getUsername())) {
        sellers.get(i).addProperty(propertyId);
      }
    }
    SellerDatabase.write(sellers);
    PropertyDatabase.write(properties);
    System.out.println("Successfully added Property");
    UI.pause();
  }

  // editing property details
  public static void editProperty(Seller currentSeller) {
    UI.clearTerminal();
    UI.showMenuTitle("Edit Property");
    ArrayList<Property> properties = currentSeller.getPropertyList();
    for (int i = 0; i < properties.size(); i++) {
      System.out.println("Property " + (i + 1));
      UI.displayLine();
      System.out.println(properties.get(i).display());
      System.out.println("\n");
    }

    if (properties.size() == 0) {
      System.out.println("You have no properties");
      UI.pause();
      return;
    }

    System.out.println("Enter property number (refer above) to edit");
    int propertyNumber = 0;
    try {
      propertyNumber = Main.terminal.nextInt();
    } catch (Exception e) {
      System.out.println("Invalid property number");
      UI.pause();
      return;
    }
    if (propertyNumber <= 0 || propertyNumber > properties.size()) {
      System.out.println("Invalid property number");
      UI.pause();
      return;
    }

    UI.clearTerminal();
    UI.showMenuTitle("Edit Property");
    Property selectedProperty = properties.get(propertyNumber - 1);
    System.out.println("Selected property ");
    System.out.println(selectedProperty.display());
    int option = UI.displayMenu(
        new ArrayList<>(Arrays.asList("Edit Address", "Edit Number of Rooms", "Edit Floor Size", "Edit Price Range",
            "Edit Facility List", "Edit sale details", "Back")),
        "Select Action");
    switch (option) {
      case 1:
        Address address = Helper.promptAddress();
        selectedProperty.setAddress(address);
        break;
      case 2:
        int numberOfRooms = Helper.promptRoomNumber();
        selectedProperty.setNumberOfRooms(numberOfRooms);
        break;
      case 3:
        int floorSize = Helper.promptFloorSize();
        selectedProperty.setFloorSize(floorSize);
        break;
      case 4:
        Integer[] priceRange = Helper.promptPriceRange();
        selectedProperty.setPriceRange(priceRange);
        break;
      case 5:
        Main.terminal.nextLine();
        ArrayList<String> facilityList = Helper.promptFacilityList();
        selectedProperty.setFacilityList(facilityList);
        break;
      case 6:
        boolean isListed = Helper.promptIsListed();
        if (!isListed)
          System.out.println("Reminder : Your existing appointment will not be cancelled.");
        selectedProperty.setListed(isListed);
        break;
      case 7:
        return;
    }
    ArrayList<Property> propertyList = PropertyDatabase.read();
    for (int i = 0; i < propertyList.size(); i++) {
      if (propertyList.get(i).getPropertyId().equals(selectedProperty.getPropertyId())) {
        propertyList.set(i, selectedProperty);
        break;
      }
    }

    PropertyDatabase.write(propertyList);
    System.out.println("Successfully edited property details");
    UI.pause();
  }
}
