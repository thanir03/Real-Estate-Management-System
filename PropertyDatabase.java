import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class PropertyDatabase {
  public final static String fileDir = "Files/Property.txt";

  public final static String fileHeader = "propertyId | sellerId | numberOfRooms | floorSize | isListed | houseNumber | street | city | state | postcode | [priceRange1 , priceRange2]|[ facilityList ]|[ appointmentIdList ]\n\n";

  public static ArrayList<Property> read() {
    ArrayList<Property> propertyList = new ArrayList<>();
    try {
      File file = new File(fileDir);
      Scanner scanner = new Scanner(file);
      scanner.nextLine();
      scanner.nextLine();
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        Property property = processLine(line);
        propertyList.add(property);
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("");
    }
    return propertyList;
  }

  private static Property processLine(String line) {
    String[] strList = line.split(Helper.readfileSplitter);
    String propertyId = strList[0];
    String sellerId = strList[1];

    int numberOfRooms = Integer.parseInt(strList[2]);
    int floorSize = Integer.parseInt(strList[3]);
    Boolean isListed = strList[4].equals("true") ? true : false;
    String houseNumber = strList[5];
    String street = strList[6];
    String city = strList[7];
    String state = strList[8];
    int postcode = Integer.parseInt(strList[9]);
    String[] priceRangeStr = strList[10].substring(1, strList[10].length() - 1).split(",");

    // Functional approach to convert String[] to Integer[]
    // using lambda expression
    Integer[] priceRange = Stream.of(priceRangeStr).map(num -> Integer.parseInt(num)).toArray(Integer[]::new);

    String facilityString = strList[11].substring(1, strList[11].length() - 1);
    ArrayList<String> facilityList = new ArrayList<>(
        Arrays.asList(strList[11].substring(1, strList[11].length() - 1).split(",")));
    if (facilityString.length() == 0) {
      facilityList = new ArrayList<>();
    }
    for (int i = 0; i < facilityList.size(); i++) {
      facilityList.set(i, facilityList.get(i).trim());
    }
    String appointmentString = strList[12].substring(1, strList[12].length() - 1);
    ArrayList<String> appointmentIdList = new ArrayList<>(
        Arrays.asList(strList[12].substring(1, strList[12].length() - 1).split(",")));
    if (appointmentString.length() == 0) {
      appointmentIdList = new ArrayList<>();
    }
    Address address = new Address(houseNumber, street, city, state, postcode);
    Property property = new Property(sellerId, propertyId, numberOfRooms, priceRange, floorSize, isListed,
        facilityList);
    property.setAppointmentList(appointmentIdList);
    property.setAddress(address);
    return property;
  }

  public static void write(ArrayList<Property> propertyList) {
    try {
      FileWriter fileWriter = new FileWriter(fileDir);
      fileWriter.write(fileHeader);
      for (Property property : propertyList) {
        fileWriter.write(property.fileString());
      }
      fileWriter.close();
    } catch (IOException e) {
      System.out.println("File do not exist");
    }

  }

}
