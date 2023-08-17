import java.time.LocalDateTime;
import java.util.ArrayList;

public class Seller extends User {
  private ArrayList<String> propertyListId;

  public Seller(String fullName, String emailAddress, String phoneNum, LocalDateTime dob, Credential credential,
      ArrayList<String> propertyList) {
    super(fullName, emailAddress, phoneNum, dob, credential);
    this.propertyListId = propertyList;
  }

  public void setPropertyList(ArrayList<String> propertyList) {
    this.propertyListId = propertyList;
  }

  public ArrayList<String> getPropertyList() {
    return propertyListId;
  }

  public boolean addPropertyList(Property property) {
    propertyListId.add(property.getPropertyId());
    // update the database
    return true;
  }
}
