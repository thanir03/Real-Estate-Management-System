import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.UUID;

public class Seller extends User {
  private ArrayList<String> propertyListId;

  public Seller(String fullName, String emailAddress, String phoneNum, LocalDateTime dob, Credential credential) {
    super(fullName, emailAddress, phoneNum, dob, credential);
  }

  public void setPropertyList(ArrayList<String> propertyList) {
    this.propertyListId = propertyList;
  }

  public ArrayList<Property> getPropertyList() {
    ArrayList<Property> properties = PropertyDatabase.read();
    ArrayList<Property> sellerProperties = new ArrayList<>();
    for (Property property : properties) {
      if (property.getSellerId().equals(getCredential().getUsername())) {
        sellerProperties.add(property);
      }
    }
    return sellerProperties;
  }

  public ArrayList<String> getPropertyListId() {
    return propertyListId;
  }

  public boolean addProperty(String propertyId) {
    propertyListId.add(propertyId);
    return true;
  }

  public String fileString() {
    String str = "";
    str += this.getCredential().getUsername() + Helper.writeFileSplitter;
    str += this.getCredential().getPassword() + Helper.writeFileSplitter;
    str += this.getFullName() + Helper.writeFileSplitter;
    str += this.getEmailAddress() + Helper.writeFileSplitter;
    str += this.getDOB().format(Helper.dateFormat) + Helper.writeFileSplitter;
    str += this.getPhoneNum() + Helper.writeFileSplitter;
    str += "[";
    for (int i = 0; i < propertyListId.size(); i++) {
      str += propertyListId.get(i) + (i == propertyListId.size() - 1 ? "]" : Helper.arraySplitter);
    }
    if (propertyListId.size() == 0)
      str += "]";
    str += "\n";
    return str;
  }

  @Override
  public String toString() {
    String str = "Name : " + getFullName() + " , Email Address : " + getEmailAddress() + " , Phone Number : "
        + getPhoneNum();
    return str;
  }

  @Override
  public void menu() {
    boolean continueNext = true;
    while (continueNext) {
      UI.clearTerminal();
      UI.showMenuTitle("Seller's Menu");
      System.out.println("Welcome " + getFullName() + "\n");
      List<String> menu = Arrays.asList("View Appointments", "Edit Appointments", "View Properties", "Add Property",
          "Edit Property", "Logout");
      ArrayList<String> sellerMenu = new ArrayList<>(menu);
      int menuOption = UI.displayMenu(sellerMenu, "Please choose your menu option");
      switch (menuOption) {
        case 1:
          viewAppointment();
          break;
        case 2:
          editAppointment();
          break;
        case 3:
          viewProperties();
          break;
        case 4:
          addProperty();
          break;
        case 5:
          editProperty();
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
  public void viewProperties() {
    UI.clearTerminal();
    UI.showMenuTitle("View Properties");
    ArrayList<Property> properties = getPropertyList();
    System.out.println("Total Properties : " + properties.size() + "\n");
    // Display property details
    for (int i = 0; i < properties.size(); i++) {
      System.out.println("Property " + (i + 1));
      UI.displayLine();
      System.out.println(properties.get(i).toString());
      System.out.println("Number of Appointments : " + properties.get(i).getAppointmentIdList().size() + "\n");
      System.out.println("\n");
    }
    UI.pause();
  }

  // View appointments that the property of the seller has
  public void viewAppointment() {
    UI.clearTerminal();
    UI.showMenuTitle("View Appointment");
    ArrayList<Appointment> appointments = AppointmentDatabase.read();
    ArrayList<Appointment> sellerAppointments = new ArrayList<>();
    // filter the appointment belongs to the seller
    for (Appointment appointment : appointments) {
      if (appointment.getProperty().getSellerId().equals(getCredential().getUsername())) {
        sellerAppointments.add(appointment);
      }
    }

    // Display appointment
    for (int i = 0; i < sellerAppointments.size(); i++) {
      System.out.println("\n");
      System.out.println("Appointment " + (i + 1));
      UI.displayLine();
      System.out.println(sellerAppointments.get(i).toString());
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
  public void editAppointment() {
    UI.clearTerminal();
    UI.showMenuTitle("Edit Appointment");
    ArrayList<Appointment> appointments = AppointmentDatabase.read();
    ArrayList<Appointment> sellerAppointments = new ArrayList<>();

    // filter the appointment belongs to the seller and appointment that are still
    // pending or on going
    for (Appointment appointment : appointments) {
      if (appointment.getProperty().getSellerId().equals(getCredential().getUsername())
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
      System.out.println(sellerAppointments.get(i).toString());
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
    System.out.println(selectedAppointment.toString());

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
          continueNextDate = !(Helper.isDateInValidTimeSlot(appointmentDate, buyer, this,
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
  public void addProperty() {
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
    Property property = new Property(getCredential().getUsername(), propertyId, numberOfRooms, priceRange,
        floorSize, isListed, facilityList);
    property.setAddress(address);
    property.setAppointmentList(new ArrayList<>());
    // adding property to the files
    ArrayList<Property> properties = PropertyDatabase.read();
    properties.add(property);
    ArrayList<Seller> sellers = SellerDatabase.read();
    for (int i = 0; i < sellers.size(); i++) {
      if (sellers.get(i).getCredential().getUsername().equals(getCredential().getUsername())) {
        sellers.get(i).addProperty(propertyId);
      }
    }
    SellerDatabase.write(sellers);
    PropertyDatabase.write(properties);
    System.out.println("Successfully added Property");
    UI.pause();
  }

  // editing property details
  public void editProperty() {
    UI.clearTerminal();
    UI.showMenuTitle("Edit Property");
    ArrayList<Property> properties = getPropertyList();
    for (int i = 0; i < properties.size(); i++) {
      System.out.println("Property " + (i + 1));
      UI.displayLine();
      System.out.println(properties.get(i).toString());
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
    System.out.println(selectedProperty.toString());
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
