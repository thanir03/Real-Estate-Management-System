import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SellerDatabase {
  private static String fileDir = "Files/Seller.txt";
  private static String sellerFileHeader = "username | password | fullName | emailAddress | ISOdateOfBirth | phoneNum |[propertyIds]\n\n";

  public static ArrayList<Seller> read() {
    ArrayList<Seller> sellerList = new ArrayList<>();
    try {
      File file = new File(fileDir);
      Scanner scanner = new Scanner(file);
      scanner.nextLine();
      scanner.nextLine();
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        Seller seller = processLine(line);
        sellerList.add(seller);
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    }
    return sellerList;
  }

  public static Seller processLine(String line) {
    String[] strList = line.split(Helper.readfileSplitter);
    String username = strList[0];
    String password = strList[1];
    String fullName = strList[2];
    String emailAddress = strList[3];
    LocalDateTime dob = LocalDateTime.parse(strList[4], Helper.dateFormat);
    String phoneNum = strList[5];
    String propertyString = strList[6].substring(1, strList[6].length() - 1);
    ArrayList<String> propertyIdList = new ArrayList<>(Arrays.asList(propertyString.split(Helper.arraySplitter)));
    if (propertyString.length() == 0) {
      propertyIdList = new ArrayList<>();
    }
    Credential credential = new Credential(username, password);
    Seller seller = new Seller(fullName, emailAddress, phoneNum, dob, credential);
    seller.setPropertyList(propertyIdList);
    return seller;
  }

  public static boolean write(ArrayList<Seller> sellerList) {
    try {
      FileWriter fileWriter = new FileWriter(fileDir);
      fileWriter.write(sellerFileHeader);
      for (Seller seller : sellerList) {
        fileWriter.write(seller.fileString());
      }
      fileWriter.close();
      return true;
    } catch (IOException e) {
      System.out.println("File do not exist");
      return false;
    }
  }

}
