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
 * 9. Check if date is in valid time slot
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
        System.out.println("\nEnter the date for appointment : (DD-MM-YY) format  ");
        String dateinput = Main.terminal.next();
        String[] dateArrayList = dateinput.split("-");
        if (dateArrayList[0].length() == 1) {
          dateArrayList[0] = "0" + dateArrayList[0];
        }
        if (dateArrayList[1].length() == 1) {
          dateArrayList[1] = "0" + dateArrayList[1];
        }

        String date = String.join("-", dateArrayList);
        System.out.println("\nEnter the time for appointment : (HH:MM) 24 hour format");
        String timeInput = Main.terminal.next();
        String[] timeArrayList = timeInput.split(":");
        if (timeArrayList[0].length() == 1) {
          timeArrayList[0] = "0" + timeArrayList[0];
        }
        String time = String.join(":", timeArrayList);
        appointmentDate = LocalDateTime.parse(date + " " + time, Helper.dateFormat);
        LocalDateTime currentTime = LocalDateTime.now();
        if (appointmentDate.isBefore(currentTime)) {
          throw new DateTimeParseException("\nAppointment date cannot be before current date\n", date, 0);
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
        System.out.println("\nEnter number of rooms : ");
        numberOfRooms = Main.terminal.nextInt();
        if (numberOfRooms <= 0) {
          System.out.println("Invalid number of rooms");
          continueNext = true;
        } else
          continueNext = false;
      } catch (Exception e) {
        System.out.println("Invalid number of rooms");
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
        System.out.println("\nEnter floor size (sqft) : ");
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
    System.out.println("\nAddress Details : ");
    Main.terminal.nextLine();
    System.out.println("\nEnter house number : ");
    String houseNumber = Main.terminal.nextLine();
    System.out.println("\nEnter street : ");
    String street = Main.terminal.nextLine();
    System.out.println("\nEnter city : ");
    String city = Main.terminal.nextLine();
    boolean continueNext = true;
    int postcode = 0;
    while (continueNext) {
      try {
        System.out.println("\nEnter postcode : ");
        postcode = Main.terminal.nextInt();
        // Digit count of postcode must be 5
        if (Math.floor(Math.log10(postcode)) + 1 != 5) {
          System.out.println("Invalid postcode");
          continueNext = true;
        } else
          continueNext = false;
      } catch (Exception e) {
        System.out.println("Invalid postcode");
        Main.terminal.nextLine();
        continueNext = true;
      }
    }
    Main.terminal.nextLine();
    System.out.println("\nEnter state : ");
    String state = Main.terminal.nextLine();
    return new Address(houseNumber.trim(), street.trim(), city.trim(), state.trim(), postcode);
  }

  public static Integer[] promptPriceRange() {
    System.out.println("\nPrice Range Details ");
    Integer[] priceRange = new Integer[2];
    boolean continueMinPrice = true;
    while (continueMinPrice) {
      try {
        System.out.println("\nEnter minimum price : ");
        String minPrice = Main.terminal.nextLine();
        minPrice = minPrice.replaceAll(" ", "").trim();
        Integer num = Integer.parseInt(minPrice);
        if (num <= 0) {
          System.out.println("Invalid minimum price");
          continueMinPrice = true;
        } else {
          continueMinPrice = false;
          priceRange[0] = num;
        }
      } catch (Exception e) {
        System.out.println("Invalid minimum price");
        continueMinPrice = true;
      }
    }

    boolean continueMaxPrice = true;
    while (continueMaxPrice) {
      try {
        System.out.println("\nEnter maximum price : ");
        String maxPrice = Main.terminal.nextLine();
        maxPrice = maxPrice.replaceAll(" ", "").trim();
        Integer num = Integer.parseInt(maxPrice);
        if (num < priceRange[0]) {
          System.out.println("Invalid maximum price");
          continueMaxPrice = true;
        } else {
          continueMaxPrice = false;
          priceRange[1] = num;
        }
      } catch (Exception e) {
        System.out.println("Invalid maximum price");
        continueMaxPrice = true;
      }
    }

    return priceRange;
  }

  public static ArrayList<String> promptFacilityList() {
    System.out.println("\nEnter facilities (separated by comma) : ");
    String facilities = Main.terminal.nextLine();
    ArrayList<String> facilityList = new ArrayList<>();
    if (facilities.trim().length() > 0) {
      facilityList = new ArrayList<>(Arrays.asList(facilities.split(",")));
      for (int i = 0; i < facilityList.size(); i++) {
        facilityList.set(i, facilityList.get(i).trim());
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
    System.out.println("\n");
    return isListed;
  }

  public static void filterCompletedAppointment() {
    LocalDateTime now = LocalDateTime.now();
    for (Appointment appointment : Main.appointmentList) {
      if (appointment.getDateOfAppointment().isBefore(now)) {
        if (appointment.getStatus().equals(Appointment.ONGOING_STATUS)) {
          appointment.setStatus(Appointment.COMPLETED_STATUS);
        } else if (appointment.getStatus().equals(Appointment.PENDING_STATUS)) {
          appointment.setStatus(Appointment.CANCELLED_STATUS);
        }
      }
    }
    AppointmentDatabase.write(Main.appointmentList);
  }

  public static boolean isDateInValidTimeSlot(LocalDateTime appointmentDate, Buyer currentBuyer, Seller currentSeller,
      String appointmentId) {
    ArrayList<String> sellerProperties = currentSeller.getPropertyListId();

    // Check if the appointment date fits in sellers appointment time table
    for (Appointment app : Main.appointmentList) {
      for (String sellerPropertyId : sellerProperties) {
        if (app.getPropertyId().equals(sellerPropertyId) && !app.getStatus().equals(Appointment.CANCELLED_STATUS)
            && !app.getStatus().equals(Appointment.COMPLETED_STATUS) && !app.getAppointmentId().equals(appointmentId)) {
          LocalDateTime sellerAppointmentDate = app.getDateOfAppointment();
          int minutes = (int) appointmentDate.until(sellerAppointmentDate, ChronoUnit.MINUTES);
          if (Math.abs(minutes) <= 60) {
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
        if (Math.abs(minutes) <= 30) {
          return false;
        }
      }
    }
    return true;
  }
}