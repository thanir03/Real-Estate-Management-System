import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

//Buyer class

public class Buyer extends User {
  private ArrayList<String> appointmentIdList;

  public Buyer(String fullName, String emailAddress, String phoneNum, LocalDateTime dob, Credential credential) {
    super(fullName, emailAddress, phoneNum, dob, credential);
  }

  // get Appointment Id List
  public ArrayList<String> getAppointmentIdList() {
    return appointmentIdList;
  }

  public void setAppointmentIdList(ArrayList<String> appointmentIdList) {
    this.appointmentIdList = appointmentIdList;
  }

  public void addAppointment(Appointment appointment) {
    appointmentIdList.add(appointment.getAppointmentId());
  }

  // get appointment object from buyer's appointmentIdList
  public ArrayList<Appointment> getAppointments() {
    ArrayList<Appointment> buyerAppointment = new ArrayList<>();
    for (String id : appointmentIdList) {
      for (Appointment appointment : Main.appointmentList) {
        if (appointment.getAppointmentId().equals(id)) {
          buyerAppointment.add(appointment);
        }
      }
    }
    return buyerAppointment;
  }

  // check whether buyer has appointment on an ongoing or pending property
  public Appointment hasAppointmentOnProperty(String propertyId) {
    ArrayList<Appointment> appointmentList = getAppointments();
    for (Appointment appointment : appointmentList) {
      if (appointment.getPropertyId().equals(propertyId) &&
          (appointment.getStatus().equals(Appointment.ONGOING_STATUS) ||
              appointment.getStatus().equals(Appointment.PENDING_STATUS))) {
        return appointment;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    // display brief information of the buyer
    String str = "";
    str += "Full Name : " + this.getFullName() + "\n";
    str += "Email Address : " + this.getEmailAddress() + "\n";
    str += "Phone Number : " + this.getPhoneNum() + "\n";
    return str;
  }

  @Override
  public String fileString() {
    StringBuilder str = new StringBuilder();
    str.append(getCredential().getUsername()).append("|");
    str.append(getCredential().getPassword()).append("|");
    str.append(getFullName()).append("|");
    str.append(getEmailAddress()).append("|");
    str.append(getDOB().format(Helper.dateFormat)).append("|");
    str.append(getPhoneNum()).append("|");
    str.append("[");
    for (int i = 0; i < appointmentIdList.size(); i++) {
      str.append(appointmentIdList.get(i));
      if (i != appointmentIdList.size() - 1) {
        str.append(",");
      }
    }
    str.append("]" + "\n");
    return str.toString();
  }

  // ==================== FUNCTIONALITY OF THE BUYER ============================

  @Override
  public void menu() {
    boolean continueNext = true;

    while (continueNext) {
      UI.clearTerminal();
      UI.showMenuTitle("Buyer's Menu");
      System.out.println("Welcome " + getFullName() + "\n");
      ArrayList<String> buyerMenu = new ArrayList<>(
          Arrays.asList("View Properties & Book Appointments", "View appointments", "Edit appointment Details",
              "Search property",
              "Logout"));
      int menuOption = UI.displayMenu(buyerMenu, "Please choose your menu option");
      switch (menuOption) {
        case 1:
          viewProperties();
          break;
        case 2:
          viewAppointment();
          break;
        case 3:
          editAppointment();
          break;
        case 4:
          searchProperty();
          break;
        case 5:
          System.out.println("Successfully logout");
          continueNext = false;
        default:
          break;
      }
    }
  }

  // Display all the properties and
  // allow buyer to book appointment with by selecting the property
  public void viewProperties() {
    UI.clearTerminal();
    UI.showMenuTitle("View Properties");
    ArrayList<Property> listedProperties = new ArrayList<>();
    for (Property property : Main.propertyList) {
      if (property.getIsListed()) {
        listedProperties.add(property);
      }
    }
    for (int i = 0; i < listedProperties.size(); i++) {
      System.out.println("Property " + (i + 1));
      UI.displayLine();
      System.out.println(listedProperties.get(i));
      Appointment appointment = hasAppointmentOnProperty(listedProperties.get(i).getPropertyId());
      if (appointment != null) {
        System.out.println("You have an appointment in this property on " + appointment.getFormatedDateTime() + "\n");
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
    int propertyNumber;
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
    bookAppointment(selectedProperty);
    UI.pause();

  }

  // View all buyers appointment
  public void viewAppointment() {
    UI.clearTerminal();
    UI.showMenuTitle("View Appointment");
    ArrayList<Appointment> userAppointments = getAppointments();
    if (userAppointments.size() == 0) {
      System.out.println("You have no appointments");
    } else {
      System.out.println("Total Appointment : " + userAppointments.size());
      for (int i = 0; i < userAppointments.size(); i++) {
        System.out.println("\nAppointment " + (i + 1));
        UI.displayLine();
        System.out.println(userAppointments.get(i));
      }
    }
    UI.pause();
  }

  // edit buyers appointment
  // 1. Edit date of appointment
  // 2. Cancel appointment
  public void editAppointment() {
    // Edit appointment
    UI.clearTerminal();
    UI.showMenuTitle("Edit Appointment");
    ArrayList<Appointment> userAppointments = getAppointments();
    ArrayList<Appointment> validUserAppointments = new ArrayList<>();

    for (Appointment appointment : userAppointments) {
      if (!appointment.getStatus().equals(Appointment.CANCELLED_STATUS)
          && !appointment.getStatus().equals(Appointment.COMPLETED_STATUS)) {
        validUserAppointments.add(appointment);
      }
    }
    for (int i = 0; i < validUserAppointments.size(); i++) {
      System.out.println("Appointment " + (i + 1));
      UI.displayLine();
      System.out.println(validUserAppointments.get(i));
    }
    if (validUserAppointments.size() == 0) {
      System.out.println("You have no ongoing or pending appointments");
      UI.pause();
      return;
    }
    System.out.println("Enter the appointment number (refer above) to edit : ");
    int appointmentNumber;
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

    Appointment selectedAppointment = validUserAppointments.get(appointmentNumber - 1);

    UI.clearTerminal();
    UI.showMenuTitle("Edit Appointment");
    System.out.println("Selected Appointment\n");
    System.out.println(selectedAppointment);
    int option = UI.displayMenu(
        new ArrayList<>(Arrays.asList("Update Date Of Appointment", "Cancel Appointment", "Back")),
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
        } else {
          continueNext = !(Helper.isDateInValidTimeSlot(appointmentDate, this, seller,
              selectedAppointment.getAppointmentId()));
          if (continueNext) {
            System.out.println("Date and time is already booked in either buyer's slot or seller's slot");
          }
        }
      }
      selectedAppointment.setStatus(Appointment.PENDING_STATUS);
      selectedAppointment.setDateOfAppointment(appointmentDate);
      System.out.println("Successfully Edited Appointment date & Pending approval from seller");
    } else if (option == 2) {
      // Cancel appointment
      selectedAppointment.setStatus(Appointment.CANCELLED_STATUS);
      System.out.println("Successfully Cancelled appointment");
    } else
      return;
    AppointmentDatabase.write(Main.appointmentList);
    UI.pause();
  }

  // Search property by
  // 1. no of rooms
  // 2. facility
  // 3. Price Range
  // 4. Facility
  // Allow buyer to book appointment by selecting property
  public void searchProperty() {
    // Search property
    ArrayList<Property> listedProperties = new ArrayList<>();
    for (Property property : Main.propertyList) {
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
    String searchTitle;
    // Search by city
    if (searchOption == 1) {
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
      searchTitle = "Search by price range between " + " RM " + priceRange[0] + " and " + " RM " + priceRange[1]
          + " rooms";
    } else {
      // Search facility
      System.out.println("Enter facility : ");
      Main.terminal.nextLine(); // clear buffer
      String facility = Main.terminal.nextLine();
      facility = facility.trim();
      for (Property property : listedProperties) {
        for (String propertyFacility : property.getFacilityList()) {
          if (propertyFacility.equalsIgnoreCase(facility)) {
            filteredPropertyList.add(property);
          }
        }
      }
      searchTitle = "Search by " + facility + " facility";
    }

    UI.clearTerminal();
    UI.showMenuTitle(searchTitle);
    if (filteredPropertyList.size() == 0) {
      System.out.println("No property found");
      UI.pause();
      return;
    }

    for (int i = 0; i < filteredPropertyList.size(); i++) {
      System.out.println("Property " + (i + 1));
      UI.displayLine();
      System.out.println(filteredPropertyList.get(i));
      // To check whether the user have ald booked appointment in this property
      Appointment appointment = hasAppointmentOnProperty(filteredPropertyList.get(i).getPropertyId());
      if (appointment != null)
        System.out.println("You have an appointment in this property on "
            + appointment.getDateOfAppointment().format(Helper.dateFormat) + "\n");
    }

    int option = UI.displayMenu(new ArrayList<>(Arrays.asList("Yes", "No")), "Do you want to book appointment");
    if (option == 2)
      return;
    System.out.print("Enter the property number (refer above) to book appointments : ");
    int propertyNumber = Main.terminal.nextInt();
    if (propertyNumber < 1 || propertyNumber > filteredPropertyList.size()) {
      System.out.println("Invalid property number");
      UI.pause();
      return;
    }
    Property selectedProperty = filteredPropertyList.get(propertyNumber - 1);
    bookAppointment(selectedProperty);
    UI.pause();
  }

  // booking appointment by requesting appointment details
  private void bookAppointment(Property selectedProperty) {
    // To check whether the buyer has already booked appointment in this property
    System.out.println(selectedProperty.getPropertyId());
    Appointment appointment = hasAppointmentOnProperty(selectedProperty.getPropertyId());
    if (appointment != null) {
      System.out.println("You have an appointment in this property on " + appointment.getFormatedDateTime() + "\n");
      System.out.println("Cannot book this appointment");
      return;
    }

    Seller seller = selectedProperty.getSeller();
    UI.clearTerminal();
    UI.showMenuTitle("Book Appointment");
    System.out.println("Selected Property for visitation");
    System.out.println(selectedProperty);
    boolean continueNext = true;
    LocalDateTime appointmentDate = null;
    while (continueNext) {
      appointmentDate = Helper.promptDateAndTime();
      continueNext = !(Helper.isDateInValidTimeSlot(appointmentDate, this, seller, ""));
      if (continueNext) {
        System.out.println("Date and time is already booked in either buyer's slot or seller's slot");
      }
    }

    String uniqueAppointmentId = UUID.randomUUID().toString();
    Appointment newAppointment = new Appointment(uniqueAppointmentId, getCredential().getUsername(),
        selectedProperty.getPropertyId());
    newAppointment.setDateOfAppointment(appointmentDate);
    newAppointment.setStatus("pending");
    Main.appointmentList.add(newAppointment);
    this.addAppointment(newAppointment);

    selectedProperty.addAppointmentId(uniqueAppointmentId);
    BuyerDatabase.write(Main.buyerList);
    PropertyDatabase.write(Main.propertyList);
    AppointmentDatabase.write(Main.appointmentList);
    System.out.println("Successfully booked appointment & Pending approval from seller");
  }
}