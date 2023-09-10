import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BuyerDatabase {

  private static final String fileDir = "Files/Buyer.txt";

  private static final String buyerFileHeader = " username | password | fullName | emailAddress | dateOfBirth | phoneNum | [budgetRange1,budgetRange2] | numberOfRooms | city | [facilityArray] | [appointmentListIds]\n\n";

  public static ArrayList<Buyer> read() {
    ArrayList<Buyer> buyerList = new ArrayList<>();
    try {
      File file = new File(fileDir);
      Scanner scanner = new Scanner(file);
      scanner.nextLine();
      scanner.nextLine();
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        Buyer buyer = processLine(line);
        buyerList.add(buyer);
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("File Not Found");
    }
    return buyerList;
  }

  private static Buyer processLine(String line) {
    String[] strList = line.split(Helper.readfileSplitter);
    String username = strList[0];
    String password = strList[1];
    String fullName = strList[2];
    String emailAddress = strList[3];
    LocalDateTime dob = LocalDateTime.parse(strList[4], Helper.dateFormat);
    String phoneNum = strList[5];
    String appointmentString = strList[6].substring(1, strList[6].length() - 1);
    ArrayList<String> appointmentIdList = new ArrayList<>(Arrays.asList(appointmentString.split(",")));
    if (appointmentString.trim().length() == 0) {
      appointmentIdList = new ArrayList<>();
    }
    Credential credential = new Credential(username, password);
    Buyer buyer = new Buyer(fullName, emailAddress, phoneNum, dob, credential);
    buyer.setAppointmentIdList(appointmentIdList);
    return buyer;
  }

  public static void write(ArrayList<Buyer> buyerList) {
    try {
      FileWriter fileWriter = new FileWriter(fileDir);
      fileWriter.write(buyerFileHeader);
      for (Buyer buyer : buyerList) {
        fileWriter.write(buyer.fileString());
      }
      fileWriter.close();
    } catch (IOException e) {
      System.out.println("File do not exist");
    }

  }

}
