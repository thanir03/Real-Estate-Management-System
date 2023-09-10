import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/*
 * Buyer App 
 * Contains all the functionality of the buyer
 * 1. View Property
 * 2. Search property
 * 3. Edit appointment Details
 * 4. Cancel appointment
 * 5. Set Property Preferences
 * 6. Search Property
 * 7. Login 
 * 8. Register
 */

public class BuyerApp {

  // Request buyer to login or register
  public static Buyer authenticate() {
    UI.clearTerminal();
    UI.showMenuTitle("Buyer Authentication");

    ArrayList<String> authMenu = new ArrayList<>(Arrays.asList("Login", "Register", "Back"));
    int authChoice = UI.displayMenu(authMenu, "Enter authentication choice");
    Buyer buyer = null;
    if (authChoice == 1) {
      buyer = login();
    } else if (authChoice == 2) {
      boolean isSuccessful = register();
      if (isSuccessful) {
        buyer = login();
      }
    } else if (authChoice == 3) {
      return null;
    }
    return buyer;
  }

  // Register new Buyer
  public static boolean register() {
    UI.clearTerminal();
    UI.showMenuTitle("Buyer Registration");
    System.out.print("Enter username : ");
    String username = Main.terminal.next();
    boolean isValidUsername = Credential.validateUsername(username, true);
    while (!isValidUsername) {
      System.out.println("Username already exists or invalid username\n");
      System.out.print("Enter username : ");
      username = Main.terminal.next();
      isValidUsername = Credential.validateUsername(username, true);
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
    // prompt fullname
    System.out.print("Enter your fullname: ");
    Main.terminal.nextLine();
    String fullName = Main.terminal.nextLine();

    // prompt email
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
      System.out.println("You are not eligible to register as a buyer");
      System.out.println("Unsuccessful registration");
      UI.pause();
      return false;
    }
    Credential newCredential = new Credential(username, Credential.encryptString(password));
    Buyer newBuyer = new Buyer(fullName, emailAddress, phoneNum, dob, newCredential);
    newBuyer.setAppointmentIdList(new ArrayList<>());

    ArrayList<Buyer> buyers = BuyerDatabase.read();
    buyers.add(newBuyer);
    BuyerDatabase.write(buyers);
    System.out.println("Successfullly registered");
    UI.pause();
    return true;
  }

  // Login into the system if credentials are correct
  public static Buyer login() {
    ArrayList<Buyer> buyerList = BuyerDatabase.read();
    // downcasting buyer array type to user array type
    ArrayList<User> userList = new ArrayList<>();
    userList.addAll(buyerList);
    Buyer buyer = null;
    boolean continueNext = true;
    while (continueNext) {
      UI.clearTerminal();
      UI.showMenuTitle("Buyer's Login");
      System.out.print("Enter username : ");
      String username = Main.terminal.next();
      System.out.print("\nEnter password : ");
      String password = Main.terminal.next();
      System.out.println("\n");
      // Credential.isValidCredentials function returns a user type (therefore we need
      // to upcast it back to buyer type)
      buyer = (Buyer) Credential.isValidCredentials(username, password, userList);
      boolean isValidCredentials = buyer != null;
      if (isValidCredentials) {
        System.out.println("Successfully logged in as " + buyer.getFullName());
        continueNext = false;
        UI.pause();
      } else {
        System.out.println("Invalid username or password");
        continueNext = UI.requestUserToContinue("Do you want to try again");
      }
    }

    return buyer;
  }

  // buyer's menu which displays all the functionality of the buyer app
  public static void buyerMenu(Buyer currentBuyer) {
    boolean continueNext = true;

    while (continueNext) {
      UI.clearTerminal();
      UI.showMenuTitle("Buyer's Menu");
      System.out.println("Welcome " + currentBuyer.getFullName() + "\n");
      ArrayList<String> buyerMenu = new ArrayList<>(
          Arrays.asList("View Properties & Book Appointments", "View appointments", "Edit appointment Details",
              "Search property",
              "Logout"));
      int menuOption = UI.displayMenu(buyerMenu, "Please choose your menu option");
      switch (menuOption) {
        case 1:
          viewProperties(currentBuyer);
          break;
        case 2:
          viewAppointment(currentBuyer);
          break;
        case 3:
          editAppointment(currentBuyer);
          break;
        case 4:
          searchProperty(currentBuyer);
          break;
        case 5:
          System.out.println("Sucessfully logout");
          continueNext = false;
        default:
          break;
      }
    }
  }

  // Display all the properties and
  // allow buyer to book appointment with by selecting the property
  public static void viewProperties(Buyer currentBuyer) {
    ArrayList<Property> propertyList = PropertyDatabase.read();
    UI.clearTerminal();
    UI.showMenuTitle("View Properties");
    ArrayList<Property> listedProperties = new ArrayList<>();
    for (Property property : propertyList) {
      if (property.getIsListed()) {
        listedProperties.add(property);
      }
    }

    for (int i = 0; i < listedProperties.size(); i++) {
      System.out.println("Property " + (i + 1) + "\n");
      System.out.println(listedProperties.get(i).display());
      Appointment appointment = currentBuyer.hasAppointmentOnProperty(listedProperties.get(i).getPropertyId());
      if (appointment != null) {
        System.out.println("You have an appointment in this property on "
            + appointment.getDateOfAppointment().format(Helper.dateFormat) + "\n");
      }
    }
    if (listedProperties.size() == 0) {
      System.out.println("No listed properties");
      UI.pause();
      return;
    }
    // Booking appointment
    int option = UI.displayMenu(new ArrayList<>(Arrays.asList("Yes", "No")), "Do you want to book appointment");
    if (option == 2) {
      return;
    }
    System.out.print("Enter the property number (refer above) to book appointments : ");
    int propertyNumber = 0;
    try {

      propertyNumber = Main.terminal.nextInt();
      if (propertyNumber < 1 || propertyNumber > listedProperties.size()) {
        System.out.println("Invalid property number");
        UI.pause();
        return;
      }
    } catch (Exception e) {
      System.out.println("Invalid property number");
      UI.pause();
      return;
    }
    Property selectedProperty = listedProperties.get(propertyNumber - 1);
    bookAppointment(currentBuyer, selectedProperty);
    UI.pause();

  }

  // View all buyers appointment
  public static void viewAppointment(Buyer currentBuyer) {
    UI.clearTerminal();
    UI.showMenuTitle("View Appointment");
    ArrayList<Appointment> userAppointments = currentBuyer.getAppointments();
    if (userAppointments.size() == 0) {
      System.out.println("You have no appointments");
    } else {
      System.out.println("Total Appointment : " + userAppointments.size());
      for (int i = 0; i < userAppointments.size(); i++) {
        System.out.println("\nAppointment " + (i + 1) + "\n");
        System.out.println(userAppointments.get(i).display());
      }
    }
    UI.pause();
  }

  // edit buyers appointment
  // 1. Edit date of appointment
  // 2. Cancel appointment
  public static void editAppointment(Buyer currentBuyer) {
    // Edit appointment
    UI.clearTerminal();
    UI.showMenuTitle("Edit Appointment");
    ArrayList<Appointment> userAppointments = currentBuyer.getAppointments();
    ArrayList<Appointment> validUserAppointments = new ArrayList<>();

    for (Appointment appointment : userAppointments) {
      if (!appointment.getStatus().equals(Appointment.CANCELLED_STATUS)
          && !appointment.getStatus().equals(Appointment.COMPLETED_STATUS)) {
        validUserAppointments.add(appointment);
      }
    }

    for (int i = 0; i < validUserAppointments.size(); i++) {
      System.out.println("Appointment " + (i + 1));
      System.out.println(validUserAppointments.get(i).display());
    }

    if (validUserAppointments.size() == 0) {
      System.out.println("You have no ongoing or pending appointments");
      UI.pause();
      return;
    }

    System.out.println("Enter the appointment number (refer above) to edit : ");

    int appointmentNumber = 0;
    try {
      appointmentNumber = Main.terminal.nextInt();
    } catch (Exception e) {
      appointmentNumber = 0;
    }

    if (appointmentNumber <= 0 || appointmentNumber > validUserAppointments.size()) {
      System.out.println("Invalid appointment number");
      UI.pause();
      return;
    }

    AppointmentDatabase.read();
    Appointment selectedAppointment = validUserAppointments.get(appointmentNumber - 1);

    UI.clearTerminal();
    UI.showMenuTitle("Edit Appointment");

    System.out.println("Selected Appointment\n");
    System.out.println(selectedAppointment.display());

    int option = UI.displayMenu(new ArrayList<>(Arrays.asList("Update Date Of Appointment", "Cancel Appointment")),
        "Select Action");

    if (option == 1) {
      // Edit appointment
      LocalDateTime appointmentDate = null;
      boolean continueNext = true;
      Seller seller = selectedAppointment.getProperty().getSeller();
      while (continueNext) {
        appointmentDate = Helper.promptDateAndTime();
        if (appointmentDate.equals(selectedAppointment.getDateOfAppointment())) {
          System.out.println("Appointment Date and time is the same");
          continueNext = true;
        } else {
          continueNext = !(Helper.isDateInValidTimeSlot(appointmentDate, currentBuyer, seller,
              selectedAppointment.getAppointmentId()));
          if (continueNext) {
            System.out.println("Date and time is already booked in either buyer's slot or seller's slot");
          }
        }
      }

      selectedAppointment.setStatus(Appointment.PENDING_STATUS);
      selectedAppointment.setDateOfAppointment(appointmentDate);
      System.out.println("Successfully Edited Appointment date & Pending approval from seller");
    } else {
      // Cancel appointment
      selectedAppointment.setStatus(Appointment.CANCELLED_STATUS);
      System.out.println("Successfully Cancelled appointment");
    }
    ArrayList<Appointment> appointments = AppointmentDatabase.read();
    for (int i = 0; i < appointments.size(); i++) {
      if (appointments.get(i).getAppointmentId().equals(selectedAppointment.getAppointmentId())) {
        appointments.set(i, selectedAppointment);
      }
    }
    AppointmentDatabase.write(appointments);
    UI.pause();
  }

  // Search property by
  // 1. no of rooms
  // 2. facility
  // 3. Price Range
  // 4. Facility
  // Allow buyer to book appointment by selecting property
  public static void searchProperty(Buyer currentBuyer) {
    // Search property
    ArrayList<Property> propertyList = PropertyDatabase.read();
    ArrayList<Property> listedProperties = new ArrayList<>();
    for (Property property : propertyList) {
      if (property.getIsListed()) {
        listedProperties.add(property);
      }
    }
    UI.clearTerminal();
    UI.showMenuTitle("Search Property");
    ArrayList<String> searchOptions = new ArrayList<>(
        Arrays.asList("Search By City", "Search By Number of Rooms", "Search by Price Range", "Search facility"));
    int searchOption = UI.displayMenu(searchOptions, "Select Search Option");
    ArrayList<Property> filteredPropertyList = new ArrayList<>();
    String searchTitle = "";
    if (searchOption == 1) {
      // Search by city
      Main.terminal.nextLine();
      System.out.println("\nEnter the city : ");
      String city = Main.terminal.nextLine();
      for (Property property : listedProperties) {
        if (property.getAddress().getCity().equalsIgnoreCase(city)) {
          filteredPropertyList.add(property);
        }
      }
      searchTitle = "Search by " + city + " city";

    } else if (searchOption == 2) {
      // Search by number of rooms
      int numberOfRooms = Helper.promptRoomNumber();
      for (Property property : listedProperties) {
        if (property.getNumberOfRooms() == numberOfRooms) {
          filteredPropertyList.add(property);
        }
      }
      searchTitle = "Search by " + numberOfRooms + " rooms";

    } else if (searchOption == 3) {
      // search by price range request both lower and upper bound
      Integer[] priceRange = Helper.promptPriceRange();
      for (Property property : listedProperties) {
        if (property.getPriceRange()[0] >= priceRange[0] && property.getPriceRange()[1] <= priceRange[1]) {
          filteredPropertyList.add(property);
        }
      }
      searchTitle = "Search by price range between " + priceRange[0] + " and " + priceRange[1] + " rooms";
    } else {
      // Search facility
      System.out.println("Enter facility : ");
      String facility = Main.terminal.next();
      facility = facility.trim();
      for (Property property : listedProperties) {
        if (property.getFacilityList().indexOf(facility) != -1) {
          filteredPropertyList.add(property);
        }
      }
      searchTitle = "Search by " + facility + "facility";
    }

    UI.clearTerminal();
    UI.showMenuTitle(searchTitle);
    if (filteredPropertyList.size() == 0) {
      System.out.println("No property found");
      UI.pause();
      return;
    }

    for (int i = 0; i < filteredPropertyList.size(); i++) {
      System.out.println("Property " + (i + 1) + "\n");
      System.out.println(filteredPropertyList.get(i).display());
      // To check whether the user have ald booked appointment in this property
      Appointment appointment = currentBuyer.hasAppointmentOnProperty(filteredPropertyList.get(i).getPropertyId());
      if (appointment != null)
        System.out.println("You have an appointment in this property on "
            + appointment.getDateOfAppointment().format(Helper.dateFormat) + "\n");
    }

    int option = UI.displayMenu(new ArrayList<>(Arrays.asList("Yes", "No")), "Do you want to book appointment");
    if (option == 2)
      return;
    System.out.print("Enter the property number (refer above) to book appointments : ");
    int propertyNumber = Main.terminal.nextInt();
    if (propertyNumber < 1 || propertyNumber > listedProperties.size()) {
      System.out.println("Invalid property number");
      UI.pause();
      return;
    }
    Property selectedProperty = listedProperties.get(propertyNumber - 1);
    bookAppointment(currentBuyer, selectedProperty);
    UI.pause();
  }

  // booking appointment by requesting appointment details
  private static void bookAppointment(Buyer currentBuyer, Property selectedProperty) {
    // To check whether the buyer has already booked appointment in this property
    Appointment appointment = currentBuyer.hasAppointmentOnProperty(selectedProperty.getPropertyId());
    if (appointment != null) {
      System.out.println("You have an appointment in this property on "
          + appointment.getDateOfAppointment().format(Helper.dateFormat) + "\n");
      System.out.println("Cannot book this appointment");
      return;
    }
    ArrayList<Appointment> appointmentList = AppointmentDatabase.read();
    ArrayList<Property> propertyList = PropertyDatabase.read();
    ArrayList<Buyer> buyerList = BuyerDatabase.read();
    Seller seller = selectedProperty.getSeller();

    UI.clearTerminal();
    UI.showMenuTitle("Book Appointment");
    System.out.println("Selected Property for visitation");
    System.out.println(selectedProperty.display());
    boolean continueNext = true;
    LocalDateTime appointmentDate = null;
    while (continueNext) {
      appointmentDate = Helper.promptDateAndTime();
      continueNext = !(Helper.isDateInValidTimeSlot(appointmentDate, currentBuyer, seller, ""));
      if (continueNext) {
        System.out.println("Date and time is already booked in either buyer's slot or seller's slot");
      }
    }

    String uniqueAppointmentId = UUID.randomUUID().toString();
    Appointment newAppointment = new Appointment(uniqueAppointmentId, currentBuyer.getCredential().getUsername(),
        selectedProperty.getPropertyId());
    newAppointment.setDateOfAppointment(appointmentDate);
    newAppointment.setStatus("pending");
    appointmentList.add(newAppointment);
    currentBuyer.addAppointment(newAppointment);

    for (Property property : propertyList) {
      if (property.getPropertyId().equals(selectedProperty.getPropertyId())) {
        property.getAppointmentList().add(uniqueAppointmentId);
      }
    }
    for (int i = 0; i < buyerList.size(); i++) {
      if (buyerList.get(i).getCredential().getUsername().equals(currentBuyer.getCredential().getUsername())) {
        buyerList.set(i, currentBuyer);
      }
    }
    BuyerDatabase.write(buyerList);
    PropertyDatabase.write(propertyList);
    AppointmentDatabase.write(appointmentList);
    System.out.println("Successfully booked appointment & Pending approval from seller");
  }

}
