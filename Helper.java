import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * Utility functions that are often reused in the application 
 * 1. Prompt user for date and time for appointment
 * 2. Prompt user for number of rooms
 * 3. Prompt user for floor size
 * 4. Prompt user for address
 * 5. Prompt user for price range
 * 6. Prompt user for facilities
 * 7. Prompt user for isListed
 * 8. Filter completed appointment
 */

public class Helper {
  public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
  public static String readfileSplitter = "\\|";
  public static String writeFileSplitter = "|";
  public static String arraySplitter = ",";

  public static LocalDateTime promptDateAndTime() {
    boolean continueNext = true;
    LocalDateTime appointmentDate = null;
    while (continueNext) {
      try {
        System.out.println("Enter the date for appointment : (DD-MM-YY) format  ");
        String dateinput = Main.terminal.next();
        String[] dateArrayList = dateinput.split("-");
        if (dateArrayList[0].length() == 1) {
          dateArrayList[0] = "0" + dateArrayList[0];
        }
        if (dateArrayList[1].length() == 1) {
          dateArrayList[1] = "0" + dateArrayList[1];
        }

        String date = String.join("-", dateArrayList);
        System.out.println("Enter the time for appointment : (HH:MM) 24 hour format");
        String timeInput = Main.terminal.next();
        String[] timeArrayList = timeInput.split(":");
        if (timeArrayList[0].length() == 1) {
          timeArrayList[0] = "0" + timeArrayList[0];
        }
        String time = String.join(":", timeArrayList);
        appointmentDate = LocalDateTime.parse(date + " " + time, Helper.dateFormat);
        LocalDateTime currentTime = LocalDateTime.now();
        if (appointmentDate.isBefore(currentTime)) {
          throw new DateTimeParseException("Appointment date cannot be before current date", date, 0);
        }
        continueNext = false;
      } catch (Exception e) {
        System.out.println(e.getMessage());
        System.out.println("You have entered invalid date");
        continueNext = true;
      }
    }
    return appointmentDate;
  }

  public static int promptRoomNumber() {
    int numberOfRooms = 0;
    boolean continueNext = true;
    while (continueNext) {
      try {
        System.out.println("Enter number of rooms : ");
        numberOfRooms = Main.terminal.nextInt();
        if (numberOfRooms <= 0) {
          System.out.println("Invalid number of rooms");
          continueNext = true;
        } else
          continueNext = false;
      } catch (Exception e) {
        continueNext = true;
        Main.terminal.next();
      }
    }
    return numberOfRooms;
  }

  public static int promptFloorSize() {
    boolean continueNext = true;
    int floorSize = 0;
    while (continueNext) {
      try {
        System.out.println("Enter floor size (sqft) : ");
        floorSize = Main.terminal.nextInt();
        if (floorSize <= 100) {
          System.out.println("Invalid floor size or floor size is too small");
          continueNext = true;
        } else
          continueNext = false;
      } catch (Exception e) {
        continueNext = true;
        Main.terminal.next();
      }
    }
    return floorSize;
  }

  public static Address promptAddress() {
    System.out.println("Address Details : ");
    System.out.println("Enter house number : ");
    Main.terminal.nextLine();
    String houseNumber = Main.terminal.nextLine();
    System.out.println("Enter street : ");
    String street = Main.terminal.nextLine();
    System.out.println("Enter city : ");
    String city = Main.terminal.nextLine();
    System.out.println("Enter state : ");
    String state = Main.terminal.nextLine();
    boolean continueNext = true;
    int postcode = 0;
    while (continueNext) {
      try {
        System.out.println("Enter postcode : ");
        postcode = Main.terminal.nextInt();
        // Digit count of postcode must be 5
        if (Math.floor(Math.log10(postcode)) + 1 != 5) {
          System.out.println("Invalid postcode");
          continueNext = true;
        } else
          continueNext = false;
      } catch (Exception e) {
        Main.terminal.next();
        continueNext = true;
      }
    }
    return new Address(houseNumber, street, city, state, postcode);
  }

  public static Integer[] promptPriceRange() {
    System.out.println("Price Range Details ");
    Integer[] priceRange = new Integer[2];
    System.out.println("Enter minimum price : ");
    priceRange[0] = Main.terminal.nextInt();
    while (priceRange[0] <= 0) {
      System.out.println("Enter minimum price : ");
      priceRange[0] = Main.terminal.nextInt();
    }
    System.out.println("Enter maximum price : ");
    priceRange[1] = Main.terminal.nextInt();
    while (priceRange[1] < priceRange[0]) {
      System.out.println("Invalid maximum price. Lower than minimum price ");
      System.out.println("Enter maximum price : ");
      priceRange[1] = Main.terminal.nextInt();
    }
    return priceRange;
  }

  public static ArrayList<String> promptFacilityList() {
    System.out.println("Enter facilities (separated by comma) : ");
    Main.terminal.nextLine();
    String facilities = Main.terminal.nextLine();
    ArrayList<String> facilityList = new ArrayList<>(Arrays.asList(facilities.split(",")));
    while (facilityList.size() == 0) {
      System.out.println("Enter facilities (separated by comma) : ");
      facilities = Main.terminal.next();
      if (facilities.trim().length() == 0) {
        facilityList = new ArrayList<>();
      } else {
        facilityList = new ArrayList<>(Arrays.asList(facilities.split(",")));
      }
    }
    return facilityList;
  }

  public static boolean promptIsListed() {
    System.out.println("Do you want to list for sale (y/n) : ");
    String isListedString = Main.terminal.next();
    while (!isListedString.toLowerCase().equals("y") && !isListedString.toLowerCase().equals("n")) {
      System.out.println("Do you want to list for sale (y/n) : ");
      isListedString = Main.terminal.next();
    }
    boolean isListed = isListedString.toLowerCase().equals("y");
    return isListed;
  }

  public static void filterCompletedAppointment() {
    ArrayList<Appointment> appointmentList = AppointmentDatabase.read();
    LocalDateTime now = LocalDateTime.now();
    for (Appointment appointment : appointmentList) {
      if (appointment.getDateOfAppointment().isBefore(now)) {
        if (appointment.getStatus().equals(Appointment.ONGOING_STATUS)) {
          appointment.setStatus(Appointment.COMPLETED_STATUS);
        } else if (appointment.getStatus().equals(Appointment.PENDING_STATUS)) {
          appointment.setStatus(Appointment.CANCELLED_STATUS);
        }
      }
    }
    AppointmentDatabase.write(appointmentList);
  }

  public static boolean isDateInValidTimeSlot(LocalDateTime appointmentDate, Buyer currentBuyer, Seller currentSeller,
      String appointmentId) {
    ArrayList<Appointment> appointmentList = AppointmentDatabase.read();
    ArrayList<String> sellerProperties = currentSeller.getPropertyListId();

    // Check if the appointment date fits in sellers appointment time table
    for (Appointment app : appointmentList) {
      for (String sellerPropertyId : sellerProperties) {
        if (app.getPropertyId().equals(sellerPropertyId) && !app.getStatus().equals(Appointment.CANCELLED_STATUS)
            && !app.getStatus().equals(Appointment.COMPLETED_STATUS) && !app.getAppointmentId().equals(appointmentId)) {
          LocalDateTime sellerAppointmentDate = app.getDateOfAppointment();
          int minutes = (int) appointmentDate.until(sellerAppointmentDate, ChronoUnit.MINUTES);
          if (Math.abs(minutes) <= 30) {
            return false;
          }
        }
      }
    }

    // Check if the appointment date fits in buyers appointment time table
    for (Appointment app : currentBuyer.getAppointments()) {
      if (!app.getStatus().equals(Appointment.CANCELLED_STATUS)
          && !app.getStatus().equals(Appointment.COMPLETED_STATUS) && !app.getAppointmentId().equals(appointmentId)) {
        LocalDateTime sellerAppointmentDate = app.getDateOfAppointment();
        int minutes = (int) appointmentDate.until(sellerAppointmentDate, ChronoUnit.MINUTES);
        System.out.println(minutes);
        if (Math.abs(minutes) <= 30) {
          return false;
        }
      }
    }
    return true;
  }
}